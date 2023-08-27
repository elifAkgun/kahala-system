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


/**
 * This class defines the endpoints for managing user-related operations.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * Creates a new user using the provided user request data.
     *
     * @param userRequest The request containing user details.
     * @return A ResponseEntity containing the created user response.
     */
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

    /**
     * Retrieves user details based on the provided user ID.
     *
     * @param userId The ID of the user to retrieve details for.
     * @return A ResponseEntity containing the user response or a not found response.
     */
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
