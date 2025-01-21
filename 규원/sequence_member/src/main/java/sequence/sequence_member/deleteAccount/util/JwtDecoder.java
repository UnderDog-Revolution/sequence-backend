package sequence.sequence_member.deleteAccount.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class JwtDecoder {

    /**
     * JWT에서 페이로드 부분만 디코딩
     * @param token JWT 토큰
     * @return 디코딩된 페이로드
     */
    public String decodePayload(String token) {
        if (token == null || !token.contains(".")) {
            throw new IllegalArgumentException("Invalid JWT token");
        }

        // JWT 구조: header.payload.signature
        String[] parts = token.split("\\.");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid JWT structure");
        }

        String payload = parts[1]; // 두 번째 부분이 Payload
        return new String(Base64.getUrlDecoder().decode(payload), StandardCharsets.UTF_8);
    }

    public String getTokenFromCookies(HttpServletRequest request, String cookieName) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}
