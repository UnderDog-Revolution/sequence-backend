package sequenceUser.sequenceUser.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sequenceUser.sequenceUser.dto.SignupDto;
import sequenceUser.sequenceUser.entity.User;
import sequenceUser.sequenceUser.exception.CustomException;
import sequenceUser.sequenceUser.repository.UserRepository;
import sequenceUser.sequenceUser.util.PasswordUtil;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(SignupDto signupDto) {
        // 이메일 중복 확인
        if (userRepository.existsByEmail(signupDto.getEmail())) {
            throw new CustomException("이미 사용 중인 이메일입니다.", HttpStatus.BAD_REQUEST);
        }

        // 아이디 중복 확인
        if (userRepository.existsByUserId(signupDto.getUserId())) {
            throw new CustomException("이미 사용 중인 아이디입니다.", HttpStatus.BAD_REQUEST);
        }

        // 비밀번호 암호화
        String encryptedPassword = PasswordUtil.encryptPassword(signupDto.getPassword());

        // User 엔티티 생성 및 저장
        User user = new User();
        user.setUserId(signupDto.getUserId());
        user.setUsername(signupDto.getUsername());
        user.setEmail(signupDto.getEmail());
        user.setPassword(encryptedPassword);
        user.setGender(signupDto.getGender());
        user.setPhoneNumber(signupDto.getPhoneNumber());
        user.setAddress(signupDto.getAddress());
        user.setBirthDate(signupDto.getBirthDate());

        userRepository.save(user);
    }
}

