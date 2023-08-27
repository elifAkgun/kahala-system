package com.bol.kahala.controller.request;

import com.bol.kahala.validation.constraint.IntegerValue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

/**
 * This class represents a request for making a move in the game. It contains information about the player's ID and the selected pit number.
 */
@Getter
@Builder
public class MoveGameRequest implements Serializable {
    /**
     * The ID of the player making the move. It must not be empty.
     */
    @NotEmpty(message = "{NotEmpty.moveGameRequest.playerId}")
    private String playerId;

    /**
     * The selected pit number for the move. It must be between 1 and 6 (inclusive).
     */
    @Max(value = 6, message = "{Max.moveGameRequest.pitNumber}")
    @Min(value = 1, message = "{Min.moveGameRequest.pitNumber}")
    @IntegerValue(message = "{NumberFormat.moveGameRequest.pitNumber}")
    private Integer pitNumber;

}

