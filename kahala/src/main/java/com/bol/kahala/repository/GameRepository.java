package com.bol.kahala.repository;

import com.bol.kahala.model.domain.Game;
import org.springframework.data.repository.CrudRepository;

/**
 * This interface defines the contract for managing game data in the repository.
 */
public interface GameRepository extends CrudRepository<Game, String> {

}


