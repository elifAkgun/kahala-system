package com.bol.kahala.service.exception;

/**
 * Custom exception class to indicate that an invalid game condition or operation occurred.
 */
public class InvalidGameException extends RuntimeException {

    /**
     * Constructs a new InvalidGameException with a specified error message.
     *
     * @param message The error message.
     */
    public InvalidGameException(String message) {
        super(message);
    }
}

