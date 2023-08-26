package com.bol.kahala.service.impl;

import com.bol.kahala.model.domain.User;
import com.bol.kahala.repository.UserRepository;
import com.bol.kahala.service.exception.DuplicateUserNameException;
import com.bol.kahala.service.exception.InvalidUserException;
import com.bol.kahala.service.exception.UserAlreadyExistException;
import com.bol.kahala.service.exception.UserNotFoundException;
import com.bol.kahala.service.input.CreateUserServiceInput;
import com.bol.kahala.service.input.UserServiceInput;
import com.bol.kahala.service.output.CreateUserServiceOutput;
import com.bol.kahala.service.output.UserServiceOutput;
import com.bol.kahala.validation.ValidationMessages;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.bol.kahala.helper.UserDataHelper.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserServiceImplTest {


    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    ValidationMessages validationMessages;

    // JUnit test for
    @Test
     void givenUserAlreadyExist_whenCreateUserCalled_thenReturnException() throws DuplicateUserNameException {
        // given- precondition or setup
        User user = User.builder().userId("123").build();
        given(userRepository.createUser(user))
                .willThrow(UserAlreadyExistException.class);

// when - action or the behaviour that we are going test
        CreateUserServiceInput serviceInput = CreateUserServiceInput.builder().user(user).build();

        // then - verify the output
        assertThatThrownBy(() ->
                userService.createUser(serviceInput))
                .isInstanceOf(UserAlreadyExistException.class);
    }

    @Test
     void givenValidCreateUserServiceInput_whenCreateUserCalled_thenReturnCreatedUser() throws DuplicateUserNameException {
        // given- precondition or setup
        User user = User.builder().userId(USER_ID)
                .password(PASSWORD)
                .userName(USER_NAME)
                .build();
        given(userRepository.createUser(any(User.class))).willReturn(user);

        // when - action or the behaviour that we are going test
        CreateUserServiceInput serviceInput = CreateUserServiceInput.builder().user(user).build();
        CreateUserServiceOutput output = userService.createUser(serviceInput);

        // then - verify the output
        assertThat(output.getUser().getUserName()).isEqualTo(USER_NAME);
        assertThat(output.getUser().getPassword()).isEqualTo(PASSWORD);
        assertThat(output.getUser().getUserId()).isEqualTo(USER_ID);

    }

    @Test
     void givenUserNotExist_whenGetUserCalled_thenReturnException() throws UserNotFoundException {
        // given- precondition or setup
        User user = User.builder().userId(USER_ID)
                .password(PASSWORD)
                .userName(USER_NAME)
                .build();
        given(userRepository.findUserById(anyString()))
                .willThrow(UserNotFoundException.class);

        // when - action or the behaviour that we are going test
        UserServiceInput serviceInput = UserServiceInput.builder().userId(USER_ID).build();

        // then - verify the output
        assertThatThrownBy(() ->
                userService.getUser(serviceInput))
                .isInstanceOf(InvalidUserException.class);
    }

    @Test
     void givenValidUserServiceInput_whenGetUserCalled_thenReturnUser() throws UserNotFoundException {
        // given- precondition or setup
        User user = User.builder().userId(USER_ID)
                .password(PASSWORD)
                .userName(USER_NAME)
                .build();
        given(userRepository.findUserById(anyString())).willReturn(user);

        // when - action or the behaviour that we are going test
        UserServiceInput serviceInput = UserServiceInput.builder().userId(USER_ID).build();
        UserServiceOutput output = userService.getUser(serviceInput);

        // then - verify the output
        assertThat(output.getUser().getUserName()).isEqualTo(USER_NAME);
        assertThat(output.getUser().getPassword()).isEqualTo(PASSWORD);
        assertThat(output.getUser().getUserId()).isEqualTo(USER_ID);

    }

}