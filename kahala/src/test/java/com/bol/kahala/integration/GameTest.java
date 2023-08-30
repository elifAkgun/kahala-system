package com.bol.kahala.integration;

import com.bol.kahala.dto.MovementDto;
import com.bol.kahala.model.User;
import com.bol.kahala.repository.GameRepository;
import com.bol.kahala.repository.UserRepository;
import com.bol.kahala.service.GameService;
import com.bol.kahala.service.UserService;
import com.bol.kahala.service.input.CreateGameServiceInput;
import com.bol.kahala.service.input.CreateUserServiceInput;
import com.bol.kahala.service.input.MoveGameServiceInput;
import com.bol.kahala.service.output.CreateGameServiceOutput;
import com.bol.kahala.service.output.CreateUserServiceOutput;
import com.bol.kahala.service.output.MoveGameServiceOutput;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class GameTest {
    @Autowired
    UserService userService;

    @Autowired
    GameService gameService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GameRepository gameRepository;

    private CreateGameServiceOutput game;
    private CreateUserServiceOutput user1;
    private CreateUserServiceOutput user2;

    @Test
    void givenNormalGameConditions_whenGamePlayed_thenReturnValidStatus() {

        //Create Users
        user1 = userService.createUser(CreateUserServiceInput.builder()
                .user(User.builder().userName("user1").build()).build());

        user2 = userService.createUser(CreateUserServiceInput.builder()
                .user(User.builder().userName("user2").build()).build());
        //Create Game
        game = gameService.createGame(CreateGameServiceInput.builder()
                .firstPlayerId(user1.getUser().getUserId())
                .secondPlayerId(user2.getUser().getUserId())
                .build());

        //user1 movementDto: 6th Pit
        MoveGameServiceOutput moveGameServiceOutput = gameService.moveGame(MoveGameServiceInput.builder()
                .gameId(game.getGame().getGameId())
                .movementDto(MovementDto.builder()
                        .playerId(user1.getUser().getUserId())
                        .position(6).build())
                .build());

        // Asserts for the game state after user1's movementDto : 6th Pit
        assertEquals(user2.getUser().getUserId(), moveGameServiceOutput.getGame().getActivePlayerId());
        assertEquals(List.of(6, 6, 6, 6, 6, 0), moveGameServiceOutput.getGame().getFirstPlayer().getBoard().getSmallPits());
        assertEquals(1, moveGameServiceOutput.getGame().getFirstPlayer().getBoard().getBigPit());
        assertEquals(List.of(7, 7, 7, 7, 7, 6), moveGameServiceOutput.getGame().getSecondPlayer().getBoard().getSmallPits());
        assertEquals(0, moveGameServiceOutput.getGame().getSecondPlayer().getBoard().getBigPit());

        //user2 movementDto: 1st Pit
        moveGameServiceOutput = gameService.moveGame(MoveGameServiceInput.builder()
                .gameId(game.getGame().getGameId())
                .movementDto(MovementDto.builder()
                        .playerId(user2.getUser().getUserId())
                        .position(1).build())
                .build());

        // Asserts for the game state after user2's movementDto : 1st Pit
        assertEquals(List.of(7, 6, 6, 6, 6, 0), moveGameServiceOutput.getGame().getFirstPlayer().getBoard().getSmallPits());
        assertEquals(1, moveGameServiceOutput.getGame().getFirstPlayer().getBoard().getBigPit());
        assertEquals(List.of(0, 8, 8, 8, 8, 7), moveGameServiceOutput.getGame().getSecondPlayer().getBoard().getSmallPits());
        assertEquals(1, moveGameServiceOutput.getGame().getSecondPlayer().getBoard().getBigPit());
        assertEquals(user1.getUser().getUserId(), moveGameServiceOutput.getGame().getActivePlayerId());


        //user1 movementDto: 1st Pit
        moveGameServiceOutput = gameService.moveGame(MoveGameServiceInput.builder()
                .gameId(game.getGame().getGameId())
                .movementDto(MovementDto.builder()
                        .playerId(user1.getUser().getUserId())
                        .position(1).build())
                .build());

        // Asserts for the game state after user1's movementDto : 1st Pit
        assertEquals(List.of(0, 7, 7, 7, 7, 1), moveGameServiceOutput.getGame().getFirstPlayer().getBoard().getSmallPits());
        assertEquals(2, moveGameServiceOutput.getGame().getFirstPlayer().getBoard().getBigPit());
        assertEquals(List.of(1, 8, 8, 8, 8, 7), moveGameServiceOutput.getGame().getSecondPlayer().getBoard().getSmallPits());
        assertEquals(1, moveGameServiceOutput.getGame().getSecondPlayer().getBoard().getBigPit());
        assertEquals(user2.getUser().getUserId(), moveGameServiceOutput.getGame().getActivePlayerId());


        //user2 movementDto: 2nd Pit
        moveGameServiceOutput = gameService.moveGame(MoveGameServiceInput.builder()
                .gameId(game.getGame().getGameId())
                .movementDto(MovementDto.builder()
                        .playerId(user2.getUser().getUserId())
                        .position(2).build())
                .build());

        // Asserts for the game state after user2's movementDto : 2nd Pit
        assertEquals(List.of(1, 8, 8, 7, 7, 1), moveGameServiceOutput.getGame().getFirstPlayer().getBoard().getSmallPits());
        assertEquals(2, moveGameServiceOutput.getGame().getFirstPlayer().getBoard().getBigPit());
        assertEquals(List.of(1, 0, 9, 9, 9, 8), moveGameServiceOutput.getGame().getSecondPlayer().getBoard().getSmallPits());
        assertEquals(2, moveGameServiceOutput.getGame().getSecondPlayer().getBoard().getBigPit());
        assertEquals(user1.getUser().getUserId(), moveGameServiceOutput.getGame().getActivePlayerId());

    }

    @DisplayName("This scenario shows stolen seed situation")
    @Test
    void givenStolenSeedScenario_whenGamePlayed_thenReturnValidStatus() {

        //Create Users
        user1 = userService.createUser(CreateUserServiceInput.builder()
                .user(User.builder().userName("user1").build()).build());

        user2 = userService.createUser(CreateUserServiceInput.builder()
                .user(User.builder().userName("user2").build()).build());
        //Create Game
        game = gameService.createGame(CreateGameServiceInput.builder()
                .firstPlayerId(user1.getUser().getUserId())
                .secondPlayerId(user2.getUser().getUserId())
                .build());

        //user1 movementDto: 6th Pit
        gameService.moveGame(MoveGameServiceInput.builder()
                .gameId(game.getGame().getGameId())
                .movementDto(MovementDto.builder()
                        .playerId(user1.getUser().getUserId())
                        .position(6).build())
                .build());


        //user2 movementDto: 1st Pit
        gameService.moveGame(MoveGameServiceInput.builder()
                .gameId(game.getGame().getGameId())
                .movementDto(MovementDto.builder()
                        .playerId(user2.getUser().getUserId())
                        .position(1).build())
                .build());


        //user1 movementDto: 1st Pit
        gameService.moveGame(MoveGameServiceInput.builder()
                .gameId(game.getGame().getGameId())
                .movementDto(MovementDto.builder()
                        .playerId(user1.getUser().getUserId())
                        .position(1).build())
                .build());


        //user2 movementDto: 2nd Pit
        gameService.moveGame(MoveGameServiceInput.builder()
                .gameId(game.getGame().getGameId())
                .movementDto(MovementDto.builder()
                        .playerId(user2.getUser().getUserId())
                        .position(2).build())
                .build());


        //user1 movementDto: 3rd Pit
        MoveGameServiceOutput moveGameServiceOutput = gameService.moveGame(MoveGameServiceInput.builder()
                .gameId(game.getGame().getGameId())
                .movementDto(MovementDto.builder()
                        .playerId(user1.getUser().getUserId())
                        .position(3).build())
                .build());

        assertEquals(List.of(1, 8, 0, 8, 8, 2), moveGameServiceOutput.getGame().getFirstPlayer().getBoard().getSmallPits());
        assertEquals(3, moveGameServiceOutput.getGame().getFirstPlayer().getBoard().getBigPit());
        assertEquals(List.of(2, 1, 10, 10, 9, 8), moveGameServiceOutput.getGame().getSecondPlayer().getBoard().getSmallPits());
        assertEquals(2, moveGameServiceOutput.getGame().getSecondPlayer().getBoard().getBigPit());
        assertEquals(user2.getUser().getUserId(), moveGameServiceOutput.getGame().getActivePlayerId());

        //user2 movementDto: 3rd Pit
        moveGameServiceOutput = gameService.moveGame(MoveGameServiceInput.builder()
                .gameId(game.getGame().getGameId())
                .movementDto(MovementDto.builder()
                        .playerId(user2.getUser().getUserId())
                        .position(3).build())
                .build());

        assertEquals(List.of(2, 9, 1, 9, 9, 3), moveGameServiceOutput.getGame().getFirstPlayer().getBoard().getSmallPits());
        assertEquals(3, moveGameServiceOutput.getGame().getFirstPlayer().getBoard().getBigPit());
        assertEquals(List.of(2, 1, 0, 11, 10, 9), moveGameServiceOutput.getGame().getSecondPlayer().getBoard().getSmallPits());
        assertEquals(3, moveGameServiceOutput.getGame().getSecondPlayer().getBoard().getBigPit());
        assertEquals(user1.getUser().getUserId(), moveGameServiceOutput.getGame().getActivePlayerId());

        //user1 movementDto: 1st Pit
        moveGameServiceOutput = gameService.moveGame(MoveGameServiceInput.builder()
                .gameId(game.getGame().getGameId())
                .movementDto(MovementDto.builder()
                        .playerId(user1.getUser().getUserId())
                        .position(1).build())
                .build());

        assertEquals(List.of(0, 10, 2, 9, 9, 3), moveGameServiceOutput.getGame().getFirstPlayer().getBoard().getSmallPits());
        assertEquals(3, moveGameServiceOutput.getGame().getFirstPlayer().getBoard().getBigPit());
        assertEquals(List.of(2, 1, 0, 11, 10, 9), moveGameServiceOutput.getGame().getSecondPlayer().getBoard().getSmallPits());
        assertEquals(3, moveGameServiceOutput.getGame().getSecondPlayer().getBoard().getBigPit());
        assertEquals(user2.getUser().getUserId(), moveGameServiceOutput.getGame().getActivePlayerId());

        //user2 movementDto: 1st Pit (Stolen seed situation)
        moveGameServiceOutput = gameService.moveGame(MoveGameServiceInput.builder()
                .gameId(game.getGame().getGameId())
                .movementDto(MovementDto.builder()
                        .playerId(user2.getUser().getUserId())
                        .position(1).build())
                .build());

        assertEquals(List.of(0, 10, 2, 0, 9, 3), moveGameServiceOutput.getGame().getFirstPlayer().getBoard().getSmallPits());
        assertEquals(3, moveGameServiceOutput.getGame().getFirstPlayer().getBoard().getBigPit());
        assertEquals(List.of(0, 2, 0, 11, 10, 9), moveGameServiceOutput.getGame().getSecondPlayer().getBoard().getSmallPits());
        assertEquals(13, moveGameServiceOutput.getGame().getSecondPlayer().getBoard().getBigPit());
        assertEquals(user1.getUser().getUserId(), moveGameServiceOutput.getGame().getActivePlayerId());

    }

    @Test
    @DisplayName("Scenario demonstrating user's ability to earn an extra move")
    void givenGameWithExtraMoveScenario_whenPlayerEarnsExtraMove_thenGameAllowsExtraMove() {

        //Create Users
        user1 = userService.createUser(CreateUserServiceInput.builder()
                .user(User.builder().userName("user1").build()).build());

        user2 = userService.createUser(CreateUserServiceInput.builder()
                .user(User.builder().userName("user2").build()).build());
        //Create Game
        game = gameService.createGame(CreateGameServiceInput.builder()
                .firstPlayerId(user1.getUser().getUserId())
                .secondPlayerId(user2.getUser().getUserId())
                .build());

        //user1 movementDto: 1st Pit
        MoveGameServiceOutput moveGameServiceOutput = gameService.moveGame(MoveGameServiceInput.builder()
                .gameId(game.getGame().getGameId())
                .movementDto(MovementDto.builder()
                        .playerId(user1.getUser().getUserId())
                        .position(1).build())
                .build());

        assertEquals(List.of(0, 7, 7, 7, 7, 7), moveGameServiceOutput.getGame().getFirstPlayer().getBoard().getSmallPits());
        assertEquals(1, moveGameServiceOutput.getGame().getFirstPlayer().getBoard().getBigPit());
        assertEquals(List.of(6, 6, 6, 6, 6, 6), moveGameServiceOutput.getGame().getSecondPlayer().getBoard().getSmallPits());
        assertEquals(0, moveGameServiceOutput.getGame().getSecondPlayer().getBoard().getBigPit());
        assertEquals(user1.getUser().getUserId(), moveGameServiceOutput.getGame().getActivePlayerId());

        //user1 movementDto: 2nd Pit
        moveGameServiceOutput = gameService.moveGame(MoveGameServiceInput.builder()
                .gameId(game.getGame().getGameId())
                .movementDto(MovementDto.builder()
                        .playerId(user1.getUser().getUserId())
                        .position(2).build())
                .build());

        assertEquals(List.of(0, 0, 8, 8, 8, 8), moveGameServiceOutput.getGame().getFirstPlayer().getBoard().getSmallPits());
        assertEquals(2, moveGameServiceOutput.getGame().getFirstPlayer().getBoard().getBigPit());
        assertEquals(List.of(7, 7, 6, 6, 6, 6), moveGameServiceOutput.getGame().getSecondPlayer().getBoard().getSmallPits());
        assertEquals(0, moveGameServiceOutput.getGame().getSecondPlayer().getBoard().getBigPit());
        assertEquals(user2.getUser().getUserId(), moveGameServiceOutput.getGame().getActivePlayerId());
    }


    @AfterEach
    void cleanTestData() {
        userRepository.deleteById(user1.getUser().getUserId());
        userRepository.deleteById(user2.getUser().getUserId());
        gameRepository.deleteById(game.getGame().getGameId());
    }
}