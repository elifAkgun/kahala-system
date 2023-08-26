package com.bol.kahala.service.input;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateGameServiceInput {

    private String firstPlayerId;
    private String secondPlayerId;
}
