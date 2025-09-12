package com.example.board.controller;

import com.example.board.model.user.UserAuthenticationResponse;
import com.example.board.model.user.UserLoginRequestBody;
import com.example.board.model.user.UserSignUpRequestBody;
import com.example.board.model.user.User;
import com.example.board.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    UserService userService;

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
