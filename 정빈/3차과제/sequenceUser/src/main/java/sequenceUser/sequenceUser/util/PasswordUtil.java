package sequenceUser.sequenceUser.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // 비밀번호 암호화
    public static String encryptPassword(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    // 비밀번호 검증
    public static boolean verifyPassword(String rawPassword, String encryptedPassword) {
        return encoder.matches(rawPassword, encryptedPassword);
    }
}