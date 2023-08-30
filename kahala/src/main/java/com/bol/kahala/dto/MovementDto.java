package com.bol.kahala.dto;

import lombok.*;

/**
 * This class represents a movementDto made by a player in a game.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class MovementDto {

    /**
     * The ID of the player making the movementDto.
     */
    private String playerId;

    /**
     * The position (pit number) chosen by the player for the movementDto.
     */
    private Integer position;
}

