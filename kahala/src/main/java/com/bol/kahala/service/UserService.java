package com.bol.kahala.service;

import com.bol.kahala.service.exception.UserAlreadyExistException;
import com.bol.kahala.service.input.CreateUserServiceInput;
import com.bol.kahala.service.input.UserServiceInput;
import com.bol.kahala.service.output.CreateUserServiceOutput;
import com.bol.kahala.service.output.UserServiceOutput;

/**
 * An interface defining the contract for user-related operations.
 */
public interface UserService {

    /**
     * Creates a new user based on the provided input parameters.
     *
     * @param input The input parameters for creating a user.
     * @return The output containing information about the created user.
     * @throws UserAlreadyExistException If the user with the provided username already exists.
     */
    CreateUserServiceOutput createUser(CreateUserServiceInput input) throws UserAlreadyExistException;

    /**
     * Retrieves user information based on the provided user ID.
     *
     * @param userId The ID of the user to retrieve information for.
     * @return The output containing information about the requested user.
     */
    UserServiceOutput getUser(UserServiceInput userId);
}

