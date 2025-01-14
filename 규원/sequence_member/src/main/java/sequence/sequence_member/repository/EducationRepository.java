package sequence.sequence_member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sequence.sequence_member.entity.EducationEntity;

public interface EducationRepository extends JpaRepository<EducationEntity, Long> {
}
