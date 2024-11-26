package com.example.login_logout.validate.service;

import com.example.login_logout.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberValidationService {

    private final MemberRepository memberRepository;

    public MemberValidationService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public boolean validateMember(Long memberId) {
        // DB에서 해당 userId를 가진 멤버가 존재하는지 확인
        return memberRepository.existsById(memberId);
    }
}
