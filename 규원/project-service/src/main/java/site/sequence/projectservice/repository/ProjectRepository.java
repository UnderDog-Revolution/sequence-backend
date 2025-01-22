package site.sequence.projectservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.sequence.projectservice.entity.ProjectEntity;

public interface ProjectRepository extends JpaRepository<ProjectEntity,Long> {
}