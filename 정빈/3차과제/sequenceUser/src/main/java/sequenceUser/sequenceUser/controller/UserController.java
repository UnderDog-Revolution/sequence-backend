package sequenceUser.sequenceUser.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sequenceUser.sequenceUser.dto.SignupDto;
import sequenceUser.sequenceUser.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    // 회원가입 성공시 메세지 출력, 실패시 에러 출력을 위해 제너릭타입 ?로 받음 
    public ResponseEntity<?> registerUser(@RequestBody @Valid SignupDto signupDto) {
        userService.registerUser(signupDto);
        return ResponseEntity.ok().body("{\"status\":\"success\", \"message\":\"회원가입이 완료되었습니다.\"}");
    }
}
