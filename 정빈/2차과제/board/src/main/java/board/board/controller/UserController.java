package board.board.controller;


import board.board.entity.User;
import board.board.repository.UserRepository;
import board.board.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController { // 마이 프로필
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // 마이 프로필 조회
    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(HttpServletRequest request) {
        String token = extractToken(request);
        String username = jwtUtil.extractUsername(token);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return ResponseEntity.ok(user);
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        throw new IllegalArgumentException("Invalid or missing Authorization header");
    }
}
