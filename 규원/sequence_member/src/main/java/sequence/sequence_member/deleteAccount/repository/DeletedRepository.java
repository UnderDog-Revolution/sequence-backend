package sequence.sequence_member.deleteAccount.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sequence.sequence_member.deleteAccount.entity.DeletedEntity;

import java.util.Optional;

public interface DeletedRepository extends JpaRepository<DeletedEntity, Long> {
    Optional<DeletedEntity> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByUserId(Long userId);
}
