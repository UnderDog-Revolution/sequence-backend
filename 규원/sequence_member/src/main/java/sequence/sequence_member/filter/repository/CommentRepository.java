package sequence.sequence_member.filter.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import sequence.sequence_member.filter.entity.CommentEntity;
public interface CommentRepository extends JpaRepository<CommentEntity,Long> {
}