package com.example.board.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Entity
@Table(
        name = "\"like\"",
        indexes = {@Index(name = "like_userid_postid_idx", columnList = "userid, postid", unique = true)}
)
@Getter
@Setter
public class LikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @Column
    private ZonedDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "userid")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "postid")
    private PostEntity post;

    public static LikeEntity of(UserEntity user, PostEntity post) {
        var likeEntity = new LikeEntity();
        likeEntity.setUser(user);
        likeEntity.setPost(post);

        return likeEntity;
    }

    @PrePersist
    private void prePersist() {
        this.createdAt = ZonedDateTime.now();
    }
}
