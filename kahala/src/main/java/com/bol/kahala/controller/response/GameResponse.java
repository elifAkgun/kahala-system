package com.bol.kahala.controller.response;

import com.bol.kahala.model.domain.Game;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * This class represents a response containing game information. It includes a reference to the game object.
 */
@Getter
@Setter
@Builder
public class GameResponse implements Serializable {
    /**
     * The game object associated with the response.
     */
    private Game game;
}
