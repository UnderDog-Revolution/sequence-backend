package com.example.APIGateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtTokenProvider);

        http
                .cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .authorizeExchange(auth -> auth
                        .pathMatchers("/api/article/**").authenticated() // 인증 필요한 경로
                        .pathMatchers("/api/article/validate/**").permitAll() // 인증 필요한 경로
                        .pathMatchers("/api/member/validate/**").permitAll() // 인증 필요한 경로
                        .anyExchange().permitAll()               // 나머지 요청은 인증 필요 없음
                )
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        SecurityWebFiltersOrder.AUTHENTICATION
                ); // 필터 등록

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
