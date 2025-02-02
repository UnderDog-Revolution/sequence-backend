package sequence.sequence_member.report.dto;

import jakarta.persistence.*;
import lombok.Data;
import sequence.sequence_member.report.entity.ReportEntity;
import sequence.sequence_member.member.entity.MemberEntity;

import java.time.LocalDateTime;

@Data
public class ReportDTO {
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

    private Long reporterId;
    private Long reportedId;


    // 신고자 ID 설정 메서드 추가
    public void setReporterId(Long reporterId) {
        this.reporterId = reporterId;
    }

    //피신고자 ID 설정 메서드 추가
    public void setReportedId(Long reportedId) {
        this.reportedId = reportedId;
    }

    //신고 정보를 DTO에서 엔티티로 변환하는 메서드
    public static ReportEntity toReportEntity(MemberEntity reporter, MemberEntity reported, String reportTypes, String reportDetail) {
        ReportEntity reportEntity = new ReportEntity();
        reportEntity.setReporter(reporter);
        reportEntity.setReported(reported);
        reportEntity.setReportTypes(reportTypes);
        reportEntity.setReportDetail(reportDetail);
        reportEntity.setReportDate(LocalDateTime.now());
        return reportEntity;
    }


    //Entity -> DTO 변환
    public static ReportDTO fromEntity(ReportEntity reportEntity) {
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setReportId(reportEntity.getReportId());
        reportDTO.setReporterId(reportEntity.getReporter().getId());  //신고자 ID 설정
        reportDTO.setReportedId(reportEntity.getReported().getId());  //피신고자 ID 설정
        reportDTO.setReportDetail(reportEntity.getReportDetail());
        reportDTO.setReportDate(reportEntity.getReportDate());
        return reportDTO;
    }
}
