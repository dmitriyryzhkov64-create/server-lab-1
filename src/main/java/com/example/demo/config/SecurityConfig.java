package com.example.demo.config;

import com.example.demo.security.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired private JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register", "/auth/login", "/auth/refresh", "/api/users").permitAll()

                        // 2. Управление тестами (ADMIN)
                        .requestMatchers(HttpMethod.POST, "/api/quizzes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/quizzes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/quizzes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/questions/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/questions/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/questions/**").hasRole("ADMIN")

                        // 3. Прохождение тестов
                        .requestMatchers("/api/attempts/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/quizzes/all").hasAnyRole("USER", "ADMIN")

                        // 4. Управление пользователями
                        .requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")
                        // Разрешаем PUT для изменения своих данных (опционально)
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").authenticated()

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}