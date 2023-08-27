package com.bol.kahala.constant;

/**
 * This utility class provides constants related to the game settings.
 * It prevents instantiation by throwing an exception in its private constructor.
 */
public class GameConstants {

    /**
     * Private constructor to prevent instantiation of this utility class.
     * This constructor throws an IllegalStateException to indicate that instances of this class cannot be created.
     */
    private GameConstants() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * The number of pits on each player's board.
     */
    public static final int PIT_NUMBER = 6;

    /**
     * The number of seed items in each pit.
     */
    public static final int SEED_NUMBER = 6;
}
