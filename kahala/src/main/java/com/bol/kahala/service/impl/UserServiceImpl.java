package com.bol.kahala.service.impl;

import com.bol.kahala.model.domain.User;
import com.bol.kahala.repository.UserRepository;
import com.bol.kahala.service.UserService;
import com.bol.kahala.service.exception.InvalidUserException;
import com.bol.kahala.service.exception.UserAlreadyExistException;
import com.bol.kahala.service.exception.UserNotFoundException;
import com.bol.kahala.service.input.CreateUserServiceInput;
import com.bol.kahala.service.input.UserServiceInput;
import com.bol.kahala.service.output.CreateUserServiceOutput;
import com.bol.kahala.service.output.UserServiceOutput;
import com.bol.kahala.validation.ValidationMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ValidationMessages validationMessages;
    private final UserRepository userRepository;

    @Override
    public CreateUserServiceOutput createUser(CreateUserServiceInput input) throws UserAlreadyExistException {
        User user = input.getUser();
        User userByUserName = userRepository.findUserByUserName(input.getUser().getUserName());
        if (userByUserName != null) {
            throw new UserAlreadyExistException(validationMessages.getExceptionMessage(
                    ValidationMessages.USER_ALREADY_EXIST_EXCEPTION_USER_ALREADY_EXIST_MESSAGE, user.getUserName()));
        }
        User createdUser = userRepository.createUser(user);
        return CreateUserServiceOutput.builder().user(createdUser).build();
    }

    @Override
    public UserServiceOutput getUser(UserServiceInput input) {
        User user = null;
        try {
            user = userRepository.findUserById(input.getUserId());
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            throw new InvalidUserException(validationMessages.getExceptionMessage(
                    ValidationMessages.INVALID_USER_EXCEPTION_USER_NOT_FOUND_MESSAGE, input.getUserId()));
        }
        return UserServiceOutput.builder().user(user).build();
    }

}
