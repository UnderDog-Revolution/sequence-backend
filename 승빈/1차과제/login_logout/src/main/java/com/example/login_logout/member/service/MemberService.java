package com.example.login_logout.member.service;

import com.example.login_logout.common.entity.MemberRefreshToken;
import com.example.login_logout.common.exception.InvalidInputException;
import com.example.login_logout.common.repository.MemberRefreshTokenRepository;
import com.example.login_logout.member.dto.*;
import com.example.login_logout.common.security.JwtTokenProvider;
import com.example.login_logout.member.entity.Member;
import com.example.login_logout.member.handler.ImageHandler;
import com.example.login_logout.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRefreshTokenRepository memberRefreshTokenRepository;

    public MemberService(MemberRepository memberRepository,
                         PasswordEncoder passwordEncoder,
                         JwtTokenProvider jwtTokenProvider,
                         MemberRefreshTokenRepository memberRefreshTokenRepository) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRefreshTokenRepository = memberRefreshTokenRepository;
    }

    // 회원가입
    @Transactional
    public void register(RegisterRequest registerRequest) throws IOException {
        if (memberRepository.existsByUsername(registerRequest.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        }

        ImageHandler imageHandler = new ImageHandler();
        String path = imageHandler.save(registerRequest.getProfileImage());

        System.out.println("path : " + path);

        Member member = new Member();

        member.setName(registerRequest.getName());
        member.setBirthDate(registerRequest.getBirthDate());
        member.setGender(registerRequest.getGender());
        member.setPhone(registerRequest.getPhone());
        member.setEmail(registerRequest.getEmail());
        member.setAddress(registerRequest.getAddress());
        member.setUsername(registerRequest.getUsername());
        member.setPassword(passwordEncoder.encode(registerRequest.getPassword())); // 비밀번호 암호화

        member.setProfileImage(path);
        member.setNickname(registerRequest.getNickname());
        member.setEducation(registerRequest.getEducation());
        member.setSkills(registerRequest.getSkills());
        member.setDesiredRole(registerRequest.getDesiredRole());
        member.setExperience(registerRequest.getExperience());
        member.setCareer(registerRequest.getCareer());
        member.setCertificationsAndAwards(registerRequest.getCertificationsAndAwards());
        member.setPortfolioLink(registerRequest.getPortfolioLink());
        member.setIntroduction(registerRequest.getIntroduction());

        memberRepository.save(member);
    }

    // 로그인
    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        Member member = memberRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        if (!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
        }
        String accessToken = jwtTokenProvider.createAccessToken(member.getId(), member.getUsername());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getId(), member.getUsername());

        // 기본 refresh token 확인
        MemberRefreshToken existingToken = memberRefreshTokenRepository.findByMember(member);

        if (existingToken != null) {
            existingToken.setRefreshToken(refreshToken);
            memberRefreshTokenRepository.save(existingToken);
        } else {
            memberRefreshTokenRepository.save(new MemberRefreshToken(member, refreshToken));
        }

        return new LoginResponse("success", accessToken, refreshToken);
    }

    @Transactional
    public NewAccessTokenResponse newAccessToken(String refreshToken) {
        // 1. Refresh Token 검증
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            System.out.println("Invalid refresh token or token expired.");
            throw new InvalidInputException("로그인이 만료되었습니다.");
        }

        // 2. Refresh Token에서 사용자 정보 추출
        String username = jwtTokenProvider.getSubject(refreshToken); // 리프레시 토큰에서 사용자 이름 추출
        Long userId = jwtTokenProvider.getUserId(refreshToken); // 리프레시 토큰에서 사용자 ID 추출

        // 3. DB에서 사용자 정보 확인
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        // 4. 새 Access Token 생성
        String newAccessToken = jwtTokenProvider.createAccessToken(userId, username);

        // 5. 응답 객체 생성
        return new NewAccessTokenResponse("success", newAccessToken);
    }
}


