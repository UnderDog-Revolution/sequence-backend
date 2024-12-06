package sequenceUser.sequenceUser.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenDto {
    private String status;
    private String token;
    private String refreshToken;
}

