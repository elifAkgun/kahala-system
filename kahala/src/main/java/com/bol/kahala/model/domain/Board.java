package com.bol.kahala.model.domain;

import lombok.*;

import java.io.Serializable;
import java.util.List;


/**
 * This class represents the game board, consisting of small pits and a big pit.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class Board implements Serializable {

    /**
     * The list of small pits on the board.
     */
    private List<Integer> smallPits;

    /**
     * The number of stones in the big pit.
     */
    private int bigPit;

    /**
     * Increases the stone count in the big pit by 1.
     */
    public void increaseBigPit() {
        bigPit++;
    }

    /**
     * Adds stones to the big pit.
     *
     * @param stoneNumber The number of stones to add.
     */
    public void addBigPit(int stoneNumber) {
        bigPit = bigPit + stoneNumber;
    }
}
