package com.example.demo.repository;

import com.example.demo.model.User;
import com.example.demo.model.UserSession;
import com.example.demo.model.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface UserSessionRepository extends JpaRepository<UserSession, Integer> {

    Optional<UserSession> findByRefreshToken(String refreshToken);

    Optional<UserSession> findByAccessToken(String accessToken);

    List<UserSession> findByUserAndStatus(User user, SessionStatus status);
}