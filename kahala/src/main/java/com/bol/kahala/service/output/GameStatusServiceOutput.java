package com.bol.kahala.service.output;

import com.bol.kahala.dto.GameDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * A data class representing the output of the game status retrieval process.
 * It contains the game instance representing the current status.
 */
@Getter
@Builder
@ToString
public class GameStatusServiceOutput {

    /**
     * The game instance representing the current status of the game.
     */
    private GameDto game;
}

