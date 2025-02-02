package sequence.sequence_member.report.dto;

import lombok.*;

import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
public class ReportDTO {
    private Long id;
    private String name;
    private String reporter;
    private String birthdate;
    private String education;
    private String reportDetail;

    private List<String> reportTypes;
    public ReportDTO() {}

    public ReportDTO(String name, String reporter, String birthdate, String education, List<String> reportTypes, String reportDetail) {
        this.name = name;
        this.reporter = reporter;
        this.birthdate = birthdate;
        this.education = education;
        this.reportDetail = reportDetail;
        this.reportTypes = reportTypes;
    }

}