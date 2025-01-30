package sequence.sequence_member.report.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "reports")
public class ReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String birthdate;
    private String education;

    private String reportDetail;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "report_types", joinColumns = @JoinColumn(name = "report_id"))
    @Column(name = "report_type")
    private List<String> reportTypes;

    public ReportEntity() {}

    public ReportEntity(String name, String birthdate, String education, List<String> reportTypes, String reportDetail) {
        this.name = name;
        this.birthdate = birthdate;
        this.education = education;
        this.reportDetail = reportDetail;
        this.reportTypes = reportTypes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public List<String> getReportTypes() {
        return reportTypes;
    }

    public void setReportTypes(List<String> reportTypes) {
        this.reportTypes = reportTypes;
    }

    public String getReportDetail() {
        return reportDetail;
    }

    public void setReportDetail(String reportDetail) {
        this.reportDetail = reportDetail;
    }
}