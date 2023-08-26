package com.bol.kahala.controller.request;

import com.bol.kahala.validation.IntegerValue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class MoveGameRequest implements Serializable {

    @NotEmpty(message = "{NotEmpty.moveGameRequest.playerId}")
    private String playerId;

    @Max(value = 6, message = "{Max.moveGameRequest.pitNumber}")
    @Min(value = 1, message = "{Min.moveGameRequest.pitNumber}")
    @IntegerValue(message = "{NumberFormat.moveGameRequest.pitNumber}")
    private Integer pitNumber;

}
