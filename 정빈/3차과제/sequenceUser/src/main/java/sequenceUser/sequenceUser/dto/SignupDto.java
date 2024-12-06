package sequenceUser.sequenceUser.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupDto {
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]{4,10}$", message = "아이디는 최소 4자 이상 10자 이하로 구성되어야 합니다.")
    private String userId; // 아이디

    @NotBlank
    private String username;

    @NotBlank
    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    private String email;

    @NotBlank
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 구성되어야 합니다.")
    private String password;

    private String gender;
    private String phoneNumber;
    private String address;

    @NotBlank
    private String birthDate;
}

