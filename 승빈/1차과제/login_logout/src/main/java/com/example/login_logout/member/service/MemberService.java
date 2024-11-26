package com.example.login_logout.member.service;

import com.example.login_logout.security.JwtTokenProvider;
import com.example.login_logout.member.dto.LoginRequest;
import com.example.login_logout.member.dto.RegisterRequest;
import com.example.login_logout.member.entity.Member;
import com.example.login_logout.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // 회원가입
    public void register(RegisterRequest registerRequest) {
        if (memberRepository.existsByUsername(registerRequest.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        }

        Member member = new Member();
        member.setLoginId(registerRequest.getLoginId());
        member.setUsername(registerRequest.getUsername());
        member.setPassword(passwordEncoder.encode(registerRequest.getPassword())); // 비밀번호 암호화
        memberRepository.save(member);
    }

    // 로그인
    public String login(LoginRequest loginRequest) {
        Member member = memberRepository.findByLoginId(loginRequest.getLoginId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }

        return jwtTokenProvider.generateToken(member.getId(), member.getLoginId()); // JWT 토큰 생성
    }
}
