package com.bol.kahala.model.domain;

import lombok.*;

/**
 * This class represents a game instance with its associated players and state.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@EqualsAndHashCode
public class Game {

    /**
     * The unique identifier of the game.
     */
    private String gameId;

    /**
     * The first player participating in the game.
     */
    private Player firstPlayer;

    /**
     * The second player participating in the game.
     */
    private Player secondPlayer;

    /**
     * The ID of the active player who has the current turn.
     */
    private String activePlayerId;

    /**
     * Indicates whether the game has finished.
     */
    private boolean isFinished;

    /**
     * The ID of the winner player who has the most seeds in their big pit.
     */
    private String winnerPlayerId;


}