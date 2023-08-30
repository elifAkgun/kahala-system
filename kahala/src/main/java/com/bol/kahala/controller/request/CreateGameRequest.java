package com.bol.kahala.controller.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

/**
 * This class represents a request for creating a game.
 * It contains information about the IDs of the first and second players.
 */
@Getter
@Builder
public class CreateGameRequest implements Serializable {
    /**
     * The ID of the first player. It must not be empty.
     */
    @NotEmpty(message = "{NotEmpty.createGameRequest.firstPlayerId}")
    private String firstPlayerId;

    /**
     * The ID of the second player. It must not be empty.
     */
    @NotEmpty(message = "{NotEmpty.createGameRequest.secondPlayerId}")
    private String secondPlayerId;

}
