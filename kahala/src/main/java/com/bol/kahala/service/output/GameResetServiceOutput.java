package com.bol.kahala.service.output;

import com.bol.kahala.model.domain.Game;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * A data class representing the output of the game reset process.
 * It contains the resulting game instance after the reset.
 */
@Getter
@Builder
@ToString
public class GameResetServiceOutput {

    /**
     * The game instance that resulted from the reset process.
     */
    private Game game;
}
