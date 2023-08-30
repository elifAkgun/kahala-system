package com.bol.kahala.service.input;

import com.bol.kahala.dto.MovementDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * A data class representing the input for making a move in a game.
 * It contains the unique identifier of the game and the movementDto details.
 */
@Getter
@Builder
@ToString
public class MoveGameServiceInput {

    /**
     * The unique identifier of the game in which the move is to be made.
     */
    private String gameId;

    /**
     * The movementDto details specifying the player and the pit for the move.
     */
    private MovementDto movementDto;
}
