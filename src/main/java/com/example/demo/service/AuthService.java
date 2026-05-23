package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Map;
import java.util.List;

@Service
public class AuthService {
    @Autowired private JwtTokenProvider tokenProvider;
    @Autowired private UserSessionRepository sessionRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private AuthenticationManager authenticationManager;

    @Transactional
    public Map<String, String> login(String username, String password) {
        // Проверка пароля через AuthenticationManager
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return generateTokenPair(user);
    }

    @Transactional
    public Map<String, String> refresh(String refreshToken) {
        // Поиск сессии по refresh токену
        UserSession session = sessionRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        // Валидация: статус должен быть ACTIVE, подпись верна, тип токена - refresh
        if (session.getStatus() != SessionStatus.ACTIVE ||
                !tokenProvider.validateToken(refreshToken) ||
                !"refresh".equals(tokenProvider.getTokenType(refreshToken))) {
            throw new RuntimeException("Invalid or revoked refresh token");
        }

        // Помечаем старую сессию как REVOKED (теперь и старый access, привязанный к ней, станет невалидным)
        session.setStatus(SessionStatus.REVOKED);
        sessionRepository.save(session);

        return generateTokenPair(session.getUser());
    }

    private Map<String, String> generateTokenPair(User user) {
        List<UserSession> activeSessions = sessionRepository.findByUserAndStatus(user, SessionStatus.ACTIVE);
        for (UserSession oldSession : activeSessions) {
            oldSession.setStatus(SessionStatus.REVOKED);
        }
        sessionRepository.saveAll(activeSessions);

        String access = tokenProvider.generateAccessToken(user.getUsername(), user.getRole());
        String refresh = tokenProvider.generateRefreshToken(user.getUsername());

        UserSession session = new UserSession();
        session.setUser(user);
        session.setRefreshToken(refresh);
        session.setAccessToken(access);
        session.setStatus(SessionStatus.ACTIVE);
        session.setExpiryDate(Instant.now().plusMillis(604800000)); // 7 дней
        sessionRepository.save(session);

        return Map.of("access", access, "refresh", refresh);
    }
}