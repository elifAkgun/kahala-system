package com.bol.kahala.model.domain;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Game {
    private String gameId;
    private Player firstPlayer;
    private Player secondPlayer;
    private String activePlayerId;
    private boolean isFinished;

}