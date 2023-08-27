package com.bol.kahala.service.exception;

/**
 * Custom exception class to indicate that a user with the same identity already exists.
 */
public class UserAlreadyExistException extends RuntimeException {

    /**
     * Constructs a new UserAlreadyExistException with a specified error message.
     *
     * @param message The error message.
     */
    public UserAlreadyExistException(String message) {
        super(message);
    }
}

