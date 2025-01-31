package sequence.sequence_member.mypage.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import sequence.sequence_member.member.entity.AwardEntity;
import sequence.sequence_member.member.entity.CareerEntity;
import sequence.sequence_member.member.entity.EducationEntity;
import sequence.sequence_member.member.entity.ExperienceEntity;
import sequence.sequence_member.member.entity.MemberEntity;
import sequence.sequence_member.member.repository.AwardRepository;
import sequence.sequence_member.member.repository.CareerRepository;
import sequence.sequence_member.member.repository.EducationRepository;
import sequence.sequence_member.member.repository.ExperienceRepository;
import sequence.sequence_member.member.repository.MemberRepository;
import sequence.sequence_member.mypage.dto.MyPageDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final MemberRepository memberRepository;
    private final AwardRepository awardRepository;
    private final CareerRepository careerRepository;
    private final EducationRepository educationRepository;
    private final ExperienceRepository experienceRepository;

    // 마이페이지 데이터 저장
    @Transactional
    public void updateMyPage(MyPageDTO myPageDTO) {
        // 1. 회원 정보 가져오기
        MemberEntity member = memberRepository.findById(myPageDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다."));

        // 2. 수상 내역 저장
        List<AwardEntity> awards = myPageDTO.getAwards().stream()
                .map(dto -> new AwardEntity(dto.getAwardName(), dto.getAwardDuration(), dto.getAwardDescription(), member))
                .collect(Collectors.toList());
        awardRepository.saveAll(awards);

        // 3. 경력 저장
        List<CareerEntity> careers = myPageDTO.getCareers().stream()
                .map(dto -> new CareerEntity(dto.getCareerName(), dto.getCareerDuration(), dto.getCareerDescription(), member))
                .collect(Collectors.toList());
        careerRepository.saveAll(careers);

        // 4. 경험 저장
        List<ExperienceEntity> experiences = myPageDTO.getExperiences().stream()
                .map(dto -> new ExperienceEntity(dto.getActivityName(), dto.getActivityDuration(), dto.getActivityDescription(), member))
                .collect(Collectors.toList());
        experienceRepository.saveAll(experiences);

        // 5. 학력 저장 (단일 엔티티)
        MyPageDTO.EducationDTO eduDto = myPageDTO.getEducation();
        EducationEntity education = new EducationEntity(
                eduDto.getSchoolName(), eduDto.getMajor(), eduDto.getEntranceDate(),
                eduDto.getGraduationDate(), eduDto.getDegree(), eduDto.getSkillCategory(),
                eduDto.getDesiredJob(), member);
        educationRepository.save(education);
    }
}
