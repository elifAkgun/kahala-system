package com.bol.kahala.repository;

import com.bol.kahala.model.domain.User;
import com.bol.kahala.service.exception.UserNotFoundException;

/**
 * This interface defines the contract for managing user data in the repository.
 */
public interface UserRepository {

    /**
     * Creates a new user in the repository.
     *
     * @param user The user object representing the user to be created.
     * @return The created user.
     */
    User createUser(User user);

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param userId The unique identifier of the user to retrieve.
     * @return The retrieved user.
     * @throws UserNotFoundException If the user with the specified ID is not found.
     */
    User findUserById(String userId) throws UserNotFoundException;

    /**
     * Retrieves a user by their username.
     *
     * @param userName The username of the user to retrieve.
     * @return The retrieved user, or null if not found.
     */
    User findUserByUserName(String userName);
}

