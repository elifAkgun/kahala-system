package com.bol.kahala.service.input;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * A data class representing the input for creating a new game.
 * It holds information about the IDs of the first and second players.
 */
@Builder
@Getter
@ToString
public class CreateGameServiceInput {

    /**
     * The ID of the first player participating in the game.
     */
    private String firstPlayerId;

    /**
     * The ID of the second player participating in the game.
     */
    private String secondPlayerId;
}
