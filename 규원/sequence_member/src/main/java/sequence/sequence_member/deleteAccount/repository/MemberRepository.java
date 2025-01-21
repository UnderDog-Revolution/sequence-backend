package sequence.sequence_member.deleteAccount.repository;

import sequence.sequence_member.deleteAccount.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity,Long> {
    Optional<MemberEntity> findByUsername(String username);
}
