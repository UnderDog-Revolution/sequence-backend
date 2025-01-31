package sequence.sequence_member.mypage.dto;

import lombok.Data;
import sequence.sequence_member.member.entity.MemberEntity.Gender;
import sequence.sequence_member.member.entity.EducationEntity.Degree;
import sequence.sequence_member.member.entity.EducationEntity.SkillCategory;
import sequence.sequence_member.member.entity.EducationEntity.DesiredJob;

import java.util.Date;
import java.util.List;

@Data
public class MyPageDTO {
    private Long userId;
    private String password;
    private String name;
    private Date birth;
    private Gender gender;
    private String address;
    private String phone;
    private String email;
    private String introduction;
    private String webUrl;

    private List<AwardDTO> awards;
    private List<CareerDTO> careers;
    private List<ExperienceDTO> experiences;
    private EducationDTO education;

    @Data
    public static class AwardDTO {
        private String awardName;
        private Date awardDuration;
        private String awardDescription;
    }

    @Data
    public static class CareerDTO {
        private String careerName;
        private Date careerDuration;
        private String careerDescription;
    }

    @Data
    public static class ExperienceDTO {
        private String activityName;
        private Date activityDuration;
        private String activityDescription;
    }

    @Data
    public static class EducationDTO {
        private String schoolName;
        private String major;
        private Date entranceDate;
        private Date graduationDate;
        private Degree degree;
        private List<SkillCategory> skillCategory;
        private List<DesiredJob> desiredJob;
    }
}
