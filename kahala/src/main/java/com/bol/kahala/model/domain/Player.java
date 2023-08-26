package com.bol.kahala.model.domain;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Player {
    private String userId;
    private Board board;
    private boolean isCurrentTurn;

}
