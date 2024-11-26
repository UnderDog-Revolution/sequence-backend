package com.example.APIGateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
    private String jwtSecret;

    public JwtTokenProvider(@Value("${jwt.secret}") String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }
    // 토큰에서 userId을 추출하는 메서드 추가
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());  // Subject가 userId 이므로, 이를 Long으로 변환하여 반환
    }

    public Claims parseClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(jwtSecret)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            throw new JwtException("Invalid JWT token", e);
        }
    }

    public boolean validateToken(String token) {
        try {
            parseClaimsFromToken(token); // 파싱 시 예외가 발생하지 않으면 유효한 토큰
            return true;
        } catch (JwtException e) {
            System.out.println("JWT validation error: " + e.getMessage());
            return false;
        }
    }
}
