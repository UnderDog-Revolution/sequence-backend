//package sequence.sequence_member.report.dto;
//
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Size;
//import lombok.*;
//import sequence.sequence_member.report.entity.ReportEntity;
//
//import java.util.Date;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class ReportDTO {
//    private Long id;
//
//    @NotNull(message = "신고 유형을 선택해주세요.")
//    private ReportEntity.ReportType reportType;
//
//    @NotBlank(message = "신고 내용을 작성해주세요.")
//    @Size(max = 500, message = "신고 내용은 500자 이하로 작성해야 합니다.")
//    private String content;
//
//    private Date createdAt;
//}
package sequence.sequence_member.report.dto;

import lombok.Data;
import sequence.sequence_member.report.entity.ReportEntity;

import java.time.LocalDateTime;

@Data
public class ReportDTO {

    private Long reportId; // 신고 ID
    private String reporterUsername; // 신고자 (username 기반)
    private String reportedUsername; // 피신고자 (username 기반)
    private Long reporterId; // 신고자 ID (추가)
    private Long reportedId; // 피신고자 ID (추가)
    private String reportTypes; // 신고 유형
    private String reportDetail; // 신고 상세 내용
    private LocalDateTime reportDate; // 신고 일자

    // Entity -> DTO 변환 메서드
    public static ReportDTO fromEntity(ReportEntity reportEntity) {
        ReportDTO dto = new ReportDTO();
        dto.setReportId(reportEntity.getReportId());
        dto.setReporterUsername(reportEntity.getReporter().getUsername());
        dto.setReportedUsername(reportEntity.getReported().getUsername());
        dto.setReporterId(reportEntity.getReporter().getId()); // 추가
        dto.setReportedId(reportEntity.getReported().getId()); // 추가
        dto.setReportTypes(reportEntity.getReportTypes());
        dto.setReportDetail(reportEntity.getReportDetail());
        dto.setReportDate(reportEntity.getReportDate());
        return dto;
    }
}
