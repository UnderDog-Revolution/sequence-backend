package user.user.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}") // application.yml 또는 application.properties에서 읽어옴
    private String secretKey;

    private byte[] keyBytes;

    @PostConstruct
    public void init() {
        keyBytes = secretKey.getBytes(); // 비밀 키를 바이트 배열로 변환
    }

    private static final long EXPIRATION_TIME = 86400000; // 1일

    // JWT 생성
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // JWT의 subject에 username 포함
                .setIssuedAt(new Date()) // 생성 시간
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 만료 시간
                .signWith(SignatureAlgorithm.HS256, keyBytes) // HS256 알고리즘으로 서명
                .compact();
    }

    // JWT에서 username 추출
    public String extractUsername(String token) {
        return getClaims(token).getSubject(); // subject에서 username 추출
    }

    // JWT 검증 및 클레임 가져오기
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(keyBytes) // 서명 검증에 사용할 비밀 키
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
