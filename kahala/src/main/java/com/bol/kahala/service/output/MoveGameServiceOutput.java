package com.bol.kahala.service.output;

import com.bol.kahala.model.domain.Game;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MoveGameServiceOutput {
    private Game game;
}
