package com.bol.kahala.repository;

import com.bol.kahala.model.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * This interface defines the contract for managing user data in the repository.
 */
public interface UserRepository extends CrudRepository<User, String> {

    /**
     * Creates a new user in the repository.
     *
     * @param user The user object representing the user to be created.
     * @return The created user.
     */
    User save(User user);

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param userId The unique identifier of the user to retrieve.
     * @return The retrieved user.
     * @throws UserNotFoundException If the user with the specified ID is not found.
     */
    Optional<User> findById(String userId);

}

