package com.bol.kahala.service.input;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * A data class representing the input for retrieving user information.
 * It contains the unique identifier of the user.
 */
@Getter
@Builder
@EqualsAndHashCode
public class UserServiceInput {

    /**
     * The unique identifier of the user.
     */
    private String userId;
}

