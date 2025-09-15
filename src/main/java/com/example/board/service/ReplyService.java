package com.example.board.service;

import com.example.board.exception.post.PostNotFountException;
import com.example.board.exception.reply.ReplyNotFountException;
import com.example.board.exception.user.UserNotAllowedException;
import com.example.board.model.entity.ReplyEntity;
import com.example.board.model.entity.UserEntity;
import com.example.board.model.reply.Reply;
import com.example.board.model.reply.ReplyPatchRequestBody;
import com.example.board.model.reply.ReplyRequestBody;
import com.example.board.repository.PostEntityRepository;
import com.example.board.repository.ReplyEntityRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReplyService {

    @Autowired private ReplyEntityRepository replyEntityRepository;

    @Autowired private PostEntityRepository postEntityRepository;

    public List<Reply> getRepliesByPostId(Long postId) {

        var postEntity = postEntityRepository.findById(postId)
                .orElseThrow(() -> new PostNotFountException(postId));

        var replyEntities = replyEntityRepository.findByPost(postEntity);

        return replyEntities.stream().map(Reply::from).toList();
    }

    @Transactional
    public Reply createReply(Long postId, ReplyRequestBody body, UserEntity user) {

        var postEntity = postEntityRepository.findById(postId)
                .orElseThrow(() -> new PostNotFountException(postId));

        var replyEntity = replyEntityRepository.save(ReplyEntity.of(body.body(), user, postEntity));

        postEntity.setRepliesCount(postEntity.getRepliesCount() + 1);
        postEntityRepository.save(postEntity);

        return Reply.from(replyEntity);
    }

    public Reply updateReply(Long postId, Long replyId, ReplyPatchRequestBody body, UserEntity user) {

        postEntityRepository.findById(postId)
                .orElseThrow(() -> new PostNotFountException(postId));

        var replyEntity = replyEntityRepository.findById(replyId)
                .orElseThrow(() -> new ReplyNotFountException(replyId));

        if (!replyEntity.getUser().equals(user)) { throw new UserNotAllowedException(); }

        replyEntity.setBody(body.body());

        return Reply.from(replyEntityRepository.save(replyEntity));
    }

    @Transactional
    public void deleteReply(Long postId, Long replyId, UserEntity user) {

         var postEntity = postEntityRepository.findById(postId)
                .orElseThrow(() -> new PostNotFountException(postId));

        var replyEntity = replyEntityRepository.findById(replyId)
                .orElseThrow(() -> new ReplyNotFountException(replyId));

        if (!replyEntity.getUser().equals(user)) { throw new UserNotAllowedException(); }

        replyEntityRepository.delete(replyEntity);

        postEntity.setRepliesCount(Math.max(0, postEntity.getRepliesCount() - 1));
        postEntityRepository.save(postEntity);
    }
}
