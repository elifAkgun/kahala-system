package com.bol.kahala.repository.impl;

import com.bol.kahala.model.domain.Game;
import com.bol.kahala.service.exception.GameNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class InMemoryGameRepositoryImplTest {

    @InjectMocks
    private InMemoryGameRepositoryImpl inMemoryGameRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test saving a new game")
    void givenNewGame_whenSaveGame_thenGameIsSaved() {
        Game game = new Game();
        inMemoryGameRepository.saveGame(game);

        assertNotNull(game.getGameId());
        assertTrue(inMemoryGameRepository.getActiveGames().containsKey(game.getGameId()));
        assertEquals(game, inMemoryGameRepository.getActiveGames().get(game.getGameId()));
    }

    @Test
    @DisplayName("Test finding an existing game by ID")
    void givenExistingGameId_whenFindGameById_thenGameIsFound() throws GameNotFoundException {
        String gameId = UUID.randomUUID().toString();
        Game game = new Game();
        game.setGameId(gameId);
        inMemoryGameRepository.getActiveGames().put(gameId, game);

        Game foundGame = inMemoryGameRepository.findGameById(gameId);
        assertEquals(game, foundGame);
    }

    @Test
    @DisplayName("Test finding a non-existing game by ID")
    void givenNonExistingGameId_whenFindGameById_thenThrowGameNotFoundException() {
        String nonExistingGameId = UUID.randomUUID().toString();

        assertThrows(GameNotFoundException.class, () -> {
            inMemoryGameRepository.findGameById(nonExistingGameId);
        });
    }
}
