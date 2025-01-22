package sequence.sequence_member.filter.entity;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@Data
@Table(name = "comment")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(nullable = false)
    private ProjectEntity projectId;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    private String writer;
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createDate;
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedDateTime;
    @ManyToOne
    @JoinColumn
    private CommentEntity parentComment;
    @OneToMany(mappedBy = "parentComment", orphanRemoval = true)
    private List<CommentEntity> childComment = new ArrayList<>();
}