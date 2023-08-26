package com.bol.kahala.repository.impl;

import com.bol.kahala.model.domain.Game;
import com.bol.kahala.repository.GameRepository;
import com.bol.kahala.service.exception.GameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
public class InMemoryGameRepositoryImpl implements GameRepository {

    private final Map<String, Game> activeGames = new HashMap<>();

    @Override
    public void saveGame(Game game) {
        if (game.getGameId() == null) {
            String gameId = UUID.randomUUID().toString();
            game.setGameId(gameId);
        }
        activeGames.put(game.getGameId(), game);
    }

    @Override
    public Game findGameById(String gameId) throws GameNotFoundException {
        if (!activeGames.containsKey(gameId)) {
            throw new GameNotFoundException();
        }
        return activeGames.get(gameId);
    }

    public Map<String, Game> getActiveGames() {
        return activeGames;
    }
}
