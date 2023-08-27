package com.bol.kahala.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

/**
 * This class represents a user in the system.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    /**
     * The unique identifier of the user.
     */
    private String userId;

    /**
     * The username of the user.
     */
    @NotEmpty(message = "{NotEmpty.userRequest.userName}")
    private String userName;

    /**
     * The password of the user.
     */
    @JsonIgnore
    private String password;
}

