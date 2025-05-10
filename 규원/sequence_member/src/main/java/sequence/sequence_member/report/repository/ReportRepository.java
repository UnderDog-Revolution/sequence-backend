package sequence.sequence_member.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sequence.sequence_member.report.entity.ReportEntity;

import java.util.List;

public interface ReportRepository extends JpaRepository<ReportEntity, Long> {

    //특정 사용자가 작성한 모든 신고 내역 조회
    List<ReportEntity> findByReporterId(Long reporterId);
}