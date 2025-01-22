package site.sequence.projectservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.sequence.projectservice.entity.CommentEntity;

public interface CommentRepository extends JpaRepository<CommentEntity,Long> {
}