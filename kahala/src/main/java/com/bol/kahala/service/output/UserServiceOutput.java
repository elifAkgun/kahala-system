package com.bol.kahala.service.output;

import com.bol.kahala.model.domain.User;
import lombok.Builder;
import lombok.Getter;

/**
 * A data class representing the output of the user service operations.
 * It holds information about a user after querying the user service.
 */
@Getter
@Builder
public class UserServiceOutput {

    /**
     * The user object containing user-related information.
     */
    private User user;
}
