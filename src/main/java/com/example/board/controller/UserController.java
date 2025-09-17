package com.example.board.controller;

import com.example.board.model.entity.UserEntity;
import com.example.board.model.post.Post;
import com.example.board.model.reply.Reply;
import com.example.board.model.user.*;
import com.example.board.service.PostService;
import com.example.board.service.ReplyService;
import com.example.board.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;
    @Autowired
    private ReplyService replyService;

    @GetMapping
    public ResponseEntity<List<User>> getUsers(
        @RequestParam(required = false) String query,
        Authentication authentication
    ) {

        var users = userService.getUsers(query, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(
            @PathVariable String username,
            Authentication authentication
    ) {

        var user = userService.getUser(username, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(user);
    }

    @PatchMapping("/{username}")
    public ResponseEntity<User> updateUser(
            @PathVariable String username,
            @RequestBody UserPatchRequestBody body,
            Authentication authentication
    ) {

        var user = userService.updateUser(username, body, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(user);
    }

    @GetMapping("/{username}/posts")
    public ResponseEntity<List<Post>> getPostsByUsername(
            @PathVariable String username,
            Authentication authentication
    ) {
        var posts = postService.getPostsByUsername(username, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(posts);
    }

    @PostMapping("/{username}/follows")
    public ResponseEntity<User> follower(
            @PathVariable String username,
            Authentication authentication
    ) {

        var user = userService.follower(username, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{username}/follows")
    public ResponseEntity<User> unFollower(
            @PathVariable String username,
            Authentication authentication
    ) {

        var user = userService.unFollower(username, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{username}/followers")
    public ResponseEntity<List<Follower>> getFollowersByUser(
            @PathVariable String username,
            Authentication authentication
    ) {

        var followers = userService.getFollowersByUsername(username, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/{username}/followings")
    public ResponseEntity<List<User>> getFollowingsByUser(
            @PathVariable String username,
            Authentication authentication
    ) {

        var followings = userService.getFollowingsByUsername(username, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(followings);
    }

    @GetMapping("/{username}/liked-users")
    public ResponseEntity<List<LikedUser>> getLikedUsersByUser(
            @PathVariable String username,
            Authentication authentication
    ) {

        var likedUsers = userService.getLikedUsersByUser(username, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(likedUsers);
    }

    @GetMapping("/{username}/replies")
    public ResponseEntity<List<Reply>> getRepliesByUser(
            @PathVariable String username
    ) {

        var replies = replyService.getRepliesByUser(username);

        return ResponseEntity.ok(replies);
    }

    // 회원가입
    @PostMapping
    public ResponseEntity<User> signup(
            @Valid @RequestBody UserSignUpRequestBody requestBody
    ) {

        var user = userService.signUp(
                requestBody.username(),
                requestBody.password()
        );

        return ResponseEntity.ok(user);
    }

    // 로그인
    @PostMapping("/authenticate")
    public ResponseEntity<UserAuthenticationResponse> authenticate(
            @Valid @RequestBody UserLoginRequestBody requestBody
    ) {

        var response = userService.authenticate(
                requestBody.username(),
                requestBody.password()
        );

        return ResponseEntity.ok(response);
    }
}
