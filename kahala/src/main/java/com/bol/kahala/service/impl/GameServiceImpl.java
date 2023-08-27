package com.bol.kahala.service.impl;

import com.bol.kahala.constant.GameConstants;
import com.bol.kahala.model.domain.Board;
import com.bol.kahala.model.domain.Game;
import com.bol.kahala.model.domain.Player;
import com.bol.kahala.model.domain.User;
import com.bol.kahala.repository.GameRepository;
import com.bol.kahala.service.GameService;
import com.bol.kahala.service.UserService;
import com.bol.kahala.service.exception.GameNotFoundException;
import com.bol.kahala.service.exception.InvalidGameException;
import com.bol.kahala.service.exception.InvalidPlayerException;
import com.bol.kahala.service.input.*;
import com.bol.kahala.service.output.*;
import com.bol.kahala.validation.ValidationMessagesUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.bol.kahala.constant.GameConstants.*;
import static com.bol.kahala.validation.ValidationMessages.*;

/**
 * Implementation of the GameService interface providing methods to manage game operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GameServiceImpl implements GameService {


    private final GameRepository gameRepository;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);

    private final ValidationMessagesUtil validationMessagesUtil;

    /**
     * Creates a new game based on the provided input parameters.
     *
     * @param input The input parameters for creating the game.
     * @return The output containing the created game.
     * @throws InvalidPlayerException If invalid player information is provided.
     */
    @Override
    public CreateGameServiceOutput createGame(CreateGameServiceInput input) {

        validateCreateGameServiceInput(input);

        Game game = createInitialGameInstance(input.getFirstPlayerId(), input.getSecondPlayerId());

        gameRepository.saveGame(game);

        return CreateGameServiceOutput.builder().game(game).build();
    }


    /**
     * Retrieves the status of a game based on the provided input parameters.
     *
     * @param input The input parameters for retrieving the game status.
     * @return The output containing the game status.
     * @throws InvalidPlayerException If invalid player information is provided.
     */
    @Override
    public GameStatusServiceOutput getGame(GameStatusServiceInput input) {
        Game game = getGame(input.getGameId());
        return GameStatusServiceOutput.builder().game(game).build();
    }

    /**
     * Resets the specified game to its initial state.
     *
     * @param input The input parameters for resetting the game.
     * @return The output containing the game in its initial state after reset.
     * @throws InvalidPlayerException If invalid player information is provided.
     */
    @Override
    public GameResetServiceOutput resetGame(GameResetServiceInput input) {
        // Retrieve the game based on the provided gameId
        Game game = getGame(input.getGameId());

        // Create an initial game instance with the same player IDs and reset attributes
        Game initialGameInstance = createInitialGameInstance(game.getFirstPlayer().getUserId(), game.getSecondPlayer().getUserId());
        initialGameInstance.setGameId(game.getGameId());

        // Save the initial game instance to the repository
        gameRepository.saveGame(initialGameInstance);

        // Return the reset game as output
        return GameResetServiceOutput.builder().game(initialGameInstance).build();
    }

    /**
     * Executes a move in the game.
     *
     * @param input The input parameters for the move.
     * @return The output containing the game state after the move.
     * @throws InvalidPlayerException If invalid player information is provided.
     * @throws InvalidGameException   If the game is not found or invalid.
     */
    @Override
    public MoveGameServiceOutput moveGame(MoveGameServiceInput input) {
        // Retrieve the game based on the provided gameId
        Game game = getGame(input.getGameId());

        // Validate that the provided player is the active player in the game
        validateActivePlayer(input.getMovement().getPlayerId(), game.getActivePlayerId());

        // Determine the current player and opponent player based on the turn
        Player currentPlayer;
        Player opponentPlayer;
        if (game.getFirstPlayer().isCurrentTurn()) {
            currentPlayer = game.getFirstPlayer();
            opponentPlayer = game.getSecondPlayer();
        } else {
            currentPlayer = game.getSecondPlayer();
            opponentPlayer = game.getFirstPlayer();
        }

        // Create copies of the current player's and opponent player's small pits
        List<Integer> currentPlayerSmallPits = currentPlayer.getBoard().getSmallPits();
        List<Integer> opponentPlayerSmallPits = opponentPlayer.getBoard().getSmallPits();

        int currentPitIndex = input.getMovement().getPosition() - 1;
        int stonesInHand = currentPlayerSmallPits.get(currentPitIndex);
        currentPlayerSmallPits.set(currentPitIndex, 0);

        // Distribute the stones in the hand to the pits based on game rules
        while (stonesInHand > 0) {
            currentPitIndex = (currentPitIndex + 1) % BOARD_TOTAL_PITS_COUNT;

            //currentPitIndex is not opponent player big pit's index
            if (currentPitIndex != BOARD_TOTAL_PITS_COUNT - 1) {

                //currentPitIndex is player big pit's index
                if (currentPitIndex == GameConstants.SMALL_PIT_NUMBER) {
                    currentPlayer.getBoard().increaseBigPit();
                } else {

                    //currentPitIndex is in the player small pits
                    if (currentPitIndex < GameConstants.SMALL_PIT_NUMBER) {
                        currentPlayerSmallPits.set(currentPitIndex,
                                currentPlayerSmallPits.get(currentPitIndex) + 1);
                    }
                    //currentPitIndex is in the opponent small pits
                    else {
                        int opponentPitIndex = currentPitIndex - GameConstants.SMALL_PIT_NUMBER - 1;
                        opponentPlayerSmallPits.set(opponentPitIndex,
                                opponentPlayer.getBoard().getSmallPits().get(opponentPitIndex) + 1);
                    }
                }
                stonesInHand--;
            }
        }

        // Handle capturing of stones based on game rules
        int capturedStones = handleStoneCaptures(currentPitIndex, currentPlayerSmallPits, opponentPlayerSmallPits);

        // Update the game board with the modified pit configurations
        updateGameBoard(game,
                currentPlayerSmallPits,
                currentPlayer.getBoard().getBigPit() + capturedStones,
                opponentPlayerSmallPits);

        // Check if the game has finished
        boolean gameFinished = isGameFinished(game);
        String activePlayerId = null;
        if (!gameFinished) {
            // Find the ID of the next active player
            activePlayerId = findActivePlayerId(game, currentPitIndex);
        }

        // Update the game status and save it to the repository
        updateGameStatus(game, activePlayerId, gameFinished);
        gameRepository.saveGame(game);

        // Return the updated game state as output
        return MoveGameServiceOutput.builder()
                .game(game)
                .build();
    }


    private void updateGameBoard(Game game,
                                 List<Integer> currentPlayerSmallPits,
                                 int currentPlayerBigPit,
                                 List<Integer> opponentPlayerSmallPits) {
        // Determine which player's turn it is
        Player activePlayer = game.getFirstPlayer().isCurrentTurn() ? game.getFirstPlayer() : game.getSecondPlayer();
        // Determine the opponent player
        Player opponentPlayer = game.getFirstPlayer().isCurrentTurn() ? game.getSecondPlayer() : game.getFirstPlayer();

        // Update the active player's board
        activePlayer.getBoard().setSmallPits(currentPlayerSmallPits);
        activePlayer.getBoard().setBigPit(currentPlayerBigPit);

        // Update the opponent player's board
        opponentPlayer.getBoard().setSmallPits(opponentPlayerSmallPits);
    }


    private static Game createInitialGameInstance(String firstPlayerId, String secondPlayerId) {
        int initialBigPit = 0;

        // Initialize the game with the board and the current user as the first user
        Board firstPlayerBoard = new Board(new ArrayList<>(Collections.nCopies(SMALL_PIT_NUMBER, SEED_COUNT)), initialBigPit);
        Board secondPlayerBoard = new Board(new ArrayList<>(Collections.nCopies(SMALL_PIT_NUMBER, SEED_COUNT)), initialBigPit);

        Player firstPlayer = Player.builder()
                .isCurrentTurn(true)
                .board(firstPlayerBoard)
                .userId(firstPlayerId)
                .build();
        Player secondPlayer = Player.builder()
                .board(secondPlayerBoard)
                .isCurrentTurn(false)
                .userId(secondPlayerId)
                .build();

        Game game = Game.builder()
                .firstPlayer(firstPlayer)
                .secondPlayer(secondPlayer)
                .isFinished(false)
                .activePlayerId(firstPlayer.getUserId())
                .build();
        return game;
    }

    private void validateCreateGameServiceInput(CreateGameServiceInput input) {
        if (input.getFirstPlayerId().equals(input.getSecondPlayerId())) {
            throw new InvalidPlayerException(validationMessagesUtil.getExceptionMessage(
                    INVALID_PLAYER_EXCEPTION_SAME_PLAYER_ID_MESSAGE, input.getFirstPlayerId()));
        }

        UserServiceOutput serviceOutputFirstUser = userService.getUser(UserServiceInput.builder().userId(input.getFirstPlayerId()).build());
        User firstUser = serviceOutputFirstUser.getUser();
        if (firstUser == null) {
            throw new InvalidPlayerException(validationMessagesUtil.getExceptionMessage(
                    INVALID_PLAYER_EXCEPTION_PLAYER_NOT_FOUND_MESSAGE, input.getFirstPlayerId()));
        }

        UserServiceOutput serviceOutputSecondUser = userService.getUser(UserServiceInput.builder().userId(input.getSecondPlayerId()).build());
        User secondUser = serviceOutputSecondUser.getUser();
        if (secondUser == null) {
            throw new InvalidPlayerException(validationMessagesUtil.getExceptionMessage(
                    INVALID_PLAYER_EXCEPTION_PLAYER_NOT_FOUND_MESSAGE, input.getSecondPlayerId()));
        }
    }

    private Game getGame(String gameId) {
        try {
            return gameRepository.findGameById(gameId);
        } catch (GameNotFoundException e) {
            logger.info("Invalid gameId: {}", gameId);
            throw new InvalidGameException(validationMessagesUtil.getExceptionMessage(
                    INVALID_GAME_EXCEPTION_GAME_NOT_FOUND_MESSAGE, gameId));
        }
    }

    private void validateActivePlayer(String playerId, String activePlayerId) {
        if (!playerId.equals(activePlayerId)) {
            throw new InvalidPlayerException(validationMessagesUtil.getExceptionMessage(
                    INVALID_PLAYER_EXCEPTION_NOT_ACTIVE_PLAYER_MESSAGE, playerId));
        }
    }

    /**
     * Handles the capture of stones from the player's pits and the opponent's pits.
     *
     * @param currentPitIndex         The index of the pit that was last played by the active player.
     * @param currentPlayerSmallPits  The list of stones in the active player's small pits.
     * @param opponentPlayerSmallPits The list of stones in the opponent player's small pits.
     * @return The number of stones captured during this move.
     */
    private int handleStoneCaptures(int currentPitIndex, List<Integer> currentPlayerSmallPits, List<Integer> opponentPlayerSmallPits) {
        // Initialize the count of captured stones
        int capturedStones = 0;

        // Check if the last played pit is on the active player's side and contains only one stone
        if (currentPitIndex < GameConstants.SMALL_PIT_NUMBER && currentPlayerSmallPits.get(currentPitIndex) == 1) {
            // Calculate the index of the opposite pit on the opponent's side
            int oppositeIndex = GameConstants.SMALL_PIT_NUMBER - currentPitIndex - 1;

            // Capture the stones from both pits and update the counts
            capturedStones = currentPlayerSmallPits.get(currentPitIndex) + opponentPlayerSmallPits.get(oppositeIndex);
            opponentPlayerSmallPits.set(oppositeIndex, 0);
            currentPlayerSmallPits.set(currentPitIndex, 0);
        }

        // Return the count of captured stones
        return capturedStones;
    }

    private String findActivePlayerId(Game game, int currentPitIndex) {
        Player firstPlayer = game.getFirstPlayer();
        Player secondPlayer = game.getSecondPlayer();

        if (currentPitIndex != GameConstants.SMALL_PIT_NUMBER) {
            // Swap the turn between players
            firstPlayer.setCurrentTurn(!firstPlayer.isCurrentTurn());
            secondPlayer.setCurrentTurn(!secondPlayer.isCurrentTurn());

        }
        return firstPlayer.isCurrentTurn() ? firstPlayer.getUserId() : secondPlayer.getUserId();
    }

    private void updateGameStatus(Game game, String nextPlayerId, boolean gameFinished) {
        game.setFinished(gameFinished);
        game.setActivePlayerId(nextPlayerId);
        if (gameFinished) {
            dealRemainingSeedsToPlayers(game);
            determineWinnerAndSetWinnerId(game);
        }
    }

    /**
     * Determine the winner or tie situation and set the winnerPlayerId accordingly.
     *
     * @param game The game instance to evaluate.
     */
    public void determineWinnerAndSetWinnerId(Game game) {
        int firstPlayerScore = game.getFirstPlayer().getBoard().getBigPit();
        int secondPlayerScore = game.getSecondPlayer().getBoard().getBigPit();

        if (firstPlayerScore > secondPlayerScore) {
            game.setWinnerPlayerId(game.getFirstPlayer().getUserId());
        } else if (secondPlayerScore > firstPlayerScore) {
            game.setWinnerPlayerId(game.getSecondPlayer().getUserId());
        } else {
            game.setWinnerPlayerId("tie"); // Indicate a tie situation
        }
    }


    /**
     * Distributes the remaining stones from small pits to players' big pits.
     *
     * @param game The game instance containing player and board information.
     */
    private void dealRemainingSeedsToPlayers(Game game) {
        // Calculate the total stones remaining for the first player
        int totalStoneForFirstPlayer = game.getFirstPlayer().getBoard().getSmallPits().stream()
                .mapMultiToInt((stoneCount, consumer) -> consumer.accept(stoneCount))
                .sum();

        // Add the total stones to the first player's big pit
        game.getFirstPlayer().getBoard().addBigPit(totalStoneForFirstPlayer);

        // Calculate the total stones remaining for the second player
        int totalStoneForSecondPlayer = game.getSecondPlayer().getBoard().getSmallPits().stream()
                .mapMultiToInt((stoneCount, consumer) -> consumer.accept(stoneCount))
                .sum();

        // Add the total stones to the second player's big pit
        game.getSecondPlayer().getBoard().addBigPit(totalStoneForSecondPlayer);
    }


    private boolean isGameFinished(Game game) {
        List<Integer> firstPlayerSmallPits = game.getFirstPlayer().getBoard().getSmallPits();
        List<Integer> secondPlayerSmallPits = game.getSecondPlayer().getBoard().getSmallPits();

        return firstPlayerSmallPits.stream().allMatch(pit -> pit == 0) ||
                secondPlayerSmallPits.stream().allMatch(pit -> pit == 0);
    }
}

