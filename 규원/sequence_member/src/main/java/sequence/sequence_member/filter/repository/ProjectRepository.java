package sequence.sequence_member.filter.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import sequence.sequence_member.filter.entity.ProjectEntity;
public interface ProjectRepository extends JpaRepository<ProjectEntity,Long> {
}