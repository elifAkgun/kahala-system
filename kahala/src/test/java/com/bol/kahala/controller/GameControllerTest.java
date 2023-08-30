package com.bol.kahala.controller;

import com.bol.kahala.controller.request.CreateGameRequest;
import com.bol.kahala.controller.request.MoveGameRequest;
import com.bol.kahala.dto.GameDto;
import com.bol.kahala.service.GameService;
import com.bol.kahala.service.exception.InvalidGameException;
import com.bol.kahala.service.exception.InvalidPlayerException;
import com.bol.kahala.service.input.CreateGameServiceInput;
import com.bol.kahala.service.input.GameResetServiceInput;
import com.bol.kahala.service.input.GameStatusServiceInput;
import com.bol.kahala.service.input.MoveGameServiceInput;
import com.bol.kahala.service.output.CreateGameServiceOutput;
import com.bol.kahala.service.output.GameResetServiceOutput;
import com.bol.kahala.service.output.GameStatusServiceOutput;
import com.bol.kahala.service.output.MoveGameServiceOutput;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.bol.kahala.helper.ErrorMessages.*;
import static com.bol.kahala.helper.GameTestDataHelper.*;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GameController.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class GameControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private GameService gameService;

    @Test
    void givenGameRequestHasNoValue_whenCreateGameCalled_thenReturnErrorResponse() throws Exception {
        // given- precondition or setup
        CreateGameRequest createGameRequest = CreateGameRequest.builder().build();

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createGameRequest)));

        // then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    void givenGameRequestHasNoSecondPlayerId_whenCreateGameCalled_thenReturnErrorResponse() throws Exception {
        // given- precondition or setup
        CreateGameRequest createGameRequest = CreateGameRequest.builder()
                .firstPlayerId("1234").build();

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createGameRequest)));

        // then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status()
                        .isBadRequest());

    }

    @Test
    void givenGameRequestHasNoFirstPlayerId_whenCreateGameCalled_thenReturnErrorResponse() throws Exception {
        // given- precondition or setup
        CreateGameRequest createGameRequest = CreateGameRequest.builder()
                .secondPlayerId("1234").build();

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createGameRequest)));

        // then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    void givenGameRequestHasEmptyFirstPlayerId_whenCreateGameCalled_thenReturnErrorResponse() throws Exception {
        // given- precondition or setup
        CreateGameRequest createGameRequest = CreateGameRequest.builder()
                .firstPlayerId("")
                .secondPlayerId("1234").build();

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createGameRequest)));

        // then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    void givenGameRequestHasEmptySecondPlayerId_whenCreateGameCalled_thenReturnErrorResponse() throws Exception {
        // given- precondition or setup
        CreateGameRequest createGameRequest = CreateGameRequest.builder()
                .firstPlayerId("").build();

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createGameRequest)));

        // then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    void givenValidGameRequest_whenCreateGameCalled_thenReturnGameResponse() throws Exception {
        // given- precondition or setup
        CreateGameRequest createGameRequest = CreateGameRequest.builder()
                .firstPlayerId(FIRST_PLAYER_USER_ID)
                .secondPlayerId(SECOND_PLAYER_USER_ID).build();

        CreateGameServiceOutput createGameServiceOutput = CreateGameServiceOutput.builder()
                .game(GameDto.toDto(GAME)).build();

        given(gameService.createGame(any(CreateGameServiceInput.class)))
                .willReturn(createGameServiceOutput);

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createGameRequest)));

        // then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.header().string("Location", "/games/" + GAME_ID))
                .andExpect(jsonPath("$.game.gameId", is(GAME_ID)))
                .andExpect(jsonPath("$.game.winnerPlayerId").isEmpty())
                .andExpect(jsonPath("$.game.activePlayerId", is(FIRST_PLAYER_USER_ID)))
                .andExpect(jsonPath("$.game.finished", is(FINISHED)))
                .andExpect(jsonPath("$.game.firstPlayer.userId", is(FIRST_PLAYER_USER_ID)))
                .andExpect(jsonPath("$.game.firstPlayer.board.smallPits", is(FIRST_PLAYER_SMALL_PITS)))
                .andExpect(jsonPath("$.game.firstPlayer.board.bigPit", is(FIRST_BIG_PIT)))
                .andExpect(jsonPath("$.game.firstPlayer.currentTurn", is(CURRENT_TURN_TRUE)))
                .andExpect(jsonPath("$.game.secondPlayer.userId", is(SECOND_PLAYER_USER_ID)))
                .andExpect(jsonPath("$.game.secondPlayer.board.smallPits", is(SECOND_PLAYER_SMALL_PITS)))
                .andExpect(jsonPath("$.game.secondPlayer.board.bigPit", is(SECOND_BIG_PIT)))
                .andExpect(jsonPath("$.game.secondPlayer.currentTurn", is(CURRENT_TURN_FALSE)));
    }

    @Test
    void givenRequestHasNoGameId_whenMoveGameCalled_thenReturnStatusCode404() throws Exception {
        // given- precondition or setup
        MoveGameRequest moveGameRequest = null;

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/games/move/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(moveGameRequest)));

        // then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

    @Test
    void givenMoveGameRequestHasNoPlayerId_whenMoveGameCalled_thenReturnErrorResponse() throws Exception {
        // given- precondition or setup
        MoveGameRequest moveGameRequest = MoveGameRequest.builder().playerId(null).pitNumber(4).build();

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/games/move/{gameId}", GAME_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(moveGameRequest)));

        // then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage", is(PLAYER_ID_CANNOT_BE_EMPTY)));
    }

    @Test
    void givenMoveGameRequestPitNumberIsMoreThan6_whenMoveGameCalled_thenReturnErrorResponse() throws Exception {
        // given- precondition or setup
        MoveGameRequest moveGameRequest = MoveGameRequest.builder()
                .playerId(FIRST_PLAYER_USER_ID)
                .pitNumber(9)
                .build();

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/games/move/{gameId}", GAME_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(moveGameRequest)));

        // then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage",
                        is(PIT_NUMBER_CANNOT_BE_MORE_THAN_6)));
    }

    @Test
    void givenMoveGameRequestPitNumberIsLessThan1_whenMoveGameCalled_thenReturnErrorResponse() throws Exception {
        // given- precondition or setup
        MoveGameRequest moveGameRequest = MoveGameRequest.builder()
                .playerId(FIRST_PLAYER_USER_ID)
                .pitNumber(-1)
                .build();
        // when - action or the behaviour that we are going test

        ResultActions response = mockMvc.perform(put("/games/move/{gameId}", GAME_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(moveGameRequest)));

        // then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage",
                        is(PIT_NUMBER_CANNOT_BE_LESS_THAN_1)));
    }

    @Test
    void givenMoveGameRequestPitNumberIsNotInteger_whenMoveGameCalled_thenReturnErrorResponse() throws Exception {
        // given- precondition or setup
        MoveGameRequest moveGameRequest = MoveGameRequest.builder()
                .playerId(FIRST_PLAYER_USER_ID)
                .build();
        // when - action or the behaviour that we are going test

        ResultActions response = mockMvc.perform(put("/games/move/{gameId}", GAME_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(moveGameRequest)));

        // then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage",
                        is(PIT_NUMBER_MUST_BE_INTEGER)));
    }

    @Test
    void givenMoveGameServiceThrowsInvalidGameException_whenMoveGameCalled_thenReturnErrorResponse() throws Exception {
        // given- precondition or setup
        given(gameService.moveGame(any(MoveGameServiceInput.class)))
                .willThrow(new InvalidGameException(MOCK_MESSAGE));

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/games/move/{gameId}", GAME_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(MoveGameRequest.builder().playerId("123")
                        .pitNumber(4).build())));

        // then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage", is(MOCK_MESSAGE)));
    }

    @Test
    void givenMoveGameServiceThrowsInvalidPlayerException_whenMoveGameCalled_thenReturnErrorResponse() throws Exception {
        // given- precondition or setup
        given(gameService.moveGame(any(MoveGameServiceInput.class)))
                .willThrow(new InvalidPlayerException(MOCK_MESSAGE));

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/games/move/{gameId}", GAME_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(MoveGameRequest.builder().playerId("123")
                        .pitNumber(4).build())));

        // then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage", is(MOCK_MESSAGE)));
    }

    @Test
    void givenMoveGameServiceThrowsUnHandledException_whenMoveGameCalled_thenReturnTryAgainErrorResponse() throws Exception {
        // given- precondition or setup
        given(gameService.moveGame(any(MoveGameServiceInput.class)))
                .willThrow(new NullPointerException(MOCK_MESSAGE));

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/games/move/" + GAME_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        MoveGameRequest.builder().playerId("123")
                                .pitNumber(4).build())));

        // then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorMessage",
                        is(AN_ERROR_HAPPENED_PLEASE_TRY_AGAIN_LATER)));
    }

    @Test
    void givenValidMoveGameRequest_whenMoveGameCalled_thenReturnGameResponse() throws Exception {
        // given- precondition or setup
        GameDto gameDto = GameDto.builder().gameId(GAME_ID).build();
        given(gameService.moveGame(any(MoveGameServiceInput.class)))
                .willReturn(MoveGameServiceOutput.builder()
                        .game(gameDto).build());


        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/games/move/" + GAME)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        MoveGameRequest.builder().playerId("123")
                                .pitNumber(4).build())));

        // then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.game.gameId", is(GAME_ID)));
    }

    @Test
    void givenValidGameId_whenGetGameCalled_thenReturnGameResponse() throws Exception {
        // Given
        GameDto game = GameDto.builder().gameId("game123").build();
        given(gameService.getGame(any(GameStatusServiceInput.class)))
                .willReturn(GameStatusServiceOutput.builder().game(game).build());

        // When and Then
        mockMvc.perform(get("/games/{gameId}", "game123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.game.gameId", is("game123")));
    }

    @Test
    void givenInvalidGameId_whenGetGameCalled_thenReturnBadRequestResponse() throws Exception {
        // Given
        given(gameService.getGame(any(GameStatusServiceInput.class)))
                .willReturn(null);

        // When and Then
        mockMvc.perform(get("/games/{gameId}", "invalidgame"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenExceptionThrownByService_whenGetGameCalled_thenReturnInternalServerErrorResponse() throws Exception {
        // Given
        given(gameService.getGame(any(GameStatusServiceInput.class)))
                .willThrow(new RuntimeException("Something went wrong"));

        // When and Then
        mockMvc.perform(get("/games/{gameId}", "game123"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void givenValidGameId_whenResetGameCalled_thenReturnOkResponse() throws Exception {
        // Given
        String gameId = "validGame";
        GameResetServiceOutput resetOutput = GameResetServiceOutput.builder().game(GameDto.builder().build()).build();

        // Mock the gameService to return the reset game output
        given(gameService.resetGame(any(GameResetServiceInput.class))).willReturn(resetOutput);

        // When and Then
        mockMvc.perform(delete("/games/" + gameId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.game").exists());
    }

    @Test
    void givenInvalidGameId_whenResetGameCalled_thenReturnBadRequestResponse() throws Exception {
        // Given
        String invalidGameId = "invalidGame";

        // Mock the gameService to throw an InvalidPlayerException
        given(gameService.resetGame(any(GameResetServiceInput.class)))
                .willThrow(new InvalidPlayerException("Invalid game ID"));

        // When and Then
        mockMvc.perform(delete("/games/" + invalidGameId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}







