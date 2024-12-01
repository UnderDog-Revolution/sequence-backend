package sequence.sequence_member.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sequence.sequence_member.entity.MemberEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {
    private String user_id;
    private String user_pwd;

    public static LoginDTO toLoginDTO(MemberEntity memberEntity){
        LoginDTO loginDTO  = new LoginDTO();
        loginDTO.setUser_id(memberEntity.getUserId());
        loginDTO.setUser_pwd(memberEntity.getUserPwd());

        return loginDTO;
    }
}
