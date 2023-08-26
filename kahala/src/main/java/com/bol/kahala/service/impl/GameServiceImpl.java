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
import com.bol.kahala.validation.ValidationMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.bol.kahala.constant.GameConstants.SEED_NUMBER;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);

    private final ValidationMessages validationMessages;

    @Override
    public CreateGameServiceOutput createGame(CreateGameServiceInput input) {

        if (input.getFirstPlayerId().equals(input.getSecondPlayerId())) {
            throw new InvalidPlayerException(validationMessages.getExceptionMessage(
                    ValidationMessages.INVALID_PLAYER_EXCEPTION_SAME_PLAYER_ID_MESSAGE, input.getFirstPlayerId()));
        }

        UserServiceOutput serviceOutputFirstUser = userService.getUser(UserServiceInput.builder().userId(input.getFirstPlayerId()).build());
        User firstUser = serviceOutputFirstUser.getUser();
        if (firstUser == null) {
            throw new InvalidPlayerException(validationMessages.getExceptionMessage(
                    ValidationMessages.INVALID_PLAYER_EXCEPTION_PLAYER_NOT_FOUND_MESSAGE, input.getFirstPlayerId()));
        }

        UserServiceOutput serviceOutputSecondUser = userService.getUser(UserServiceInput.builder().userId(input.getSecondPlayerId()).build());
        User secondUser = serviceOutputSecondUser.getUser();
        if (secondUser == null) {
            throw new InvalidPlayerException(validationMessages.getExceptionMessage(
                    ValidationMessages.INVALID_PLAYER_EXCEPTION_PLAYER_NOT_FOUND_MESSAGE, input.getSecondPlayerId()));
        }

        List<Integer> initialSmallPits = List.of(SEED_NUMBER, SEED_NUMBER, SEED_NUMBER,
                SEED_NUMBER, SEED_NUMBER, SEED_NUMBER);
        int initialBigPit = 0;

        // Initialize the actualGame with the board and the current user as the second user
        Board firstPlayerBoard = new Board(initialSmallPits, initialBigPit);
        Board secondPlayerBoard = new Board(initialSmallPits, initialBigPit);

        Player firstPlayer = Player.builder()
                .isCurrentTurn(true)
                .board(firstPlayerBoard)
                .userId(input.getFirstPlayerId())
                .build();
        Player secondPlayer = Player.builder()
                .board(secondPlayerBoard)
                .isCurrentTurn(false)
                .userId(input.getSecondPlayerId())
                .build();

        // Initialize a actualGame instance here
        Game game = Game.builder()
                .firstPlayer(firstPlayer)
                .secondPlayer(secondPlayer)
                .isFinished(false)
                .activePlayerId(firstPlayer.getUserId())
                .build();

        gameRepository.saveGame(game);

        return CreateGameServiceOutput.builder().game(game).build();
    }

    @Override
    public MoveGameServiceOutput moveGame(MoveGameServiceInput input) {

        Game game = getGame(input.getGameId());
        validateActivePlayer(input.getMovement().getPlayerId(), game.getActivePlayerId());

        Player currentPlayer, opponentPlayer;

        if (game.getFirstPlayer().isCurrentTurn()) {
            currentPlayer = game.getFirstPlayer();
            opponentPlayer = game.getSecondPlayer();

        } else {
            currentPlayer = game.getSecondPlayer();
            opponentPlayer = game.getFirstPlayer();
        }

        List<Integer> currentPlayerSmallPits = new ArrayList<>(currentPlayer.getBoard().getSmallPits());
        List<Integer> opponentPlayerSmallPits = new ArrayList<>(opponentPlayer.getBoard().getSmallPits());

        int currentPitIndex = input.getMovement().getPosition() - 1;
        int stonesInHand = currentPlayerSmallPits.get(currentPitIndex);
        currentPlayerSmallPits.set(currentPitIndex, 0);

        while (stonesInHand > 0) {
            currentPitIndex = (currentPitIndex + 1) % 14;
            if (currentPitIndex != 13) {
                if (currentPitIndex == GameConstants.PIT_NUMBER) {
                    currentPlayer.getBoard().increaseBigPit();
                } else {
                    if (currentPitIndex < GameConstants.PIT_NUMBER) {
                        currentPlayerSmallPits.set(currentPitIndex,
                                currentPlayerSmallPits.get(currentPitIndex) + 1);
                    } else {
                        int opponentPitIndex = currentPitIndex - GameConstants.PIT_NUMBER - 1;
                        opponentPlayerSmallPits.set(opponentPitIndex,
                                opponentPlayer.getBoard().getSmallPits().get(opponentPitIndex) + 1);
                    }
                }
                stonesInHand--;
            }
        }

        int capturedStones = handleStoneCaptures(currentPitIndex, currentPlayerSmallPits, opponentPlayerSmallPits);

        updateGameBoard(game,
                currentPlayerSmallPits,
                currentPlayer.getBoard().getBigPit() + capturedStones,
                opponentPlayerSmallPits);

        boolean gameFinished = isGameFinished(game);
        String activePlayerId = null;
        if (!gameFinished) {
            activePlayerId = findActivePlayerId(game, currentPitIndex);
        }

        updateGameStatus(game, activePlayerId, gameFinished);

        gameRepository.saveGame(game);
        return MoveGameServiceOutput.builder()
                .game(game)
                .build();
    }

    private void updateGameBoard(Game game,
                                 List<Integer> currentPlayerSmallPits,
                                 int currentPlayerBigPit,
                                 List<Integer> opponentPlayerSmallPits) {
        if (game.getFirstPlayer().isCurrentTurn()) {
            game.getFirstPlayer().getBoard().setSmallPits(new ArrayList<>(currentPlayerSmallPits));
            game.getFirstPlayer().getBoard().setBigPit(currentPlayerBigPit);
            game.getSecondPlayer().getBoard().setSmallPits(new ArrayList<>(opponentPlayerSmallPits));
        } else {
            game.getSecondPlayer().getBoard().setSmallPits(new ArrayList<>(currentPlayerSmallPits));
            game.getSecondPlayer().getBoard().setBigPit(currentPlayerBigPit);
            game.getFirstPlayer().getBoard().setSmallPits(new ArrayList<>(opponentPlayerSmallPits));
        }
    }

    private Game getGame(String gameId) {
        try {
            return gameRepository.findGameById(gameId);
        } catch (GameNotFoundException e) {
            logger.info("Invalid gameId: " + gameId);
            throw new InvalidGameException(validationMessages.getExceptionMessage(
                    ValidationMessages.INVALID_GAME_EXCEPTION_GAME_NOT_FOUND_MESSAGE, gameId));
        }
    }

    private void validateActivePlayer(String playerId, String activePlayerId) {
        if (!playerId.equals(activePlayerId)) {
            throw new InvalidPlayerException(validationMessages.getExceptionMessage(
                    ValidationMessages.INVALID_PLAYER_EXCEPTION_NOT_ACTIVE_PLAYER_MESSAGE, playerId));
        }
    }

    private int handleStoneCaptures(int currentPitIndex, List<Integer> currentPlayerSmallPits, List<Integer> opponentPlayerSmallPits) {
        int capturedStones = 0;
        if (currentPitIndex < GameConstants.PIT_NUMBER && currentPlayerSmallPits.get(currentPitIndex) == 1) {
            int oppositeIndex = GameConstants.PIT_NUMBER - currentPitIndex - 1;
            capturedStones = currentPlayerSmallPits.get(currentPitIndex) + opponentPlayerSmallPits.get(oppositeIndex);
            opponentPlayerSmallPits.set(oppositeIndex, 0);
            currentPlayerSmallPits.set(currentPitIndex, 0);
        }
        return capturedStones;
    }

    private String findActivePlayerId(Game game, int currentPitIndex) {
        String currentPlayerId;
        if (currentPitIndex == GameConstants.PIT_NUMBER) {
            currentPlayerId = game.getFirstPlayer().isCurrentTurn()
                    ? game.getFirstPlayer().getUserId()
                    : game.getSecondPlayer().getUserId();
        } else if (game.getFirstPlayer().isCurrentTurn()) {
            currentPlayerId = game.getSecondPlayer().getUserId();
            game.getSecondPlayer().setCurrentTurn(true);
            game.getFirstPlayer().setCurrentTurn(false);
        } else {
            currentPlayerId = game.getFirstPlayer().getUserId();
            game.getFirstPlayer().setCurrentTurn(true);
            game.getSecondPlayer().setCurrentTurn(false);

        }

        return currentPlayerId;
    }

    private void updateGameStatus(Game game, String nextPlayerId, boolean gameFinished) {
        game.setFinished(gameFinished);
        game.setActivePlayerId(nextPlayerId);
        if (gameFinished) {
            dealRemainingTilesToPlayers(game);
        }
    }

    private void dealRemainingTilesToPlayers(Game game) {
        int totalStoneForFirstPlayer = game.getFirstPlayer().getBoard().getSmallPits().stream()
                .mapMultiToInt((stoneCount, consumer) -> consumer.accept(stoneCount))
                .sum();
        game.getFirstPlayer().getBoard().addBigPit(totalStoneForFirstPlayer);
        int totalStoneForSecondPlayer = game.getSecondPlayer().getBoard().getSmallPits().stream()
                .mapMultiToInt((stoneCount, consumer) -> consumer.accept(stoneCount))
                .sum();
        game.getSecondPlayer().getBoard().addBigPit(totalStoneForSecondPlayer);
    }

    private boolean isGameFinished(Game game) {
        List<Integer> firstPlayerSmallPits = game.getFirstPlayer().getBoard().getSmallPits();
        List<Integer> secondPlayerSmallPits = game.getSecondPlayer().getBoard().getSmallPits();

        return firstPlayerSmallPits.stream().allMatch(pit -> pit == 0) ||
                secondPlayerSmallPits.stream().allMatch(pit -> pit == 0);
    }

    @Override
    public GameStatusServiceOutput getGame(GameStatusServiceInput input) {
        Game game = null;
        try {
            game = gameRepository.findGameById(input.getGameId());
        } catch (GameNotFoundException e) {
            logger.info(e.getMessage());
            throw new InvalidPlayerException(validationMessages.getExceptionMessage(
                    ValidationMessages.INVALID_GAME_EXCEPTION_GAME_NOT_FOUND_MESSAGE, input.getGameId()));

        }
        return GameStatusServiceOutput.builder().game(game).build();
    }

    @Override
    public GameResetServiceOutput resetGame(GameResetServiceInput input) {
        Game game = null;
        try {
            game = gameRepository.findGameById(input.getGameId());
        } catch (GameNotFoundException e) {
            logger.info("Invalid gameId: " + input);
            throw new InvalidPlayerException(validationMessages.getExceptionMessage(
                    ValidationMessages.INVALID_GAME_EXCEPTION_GAME_NOT_FOUND_MESSAGE, input.getGameId()));

        }

        List<Integer> initialPits = new ArrayList<>(Collections.nCopies(GameConstants.PIT_NUMBER, SEED_NUMBER));
        game.getFirstPlayer().getBoard().setSmallPits(initialPits);
        game.getSecondPlayer().getBoard().setSmallPits(initialPits);
        game.getFirstPlayer().getBoard().setBigPit(0);
        game.getSecondPlayer().getBoard().setBigPit(0);

        gameRepository.saveGame(game);
        return GameResetServiceOutput.builder().game(game).build();
    }
}

