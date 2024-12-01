package sequence.sequence_member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sequence.sequence_member.dto.LoginDTO;
import sequence.sequence_member.dto.LoginResDTO;
import sequence.sequence_member.dto.MemberDTO;
import sequence.sequence_member.entity.*;
import sequence.sequence_member.repository.*;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {
    @Autowired
    private final MemberRepository memberRepository;
    private final AwardRepository awardRepository;
    private final CareerRepository careerRepository;
    private final EducationRepository educationRepository;
    private final ExperienceRepository experienceRepository;

    public void save(MemberDTO memberDTO){
        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        ExperienceEntity experienceEntity = ExperienceEntity.toExperienceEntity(memberDTO);
        EducationEntity educationEntity = EducationEntity.toEducationEntity(memberDTO);
        CareerEntity careerEntity = CareerEntity.toCareerEntity(memberDTO);
        AwardEntity awardEntity = AwardEntity.toAwardEntity(memberDTO);

        memberRepository.save(memberEntity);
        experienceRepository.save(experienceEntity);
        educationRepository.save(educationEntity);
        careerRepository.save(careerEntity);
        awardRepository.save(awardEntity);
    }



    public LoginResDTO loginCheck(LoginDTO loginDTO){
        Optional<MemberEntity> byMemberEntity =  memberRepository.findByUserId(loginDTO.getUser_id());
        if(byMemberEntity.isPresent()){
            MemberEntity memberEntity =  byMemberEntity.get();
            if(memberEntity.getUserPwd().equals(loginDTO.getUser_pwd())){
                //로그인 성공 시 처리 로직
                String randomKey = UUID.randomUUID().toString();
                memberEntity.setRandomKey(randomKey);
                memberRepository.save(memberEntity);
                return new LoginResDTO("success",randomKey,loginDTO.getUser_id());
            } else {
                return new LoginResDTO("fail",null, loginDTO.getUser_id());
            }
        }else return new LoginResDTO("fail", null, loginDTO.getUser_id());

    }

    public boolean validate(String userId, String randomKey) {
        Optional<MemberEntity> member = memberRepository.findByUserId(userId);
        if (member.isPresent()) {
            // 사용자 랜덤키 검증
            return randomKey.equals(member.get().getRandomKey());
        }
        return false; // 사용자가 없거나 랜덤키가 틀린 경우
    }
}
