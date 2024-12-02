package com.example.user;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.sound.midi.MetaMessage;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @GetMapping("/register")
    String getRegister() {
        return "register.html";
    }

    @PostMapping("/register")
    String createUser(@RequestParam String username,
                      @RequestParam String password,
                      @RequestParam String password_check) {
        if (Objects.equals(password, password_check)) {
            Member member = new Member();
            member.setUsername(username);
            member.setPassword(passwordEncoder.encode(password));
            userRepository.save(member);
            return "redirect:/login";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/login")
    String getLogin() {
        var res = userRepository.findByUsername("test");
        System.out.println(res.get().toString());
        return "login.html";
    }

    @GetMapping("/")
    String getMain() { return "index.html"; }


}

