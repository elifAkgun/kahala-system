package com.bol.kahala.service.exception;

/**
 * Custom exception class to indicate that a duplicate user name is detected during user creation.
 */
public class DuplicateUserNameException extends Exception {

    /**
     * Constructs a new DuplicateUserNameException with a default error message.
     */
    public DuplicateUserNameException() {
        super("A user with the same user name already exists.");
    }
}

