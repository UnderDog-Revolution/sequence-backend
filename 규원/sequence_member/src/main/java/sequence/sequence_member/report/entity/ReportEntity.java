package sequence.sequence_member.report.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import sequence.sequence_member.member.entity.MemberEntity;
import sequence.sequence_member.report.dto.ReportDTO;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "reports")
public class ReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId; // 신고 ID

    @ManyToOne
    @JoinColumn(name = "reporter_id", nullable = false)
    private MemberEntity reporter; // 신고자

    @ManyToOne
    @JoinColumn(name = "reported_id", nullable = false)
    private MemberEntity reported; // 피신고자

    @Column(name = "report_types", nullable = false)
    private String reportTypes; // 신고 유형

    @Column(name = "report_detail", nullable = false, length = 500)
    private String reportDetail; // 신고 상세 내용

    @Column(name = "report_date", nullable = false)
    private LocalDateTime reportDate; // 신고 일자

    // DTO -> Entity 변환 (수정된 부분)
    public static ReportEntity toReportEntity(ReportDTO reportDTO, MemberEntity reporter, MemberEntity reported) {
        ReportEntity reportEntity = new ReportEntity();
        reportEntity.setReporter(reporter);
        reportEntity.setReported(reported);
        reportEntity.setReportTypes(reportDTO.getReportTypes());
        reportEntity.setReportDetail(reportDTO.getReportDetail());
        reportEntity.setReportDate(LocalDateTime.now()); // 현재 시간 설정
        return reportEntity;
    }

}
