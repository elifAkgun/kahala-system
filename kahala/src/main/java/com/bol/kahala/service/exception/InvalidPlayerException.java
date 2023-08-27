package com.bol.kahala.service.exception;

/**
 * Custom exception class to indicate that an invalid player condition or operation occurred.
 */
public class InvalidPlayerException extends RuntimeException {

    /**
     * Constructs a new InvalidPlayerException with a specified error message.
     *
     * @param message The error message.
     */
    public InvalidPlayerException(String message) {
        super(message);
    }
}

