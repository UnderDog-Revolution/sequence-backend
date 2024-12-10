package com.example.login_logout.member.repository;

import com.example.login_logout.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);

    // 이미 존재하는 사용자인지 확인
    boolean existsByUsername(String username);
}
