package site.sequence.projectservice.entity;
import jakarta.persistence.*;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.sequence.projectservice.enums.Category;
import site.sequence.projectservice.enums.MeetingOption;
import site.sequence.projectservice.enums.Period;
import site.sequence.projectservice.enums.Step;
import site.sequence.projectservice.utils.BaseTimeEntity;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "project")
public class ProjectEntity extends BaseTimeEntity {
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
    private MeetingOption meetingOption;

    @Column(nullable = false)
    private Step step;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String introduce;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String article;

    private String link;

    @Column(nullable = false)
    private String writer;

    @OneToMany(mappedBy = "project",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProjectMemberEntity> members;

    // List<ProjectMemberEntity> 에서 username만을 가지고 있는 List<String> 반환
    public List<String> getMemberUsernames() {
        return this.getMembers() // List<ProjectMemberEntity> 반환
                .stream() // Stream<ProjectMemberEntity>
                .map(ProjectMemberEntity::getUsername) // 각 객체의 username 필드 추출
                .collect(Collectors.toList()); // List<String>으로 변환
    }
}