package com.bol.kahala.service;

import com.bol.kahala.service.input.CreateGameServiceInput;
import com.bol.kahala.service.input.GameResetServiceInput;
import com.bol.kahala.service.input.GameStatusServiceInput;
import com.bol.kahala.service.input.MoveGameServiceInput;
import com.bol.kahala.service.output.CreateGameServiceOutput;
import com.bol.kahala.service.output.GameResetServiceOutput;
import com.bol.kahala.service.output.GameStatusServiceOutput;
import com.bol.kahala.service.output.MoveGameServiceOutput;

/**
 * An interface defining the contract for game-related operations.
 */
public interface GameService {

    /**
     * Creates a new game based on the provided input parameters.
     *
     * @param input The input parameters for creating a game.
     * @return The output containing information about the created game.
     */
    CreateGameServiceOutput createGame(CreateGameServiceInput input);

    /**
     * Moves the game state based on the provided move input.
     *
     * @param input The input specifying the game and move details.
     * @return The output containing information about the updated game state after the move.
     */
    MoveGameServiceOutput moveGame(MoveGameServiceInput input);

    /**
     * Retrieves the current status of a game based on the provided input.
     *
     * @param input The input specifying the game for which to retrieve the status.
     * @return The output containing information about the current status of the game.
     */
    GameStatusServiceOutput getGame(GameStatusServiceInput input);

    /**
     * Resets the game to its initial state based on the provided input.
     *
     * @param input The input specifying the game to be reset.
     * @return The output containing information about the game after the reset operation.
     */
    GameResetServiceOutput resetGame(GameResetServiceInput input);
}
