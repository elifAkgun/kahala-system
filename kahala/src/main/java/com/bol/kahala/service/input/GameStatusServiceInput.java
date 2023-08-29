package com.bol.kahala.service.input;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * A data class representing the input for retrieving the status of a game.
 * It holds the unique identifier of the game for which the status is requested.
 */
@Getter
@Builder
@ToString
public class GameStatusServiceInput {

    /**
     * The unique identifier of the game for which the status is requested.
     */
    private String gameId;
}
