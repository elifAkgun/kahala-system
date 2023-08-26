package com.bol.kahala.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Board {
    private List<Integer> smallPits;
    private int bigPit;

    public void increaseBigPit() {
        bigPit++;
    }

    public void addBigPit(int stoneNumber) {
        bigPit = bigPit + stoneNumber;
    }
}