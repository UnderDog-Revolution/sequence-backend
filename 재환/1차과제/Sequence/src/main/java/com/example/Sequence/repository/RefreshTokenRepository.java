package com.example.Sequence.repository;

import com.example.Sequence.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUserIdAndToken(String userId, String token);
    void deleteByUserId(String userId);
} 