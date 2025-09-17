package com.example.board.model.user;

import java.time.ZonedDateTime;

public record Follower(
        Long userId,
        String username,
        String profile,
        String description,
        Long followersCount,
        Long followingsCount,
        ZonedDateTime createdAt,
        ZonedDateTime updatedAt,
        ZonedDateTime followedDateTime,
        Boolean isFollowing
) {

    public static Follower from(User user, ZonedDateTime followedDateTime) {
        return new Follower(
                user.userId(),
                user.username(),
                user.profile(),
                user.description(),
                user.followersCount(),
                user.followingsCount(),
                user.createdAt(),
                user.updatedAt(),
                followedDateTime,
                user.isFollowing()
        );
    }
}
