package sequenceUser.sequenceUser.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]{4,10}$", message = "아이디는 최소 4자 이상 10자 이하로 구성되어야 합니다.")
    private String userId;

    @NotBlank
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 구성되어야 합니다.")
    private String password;
}

