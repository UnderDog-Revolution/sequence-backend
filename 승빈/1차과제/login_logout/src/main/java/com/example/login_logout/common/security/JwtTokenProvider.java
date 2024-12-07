package com.example.login_logout.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    private static final long ACCESS_EXPIRATION_MILLISECONDS = 1000 * 60 * 30; // 30분
    private static final long REFRESH_EXPIRATION_MILLISECONDS = 1000 * 60 * 60 * 24; // 24시간

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    /**
     * Access Token 생성
     */
    public String createAccessToken(Long userId, String username) {
        Claims claims = Jwts.claims().setSubject(userId.toString());
        claims.put("username", username);  // 예: username을 추가

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRATION_MILLISECONDS)) // 만료 시간 설정
                .signWith(SignatureAlgorithm.HS512, secretKey) // 서명 설정
                .compact();
    }

    /**
     * Refresh token 생성
     */
    public String createRefreshToken(Long userId, String username) {
        // 현재 시간을 기준으로 토큰 생성
        Date now = new Date();
        Date refreshExpiration = new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_MILLISECONDS);

        // 토큰 생성
        return Jwts.builder()
                .setSubject(username) // 사용자 이름을 Subject로 설정
                .claim("userId", userId) // 사용자 ID를 claim에 추가
                .setIssuedAt(now) // 토큰 발급 시간
                .setExpiration(refreshExpiration) // 토큰 만료 시간
                .signWith(SignatureAlgorithm.HS512, secretKey) // 서명 알고리즘과 키 설정
                .compact();
    }

    /**
     * Refresh Token에서 Subject(사용자 이름) 추출
     */
    public String getSubject(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Refresh Token에서 사용자 ID 추출
     */
    public Long getUserId(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.get("userId").toString());
    }

    /**
     * Token 검증
     */
    public boolean validateToken(String token) {
        try {
            // 토큰을 파싱하고 서명 키 검증
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            logger.error("토큰 만료됨: " + e.getMessage());
        } catch (io.jsonwebtoken.SignatureException e) {
            logger.error("서명 불일치: " + e.getMessage());
        } catch (Exception e) {
            logger.error("토큰 검증 실패: " + e.getMessage(), e);
        }
        return false;
    }
}
