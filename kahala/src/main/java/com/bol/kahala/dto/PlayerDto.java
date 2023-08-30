package com.bol.kahala.dto;

import com.bol.kahala.model.Player;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * This class represents a player in the game.
 */
@Data
@Builder
public class PlayerDto implements Serializable {

    /**
     * The unique identifier of the player.
     */
    private String userId;

    /**
     * The board associated with the player.
     */
    private BoardDto board;

    /**
     * Indicates whether it's the current turn of this player.
     */
    private boolean isCurrentTurn;

    /**
     * Converts a Player object to its corresponding Data Transfer Object (DTO) representation.
     *
     * @param player The Player object to be converted.
     * @return The PlayerDto representation of the input Player object.
     */
    public static PlayerDto toDto(Player player) {
        return player == null ? null : PlayerDto.builder()
                .board(BoardDto.toDto(player.getBoard()))
                .isCurrentTurn(player.isCurrentTurn())
                .userId(player.getUserId())
                .build();
    }

}

