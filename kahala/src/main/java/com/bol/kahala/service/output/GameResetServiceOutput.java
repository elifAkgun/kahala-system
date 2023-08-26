package com.bol.kahala.service.output;

import com.bol.kahala.model.domain.Game;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GameResetServiceOutput {
    private Game game;
}
