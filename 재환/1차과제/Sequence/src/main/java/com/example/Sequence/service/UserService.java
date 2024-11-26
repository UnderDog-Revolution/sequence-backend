package com.example.Sequence.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.Sequence.entity.User;
import com.example.Sequence.repository.UserRepository;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import com.example.Sequence.entity.RefreshToken;
import com.example.Sequence.repository.RefreshTokenRepository;
import java.time.Instant;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findById(username)
            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));

        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getId())
            .password(user.getPassword())
            .roles("USER")
            .build();
    }

    public User login(String id, String password) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return user;
    }

    public User register(User user) {
        // 아이디 중복 검사
        if (userRepository.findById(user.getId()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }
        
        // 이메일 중복 검사
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        
        // 전화번호 중복 검사
        if (userRepository.findByPhoneNumber(user.getPhoneNumber()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 전화번호입니다.");
        }
        
        return userRepository.save(user);
    }

    public String savePortfolioFile(MultipartFile file, String userId) {
        try {
            String fileName = userId + "_" + file.getOriginalFilename();
            String filePath = "E:\\Github_Study\\Sequence\\Sequence\\uploads\\portfolio\\" + fileName;
    
            // 파일 저장
            file.transferTo(new File(filePath));
    
            // User 엔티티 업데이트
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
            user.setPortfolioFile(fileName);
            userRepository.save(user);
    
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("파일 저장에 실패했습니다: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("알 수 없는 오류 발생: " + e.getMessage(), e);
        }
    }

    public String saveProfileImage(MultipartFile file, String userId) {
        try {
            String fileName = userId + "_profile_" + file.getOriginalFilename();
            String filePath = "E:\\Github_Study\\Sequence\\Sequence\\uploads\\profileImage\\" + fileName;
            
            // 파일 저장
            file.transferTo(new File(filePath));
            
            // User 엔티티 업데이트
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
            user.setProfileImage(fileName);
            userRepository.save(user);
            
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("프로필 이미지 저장에 실패했습니다.", e);
        }
    }

    public boolean existsByUserId(String userId) {
        log.debug("Checking existence for userId: {}", userId);
        boolean exists = userRepository.findById(userId).isPresent();
        log.debug("User exists result: {}", exists);
        return exists;
    }

    @Transactional
    public void saveRefreshToken(String userId, String token) {
        RefreshToken refreshToken = RefreshToken.builder()
                .userId(userId)
                .token(token)
                .expiryDate(Instant.now().plusMillis(7 * 24 * 60 * 60 * 1000L).toEpochMilli())
                .build();
        
        // 기존 리프레시 토큰이 있다면 삭제
        refreshTokenRepository.deleteByUserId(userId);
        refreshTokenRepository.save(refreshToken);
    }

    public boolean validateRefreshToken(String userId, String token) {
        return refreshTokenRepository.findByUserIdAndToken(userId, token)
                .filter(refreshToken -> refreshToken.getExpiryDate() > Instant.now().toEpochMilli())
                .isPresent();
    }

    public User findById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    @Transactional
    public void deleteRefreshToken(String userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
}