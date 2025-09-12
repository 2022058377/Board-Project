package com.example.board.model.post;

import com.example.board.model.entity.PostEntity;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Post(
        Long postId,
        String body,
        ZonedDateTime createdAt,
        ZonedDateTime updatedAt,
        ZonedDateTime deletedAt
) {

    public static Post from(PostEntity entity) {
        return new Post(
                entity.getPostId(),
                entity.getBody(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
    }
}
