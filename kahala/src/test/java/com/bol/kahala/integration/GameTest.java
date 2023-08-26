package com.bol.kahala.integration;

import com.bol.kahala.model.domain.Movement;
import com.bol.kahala.model.domain.User;
import com.bol.kahala.service.GameService;
import com.bol.kahala.service.UserService;
import com.bol.kahala.service.input.CreateGameServiceInput;
import com.bol.kahala.service.input.CreateUserServiceInput;
import com.bol.kahala.service.input.MoveGameServiceInput;
import com.bol.kahala.service.output.CreateGameServiceOutput;
import com.bol.kahala.service.output.CreateUserServiceOutput;
import com.bol.kahala.service.output.MoveGameServiceOutput;
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

    @Test
    void givenNormalGameConditions_whenGamePlayed_thenReturnValidStatus() {

        //Create Users
        CreateUserServiceOutput jane = userService.createUser(CreateUserServiceInput.builder()
                .user(User.builder().userName("Jane").build()).build());
        CreateUserServiceOutput emma = userService.createUser(CreateUserServiceInput.builder()
                .user(User.builder().userName("Emma").build()).build());

        //Create Game
        CreateGameServiceOutput game = gameService.createGame(CreateGameServiceInput.builder()
                .firstPlayerId(jane.getUser().getUserId())
                .secondPlayerId(emma.getUser().getUserId())
                .build());

        //Jane movement: 6th Pit
        MoveGameServiceOutput moveGameServiceOutput = gameService.moveGame(MoveGameServiceInput.builder()
                .gameId(game.getGame().getGameId())
                .movement(Movement.builder()
                        .playerId(jane.getUser().getUserId())
                        .position(6).build())
                .build());

        // Asserts for the game state after Jane's movement : 6th Pit
        assertEquals(emma.getUser().getUserId(), moveGameServiceOutput.getGame().getActivePlayerId());
        assertEquals(List.of(6, 6, 6, 6, 6, 0), moveGameServiceOutput.getGame().getFirstPlayer().getBoard().getSmallPits());
        assertEquals(1, moveGameServiceOutput.getGame().getFirstPlayer().getBoard().getBigPit());
        assertEquals(List.of(7, 7, 7, 7, 7, 6), moveGameServiceOutput.getGame().getSecondPlayer().getBoard().getSmallPits());
        assertEquals(0, moveGameServiceOutput.getGame().getSecondPlayer().getBoard().getBigPit());

        //Emma movement: 1st Pit
        moveGameServiceOutput = gameService.moveGame(MoveGameServiceInput.builder()
                .gameId(game.getGame().getGameId())
                .movement(Movement.builder()
                        .playerId(emma.getUser().getUserId())
                        .position(1).build())
                .build());

        // Asserts for the game state after Emma's movement : 1st Pit
        assertEquals(List.of(7, 6, 6, 6, 6, 0), moveGameServiceOutput.getGame().getFirstPlayer().getBoard().getSmallPits());
        assertEquals(1, moveGameServiceOutput.getGame().getFirstPlayer().getBoard().getBigPit());
        assertEquals(List.of(0, 8, 8, 8, 8, 7), moveGameServiceOutput.getGame().getSecondPlayer().getBoard().getSmallPits());
        assertEquals(1, moveGameServiceOutput.getGame().getSecondPlayer().getBoard().getBigPit());
        assertEquals(jane.getUser().getUserId(), moveGameServiceOutput.getGame().getActivePlayerId());


        //Jane movement: 1st Pit
        moveGameServiceOutput = gameService.moveGame(MoveGameServiceInput.builder()
                .gameId(game.getGame().getGameId())
                .movement(Movement.builder()
                        .playerId(jane.getUser().getUserId())
                        .position(1).build())
                .build());

        // Asserts for the game state after Jane's movement : 1st Pit
        assertEquals(List.of(0, 7, 7, 7, 7, 1), moveGameServiceOutput.getGame().getFirstPlayer().getBoard().getSmallPits());
        assertEquals(2, moveGameServiceOutput.getGame().getFirstPlayer().getBoard().getBigPit());
        assertEquals(List.of(1, 8, 8, 8, 8, 7), moveGameServiceOutput.getGame().getSecondPlayer().getBoard().getSmallPits());
        assertEquals(1, moveGameServiceOutput.getGame().getSecondPlayer().getBoard().getBigPit());
        assertEquals(emma.getUser().getUserId(), moveGameServiceOutput.getGame().getActivePlayerId());


        //Emma movement: 2nd Pit
        moveGameServiceOutput = gameService.moveGame(MoveGameServiceInput.builder()
                .gameId(game.getGame().getGameId())
                .movement(Movement.builder()
                        .playerId(emma.getUser().getUserId())
                        .position(2).build())
                .build());

        // Asserts for the game state after Emma's movement : 2nd Pit
        assertEquals(List.of(1, 8, 8, 7, 7, 1), moveGameServiceOutput.getGame().getFirstPlayer().getBoard().getSmallPits());
        assertEquals(2, moveGameServiceOutput.getGame().getFirstPlayer().getBoard().getBigPit());
        assertEquals(List.of(1, 0, 9, 9, 9, 8), moveGameServiceOutput.getGame().getSecondPlayer().getBoard().getSmallPits());
        assertEquals(2, moveGameServiceOutput.getGame().getSecondPlayer().getBoard().getBigPit());
        assertEquals(jane.getUser().getUserId(), moveGameServiceOutput.getGame().getActivePlayerId());
    }

    @DisplayName("This scenario shows stolen seed situation")
    @Test
    void givenStolenSeedScenario_whenGamePlayed_thenReturnValidStatus() {

        //Create Users
        CreateUserServiceOutput jane = userService.createUser(CreateUserServiceInput.builder()
                .user(User.builder().userName("Jane2").build()).build());
        CreateUserServiceOutput emma = userService.createUser(CreateUserServiceInput.builder()
                .user(User.builder().userName("Emma2").build()).build());

        //Create Game
        CreateGameServiceOutput game = gameService.createGame(CreateGameServiceInput.builder()
                .firstPlayerId(jane.getUser().getUserId())
                .secondPlayerId(emma.getUser().getUserId())
                .build());

        //Jane movement: 6th Pit
        MoveGameServiceOutput moveGameServiceOutput = gameService.moveGame(MoveGameServiceInput.builder()
                .gameId(game.getGame().getGameId())
                .movement(Movement.builder()
                        .playerId(jane.getUser().getUserId())
                        .position(6).build())
                .build());


        //Emma movement: 1st Pit
        moveGameServiceOutput = gameService.moveGame(MoveGameServiceInput.builder()
                .gameId(game.getGame().getGameId())
                .movement(Movement.builder()
                        .playerId(emma.getUser().getUserId())
                        .position(1).build())
                .build());


        //Jane movement: 1st Pit
        moveGameServiceOutput = gameService.moveGame(MoveGameServiceInput.builder()
                .gameId(game.getGame().getGameId())
                .movement(Movement.builder()
                        .playerId(jane.getUser().getUserId())
                        .position(1).build())
                .build());


        //Emma movement: 2nd Pit
        moveGameServiceOutput = gameService.moveGame(MoveGameServiceInput.builder()
                .gameId(game.getGame().getGameId())
                .movement(Movement.builder()
                        .playerId(emma.getUser().getUserId())
                        .position(2).build())
                .build());


        //Jane movement: 3rd Pit
        moveGameServiceOutput = gameService.moveGame(MoveGameServiceInput.builder()
                .gameId(game.getGame().getGameId())
                .movement(Movement.builder()
                        .playerId(jane.getUser().getUserId())
                        .position(3).build())
                .build());

        assertEquals(List.of(1, 8, 0, 8, 8, 2), moveGameServiceOutput.getGame().getFirstPlayer().getBoard().getSmallPits());
        assertEquals(3, moveGameServiceOutput.getGame().getFirstPlayer().getBoard().getBigPit());
        assertEquals(List.of(2, 1, 10, 10, 9, 8), moveGameServiceOutput.getGame().getSecondPlayer().getBoard().getSmallPits());
        assertEquals(2, moveGameServiceOutput.getGame().getSecondPlayer().getBoard().getBigPit());
        assertEquals(emma.getUser().getUserId(), moveGameServiceOutput.getGame().getActivePlayerId());

        //Emma movement: 3rd Pit
        moveGameServiceOutput = gameService.moveGame(MoveGameServiceInput.builder()
                .gameId(game.getGame().getGameId())
                .movement(Movement.builder()
                        .playerId(emma.getUser().getUserId())
                        .position(3).build())
                .build());

        assertEquals(List.of(2, 9, 1, 9, 9, 3), moveGameServiceOutput.getGame().getFirstPlayer().getBoard().getSmallPits());
        assertEquals(3, moveGameServiceOutput.getGame().getFirstPlayer().getBoard().getBigPit());
        assertEquals(List.of(2, 1, 0, 11, 10, 9), moveGameServiceOutput.getGame().getSecondPlayer().getBoard().getSmallPits());
        assertEquals(3, moveGameServiceOutput.getGame().getSecondPlayer().getBoard().getBigPit());
        assertEquals(jane.getUser().getUserId(), moveGameServiceOutput.getGame().getActivePlayerId());

        //Jane movement: 1st Pit
        moveGameServiceOutput = gameService.moveGame(MoveGameServiceInput.builder()
                .gameId(game.getGame().getGameId())
                .movement(Movement.builder()
                        .playerId(jane.getUser().getUserId())
                        .position(1).build())
                .build());

        assertEquals(List.of(0, 10, 2, 9, 9, 3), moveGameServiceOutput.getGame().getFirstPlayer().getBoard().getSmallPits());
        assertEquals(3, moveGameServiceOutput.getGame().getFirstPlayer().getBoard().getBigPit());
        assertEquals(List.of(2, 1, 0, 11, 10, 9), moveGameServiceOutput.getGame().getSecondPlayer().getBoard().getSmallPits());
        assertEquals(3, moveGameServiceOutput.getGame().getSecondPlayer().getBoard().getBigPit());
        assertEquals(emma.getUser().getUserId(), moveGameServiceOutput.getGame().getActivePlayerId());

        //Emma movement: 1st Pit (Stolen seed situation)
        moveGameServiceOutput = gameService.moveGame(MoveGameServiceInput.builder()
                .gameId(game.getGame().getGameId())
                .movement(Movement.builder()
                        .playerId(emma.getUser().getUserId())
                        .position(1).build())
                .build());

        assertEquals(List.of(0, 10, 2, 0, 9, 3), moveGameServiceOutput.getGame().getFirstPlayer().getBoard().getSmallPits());
        assertEquals(3, moveGameServiceOutput.getGame().getFirstPlayer().getBoard().getBigPit());
        assertEquals(List.of(0, 2, 0, 11, 10, 9), moveGameServiceOutput.getGame().getSecondPlayer().getBoard().getSmallPits());
        assertEquals(13, moveGameServiceOutput.getGame().getSecondPlayer().getBoard().getBigPit());
        assertEquals(jane.getUser().getUserId(), moveGameServiceOutput.getGame().getActivePlayerId());
    }

    @Test
    @DisplayName("Scenario demonstrating user's ability to earn an extra move")
    void givenGameWithExtraMoveScenario_whenPlayerEarnsExtraMove_thenGameAllowsExtraMove() {

        //Create Users
        CreateUserServiceOutput jane = userService.createUser(CreateUserServiceInput.builder()
                .user(User.builder().userName("Jane3").build()).build());
        CreateUserServiceOutput emma = userService.createUser(CreateUserServiceInput.builder()
                .user(User.builder().userName("Emma3").build()).build());

        //Create Game
        CreateGameServiceOutput game = gameService.createGame(CreateGameServiceInput.builder()
                .firstPlayerId(jane.getUser().getUserId())
                .secondPlayerId(emma.getUser().getUserId())
                .build());

        //Jane movement: 1st Pit
        MoveGameServiceOutput moveGameServiceOutput = gameService.moveGame(MoveGameServiceInput.builder()
                .gameId(game.getGame().getGameId())
                .movement(Movement.builder()
                        .playerId(jane.getUser().getUserId())
                        .position(1).build())
                .build());

        assertEquals(List.of(0, 7, 7, 7, 7, 7), moveGameServiceOutput.getGame().getFirstPlayer().getBoard().getSmallPits());
        assertEquals(1, moveGameServiceOutput.getGame().getFirstPlayer().getBoard().getBigPit());
        assertEquals(List.of(6, 6, 6, 6, 6, 6), moveGameServiceOutput.getGame().getSecondPlayer().getBoard().getSmallPits());
        assertEquals(0, moveGameServiceOutput.getGame().getSecondPlayer().getBoard().getBigPit());
        assertEquals(jane.getUser().getUserId(), moveGameServiceOutput.getGame().getActivePlayerId());

        //Jane movement: 2nd Pit
        moveGameServiceOutput = gameService.moveGame(MoveGameServiceInput.builder()
                .gameId(game.getGame().getGameId())
                .movement(Movement.builder()
                        .playerId(jane.getUser().getUserId())
                        .position(2).build())
                .build());

        assertEquals(List.of(0, 0, 8, 8, 8, 8), moveGameServiceOutput.getGame().getFirstPlayer().getBoard().getSmallPits());
        assertEquals(2, moveGameServiceOutput.getGame().getFirstPlayer().getBoard().getBigPit());
        assertEquals(List.of(7, 7, 6, 6, 6, 6), moveGameServiceOutput.getGame().getSecondPlayer().getBoard().getSmallPits());
        assertEquals(0, moveGameServiceOutput.getGame().getSecondPlayer().getBoard().getBigPit());
        assertEquals(emma.getUser().getUserId(), moveGameServiceOutput.getGame().getActivePlayerId());

    }
}
