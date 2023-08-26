package com.bol.kahala.controller.response;

import com.bol.kahala.model.domain.Game;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class GameResponse implements Serializable {
    private Game game;
}
