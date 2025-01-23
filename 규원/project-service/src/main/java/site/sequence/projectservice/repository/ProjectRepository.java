package site.sequence.projectservice.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import site.sequence.projectservice.entity.ProjectEntity;

public interface ProjectRepository extends JpaRepository<ProjectEntity,Long> {
    public Optional<ProjectEntity> getProjectEntityById(Long projectId);
}