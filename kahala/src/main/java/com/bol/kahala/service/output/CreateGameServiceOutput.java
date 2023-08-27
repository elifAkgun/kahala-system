package com.bol.kahala.service.output;

import com.bol.kahala.model.domain.Game;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * A data class representing the output of the game creation process.
 * It contains the resulting game instance after creation.
 */
@Getter
@Builder
public class CreateGameServiceOutput {

    /**
     * The game instance that was created.
     */
    private Game game;
}

