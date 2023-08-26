package com.bol.kahala.repository;

import com.bol.kahala.model.domain.Game;
import com.bol.kahala.service.exception.GameNotFoundException;

public interface GameRepository {
    void saveGame(Game game);

    Game findGameById(String gameId) throws GameNotFoundException;
}

