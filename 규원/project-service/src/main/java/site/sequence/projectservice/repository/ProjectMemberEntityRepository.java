package site.sequence.projectservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.sequence.projectservice.entity.ProjectMemberEntity;

public interface ProjectMemberEntityRepository extends JpaRepository<ProjectMemberEntity,Long > {
}
