package com.example.board.controller;

import com.example.board.model.entity.UserEntity;
import com.example.board.model.post.Post;
import com.example.board.model.user.*;
import com.example.board.service.PostService;
import com.example.board.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    private PostService postService;

    @GetMapping
    public ResponseEntity<List<User>> getUsers(
        @RequestParam(required = false) String query
    ) {

        var users = userService.getUsers(query);

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(
            @PathVariable String username
    ) {

        var user = userService.getUser(username);

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
            @PathVariable String username
    ) {
        var posts = postService.getPostsByUsername(username);

        return ResponseEntity.ok(posts);
    }

    @PostMapping
    public ResponseEntity<User> signUp(
            @Valid @RequestBody UserSignUpRequestBody requestBody
    ) {

        var user = userService.singUp(
                requestBody.username(),
                requestBody.password()
        );
        return ResponseEntity.ok(user);
    }

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
