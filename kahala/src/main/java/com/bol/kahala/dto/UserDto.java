package com.bol.kahala.dto;

import com.bol.kahala.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * This class represents a user in the system.
 */
@Data
@Builder
public class UserDto {

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

    public static UserDto toDto(User createdUser) {
        return UserDto.builder()
                .userId(createdUser.getUserId())
                .userName(createdUser.getUserName())
                .password(createdUser.getPassword())
                .build();
    }
}

