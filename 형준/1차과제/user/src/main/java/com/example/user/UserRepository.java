package com.example.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Member, Integer> {
    Optional<Member> findByUsername(String username);
}
