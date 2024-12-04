package board.board.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;



@Component
public class JwtUtil {

    @Value("${jwt.secret}") // application.yml 또는 application.properties에서 읽어옴
    private String secretKey;

    private byte[] keyBytes;

    @PostConstruct
    public void init() {
        keyBytes = secretKey.getBytes(); // 비밀 키를 바이트 배열로 변환
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
