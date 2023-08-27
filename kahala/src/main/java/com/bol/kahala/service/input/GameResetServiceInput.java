package com.bol.kahala.service.input;

import lombok.Builder;
import lombok.Getter;

/**
 * A data class representing the input for resetting a game.
 * It holds the unique identifier of the game to be reset.
 */
@Getter
@Builder
public class GameResetServiceInput {

    /**
     * The unique identifier of the game to be reset.
     */
    private String gameId;
}
