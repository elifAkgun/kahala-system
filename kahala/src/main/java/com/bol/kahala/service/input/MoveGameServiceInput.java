package com.bol.kahala.service.input;

import com.bol.kahala.model.domain.Movement;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MoveGameServiceInput {
    String gameId;
    Movement movement;
}
