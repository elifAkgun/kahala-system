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

import static com.bol.kahala.util.LoggingUtil.logError;
import static com.bol.kahala.util.LoggingUtil.logInfo;
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

    @Override
    public CreateUserServiceOutput createUser(CreateUserServiceInput input) throws UserAlreadyExistException {
        User user = input.getUser();
        Iterable<User> userRepositoryAll = userRepository.findAll();
        userRepositoryAll.forEach(user1 -> {
            if (user1.getUserName().equals(input.getUser().getUserName())) {
                String errorMessage = validationMessagesUtil.getExceptionMessage(
                        USER_ALREADY_EXIST_EXCEPTION_USER_ALREADY_EXIST_MESSAGE, user.getUserName());
                logError("Failed to create user. User already exists: {}", errorMessage);
                throw new UserAlreadyExistException(errorMessage);
            }
        });

        User createdUser = userRepository.save(user);
        logInfo("User created successfully: {}", createdUser);
        return CreateUserServiceOutput.builder().user(createdUser).build();
    }

    @Override
    public UserServiceOutput getUser(UserServiceInput input) {
        Optional<User> optionalUser = userRepository.findById(input.getUserId());
        if (optionalUser.isPresent()) {
            User retrievedUser = optionalUser.get();
            logInfo("Retrieved user information: {}", retrievedUser);
            return UserServiceOutput.builder().user(retrievedUser).build();
        } else {
            String errorMessage = validationMessagesUtil.getExceptionMessage(
                    INVALID_USER_EXCEPTION_USER_NOT_FOUND_MESSAGE, input.getUserId());
            logError("Failed to retrieve user. User not found: {}", errorMessage);
            throw new InvalidUserException(errorMessage);
        }
    }
}
