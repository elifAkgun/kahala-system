package com.bol.kahala.service.input;

import com.bol.kahala.model.domain.Movement;
import lombok.Builder;
import lombok.Getter;

/**
 * A data class representing the input for making a move in a game.
 * It contains the unique identifier of the game and the movement details.
 */
@Getter
@Builder
public class MoveGameServiceInput {

    /**
     * The unique identifier of the game in which the move is to be made.
     */
    private String gameId;

    /**
     * The movement details specifying the player and the pit for the move.
     */
    private Movement movement;
}
