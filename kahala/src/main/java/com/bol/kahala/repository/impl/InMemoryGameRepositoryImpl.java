package com.bol.kahala.repository.impl;

import com.bol.kahala.model.domain.Game;
import com.bol.kahala.repository.GameRepository;
import com.bol.kahala.service.exception.GameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * This class implements the GameRepository interface and provides an in-memory storage for game data.
 */
@Repository
public class InMemoryGameRepositoryImpl implements GameRepository {

    // A map to store active games using their unique IDs as keys
    private final Map<String, Game> activeGames = new HashMap<>();

    /**
     * Saves a game in the repository.
     *
     * @param game The game object to be saved.
     */
    @Override
    public void saveGame(Game game) {
        if (game.getGameId() == null) {
            String gameId = UUID.randomUUID().toString();
            game.setGameId(gameId);
        }
        activeGames.put(game.getGameId(), game);
    }

    /**
     * Retrieves a game by its unique identifier.
     *
     * @param gameId The unique identifier of the game to retrieve.
     * @return The retrieved game.
     * @throws GameNotFoundException If the game with the specified ID is not found.
     */
    @Override
    public Game findGameById(String gameId) throws GameNotFoundException {
        if (!activeGames.containsKey(gameId)) {
            throw new GameNotFoundException();
        }
        return activeGames.get(gameId);
    }

    /**
     * Returns the map containing active games.
     *
     * @return The map of active games.
     */
    public Map<String, Game> getActiveGames() {
        return activeGames;
    }
}
