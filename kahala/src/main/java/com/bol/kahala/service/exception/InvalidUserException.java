package com.bol.kahala.service.exception;

/**
 * Custom exception class to indicate that an invalid user condition or operation occurred.
 */
public class InvalidUserException extends RuntimeException {

    /**
     * Constructs a new InvalidUserException with a specified error message.
     *
     * @param message The error message.
     */
    public InvalidUserException(String message) {
        super(message);
    }
}






