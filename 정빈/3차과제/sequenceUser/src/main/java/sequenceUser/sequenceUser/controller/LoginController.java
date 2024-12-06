package sequenceUser.sequenceUser.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sequenceUser.sequenceUser.dto.TokenDto;
import sequenceUser.sequenceUser.service.LoginService;

@RestController
@RequestMapping("/api")
public class LoginController {

    private final LoginService authService;

    public LoginController(LoginService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody @Valid sequenceUser.sequenceUser.dto.LoginDto loginDto) {
        TokenDto tokenResponse = authService.authenticateUser(loginDto);
        return ResponseEntity.ok(tokenResponse);
    }
}