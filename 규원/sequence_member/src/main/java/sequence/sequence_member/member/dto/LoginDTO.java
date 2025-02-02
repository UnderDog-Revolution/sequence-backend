package sequence.sequence_member.member.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sequence.sequence_member.member.entity.MemberEntity;

//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class LoginDTO {
//    private String user_id;
//    private String user_pwd;
//
//    public static LoginDTO toLoginDTO(MemberEntity memberEntity){
//        LoginDTO loginDTO  = new LoginDTO();
//        loginDTO.setUser_id(memberEntity.getUsername());
//        loginDTO.setUser_pwd(memberEntity.getPassword());
//
//        return loginDTO;
//    }
//}

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {
    private String username; // user_id → username 변경
    private String password; // user_pwd → password 변경

    public static LoginDTO toLoginDTO(MemberEntity memberEntity){
        LoginDTO loginDTO  = new LoginDTO();
        loginDTO.setUsername(memberEntity.getUsername());
        loginDTO.setPassword(memberEntity.getPassword());
        return loginDTO;
    }
}
