package com.seq.deleteAccount.controller;

import com.seq.deleteAccount.dto.AccountDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/info")
    public ResponseEntity<AccountDto> getUserInfo() {
        // JSON 데이터 생성
        AccountDto user = new AccountDto();
        user.setUsername("123");
        user.setPassword("$2a$10$KRe2wTXUH5g52r2eQ2y5IefkutfXjKtOI7qpjssgx6MwGpHmbqqeO"); //123

        // ResponseEntity로 반환
        return ResponseEntity.ok(user);
    }
}
