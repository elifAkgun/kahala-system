package com.bol.kahala.service.input;

import lombok.Builder;
import lombok.Getter;

/**
 * A data class representing the input for retrieving the status of a game.
 * It holds the unique identifier of the game for which the status is requested.
 */
@Getter
@Builder
public class GameStatusServiceInput {

    /**
     * The unique identifier of the game for which the status is requested.
     */
    private String gameId;
}
