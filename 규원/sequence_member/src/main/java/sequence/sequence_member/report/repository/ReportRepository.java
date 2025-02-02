package sequence.sequence_member.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sequence.sequence_member.report.entity.ReportEntity;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<ReportEntity, Long> {
    Boolean existsByName(String name);
    Boolean existsByReporter(String reporter);
}
