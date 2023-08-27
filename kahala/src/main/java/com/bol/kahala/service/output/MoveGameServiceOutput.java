package com.bol.kahala.service.output;

import com.bol.kahala.model.domain.Game;
import lombok.Builder;
import lombok.Getter;

/**
 * A data class representing the output of the move game process.
 * It contains the updated game instance after a player's move.
 */
@Getter
@Builder
public class MoveGameServiceOutput {

    /**
     * The updated game instance after a player's move.
     */
    private Game game;
}
