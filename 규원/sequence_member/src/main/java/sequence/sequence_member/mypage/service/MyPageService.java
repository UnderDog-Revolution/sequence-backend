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
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final MemberRepository memberRepository;
    private final AwardRepository awardRepository;
    private final CareerRepository careerRepository;
    private final EducationRepository educationRepository;
    private final ExperienceRepository experienceRepository;

    // 마이페이지 조회
    public MyPageDTO searchMyPage(Long userId) {
        // 1. 회원 정보 가져오기
        MemberEntity member = memberRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다."));

        return convertToDTO(member);
    }

    private MyPageDTO convertToDTO(MemberEntity member) {
        MyPageDTO dto = new MyPageDTO();
        dto.setUserId(member.getId());
        dto.setName(member.getName());
        dto.setBirth(member.getBirth());
        dto.setGender(member.getGender());
        dto.setAddress(member.getAddress());
        dto.setPhone(member.getPhone());
        dto.setEmail(member.getEmail());
        dto.setIntroduction(member.getIntroduction());
        dto.setWebUrl(member.getWebUrl());

        dto.setAwards(member.getAwards().stream()
                .map(award -> {
                    MyPageDTO.AwardDTO awardDTO = new MyPageDTO.AwardDTO();
                    awardDTO.setAwardName(award.getAwardName());
                    awardDTO.setAwardDuration(award.getAwardDuration());
                    awardDTO.setAwardDescription(award.getAwardDescription());
                    return awardDTO;
                })
                .collect(Collectors.toList()));

        dto.setCareers(member.getCareers().stream()
                .map(career -> {
                    MyPageDTO.CareerDTO careerDTO = new MyPageDTO.CareerDTO();
                    careerDTO.setCareerName(career.getCareerName());
                    careerDTO.setCareerDuration(career.getCareerDuration());
                    careerDTO.setCareerDescription(career.getCareerDescription());
                    return careerDTO;
                })
                .collect(Collectors.toList()));

        dto.setExperiences(member.getExperiences().stream()
                .map(experience -> {
                    MyPageDTO.ExperienceDTO experienceDTO = new MyPageDTO.ExperienceDTO();
                    experienceDTO.setActivityName(experience.getActivityName());
                    experienceDTO.setActivityDuration(experience.getActivityDuration());
                    experienceDTO.setActivityDescription(experience.getActivityDescription());
                    return experienceDTO;
                })
                .collect(Collectors.toList()));

        if (member.getEducation() != null) {
            MyPageDTO.EducationDTO educationDTO = new MyPageDTO.EducationDTO();
            educationDTO.setSchoolName(member.getEducation().getSchoolName());
            educationDTO.setMajor(member.getEducation().getMajor());
            educationDTO.setEntranceDate(member.getEducation().getEntranceDate());
            educationDTO.setGraduationDate(member.getEducation().getGraduationDate());
            educationDTO.setDegree(member.getEducation().getDegree());
            educationDTO.setSkillCategory(member.getEducation().getSkillCategory());
            educationDTO.setDesiredJob(member.getEducation().getDesiredJob());

            dto.setEducation(educationDTO);
        }

        return dto;
    }

    /**
     * 사용자 마이페이지 정보를 업데이트하는 메서드입니다.
     *
     * 현재는 기존 데이터를 삭제하고 새로 입력된 데이터를 저장하는 방식으로 구현되어 있습니다.
     * 이 방식은 입력에 없는 데이터를 삭제하기 위해 선택되었습니다. 왜냐하면, 입력된 데이터와 기존 데이터를 비교하여
     * 어떤 데이터를 삭제할지 결정하는 로직을 구현하기 어려운 상황이기 때문입니다.
     * 이 방식은 데이터가 많을 경우 성능에 비효율적일 수 있으므로, 향후 효율적인 방식으로 개선이 필요합니다.
     */
    // 마이페이지 데이터 저장
    @Transactional
    public void updateMyPage(MyPageDTO myPageDTO) {
        // 1. 회원 정보 가져오기
        MemberEntity member = memberRepository.findById(myPageDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다."));

        // 기본 정보 업데이트 (변경 감지 사용)
        member.setName(myPageDTO.getName());
        member.setBirth(myPageDTO.getBirth());
        member.setGender(myPageDTO.getGender());
        member.setAddress(myPageDTO.getAddress());
        member.setPhone(myPageDTO.getPhone());
        member.setEmail(myPageDTO.getEmail());
        member.setIntroduction(myPageDTO.getIntroduction());
        member.setWebUrl(myPageDTO.getWebUrl());

        // 수상 내역 업데이트
        List<AwardEntity> existingAwards = awardRepository.findByMember(member);
        awardRepository.deleteAll(existingAwards);

        for (MyPageDTO.AwardDTO dto : myPageDTO.getAwards()) {
            AwardEntity newAward = new AwardEntity(dto.getAwardName(), dto.getAwardDuration(), dto.getAwardDescription(), member);
            awardRepository.save(newAward);
        }

        // 경력 업데이트
        List<CareerEntity> existingCareers = careerRepository.findByMember(member);
        careerRepository.deleteAll(existingCareers);

        for (MyPageDTO.CareerDTO dto : myPageDTO.getCareers()) {
            CareerEntity newCareer = new CareerEntity(dto.getCareerName(), dto.getCareerDuration(), dto.getCareerDescription(), member);
            careerRepository.save(newCareer);
        }

        // 경험 업데이트
        List<ExperienceEntity> existingExperiences = experienceRepository.findByMember(member);
        experienceRepository.deleteAll(existingExperiences);

        for (MyPageDTO.ExperienceDTO dto : myPageDTO.getExperiences()) {
            ExperienceEntity newExperience = new ExperienceEntity(dto.getActivityName(), dto.getActivityDuration(), dto.getActivityDescription(), member);
            experienceRepository.save(newExperience);
        }

        // 학력 업데이트 (없으면 생성, 있으면 수정)
        educationRepository.findByMember(member).ifPresentOrElse(
                education -> {
                    education.updateEducation(
                            myPageDTO.getEducation().getSchoolName(),
                            myPageDTO.getEducation().getMajor(),
                            myPageDTO.getEducation().getEntranceDate(),
                            myPageDTO.getEducation().getGraduationDate(),
                            myPageDTO.getEducation().getDegree(),
                            myPageDTO.getEducation().getSkillCategory(),
                            myPageDTO.getEducation().getDesiredJob()
                    );
                },
                () -> {
                    EducationEntity newEducation = new EducationEntity(
                            myPageDTO.getEducation().getSchoolName(),
                            myPageDTO.getEducation().getMajor(),
                            myPageDTO.getEducation().getEntranceDate(),
                            myPageDTO.getEducation().getGraduationDate(),
                            myPageDTO.getEducation().getDegree(),
                            myPageDTO.getEducation().getSkillCategory(),
                            myPageDTO.getEducation().getDesiredJob(),
                            member
                    );
                    educationRepository.save(newEducation);
                }
        );
    }
}
