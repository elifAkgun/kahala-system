package com.bol.kahala.service.output;

import com.bol.kahala.model.domain.Game;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateGameServiceOutput {

    private Game game;
}
