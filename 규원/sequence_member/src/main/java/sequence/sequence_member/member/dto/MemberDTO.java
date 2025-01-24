package sequence.sequence_member.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import lombok.*;
import sequence.sequence_member.member.entity.EducationEntity.*;
import sequence.sequence_member.member.entity.MemberEntity;
import sequence.sequence_member.member.entity.MemberEntity.Gender;

import java.util.ArrayList;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MemberDTO {
    private Long id;

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    @Size(min= 4, max= 10, message = "아이디는 최소 4자 이상 최대 10자 이하입니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}", message = "비밀번호는 8~20자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    private Date birth;

    private Gender gender;

    @NotBlank(message = "주소지는 필수 입력 값입니다.")
    private String address;

    @NotBlank(message = "휴대폰 번호는 필수 입력 값입니다.")
    private String phone;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    private String introduction;
    private String web_url;
    private String school_name;
    private String major;
    private Date entrance_date;
    private Date graduation_date;
    private Degree degree;
    private SkillCategory skill_category;
    private ArrayList<DesiredJob> desiredJob;
    private String activity_name;
    private Date activity_duration;
    private String activity_description;
    private String career_name;
    private Date career_duration;
    private String career_description;
    private String award_name;
    private Date award_duration;
    private String award_description;

    // 추가 코드 : MemberDTO 변환 메서드 추가
    public static MemberDTO fromMemberEntity(MemberEntity memberEntity) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(memberEntity.getId());
        memberDTO.setUsername(memberEntity.getUsername());
        memberDTO.setPassword(memberEntity.getPassword());
        memberDTO.setName(memberEntity.getName());
        memberDTO.setBirth(memberEntity.getBirth());
        memberDTO.setGender(memberEntity.getGender());
        memberDTO.setAddress(memberEntity.getAddress());
        memberDTO.setPhone(memberEntity.getPhone());
        memberDTO.setEmail(memberEntity.getEmail());
        memberDTO.setIntroduction(memberEntity.getIntroduction());
        memberDTO.setWeb_url(memberEntity.getWebUrl());
        return memberDTO;
    }
}
