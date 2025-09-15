package com.example.board.model.reply;

import com.example.board.model.entity.ReplyEntity;
import com.example.board.model.post.Post;
import com.example.board.model.user.User;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Reply(
        Long replyId,
        String body,
        User user,
        Post post,
        ZonedDateTime createdAt,
        ZonedDateTime updatedAt,
        ZonedDateTime deletedAt
) {

    public static Reply from(ReplyEntity entity) {
        return new Reply(
                entity.getReplyId(),
                entity.getBody(),
                User.from(entity.getUser()),
                Post.from(entity.getPost()),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
    }
}
