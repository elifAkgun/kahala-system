package com.bol.kahala.constant;

/**
 * This utility class provides constants related to the game settings.
 * It prevents instantiation by throwing an exception in its private constructor.
 */
public class GameConstants {

    /**
     * The number of small pits on each player's board.
     */
    public static final int SMALL_PIT_NUMBER = 6;
    /**
     * The count of seed items in each small pit.
     */
    public static final int SEED_COUNT = 6;
    /**
     * The number of pits on the board.
     */
    public static final int BOARD_TOTAL_PITS_COUNT = 14;
    /**
     * Represents the game of TIE status
     */
    public static final String TIE = "TIE";

    /**
     * Private constructor to prevent instantiation of this utility class.
     * This constructor throws an IllegalStateException to indicate that instances of this class cannot be created.
     */
    private GameConstants() {
        throw new IllegalStateException("Utility class");
    }
}
