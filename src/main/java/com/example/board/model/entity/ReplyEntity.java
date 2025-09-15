package com.example.board.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.ZonedDateTime;

@Entity
@Table(
        name = "reply",
        indexes = {@Index(name = "reply_userid_idx", columnList = "userid"),
                @Index(name = "reply_postid_idx", columnList = "postid")}
)
@SQLDelete(sql = "UPDATE \"reply\" SET deletedAt = CURRENT_TIMESTAMP WHERE replyid = ?")
@SQLRestriction("deletedAt IS NULL")
@Getter
@Setter
public class ReplyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyId;

    @Column(columnDefinition = "TEXT")
    private String body;

    @Column
    private ZonedDateTime createdAt;

    @Column
    private ZonedDateTime updatedAt;

    @Column
    private ZonedDateTime deletedAt;

    @ManyToOne
    @JoinColumn(name = "userid")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "postid")
    private PostEntity post;

    public static ReplyEntity of(String body, UserEntity user,  PostEntity post) {
        var replyEntity = new ReplyEntity();
        replyEntity.setBody(body);
        replyEntity.setUser(user);
        replyEntity.setPost(post);

        return replyEntity;
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
