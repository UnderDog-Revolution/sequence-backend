package com.example.login_logout.common.repository;

import com.example.login_logout.common.entity.MemberRefreshToken;
import com.example.login_logout.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRefreshTokenRepository extends JpaRepository<MemberRefreshToken, Long> {
    MemberRefreshToken findByMember(Member member);
}