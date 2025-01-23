package site.sequence.projectservice.entity;
import jakarta.persistence.*;
import java.util.List;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import java.time.LocalDateTime;
import site.sequence.projectservice.enums.Category;
import site.sequence.projectservice.enums.MeetingOption;
import site.sequence.projectservice.enums.Period;
import site.sequence.projectservice.enums.Step;

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
    @Enumerated(EnumType.STRING)
    private Period period;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    private int personnel;

    @Column(nullable = false)
    private String roles;

    @Column(nullable = false)
    private String skills;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MeetingOption meeting_option;

    @Column(nullable = false)
    private Step step;

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

    @OneToMany(mappedBy = "project",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProjectMemberEntity> members;
}