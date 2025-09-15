package com.example.board.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.ZonedDateTime;

@Entity
@Table(
        name = "post",
        indexes = {@Index(name = "post_userid_idx", columnList = "userid")}
)
@SQLDelete(sql = "UPDATE \"post\" SET deletedAt = CURRENT_TIMESTAMP WHERE postid = ?")
@SQLRestriction("deletedAt IS NULL")
@Getter
@Setter
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(columnDefinition = "TEXT")
    private String body;

    @Column
    private Long repliesCount;

    @Column
    private Long likesCount;

    @Column
    private ZonedDateTime createdAt;

    @Column
    private ZonedDateTime updatedAt;

    @Column
    private ZonedDateTime deletedAt;

    @ManyToOne
    @JoinColumn(name = "userid")
    private UserEntity user;

    public static PostEntity of(String body, UserEntity user) {
        var postEntity = new PostEntity();
        postEntity.setBody(body);
        postEntity.setUser(user);

        return postEntity;
    }

    @PrePersist
    private void prePersist() {
        this.createdAt = ZonedDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    private void preUpdate() {
        this.updatedAt = ZonedDateTime.now();
    }
}
