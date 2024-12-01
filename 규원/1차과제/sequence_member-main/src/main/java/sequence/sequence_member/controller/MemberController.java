package sequence.sequence_member.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sequence.sequence_member.dto.LoginDTO;
import sequence.sequence_member.dto.LoginResDTO;
import sequence.sequence_member.dto.MemberDTO;
import sequence.sequence_member.service.MemberService;

@Controller
@RequiredArgsConstructor
public class MemberController {

    @Autowired
    private final MemberService memberService;

    @PostMapping("/api/member/join")
    public String join(@RequestBody MemberDTO memberDTO){
        memberService.save(memberDTO);
        return "complete";
    }

    @PostMapping("/api/member/login")
    public ResponseEntity<LoginResDTO> login(@RequestBody LoginDTO loginDTO, HttpSession session){
        LoginResDTO loginResult =  memberService.loginCheck(loginDTO);
        if("success".equals(loginResult.getMessage())) {
            return ResponseEntity.ok(loginResult);

        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(loginResult);
        }
    }

    @PostMapping("/api/member/validate")
    public ResponseEntity<String> validateUser(@RequestParam String user_id, @RequestParam String randomKey) {
        boolean isValid = memberService.validate(user_id, randomKey);
        if (isValid) {
            return ResponseEntity.ok("사용자 인증 성공");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("사용자 인증 실패");
        }
    }





}
