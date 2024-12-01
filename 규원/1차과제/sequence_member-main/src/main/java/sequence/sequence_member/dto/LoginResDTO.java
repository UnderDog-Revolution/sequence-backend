package sequence.sequence_member.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginResDTO {
    private String message;
    private String randomKey;
    private String user_id;
    public LoginResDTO (String message, String randomKey, String user_id){
        this.message=message;
        this.randomKey=randomKey;
        this.user_id=user_id;
    }
}
