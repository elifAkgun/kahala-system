package com.bol.kahala.controller;

import com.bol.kahala.controller.request.UserRequest;
import com.bol.kahala.controller.response.UserResponse;
import com.bol.kahala.model.domain.User;
import com.bol.kahala.service.UserService;
import com.bol.kahala.service.input.CreateUserServiceInput;
import com.bol.kahala.service.input.UserServiceInput;
import com.bol.kahala.service.output.CreateUserServiceOutput;
import com.bol.kahala.service.output.UserServiceOutput;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRequest userRequest) {
        CreateUserServiceOutput userServiceOutput = userService.createUser(CreateUserServiceInput.builder()
                .user(User.builder()
                        .userName(userRequest.getUserName())
                        .password(userRequest.getPassword()).build()).build());
        return userServiceOutput.getUser() != null ? ResponseEntity.created(
                URI.create("/users/" + userServiceOutput.getUser().getUserId())).build()
                : ResponseEntity.badRequest().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable @Valid
                                                @NotEmpty(message = "{NotEmpty.userRequest.user.userId}") String userId) {
        UserServiceOutput userServiceOutput = userService.getUser(UserServiceInput.builder()
                .userId(userId).build());
        User user = userServiceOutput.getUser();
        return user != null ? ResponseEntity.ok(UserResponse.builder().user(user).build())
                : ResponseEntity.notFound().build();
    }
}
