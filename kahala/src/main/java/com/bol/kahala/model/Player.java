package com.bol.kahala.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * This class represents a player in the game.
 */
@Data
@Builder
public class Player implements Serializable {

    /**
     * The unique identifier of the player.
     */
    private String userId;

    /**
     * The board associated with the player.
     */
    private Board board;

    /**
     * Indicates whether it's the current turn of this player.
     */
    private boolean isCurrentTurn;
}

