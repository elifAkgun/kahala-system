package com.bol.kahala.service.exception;

/**
 * Custom exception class to indicate that a requested user was not found.
 */
public class UserNotFoundException extends Exception {

    /**
     * Constructs a new UserNotFoundException with a specified error message.
     *
     */
    public UserNotFoundException() {
        super("User not found.");
    }
}
