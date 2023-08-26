package com.bol.kahala.model.domain;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Player implements Serializable {
    private String userId;
    private Board board;
    private boolean isCurrentTurn;

}
