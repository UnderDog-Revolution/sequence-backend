package com.example.APIGateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collections;

public class JwtAuthenticationFilter implements WebFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                // JwtTokenProvider를 통해 토큰 검증 및 파싱
                Claims claims = jwtTokenProvider.parseClaimsFromToken(token);
                String userId = claims.getSubject();  // subject에서 userId를 가져옴

                System.out.println("Authenticated claims.getSubject(): " + claims.getSubject());  // 로그 추가
                System.out.println("Authenticated userId: " + userId);  // 로그 추가

                // SecurityContext에 인증 정보 저장
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());

                // 요청 헤더에 인증된 사용자 ID 추가
                exchange.getRequest().mutate()
                        .header("X-User-Id", userId) // 사용자 ID를 X-User-Id 헤더에 추가
                        .build();

                // ReactiveSecurityContextHolder를 사용하여 인증 정보 설정
                return chain.filter(exchange)
                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
            } catch (JwtException e) {
                System.out.println("JWT Token parsing error: " + e.getMessage());
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        }
        return chain.filter(exchange);
    }
}
