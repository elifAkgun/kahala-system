package com.bol.kahala.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * This class represents a movement made by a player in a game.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Movement {

    /**
     * The ID of the player making the movement.
     */
    private String playerId;

    /**
     * The position (pit number) chosen by the player for the movement.
     */
    private Integer position;
}

