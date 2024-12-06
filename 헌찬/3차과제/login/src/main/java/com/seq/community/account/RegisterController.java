package com.seq.community.account;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class RegisterController {

    private final AccountRepository accountRepository;

    @GetMapping("/register")
    String register(Model model) {
        model.addAttribute("error", "");
        return "register.html";
    }

    @PostMapping("/account")
    @ResponseBody
    public Map<String, String> addAccount(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String name,
            @RequestParam String birthDate, // 문자열로 받되, 이후 변환
            @RequestParam String gender,
            @RequestParam String phoneNumber,
            @RequestParam String email,
            @RequestParam String address
    ) {
        Map<String, String> response = new HashMap<>();

        // 필수 정보 확인
        if (username == null || username.isEmpty() ||
                password == null || password.isEmpty() ||
                name == null || name.isEmpty() ||
                birthDate == null || birthDate.isEmpty() ||
                gender == null || gender.isEmpty() ||
                phoneNumber == null || phoneNumber.isEmpty() ||
                email == null || email.isEmpty() ||
                address == null || address.isEmpty()) {
            response.put("status", "fail");
            response.put("message", "모든 필드를 입력해주세요.");
            return response;
        }

        // 중복 아이디 확인
        if (accountRepository.existsByUsername(username)) {
            response.put("status", "fail");
            response.put("message", "이미 사용 중인 사용자이름 입니다.");
            return response;
        }

        // Account 객체 생성 및 필수 정보 설정
        Account account = new Account();
        account.setUsername(username);
        var hash = new BCryptPasswordEncoder().encode(password);
        account.setPassword(hash);
        account.setName(name);
        try {
            Date parsedBirthDate = new SimpleDateFormat("yyyy-MM-dd").parse(birthDate);
            account.setBirthDate(parsedBirthDate);
        } catch (ParseException e) {
            response.put("status", "fail");
            response.put("message", "생년월일 형식이 잘못되었습니다. (yyyy-MM-dd)");
            return response;
        }
        account.setGender(gender);
        account.setPhoneNumber(phoneNumber);
        account.setEmail(email);
        account.setAddress(address);

        // 계정 정보 저장
        accountRepository.save(account);

        response.put("status", "success");
        response.put("message", "회원가입이 완료되었습니다.");
        return response;
    }



    @GetMapping("/info")
    String info(Model model) {
        List<Account> result = accountRepository.findAll();
        model.addAttribute("accounts", result);

        return "info.html";
    }
}
