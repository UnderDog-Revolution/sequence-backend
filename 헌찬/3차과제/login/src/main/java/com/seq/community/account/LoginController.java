package com.seq.community.account;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;


    @GetMapping("/login")
    String register(Model model) {
        model.addAttribute("error", "");
        return "login.html";
    }

    @PostMapping("/api/login")
    @ResponseBody
    public Map<String, Object> loginJWT(@RequestBody Map<String, String> data,  HttpServletResponse response) {
        var authToken = new UsernamePasswordAuthenticationToken(
                data.get("username"), data.get("password")
        );
        var auth = authenticationManagerBuilder.getObject().authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);

        var auth2 = SecurityContextHolder.getContext().getAuthentication();
        var jwt = JwtUtil.createToken(auth2);
        var refreshToken = JwtUtil.createRefreshToken(auth2);

        var cookie = new Cookie("jwt", jwt);
        cookie.setMaxAge(100);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        // 응답 데이터 생성
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("status", "success");
        responseData.put("token", jwt);
        responseData.put("refreshToken", refreshToken);

        return responseData;
    }
}
