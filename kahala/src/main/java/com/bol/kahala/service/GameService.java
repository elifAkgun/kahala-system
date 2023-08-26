package com.bol.kahala.service;

import com.bol.kahala.service.input.CreateGameServiceInput;
import com.bol.kahala.service.input.GameResetServiceInput;
import com.bol.kahala.service.input.GameStatusServiceInput;
import com.bol.kahala.service.input.MoveGameServiceInput;
import com.bol.kahala.service.output.CreateGameServiceOutput;
import com.bol.kahala.service.output.GameResetServiceOutput;
import com.bol.kahala.service.output.GameStatusServiceOutput;
import com.bol.kahala.service.output.MoveGameServiceOutput;

public interface GameService {

    CreateGameServiceOutput createGame(CreateGameServiceInput input);

    MoveGameServiceOutput moveGame(MoveGameServiceInput input);

    GameStatusServiceOutput getGame(GameStatusServiceInput input);

    GameResetServiceOutput resetGame(GameResetServiceInput input);

}