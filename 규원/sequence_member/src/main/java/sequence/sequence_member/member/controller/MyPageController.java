package sequence.sequence_member.member.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sequence.sequence_member.member.entity.EducationEntity;
import sequence.sequence_member.member.entity.MemberEntity;
import sequence.sequence_member.member.repository.MemberRepository;
import sequence.sequence_member.member.response.ResponseMsg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MyPageController {
    @Autowired
    private final MemberRepository memberRepository;

    @GetMapping("/api/mypage")
    public ResponseEntity<ResponseMsg> getMyPage(@RequestParam Long userId) throws JsonProcessingException {
        MemberEntity member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다"));

        // MemberEntity 기본 정보
        Map<String, String> memberData = Map.of(
                "id", String.valueOf(member.getId()),
                "username", member.getUsername(),
                "name", member.getName(),
                "birth", member.getBirth().toString(),
                "gender", member.getGender().toString(),
                "address", member.getAddress(),
                "phone", member.getPhone(),
                "email", member.getEmail(),
                "introduction", member.getIntroduction(),
                "webUrl", member.getWebUrl() != null ? member.getWebUrl() : "N/A"
        );

        // AwardEntity 리스트 변환
        List<Map<String, String>> awardData = member.getAwards().stream()
                .map(award -> Map.of(
                        "awardId", String.valueOf(award.getAwardId()),
                        "awardName", award.getAwardName(),
                        "awardDuration", award.getAwardDuration() != null ? award.getAwardDuration().toString() : "N/A",
                        "awardDescription", award.getAwardDescription()
                ))
                .toList();

        // CareerEntity 리스트 변환
        List<Map<String, String>> careerData = member.getCareers().stream()
                .map(career -> Map.of(
                        "careerId", String.valueOf(career.getCareer_id()),
                        "careerName", career.getCareerName(),
                        "careerDuration", career.getCareerDuration() != null ? career.getCareerDuration().toString() : "N/A",
                        "careerDescription", career.getCareerDescription()
                ))
                .toList();

        // ExperienceEntity 리스트 변환
        List<Map<String, String>> experienceData = member.getExperiences().stream()
                .map(exp -> Map.of(
                        "experienceId", String.valueOf(exp.getExperienceId()),
                        "activityName", exp.getActivityName(),
                        "activityDuration", exp.getActivityDuration() != null ? exp.getActivityDuration().toString() : "N/A",
                        "activityDescription", exp.getActivityDescription()
                ))
                .toList();

        // EducationEntity 변환
        EducationEntity education = member.getEducation();
        Map<String, String> educationData = education != null ? Map.of(
                "educationId", String.valueOf(education.getEducationId()),
                "schoolName", education.getSchoolName(),
                "major", education.getMajor(),
                "entranceDate", education.getEntranceDate().toString(),
                "graduationDate", education.getGraduationDate() != null ? education.getGraduationDate().toString() : "N/A",
                "degree", education.getDegree().toString(),
                "skillCategory", education.getSkillCategory().toString(),
                "desiredJob", education.getDesiredJob().toString()
        ) : Map.of("education", "N/A");

        // 전체 응답 데이터 구성
        Map<String, String> resultData = new HashMap<>(memberData);

        // JSON으로 리스트와 복잡한 데이터를 직렬화
        ObjectMapper objectMapper = new ObjectMapper();
        resultData.put("awards", objectMapper.writeValueAsString(awardData));
        resultData.put("careers", objectMapper.writeValueAsString(careerData));
        resultData.put("experiences", objectMapper.writeValueAsString(experienceData));
        resultData.put("education", objectMapper.writeValueAsString(educationData));

        ResponseMsg responseMsg = new ResponseMsg(200, "사용자 정보를 성공적으로 가져왔습니다", resultData);
        return ResponseEntity.ok().body(responseMsg);
    }
}