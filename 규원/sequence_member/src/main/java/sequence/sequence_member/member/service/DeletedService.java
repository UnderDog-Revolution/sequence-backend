package sequence.sequence_member.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sequence.sequence_member.member.entity.DeletedEntity;
import sequence.sequence_member.member.repository.DeletedRepository;
import sequence.sequence_member.member.repository.ExperienceRepository;
import sequence.sequence_member.member.repository.MemberRepository;

@Service
public class DeletedService {

    private final DeletedRepository deletedRepository;
    private final MemberRepository memberRepository;
    private final ExperienceRepository experienceRepository;

    public DeletedService(DeletedRepository deletedUserRepository,
                          MemberRepository memberRepository,
                          ExperienceRepository experienceRepository
    ) {
        this.deletedRepository = deletedUserRepository;
        this.memberRepository = memberRepository;
        this.experienceRepository = experienceRepository;
    }

    @Transactional
    public void saveDeletedUser(Long userId, String username,Boolean is_deleted ,String reason) {
        DeletedEntity deletedUser = new DeletedEntity(userId, username, is_deleted, reason);
        deletedRepository.save(deletedUser);
    }

    public void deleteUser(Long userId) {
        memberRepository.deleteById(userId);
    }

    // 사용자 탈퇴 여부 확인(id)
    public boolean isDeletedUser(Long userId) {
        return deletedRepository.existsByUserId(userId);
    }

    // 사용자 탈퇴 여부 확인(username)
    public boolean isDeletedUser(String username) {
        return deletedRepository.existsByUsername(username);
    }
}
