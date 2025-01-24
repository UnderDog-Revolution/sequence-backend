package sequence.sequence_member.report.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import sequence.sequence_member.member.entity.MemberEntity;

@Entity
@Getter
@Setter
@Table(name = "reports")
public class ReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    @Column(name = "report_type", nullable = false)
    private String reportType;

    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @Column(name = "status", nullable = false)
    private String status = "Pending";

    public static ReportEntity toEntity(MemberEntity member, String reportType, String description) {
        ReportEntity report = new ReportEntity();
        report.setMember(member);
        report.setReportType(reportType);
        report.setDescription(description);
        return report;
    }
}
