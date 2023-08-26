package com.bol.kahala.controller.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class GameRequest implements Serializable {
    @NotEmpty(message = "{NotEmpty.createGameRequest.firstPlayerId}")
    private String firstPlayerId;

    @NotEmpty(message = "{NotEmpty.createGameRequest.secondPlayerId}")
    private String secondPlayerId;

}