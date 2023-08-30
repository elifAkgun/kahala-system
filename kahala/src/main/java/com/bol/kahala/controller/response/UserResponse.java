package com.bol.kahala.controller.response;

import com.bol.kahala.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * This class represents a response containing user information. It includes a reference to the user object.
 */
@Builder
@Getter
@Setter
public class UserResponse implements Serializable {
    /**
     * The user object associated with the response.
     */
    private User user;
}
