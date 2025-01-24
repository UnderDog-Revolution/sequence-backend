package site.sequence.projectservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.sequence.projectservice.entity.ProjectInvitedMemberEntity;

public interface ProjectInvitedMemberEntityRepository extends JpaRepository<ProjectInvitedMemberEntity,Long> {
}
