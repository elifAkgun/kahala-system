package com.bol.kahala.repository.impl;

import com.bol.kahala.model.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GameRepositoryInMemoryImplTest {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @InjectMocks
    private GameRepositoryInMemoryImpl gameRepository;
    @InjectMocks
    private GameRepositoryInMemoryImpl inMemoryGameRepository;

    @Test
    void injectedComponentsAreNotNull() {
        assertNotNull(dataSource);
        assertNotNull(jdbcTemplate);
        assertNotNull(gameRepository);
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test saving a new game")
    void givenNewGame_whenSaveGame_thenGameIsSaved() {
        Game game =Game.builder().build();
        inMemoryGameRepository.save(game);

        assertNotNull(game.getGameId());
        assertEquals(game, inMemoryGameRepository.findAll().iterator().next());
    }

    @Test
    @DisplayName("Test finding an existing game by ID")
    void givenExistingGameId_whenFindGameById_thenGameIsFound() {
        String gameId = UUID.randomUUID().toString();
        Game game = Game.builder().build();
        game.setGameId(gameId);
        inMemoryGameRepository.save(game);

        Game foundGame = inMemoryGameRepository.findById(gameId).get();
        assertEquals(game, foundGame);
    }
}
