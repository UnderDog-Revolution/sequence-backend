package sequence.sequence_member.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sequence.sequence_member.report.entity.ReportEntity;

public interface ReportRepository extends JpaRepository<ReportEntity, Long> {
}
