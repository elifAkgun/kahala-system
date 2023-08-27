package com.bol.kahala.service.output;

import com.bol.kahala.model.domain.Game;
import lombok.Builder;
import lombok.Getter;

/**
 * A data class representing the output of the game status retrieval process.
 * It contains the game instance representing the current status.
 */
@Getter
@Builder
public class GameStatusServiceOutput {

    /**
     * The game instance representing the current status of the game.
     */
    private Game game;
}

