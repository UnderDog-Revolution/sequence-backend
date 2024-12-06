package sequenceUser.sequenceUser.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sequenceUser.sequenceUser.config.JwtTokenProvider;
import sequenceUser.sequenceUser.dto.LoginDto;
import sequenceUser.sequenceUser.dto.TokenDto;
import sequenceUser.sequenceUser.entity.User;
import sequenceUser.sequenceUser.exception.CustomException;
import sequenceUser.sequenceUser.repository.UserRepository;
import sequenceUser.sequenceUser.util.PasswordUtil;

@Service
public class LoginService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenDto authenticateUser(LoginDto loginDto) {
        User user = userRepository.findByUserId(loginDto.getUserId())
                .orElseThrow(() -> new CustomException("존재하지 않는 아이디입니다.", HttpStatus.NOT_FOUND));

        if (!PasswordUtil.verifyPassword(loginDto.getPassword(), user.getPassword())) {
            throw new CustomException("비밀번호가 올바르지 않습니다.", HttpStatus.UNAUTHORIZED);
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getUserId());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUserId());

        return new TokenDto("success", accessToken, refreshToken);
    }
}

