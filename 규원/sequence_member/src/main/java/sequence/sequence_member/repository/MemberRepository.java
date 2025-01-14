package sequence.sequence_member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sequence.sequence_member.entity.MemberEntity;

import java.lang.reflect.Member;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity,Long> {
    Optional<MemberEntity> findByUsername(String username);
}
