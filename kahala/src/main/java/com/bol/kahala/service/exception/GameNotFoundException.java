package com.bol.kahala.service.exception;

/**
 * Custom exception class to indicate that a game with the specified ID was not found.
 */
public class GameNotFoundException extends Exception {

    /**
     * Constructs a new GameNotFoundException with a default error message.
     */
    public GameNotFoundException() {
        super("Game not found.");
    }
}

