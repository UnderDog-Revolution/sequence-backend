package sequence.sequence_member.member.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteDto {
    private String username;
    private String password;
    private String confirm_password;
}