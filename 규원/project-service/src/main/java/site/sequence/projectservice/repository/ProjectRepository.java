package site.sequence.projectservice.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import sequence.sequence_member.filter.entity.ProjectEntity;
public interface ProjectRepository extends JpaRepository<ProjectEntity,Long> {
}