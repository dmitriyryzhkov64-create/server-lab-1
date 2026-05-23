package com.example.demo.security;

import com.example.demo.model.SessionStatus;
import com.example.demo.model.UserSession;
import com.example.demo.repository.UserSessionRepository;
import com.example.demo.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Optional;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired private JwtTokenProvider jwtTokenProvider;
    @Autowired private UserDetailsServiceImpl userDetailsService;
    @Autowired private UserSessionRepository sessionRepository; // Добавили репозиторий сессий

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                username = jwtTokenProvider.getUsernameFromToken(jwt);
            } catch (Exception e) {
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            if (jwtTokenProvider.validateToken(jwt)) {

                Optional<UserSession> sessionOpt = sessionRepository.findByAccessToken(jwt);

                if (sessionOpt.isPresent() && sessionOpt.get().getStatus() == SessionStatus.ACTIVE) {

                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }

            }
        }
        filterChain.doFilter(request, response);
    }
}