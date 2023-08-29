package com.bol.kahala.service.input;

import com.bol.kahala.model.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * A data class representing the input for creating a new user.
 * It holds information about the user to be created.
 */
@Builder
@Getter
@ToString
public class CreateUserServiceInput {

    /**
     * The user information to be used for creating a new user.
     */
    private User user;
}
