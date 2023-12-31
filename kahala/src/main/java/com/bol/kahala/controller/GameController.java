package com.bol.kahala.controller;

import com.bol.kahala.controller.request.CreateGameRequest;
import com.bol.kahala.controller.request.MoveGameRequest;
import com.bol.kahala.controller.response.GameResponse;
import com.bol.kahala.dto.MovementDto;
import com.bol.kahala.service.GameService;
import com.bol.kahala.service.input.CreateGameServiceInput;
import com.bol.kahala.service.input.GameResetServiceInput;
import com.bol.kahala.service.input.GameStatusServiceInput;
import com.bol.kahala.service.input.MoveGameServiceInput;
import com.bol.kahala.service.output.CreateGameServiceOutput;
import com.bol.kahala.service.output.GameResetServiceOutput;
import com.bol.kahala.service.output.GameStatusServiceOutput;
import com.bol.kahala.service.output.MoveGameServiceOutput;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

/**
 * This class defines the endpoints for managing game-related operations.
 */
@RestController
@RequestMapping("/games")
@AllArgsConstructor
public class GameController {

    private final GameService gameService;

    /**
     * Creates a new game using the provided player IDs.
     *
     * @param createGameRequest The request containing player IDs.
     * @return A ResponseEntity containing the created game response.
     */
    @PostMapping
    public ResponseEntity<GameResponse> createGame(@RequestBody @Valid CreateGameRequest createGameRequest) {
        CreateGameServiceOutput createdGame = gameService.createGame(CreateGameServiceInput.builder()
                .firstPlayerId(createGameRequest.getFirstPlayerId())
                .secondPlayerId(createGameRequest.getSecondPlayerId()).build());
        return createdGame != null ?
                ResponseEntity
                        .status(HttpStatus.CREATED)
                        .location(URI.create("/games/" + createdGame.getGame().getGameId()))
                        .body(GameResponse.builder().game(createdGame.getGame()).build())
                : ResponseEntity.badRequest().build();
    }

    /**
     * Moves the game state based on the provided game ID and move details.
     *
     * @param gameId          The ID of the game to be moved.
     * @param moveGameRequest The request containing move details.
     * @return A ResponseEntity containing the updated game response.
     */
    @PutMapping("/move/{gameId}")
    public ResponseEntity<GameResponse> moveGame(@PathVariable("gameId") @Valid @NotNull String gameId,
                                                 @RequestBody @Valid MoveGameRequest moveGameRequest) {
        MoveGameServiceOutput movedGame = gameService.moveGame(MoveGameServiceInput.builder()
                .gameId(gameId)
                .movementDto(MovementDto.builder()
                        .playerId(moveGameRequest.getPlayerId())
                        .position(moveGameRequest.getPitNumber())
                        .build())
                .build());
        return movedGame != null ?
                ResponseEntity.ok(GameResponse.builder().game(movedGame.getGame()).build())
                : ResponseEntity.badRequest().build();
    }

    /**
     * Retrieves the current status of the game based on the provided game ID.
     *
     * @param gameId The ID of the game to retrieve the status for.
     * @return A ResponseEntity containing the game status response.
     */
    @GetMapping("/{gameId}")
    public ResponseEntity<GameResponse> getGame(@PathVariable @Valid String gameId) {
        GameStatusServiceOutput gameStatusServiceOutput = gameService.getGame(GameStatusServiceInput.builder()
                .gameId(gameId).build());
        return gameStatusServiceOutput != null ?
                ResponseEntity.ok(GameResponse.builder()
                        .game(gameStatusServiceOutput.getGame())
                        .build())
                : ResponseEntity.badRequest().build();
    }

    /**
     * Resets the game state to its initial configuration based on the provided game ID.
     *
     * @param gameId The ID of the game to be reset.
     * @return A ResponseEntity containing the reset game response.
     */
    @DeleteMapping("/{gameId}")
    public ResponseEntity<GameResponse> resetGame(@Valid @PathVariable String gameId) {
        GameResetServiceOutput gameResetServiceOutput = gameService.resetGame(GameResetServiceInput.builder()
                .gameId(gameId).build());
        return gameResetServiceOutput != null ? ResponseEntity.ok(GameResponse.builder()
                .game(gameResetServiceOutput.getGame()).build())
                : ResponseEntity.badRequest().build();
    }
}
