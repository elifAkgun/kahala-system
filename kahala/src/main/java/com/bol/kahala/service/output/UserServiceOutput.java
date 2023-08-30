package com.bol.kahala.service.output;

import com.bol.kahala.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * A data class representing the output of the user service operations.
 * It holds information about a user after querying the user service.
 */
@Getter
@Builder
@ToString
public class UserServiceOutput {

    /**
     * The user object containing user-related information.
     */
    private User user;
}
