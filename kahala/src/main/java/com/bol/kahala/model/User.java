package com.bol.kahala.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

/**
 * This class represents a user in the system.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
@RedisHash("user")
public class User {

    /**
     * The unique identifier of the user.
     */
    @Id
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

