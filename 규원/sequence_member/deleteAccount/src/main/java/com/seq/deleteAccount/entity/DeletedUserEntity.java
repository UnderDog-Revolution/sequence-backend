package com.seq.deleteAccount.entity;

import jakarta.persistence.*;


import java.time.LocalDateTime;

@Entity
@Table(name = "deleted")
public class DeletedUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deleteId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private Boolean isDeleted;

    @Column(nullable = true)
    private String reason; // 삭제 이유

    @Column(nullable = false)
    private LocalDateTime deletedAt; // 삭제된 시간

    // 기본 생성자
    public DeletedUserEntity() {}

    // 생성자
    public DeletedUserEntity(String username, Boolean isDeleted, String reason) {
        this.username = username;
        this.reason = reason;
        this.isDeleted = isDeleted;
        this.deletedAt = LocalDateTime.now();
    }
}
