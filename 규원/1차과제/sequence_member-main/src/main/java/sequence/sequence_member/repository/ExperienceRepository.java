package sequence.sequence_member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sequence.sequence_member.entity.ExperienceEntity;

public interface ExperienceRepository extends JpaRepository<ExperienceEntity, Long> {
}
