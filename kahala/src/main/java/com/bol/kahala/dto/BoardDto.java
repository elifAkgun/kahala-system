package com.bol.kahala.dto;

import com.bol.kahala.model.Board;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * This class represents the game board, consisting of small pits and a big pit.
 */
@Data
@Builder
public class BoardDto implements Serializable {

    /**
     * The list of small pits on the board.
     */
    private List<Integer> smallPits;

    /**
     * The number of stones in the big pit.
     */
    private int bigPit;

    /**
     * Converts a Board object to its corresponding Data Transfer Object (DTO) representation.
     *
     * @param board The Board object to be converted.
     * @return The BoardDto representation of the input Board object.
     */
    public static BoardDto toDto(Board board) {
        return board == null ? null : BoardDto.builder()
                .bigPit(board.getBigPit())
                .smallPits(board.getSmallPits())
                .build();
    }

}
