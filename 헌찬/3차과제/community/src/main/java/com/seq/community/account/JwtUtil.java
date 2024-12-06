package com.seq.community.account;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    @Value("${JWT_SECRET_KEY}")
    private String jwtSecretKeyFromYaml;

    public static SecretKey key;

    @PostConstruct
    public void init() {
        if (jwtSecretKeyFromYaml == null || jwtSecretKeyFromYaml.isEmpty()) {
            throw new IllegalStateException("JWT_SECRET_KEY가 설정되지 않았습니다.");
        }

        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecretKeyFromYaml));
    }

    // JWT 만들어주는 함수
    public static String createToken(Authentication auth) {
        var user = (CustomUser) auth.getPrincipal();
        var authorities = auth.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.joining(","));
        String jwt = Jwts.builder()
                .claim("username", user.getUsername())
                .claim("authorities", authorities)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (15 * 60000))) // 유효기간 15분
                .signWith(key)
                .compact();
        return jwt;
    }

    // 리프레시 토큰 만들어주는 함수
    public static String createRefreshToken(Authentication auth) {
        var user = (CustomUser) auth.getPrincipal();
        var authorities = auth.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.joining(","));
        String refreshToken = Jwts.builder()
                .claim("username", user.getUsername())
                .claim("authorities", authorities)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (7 *86400000))) // 유효기간 7일
                .signWith(key)
                .compact();
        return refreshToken;
    }

    // JWT 까주는 함수
    public static Claims extractToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
        return claims;
    }

    // JWT 유효성 검증 함수
    public static boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            Date expiration = claimsJws.getBody().getExpiration();
            return expiration.after(new Date());
        } catch (SignatureException | IllegalArgumentException e) {
            // 유효하지 않은 서명 또는 토큰이 비어있음
            return false;
        }
    }
}