package com.bol.kahala.repository;

import com.bol.kahala.model.domain.Game;
import com.bol.kahala.service.exception.GameNotFoundException;

/**
 * This interface defines the contract for managing game data in the repository.
 */
public interface GameRepository {

    /**
     * Saves the provided game in the repository.
     *
     * @param game The game to be saved.
     */
    void saveGame(Game game);

    /**
     * Retrieves a game by its unique identifier.
     *
     * @param gameId The unique identifier of the game to retrieve.
     * @return The retrieved game.
     * @throws GameNotFoundException If the game with the specified ID is not found.
     */
    Game findGameById(String gameId) throws GameNotFoundException;
}


