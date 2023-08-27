package com.bol.kahala.repository.impl;

import com.bol.kahala.model.domain.Game;
import com.bol.kahala.repository.GameRepository;
import com.bol.kahala.service.exception.GameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.*;

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
    public Game save(Game game) {
        if (game.getGameId() == null) {
            String gameId = UUID.randomUUID().toString();
            game.setGameId(gameId);
        }
        activeGames.put(game.getGameId(), game);
        return game;
    }

    @Override
    public <S extends Game> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    /**
     * Retrieves a game by its unique identifier.
     *
     * @param gameId The unique identifier of the game to retrieve.
     * @return The retrieved game.
     * @throws GameNotFoundException If the game with the specified ID is not found.
     */
    @Override
    public Optional<Game> findById(String gameId) {
        if (!activeGames.containsKey(gameId)) {
            return Optional.empty();
        }
        return Optional.of(activeGames.get(gameId));
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    /**
     * Returns the map containing active games.
     *
     * @return The map of active games.
     */
    public Iterable<Game> findAll() {
        return activeGames.values();
    }

    @Override
    public Iterable<Game> findAllById(Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        return activeGames.size();
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(Game entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends Game> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
