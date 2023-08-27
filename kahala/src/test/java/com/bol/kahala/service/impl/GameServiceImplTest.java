package com.bol.kahala.service.impl;

import com.bol.kahala.model.domain.*;
import com.bol.kahala.repository.GameRepository;
import com.bol.kahala.service.UserService;
import com.bol.kahala.service.exception.GameNotFoundException;
import com.bol.kahala.service.exception.InvalidGameException;
import com.bol.kahala.service.exception.InvalidPlayerException;
import com.bol.kahala.service.input.*;
import com.bol.kahala.service.output.*;
import com.bol.kahala.validation.ValidationMessagesUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static com.bol.kahala.constant.GameConstants.SMALL_PIT_NUMBER;
import static com.bol.kahala.constant.GameConstants.SEED_COUNT;
import static com.bol.kahala.helper.GameTestDataHelper.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceImplTest {

    @InjectMocks
    GameServiceImpl gameService;

    @Mock
    UserService userService;

    @Mock
    GameRepository gameRepository;

    @Mock
    ValidationMessagesUtil validationMessagesUtil;

    @Test
    void givenGameObject_whenCreateGameCalled_thenCallSaveGame() {
        // given- precondition or setup
        given(gameRepository.save(any(Game.class)))
                .willReturn(GAME);

        given(userService.getUser(UserServiceInput.builder().userId(FIRST_PLAYER_USER_ID).build()))
                .willReturn(UserServiceOutput.builder()
                        .user(User.builder().userId(FIRST_PLAYER_USER_ID).build()).build());

        given(userService.getUser(UserServiceInput.builder().userId(SECOND_PLAYER_USER_ID).build()))
                .willReturn(UserServiceOutput.builder()
                        .user(User.builder().userId(SECOND_PLAYER_USER_ID).build()).build());

        // when - action or the behaviour that we are going test
        gameService.createGame(CreateGameServiceInput.builder()
                .firstPlayerId(FIRST_PLAYER_USER_ID)
                .secondPlayerId(SECOND_PLAYER_USER_ID).build());

        // then - verify the output
        verify(gameRepository, atLeastOnce()).save(any(Game.class));
    }

    @Test
    void givenGameObjectHasSameUserIds_whenCreateGameCalled_thenThrowInvalidUserException() {
        // given- precondition or setup
        CreateGameServiceInput gameDto = CreateGameServiceInput.builder()
                .firstPlayerId(FIRST_PLAYER_USER_ID)
                .secondPlayerId(FIRST_PLAYER_USER_ID)
                .build();

        // when - action or the behaviour that we are going test
        assertThatThrownBy(() ->
                gameService.createGame(gameDto))
                .isInstanceOf(InvalidPlayerException.class);

        // then - verify the output
        verify(gameRepository, never()).save(any(Game.class));
    }

    @Test
    void givenGameObject_whenCreateGameCalled_thenReturnGameWithInitialProperties() {
        // given- precondition or setup
        CreateGameServiceInput gameDto = CreateGameServiceInput.builder()
                .firstPlayerId(FIRST_PLAYER_USER_ID)
                .secondPlayerId(SECOND_PLAYER_USER_ID)
                .build();


        given(gameRepository.save(any(Game.class)))
                .willReturn(GAME);

        given(userService.getUser(UserServiceInput.builder().userId(FIRST_PLAYER_USER_ID).build()))
                .willReturn(UserServiceOutput.builder()
                        .user(User.builder().userId(FIRST_PLAYER_USER_ID).build()).build());

        given(userService.getUser(UserServiceInput.builder().userId(SECOND_PLAYER_USER_ID).build()))
                .willReturn(UserServiceOutput.builder()
                        .user(User.builder().userId(SECOND_PLAYER_USER_ID).build()).build());


        // when - action or the behaviour that we are going test
        CreateGameServiceOutput createGameServiceOutput = gameService.createGame(gameDto);
        Game game = createGameServiceOutput.getGame();
        List<Integer> expectedInitialSmallPits = new ArrayList<>(Collections.nCopies(SMALL_PIT_NUMBER, SEED_COUNT));


        // then - verify the output
        assertThat(game.getFirstPlayer().getUserId()).isEqualTo(FIRST_PLAYER_USER_ID);
        assertThat(game.getSecondPlayer().getUserId()).isEqualTo(SECOND_PLAYER_USER_ID);
        assertThat(game.getFirstPlayer().getBoard().getBigPit()).isZero();
        assertThat(game.getSecondPlayer().getBoard().getBigPit()).isZero();
        assertThat(game.getFirstPlayer().getBoard().getSmallPits()).isEqualTo(expectedInitialSmallPits);
        assertThat(game.getSecondPlayer().getBoard().getSmallPits()).isEqualTo(expectedInitialSmallPits);
        assertThat(game.isFinished()).isFalse();
        assertThat(game.getActivePlayerId()).isEqualTo(FIRST_PLAYER_USER_ID);
        assertThat(game.getFirstPlayer().isCurrentTurn()).isTrue();
        assertThat(game.getSecondPlayer().isCurrentTurn()).isFalse();
    }

    @Test
    void givenInitialGameObject_whenMoveGameCalled_returnNextGameBoardPosition() throws GameNotFoundException {
        // given- precondition or setup
        String gameId = GAME_ID;

        // Initialize the board with a specific stone distribution
        int initialBigPit = 0;

        // Initialize the actualGame with the board and the current user as the second user
        Board firstPlayerBoard = new Board(new ArrayList<>(Collections.nCopies(SMALL_PIT_NUMBER, SEED_COUNT)), initialBigPit);
        Board secondPlayerBoard = new Board(new ArrayList<>(Collections.nCopies(SMALL_PIT_NUMBER, SEED_COUNT)), initialBigPit);

        Player firstPlayer = Player.builder()
                .isCurrentTurn(true)
                .board(firstPlayerBoard)
                .userId(FIRST_PLAYER_USER_ID)
                .build();
        Player secondPlayer = Player.builder()
                .board(secondPlayerBoard)
                .isCurrentTurn(false)
                .userId(SECOND_PLAYER_USER_ID)
                .build();

        // Initialize a actualGame instance here
        Game game = Game.builder().gameId(GAME_ID)
                .firstPlayer(firstPlayer)
                .secondPlayer(secondPlayer).activePlayerId(FIRST_PLAYER_USER_ID)
                .isFinished(false)
                .winnerPlayerId(null).build();

        given(gameRepository.findById(gameId)).willReturn(Optional.of(game));

        // when - action or the behaviour that we are going test
        Movement movement = new Movement(FIRST_PLAYER_USER_ID, 3);
        MoveGameServiceOutput gameServiceOutput =
                gameService.moveGame(MoveGameServiceInput.builder()
                        .gameId(gameId)
                        .movement(movement)
                        .build());

        Game actualGame = gameServiceOutput.getGame();

        // then - verify the output
        List<Integer> expectedSmallPitsFirstPlayer = List.of(6, 6, 0, 7, 7, 7);
        List<Integer> expectedSmallPitsSecondPlayer = List.of(7, 7, 6, 6, 6, 6);

        assertEquals(expectedSmallPitsFirstPlayer, actualGame.getFirstPlayer().getBoard().getSmallPits());
        assertEquals(expectedSmallPitsSecondPlayer, actualGame.getSecondPlayer().getBoard().getSmallPits());
        assertEquals(1, actualGame.getFirstPlayer().getBoard().getBigPit());
        assertEquals(0, actualGame.getSecondPlayer().getBoard().getBigPit());
    }

    @Test
    void givenInitialGameObject_whenMoveGameFrom6thPit_returnNextGameBoardPosition() throws GameNotFoundException {
        // given- precondition or setup
        String gameId = GAME_ID;

        // Initialize the board with a specific stone distribution
        int initialBigPit = 0;

        // Initialize the actualGame with the board and the current user as the second user
        Board firstPlayerBoard = new Board(new ArrayList<>(Collections.nCopies(SMALL_PIT_NUMBER, SEED_COUNT)), initialBigPit);
        Board secondPlayerBoard = new Board(new ArrayList<>(Collections.nCopies(SMALL_PIT_NUMBER, SEED_COUNT)), initialBigPit);

        Player firstPlayer = Player.builder()
                .isCurrentTurn(true)
                .board(firstPlayerBoard)
                .userId(FIRST_PLAYER_USER_ID)
                .build();
        Player secondPlayer = Player.builder()
                .board(secondPlayerBoard)
                .isCurrentTurn(false)
                .userId(SECOND_PLAYER_USER_ID)
                .build();

        // Initialize a actualGame instance here
        Game game = Game.builder().gameId(GAME_ID)
                .firstPlayer(firstPlayer)
                .secondPlayer(secondPlayer).activePlayerId(FIRST_PLAYER_USER_ID)
                .isFinished(false)
                .winnerPlayerId(null).build();

        given(gameRepository.findById(GAME_ID)).willReturn(Optional.of(game));

        // when - action or the behaviour that we are going test
        Movement movement = new Movement(FIRST_PLAYER_USER_ID, 6);
        MoveGameServiceOutput gameServiceOutput =
                gameService.moveGame(MoveGameServiceInput.builder()
                        .gameId(GAME_ID)
                        .movement(movement)
                        .build());

        Game actualGame = gameServiceOutput.getGame();

        // then - verify the output
        List<Integer> expectedSmallPitsFirstPlayer = List.of(6, 6, 6, 6, 6, 0);
        List<Integer> expectedSmallPitsSecondPlayer = List.of(7, 7, 7, 7, 7, 6);

        assertEquals(expectedSmallPitsFirstPlayer, actualGame.getFirstPlayer().getBoard().getSmallPits());
        assertEquals(expectedSmallPitsSecondPlayer, actualGame.getSecondPlayer().getBoard().getSmallPits());
        assertEquals(1, actualGame.getFirstPlayer().getBoard().getBigPit());
        assertEquals(0, actualGame.getSecondPlayer().getBoard().getBigPit());
    }

    @Test
    void givenGameWithStones_whenMoveGameCaptureOpponentStones_returnUpdatedGameBoard() throws GameNotFoundException {
        // Given
        // Initialize the board with a specific stone distribution
        List<Integer> firstPlayerSmallPits = new ArrayList<>(Arrays.asList(1, 0, 2, 0, 8, 7));
        List<Integer> secondPlayerSmallPits = new ArrayList<>(Arrays.asList(1, 0, 3, 7, 7, 6));
        int firstPlayerBigPit = 3;
        int secondPlayerBigPit = 2;

        // Initialize the game with the board and the current user as the second user
        Board firstPlayerBoard = new Board(firstPlayerSmallPits, firstPlayerBigPit);
        Board secondPlayerBoard = new Board(secondPlayerSmallPits, secondPlayerBigPit);

        Player firstPlayer = Player.builder()
                .isCurrentTurn(true)
                .board(firstPlayerBoard)
                .userId(FIRST_PLAYER_USER_ID)
                .build();
        Player secondPlayer = Player.builder()
                .board(secondPlayerBoard)
                .isCurrentTurn(false)
                .userId(SECOND_PLAYER_USER_ID)
                .build();

        Game game = Game.builder().gameId(GAME_ID)
                .firstPlayer(firstPlayer)
                .secondPlayer(secondPlayer).activePlayerId(FIRST_PLAYER_USER_ID)
                .isFinished(false)
                .winnerPlayerId(null).build();

        given(gameRepository.findById(GAME_ID)).willReturn(Optional.of(game));

        // Define the movement to capture stones from the 1st pit
        Movement movement = new Movement(FIRST_PLAYER_USER_ID, 1);
        //After capturing scenario
        List<Integer> expectedFirstUserSmallPits = List.of(0, 0, 2, 0, 8, 7);
        List<Integer> expectedSecondUserSmallPits = List.of(1, 0, 3, 7, 0, 6);
        int expectedFirstUserBigPit = 11;

        // When
        MoveGameServiceOutput gameServiceOutput =
                gameService.moveGame(MoveGameServiceInput.builder()
                        .gameId(GAME_ID)
                        .movement(movement)
                        .build());

        Game actualGame = gameServiceOutput.getGame();
        // Then
        assertEquals(expectedFirstUserSmallPits, actualGame.getFirstPlayer().getBoard().getSmallPits());
        assertEquals(expectedFirstUserBigPit, actualGame.getFirstPlayer().getBoard().getBigPit());
        assertEquals(expectedSecondUserSmallPits, actualGame.getSecondPlayer().getBoard().getSmallPits());
    }


    @Test
    void givenGameWithStones_whenMovementEndWithBigPit_thenContinueWithSameUser() throws GameNotFoundException {
        // Given
        String gameId = GAME_ID;

        // Initialize the board with a specific stone distribution
        List<Integer> firstPlayerSmallPits = new ArrayList<>(Arrays.asList(4, 4, 4, 4, 4, 4));
        List<Integer> secondPlayerSmallPits = new ArrayList<>(Arrays.asList(4, 4, 4, 4, 4, 4));

        int firstPlayerBigPit = 0;
        int secondPlayerBigPit = 0;

        Board firstPlayerBoard = new Board(firstPlayerSmallPits, firstPlayerBigPit);
        Board secondPlayerBoard = new Board(secondPlayerSmallPits, secondPlayerBigPit);

        Player firstPlayer = Player.builder()
                .isCurrentTurn(true)
                .board(firstPlayerBoard)
                .userId(FIRST_PLAYER_USER_ID)
                .build();
        Player secondPlayer = Player.builder()
                .board(secondPlayerBoard)
                .isCurrentTurn(false)
                .userId(SECOND_PLAYER_USER_ID)
                .build();

        Game game = Game.builder().gameId(GAME_ID)
                .firstPlayer(firstPlayer)
                .secondPlayer(secondPlayer).activePlayerId(FIRST_PLAYER_USER_ID)
                .isFinished(false)
                .winnerPlayerId(null).build();

        given(gameRepository.findById(GAME_ID)).willReturn(Optional.of(game));

        // Define the movement to capture stones from the 3rd pit
        Movement movement = new Movement(FIRST_PLAYER_USER_ID, 3);
        //After capturing scenario
        List<Integer> expectedFirstUserSmallPits = List.of(4, 4, 0, 5, 5, 5);
        List<Integer> expectedSecondUserSmallPits = List.of(4, 4, 4, 4, 4, 4);
        int expectedFirstUserBigPit = 1;

        // When
        MoveGameServiceOutput gameServiceOutput =
                gameService.moveGame(MoveGameServiceInput.builder()
                        .gameId(gameId)
                        .movement(movement)
                        .build());

        Game actualGame = gameServiceOutput.getGame();

        // Then
        assertEquals(expectedFirstUserSmallPits, actualGame.getFirstPlayer().getBoard().getSmallPits());
        assertEquals(expectedFirstUserBigPit, actualGame.getFirstPlayer().getBoard().getBigPit());
        assertEquals(expectedSecondUserSmallPits, actualGame.getSecondPlayer().getBoard().getSmallPits());
        assertEquals(true, actualGame.getFirstPlayer().isCurrentTurn());
    }


    @Test
    void givenGameWithStones_whenThereIsNoStone_thenGameOver() throws GameNotFoundException {
        // Given
        String gameId = GAME_ID;

        // Initialize the board with a specific stone distribution
        List<Integer> firstPlayerSmallPits = new ArrayList<>(Arrays.asList(0, 1, 0, 0, 0, 1));
        List<Integer> secondPlayerSmallPits = new ArrayList<>(Arrays.asList(1, 0, 0, 0, 0, 0));

        int firstPlayerBigPit = 30;
        int secondPlayerBigPit = 20;

        Board firstPlayerBoard = new Board(firstPlayerSmallPits, firstPlayerBigPit);
        Board secondPlayerBoard = new Board(secondPlayerSmallPits, secondPlayerBigPit);

        Player firstPlayer = Player.builder()
                .isCurrentTurn(false)
                .board(firstPlayerBoard)
                .userId(FIRST_PLAYER_USER_ID)
                .build();
        Player secondPlayer = Player.builder()
                .board(secondPlayerBoard)
                .isCurrentTurn(true)
                .userId(SECOND_PLAYER_USER_ID)
                .build();

        Game game = Game.builder().gameId(GAME_ID)
                .firstPlayer(firstPlayer)
                .secondPlayer(secondPlayer).activePlayerId(SECOND_PLAYER_USER_ID)
                .isFinished(false)
                .winnerPlayerId(null).build();

        // Initialize the game with the board and the current user as the second user
        given(gameRepository.findById(GAME_ID)).willReturn(Optional.of(game));


        // Define the movement to capture stones from the 3rd pit
        Movement movement = new Movement(SECOND_PLAYER_USER_ID, 1);
        //After capturing scenario
        List<Integer> expectedFirstUserSmallPits = List.of(0, 1, 0, 0, 0, 1);
        List<Integer> expectedSecondUserSmallPits = List.of(0, 0, 0, 0, 0, 0);

        int expectedFirstUserBigPit = 32;
        int expectedSecondUserBigPit = 21;
        boolean expectedGameIsFinished = true;

        // When
        MoveGameServiceOutput gameServiceOutput =
                gameService.moveGame(MoveGameServiceInput.builder()
                        .gameId(gameId)
                        .movement(movement)
                        .build());

        Game actualGame = gameServiceOutput.getGame();
        // Then
        assertEquals(expectedFirstUserSmallPits, actualGame.getFirstPlayer().getBoard().getSmallPits());
        assertEquals(expectedFirstUserBigPit, actualGame.getFirstPlayer().getBoard().getBigPit());
        assertEquals(expectedSecondUserSmallPits, actualGame.getSecondPlayer().getBoard().getSmallPits());
        assertEquals(expectedSecondUserBigPit, actualGame.getSecondPlayer().getBoard().getBigPit());
        assertEquals(expectedGameIsFinished, actualGame.isFinished());
        assertEquals(FIRST_PLAYER_USER_ID, actualGame.getWinnerPlayerId());
    }


    @Test
    void givenValidGameId_whenResetGameCalled_thenGameResetAndReturned() throws GameNotFoundException {
        // Given

        Game game = getPlayedGame();

        // Mock the repository to return a game with validGameId

        given(gameRepository.findById(GAME_ID)).willReturn(Optional.of(game));

        GameResetServiceInput resetInput = GameResetServiceInput.builder().gameId(GAME_ID).build();

        // When
        GameResetServiceOutput resetOutput = gameService.resetGame(resetInput);

        // Then
        // Check that the small pits of both players are filled with initial seeds
        assertThat(resetOutput.getGame().getFirstPlayer().getBoard().getSmallPits())
                .isEqualTo(List.of(6, 6, 6, 6, 6, 6));
        assertThat(resetOutput.getGame().getSecondPlayer().getBoard().getSmallPits())
                .isEqualTo(List.of(6, 6, 6, 6, 6, 6));

        // Check that the big pits are reset to 0
        assertThat(resetOutput.getGame().getFirstPlayer().getBoard().getBigPit()).isZero();
        assertThat(resetOutput.getGame().getSecondPlayer().getBoard().getBigPit()).isZero();

        // Verify that the game is saved after resetting
        verify(gameRepository).save(resetOutput.getGame());
    }


    @Test
    void givenInvalidGameId_whenResetGameCalled_thenThrowInvalidPlayerException() throws GameNotFoundException {
        // Given
        String invalidGameId = "invalidGame";
        GameResetServiceInput resetInput = GameResetServiceInput.builder().gameId(invalidGameId).build();

        // Mock the repository to throw a GameNotFoundException
        given(gameRepository.findById(invalidGameId)).willReturn(Optional.empty());

        // When and Then
        assertThatThrownBy(() -> gameService.resetGame(resetInput))
                .isInstanceOf(InvalidGameException.class);
    }

    @Test
    void givenValidGameId_whenGetGameCalled_thenRetrieveGameStatus() throws GameNotFoundException {
        // Given
        String gameId = GAME_ID;
        GameStatusServiceInput gameStatusInput = GameStatusServiceInput.builder().gameId(gameId).build();
        Game mockGame = new Game(); // Create a mock game

        // Mock the repository to return the mock game
        given(gameRepository.findById(GAME_ID)).willReturn(Optional.of(mockGame));

        // When
        GameStatusServiceOutput gameStatusOutput = gameService.getGame(gameStatusInput);

        // Then
        assertThat(gameStatusOutput.getGame()).isEqualTo(mockGame);
    }

    @Test
    void givenInvalidGameId_whenGetGameCalled_thenThrowInvalidPlayerException() throws GameNotFoundException {
        // Given
        String invalidGameId = "invalidGame";
        GameStatusServiceInput gameStatusInput = GameStatusServiceInput.builder().gameId(invalidGameId).build();

        // Mock the repository to throw a GameNotFoundException
        given(gameRepository.findById(invalidGameId)).willReturn(Optional.empty());

        // When and Then
        assertThatThrownBy(() -> gameService.getGame(gameStatusInput))
                .isInstanceOf(InvalidGameException.class);
    }
}
