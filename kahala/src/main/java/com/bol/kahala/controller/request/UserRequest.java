package com.bol.kahala.controller.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serializable;

/**
 * This class represents a request for creating a new user.
 * It contains information about the user's username and password.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class UserRequest implements Serializable {
    /**
     * The username of the user. It must not be empty.
     */
    @NotEmpty(message = "{NotEmpty.userRequest.user.userName}")
    private String userName;

    /**
     * The password of the user. It must not be empty.
     */
    @NotEmpty(message = "{NotEmpty.userRequest.user.password}")
    private String password;
}
