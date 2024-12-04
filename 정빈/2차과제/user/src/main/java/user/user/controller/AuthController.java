package user.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import user.user.dto.LoginDto;
import user.user.dto.UserDto;
import user.user.entity.User;
import user.user.service.AuthService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signup(@RequestBody UserDto userDto) {
        User user = authService.register(userDto);

        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginDto loginDto) {
        String token = authService.login(loginDto);

        Map<String, String> response = new HashMap<>();
        response.put("message", "로그인 성공");
        response.put("username", loginDto.getEmail());
        response.put("token", token); // JWT 토큰 추가

        return ResponseEntity.ok(response);
    }

    // username 존재 여부 확인
    @GetMapping("users/{username}")
    public ResponseEntity<Boolean> checkUserExists(@PathVariable("username") String username) {
        boolean exists = authService.existsByUsername(username);
        return ResponseEntity.ok(exists);
    }
}
