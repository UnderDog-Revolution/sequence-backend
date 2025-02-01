package sequence.sequence_member.deleteAccount.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "deleted")
public class DeletedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deleteId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private Boolean isDeleted;

    @Column
    private String reason; // 삭제 이유

    @Column
    private LocalDateTime deletedAt; // 삭제된 시간

    // 기본 생성자
    public DeletedEntity() {}

    // 생성자
    public DeletedEntity(Long userId , String username, Boolean isDeleted, String reason) {
        this.userId = userId;
        this.username = username;
        this.reason = reason;
        this.isDeleted = isDeleted;
        this.deletedAt = LocalDateTime.now();
    }
}
