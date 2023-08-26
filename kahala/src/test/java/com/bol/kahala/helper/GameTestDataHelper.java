package com.bol.kahala.helper;

import com.bol.kahala.model.domain.Board;
import com.bol.kahala.model.domain.Game;
import com.bol.kahala.model.domain.Player;

import java.util.List;

public class GameTestDataHelper {

    public static final String FIRST_PLAYER_USER_ID = "player123";
    public static final String SECOND_PLAYER_USER_ID = "player456";
    public static final String GAME_ID = "game123";
    public static final boolean CURRENT_TURN_TRUE = true;
    public static final boolean CURRENT_TURN_FALSE = false;
    public static final int FIRST_BIG_PIT = 1;
    public static final List<Integer> FIRST_PLAYER_SMALL_PITS = List.of(1, 2, 3);
    public static final List<Integer> SECOND_PLAYER_SMALL_PITS = List.of(4, 5, 6);
    public static final int SECOND_BIG_PIT = 2;
    public static final boolean FINISHED = false;

    public static final Game GAME = Game.builder()
            .gameId(GAME_ID)
            .activePlayerId(FIRST_PLAYER_USER_ID)
            .firstPlayer(Player.builder()
                    .isCurrentTurn(CURRENT_TURN_TRUE)
                    .board(Board.builder().bigPit(FIRST_BIG_PIT)
                            .smallPits(FIRST_PLAYER_SMALL_PITS)
                            .build())
                    .userId(FIRST_PLAYER_USER_ID)
                    .build())
            .secondPlayer((Player.builder()
                    .isCurrentTurn(CURRENT_TURN_FALSE)
                    .board(Board.builder().bigPit(SECOND_BIG_PIT)
                            .smallPits(SECOND_PLAYER_SMALL_PITS)
                            .build())
                    .userId(SECOND_PLAYER_USER_ID)
                    .build()))
            .isFinished(FINISHED).build();

    public static final Game GAME_WITHOUT_ID = Game.builder()
            .activePlayerId(FIRST_PLAYER_USER_ID)
            .firstPlayer(Player.builder()
                    .isCurrentTurn(CURRENT_TURN_TRUE)
                    .board(Board.builder().bigPit(FIRST_BIG_PIT)
                            .smallPits(FIRST_PLAYER_SMALL_PITS)
                            .build())
                    .userId(FIRST_PLAYER_USER_ID)
                    .build())
            .secondPlayer((Player.builder()
                    .isCurrentTurn(CURRENT_TURN_FALSE)
                    .board(Board.builder().bigPit(SECOND_BIG_PIT)
                            .smallPits(SECOND_PLAYER_SMALL_PITS)
                            .build())
                    .userId(SECOND_PLAYER_USER_ID)
                    .build()))
            .isFinished(FINISHED).build();
}
