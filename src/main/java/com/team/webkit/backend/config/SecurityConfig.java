package com.team.webkit.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            // CSRF 보호 끄기
            .csrf(csrf -> csrf.disable())
            // 세션 만들지 않
            .sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // 누구나 접근 허용 or 그 외 모든 요청은 인증 필요
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/member/join", "/api/member/login", "/api/member/logout",
                    "/api/member/find", "/api/member/findId", "/api/member/chkPwd",
                    "/api/member/duplicate", "api/member/info", "api/member/duplicate/nickname",
                    "api/member/password")
                .permitAll()
                .anyRequest().authenticated()
            )
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            )
            // 요청 처리 전, JwtAUthFilter를 거쳐 토큰 검증
            .addFilterBefore(jwtAuthFilter,
                org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}