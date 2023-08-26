package com.bol.kahala.controller;

import com.bol.kahala.controller.request.GameRequest;
import com.bol.kahala.controller.request.MoveGameRequest;
import com.bol.kahala.controller.response.GameResponse;
import com.bol.kahala.model.domain.Movement;
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

@RestController
@RequestMapping("/games")
@AllArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping
    public ResponseEntity<GameResponse> createGame(@RequestBody @Valid GameRequest gameRequest) {
        CreateGameServiceOutput createdGame = gameService.createGame(CreateGameServiceInput.builder()
                .firstPlayerId(gameRequest.getFirstPlayerId())
                .secondPlayerId(gameRequest.getSecondPlayerId()).build());
        return createdGame != null ?
                ResponseEntity
                        .status(HttpStatus.CREATED)
                        .location(URI.create("/games/" + createdGame.getGame().getGameId()))
                        .body(GameResponse.builder().game(createdGame.getGame()).build())
                : ResponseEntity.badRequest().build();
    }

    @PostMapping("/move/{gameId}")
    public ResponseEntity<GameResponse> moveGame(@PathVariable("gameId") @Valid @NotNull String gameId,
                                                 @RequestBody @Valid MoveGameRequest moveGameRequest) {
        MoveGameServiceOutput movedGame = gameService.moveGame(MoveGameServiceInput.builder()
                .gameId(gameId)
                .movement(Movement.builder()
                        .playerId(moveGameRequest.getPlayerId())
                        .position(moveGameRequest.getPitNumber())
                        .build())
                .build());
        return movedGame != null ?
                ResponseEntity.ok(GameResponse.builder().game(movedGame.getGame()).build())
                : ResponseEntity.badRequest().build();
    }

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

    @PutMapping("/{gameId}")
    public ResponseEntity<GameResponse> resetGame(@Valid @PathVariable String gameId) {
        GameResetServiceOutput gameResetServiceOutput = gameService.resetGame(GameResetServiceInput.builder()
                .gameId(gameId).build());
        return gameResetServiceOutput != null ? ResponseEntity.ok(GameResponse.builder()
                .game(gameResetServiceOutput.getGame()).build())
                : ResponseEntity.badRequest().build();
    }
}
