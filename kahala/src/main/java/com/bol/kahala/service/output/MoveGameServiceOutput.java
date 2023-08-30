package com.bol.kahala.service.output;

import com.bol.kahala.model.Game;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * A data class representing the output of the move game process.
 * It contains the updated game instance after a player's move.
 */
@Getter
@Builder
@ToString
public class MoveGameServiceOutput {

    /**
     * The updated game instance after a player's move.
     */
    private Game game;
}
