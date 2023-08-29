package com.bol.kahala.model.domain;

import lombok.*;

import java.io.Serializable;

/**
 * This class represents a player in the game.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
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

