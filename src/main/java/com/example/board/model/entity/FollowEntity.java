package com.example.board.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Entity
@Table(
        name = "\"follow\"",
        indexes = {@Index(name = "follow_follower_following_idx",
                columnList = "follower, following", unique = true)}
)
@Getter
@Setter
public class FollowEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long followId;

    @Column
    private ZonedDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "follower")
    private UserEntity follower;

    @ManyToOne
    @JoinColumn(name = "following")
    private UserEntity following;

    public static FollowEntity of(UserEntity follower, UserEntity following) {
        var followEntity = new FollowEntity();
        followEntity.setFollower(follower);
        followEntity.setFollowing(following);

        return followEntity;
    }

    @PrePersist
    private void prePersist() {
        this.createdAt = ZonedDateTime.now();
    }
}
