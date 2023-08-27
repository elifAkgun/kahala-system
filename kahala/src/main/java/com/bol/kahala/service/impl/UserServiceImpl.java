package com.bol.kahala.service.impl;

import com.bol.kahala.model.domain.User;
import com.bol.kahala.repository.UserRepository;
import com.bol.kahala.service.UserService;
import com.bol.kahala.service.exception.InvalidUserException;
import com.bol.kahala.service.exception.UserAlreadyExistException;
import com.bol.kahala.service.input.CreateUserServiceInput;
import com.bol.kahala.service.input.UserServiceInput;
import com.bol.kahala.service.output.CreateUserServiceOutput;
import com.bol.kahala.service.output.UserServiceOutput;
import com.bol.kahala.validation.ValidationMessagesUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.bol.kahala.validation.ValidationMessages.INVALID_USER_EXCEPTION_USER_NOT_FOUND_MESSAGE;
import static com.bol.kahala.validation.ValidationMessages.USER_ALREADY_EXIST_EXCEPTION_USER_ALREADY_EXIST_MESSAGE;

/**
 * Implementation of the UserService interface that provides user-related operations.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ValidationMessagesUtil validationMessagesUtil;
    private final UserRepository userRepository;

    /**
     * Creates a new user with the provided user information.
     *
     * @param input The input containing user information to be created.
     * @return The output containing the created user.
     * @throws UserAlreadyExistException If a user with the same username already exists.
     */
    @Override
    public CreateUserServiceOutput createUser(CreateUserServiceInput input) throws UserAlreadyExistException {
        User user = input.getUser();
        Iterable<User> userRepositoryAll = userRepository.findAll();
        userRepositoryAll.iterator().forEachRemaining(user1 -> {
            if (user1.getUserName().equals(input.getUser().getUserName())) {
                throw new UserAlreadyExistException(validationMessagesUtil.getExceptionMessage(
                        USER_ALREADY_EXIST_EXCEPTION_USER_ALREADY_EXIST_MESSAGE, user.getUserName()));
            }
        });

        User createdUser = userRepository.save(user);
        return CreateUserServiceOutput.builder().user(createdUser).build();
    }

    /**
     * Retrieves the user information based on the provided user ID.
     *
     * @param input The input containing the user ID to retrieve.
     * @return The output containing the retrieved user information.
     * @throws InvalidUserException If the user with the specified ID is not found.
     */
    @Override
    public UserServiceOutput getUser(UserServiceInput input) {
        Optional<User> optionalUser = userRepository.findById(input.getUserId());
        if (optionalUser.isPresent())
            return UserServiceOutput.builder().user(optionalUser.get()).build();
        else {
            throw new InvalidUserException(validationMessagesUtil.getExceptionMessage(
                    INVALID_USER_EXCEPTION_USER_NOT_FOUND_MESSAGE, input.getUserId()));
        }
    }
}