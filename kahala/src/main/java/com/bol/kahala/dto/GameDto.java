package com.bol.kahala.dto;

import com.bol.kahala.model.Game;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameDto {

    /**
     * The unique identifier of the game.
     */
    private String gameId;

    /**
     * The first player participating in the game.
     */
    private PlayerDto firstPlayer;

    /**
     * The second player participating in the game.
     */
    private PlayerDto secondPlayer;

    /**
     * The ID of the active player who has the current turn.
     */
    private String activePlayerId;

    /**
     * Indicates whether the game has finished.
     */
    private boolean isFinished;

    /**
     * The ID of the winner player who has the most seeds in their big pit.
     */
    private String winnerPlayerId;

    /**
     * Converts a Game object to its corresponding Data Transfer Object (DTO) representation.
     *
     * @param game The Game object to be converted.
     * @return The GameDto representation of the input Game object.
     */
    public static GameDto toDto(Game game) {
        return game == null ? null : GameDto.builder()
                .gameId(game.getGameId())
                .activePlayerId(game.getActivePlayerId())
                .firstPlayer(PlayerDto.toDto(game.getFirstPlayer()))
                .secondPlayer(PlayerDto.toDto(game.getSecondPlayer()))
                .isFinished(game.isFinished())
                .winnerPlayerId(game.getWinnerPlayerId())
                .build();
    }

}
