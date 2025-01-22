package sequence.sequence_member.filter.entity;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import java.time.LocalDateTime;
@Entity
@Data
@Table(name = "project")
public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private Period period;
    @Column(nullable = false)
    private Category category;
    @Column(nullable = false)
    private int personnel;
    @Column(nullable = false)
    private String roles;
    @Column(nullable = false)
    private String skills;
    @Column(nullable = false)
    private MeetingOption meeting_option;
    @Column(nullable = false)
    private Step step;
    @Column(nullable = false)
    private String members;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String introduce;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String article;
    private String link;
    @Column(nullable = false)
    private String writer;
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createDate;
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedDateTime;
    public enum Period {
        OneMonthLess, OneToThreeMonth, ThreeToSixMonth, SixToOneYear, OverOneYear
    }
    public enum Category{
        대회, 창업, 대외활동, 경험, 스터디
    }
    public enum MeetingOption{
        오프라인, 온라인, 병행
    }
    public enum Step {
        BeforeStart, Planning, Designing, Developing, InBusiness
    }
}