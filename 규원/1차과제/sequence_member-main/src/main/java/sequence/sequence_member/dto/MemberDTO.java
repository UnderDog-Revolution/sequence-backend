package sequence.sequence_member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import lombok.*;
import sequence.sequence_member.entity.EducationEntity.*;
import sequence.sequence_member.entity.MemberEntity.Gender;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberDTO {
    private Long id;
    private String user_id;
    private String user_pwd;
    private String name;
    private Date birth;
    private Gender gender;
    private String address;
    private String phone;
    private String email;
    private String introduction;
    private String web_url;
    private String school_name;
    private String major;
    private Date entrance_date;
    private Date graduation_date;
    private Degree degree;
    private SkillCategory skill_category;
    private DesiredJob desired_job;
    private String activity_name;
    private Date activity_duration;
    private String activity_description;
    private String career_name;
    private Date career_duration;
    private String career_description;
    private String award_name;
    private Date award_duration;
    private String award_description;

}
