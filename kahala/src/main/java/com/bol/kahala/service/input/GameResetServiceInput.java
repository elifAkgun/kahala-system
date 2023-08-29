package com.bol.kahala.service.input;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * A data class representing the input for resetting a game.
 * It holds the unique identifier of the game to be reset.
 */
@Getter
@Builder
@ToString
public class GameResetServiceInput {

    /**
     * The unique identifier of the game to be reset.
     */
    private String gameId;
}
