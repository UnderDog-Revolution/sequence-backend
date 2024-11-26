package com.example.login_logout.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    private static final long EXPIRATION_TIME = 86400000; // 24시간 (1일) 동안 유효한 토큰

    // JWT 토큰 생성
    public String generateToken(Long userId, String username) {
        Claims claims = Jwts.claims().setSubject(userId.toString());
        claims.put("username", username);  // 예: username을 추가

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 만료 시간 설정
                .signWith(SignatureAlgorithm.HS512, secretKey) // 서명 설정
                .compact();
    }
}
