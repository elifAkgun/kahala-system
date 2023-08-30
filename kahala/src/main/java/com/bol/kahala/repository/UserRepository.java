package com.bol.kahala.repository;

import com.bol.kahala.model.domain.User;
import org.springframework.data.repository.CrudRepository;

/**
 * This interface defines the contract for managing user data in the repository.
 */
public interface UserRepository extends CrudRepository<User, String> {

}

