package com.bol.kahala.service.impl;

import com.bol.kahala.dto.UserDto;
import com.bol.kahala.model.User;
import com.bol.kahala.repository.UserRepository;
import com.bol.kahala.service.exception.DuplicateUserNameException;
import com.bol.kahala.service.exception.InvalidUserException;
import com.bol.kahala.service.exception.UserAlreadyExistException;
import com.bol.kahala.service.input.CreateUserServiceInput;
import com.bol.kahala.service.input.UserServiceInput;
import com.bol.kahala.service.output.CreateUserServiceOutput;
import com.bol.kahala.service.output.UserServiceOutput;
import com.bol.kahala.validation.ValidationMessagesUtil;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.bol.kahala.helper.UserDataHelper.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserServiceImplTest {


    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    ValidationMessagesUtil validationMessagesUtil;

    // JUnit test for
    @Test
    void givenUserAlreadyExist_whenCreateUserCalled_thenReturnException() throws DuplicateUserNameException {
        // given- precondition or setup
        User user = User.builder().userId("123").build();
        given(userRepository.save(user))
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
        given(userRepository.save(any(User.class))).willReturn(user);

        // when - action or the behaviour that we are going test
        CreateUserServiceInput serviceInput = CreateUserServiceInput.builder().user(user).build();
        CreateUserServiceOutput output = userService.createUser(serviceInput);

        // then - verify the output
        assertThat(output.getUser().getUserName()).isEqualTo(USER_NAME);
        assertThat(output.getUser().getPassword()).isEqualTo(PASSWORD);
        assertThat(output.getUser().getUserId()).isEqualTo(USER_ID);
    }

    @Test
    public void testCreateUser_Success() throws UserAlreadyExistException {
        // Creating mock data
        User newUser = new User("userId", "newuser", "New User");
        CreateUserServiceInput input = CreateUserServiceInput.builder().user(newUser).build();

        // Simulating userRepository.findAll
        when(userRepository.findAll()).thenReturn(List.of());
        // Simulating userRepository.save
        when(userRepository.save(newUser)).thenReturn(newUser);

        // Calling the service method
        CreateUserServiceOutput output = userService.createUser(input);

        // Checking the output
        assertNotNull(output);
        assertEquals(UserDto.toDto(newUser), output.getUser());

        // Verifying correct method invocations
        verify(userRepository, times(1)).findAll();
        verify(userRepository, times(1)).save(newUser);
        verify(validationMessagesUtil, times(0)).getExceptionMessage(any(), any()); // User not found case

        // Verifying no more interactions
        verifyNoMoreInteractions(userRepository, validationMessagesUtil);
    }

    @Test
    public void testCreateUser_UserAlreadyExists() {
        User existingUser = new User("userId", "user", "Existing User");
        CreateUserServiceInput input = CreateUserServiceInput.builder()
                .user(existingUser).build();

        // Simulating userRepository.findAll
        when(userRepository.findAll()).thenReturn(List.of(existingUser));

        // Simulating validationMessagesUtil.getExceptionMessage
        when(validationMessagesUtil.getExceptionMessage(any(), any())).thenReturn("User already exists");

        // Calling the service method and expecting UserAlreadyExistException
        assertThrows(UserAlreadyExistException.class, () -> userService.createUser(input));

        // Verifying correct method invocations
        verify(userRepository, times(1)).findAll();
        verify(validationMessagesUtil, times(1)).getExceptionMessage(any(), any());

        // Verifying no more interactions
        verifyNoMoreInteractions(userRepository, validationMessagesUtil);
    }


    @Test
    void givenUserNotExist_whenGetUserCalled_thenReturnException() {
        // given- precondition or setup
        User user = User.builder().userId(USER_ID)
                .password(PASSWORD)
                .userName(USER_NAME)
                .build();
        given(userRepository.findById(anyString()))
                .willReturn(Optional.empty());

        // when - action or the behaviour that we are going test
        UserServiceInput serviceInput = UserServiceInput.builder().userId(USER_ID).build();

        // then - verify the output
        assertThatThrownBy(() ->
                userService.getUser(serviceInput))
                .isInstanceOf(InvalidUserException.class);
    }

    @Test
    void givenValidUserServiceInput_whenGetUserCalled_thenReturnUser() {
        // given- precondition or setup
        User user = User.builder().userId(USER_ID)
                .password(PASSWORD)
                .userName(USER_NAME)
                .build();
        given(userRepository.findById(anyString())).willReturn(Optional.of(user));

        // when - action or the behaviour that we are going test
        UserServiceInput serviceInput = UserServiceInput.builder().userId(USER_ID).build();
        UserServiceOutput output = userService.getUser(serviceInput);

        // then - verify the output
        assertThat(output.getUser().getUserName()).isEqualTo(USER_NAME);
        assertThat(output.getUser().getPassword()).isEqualTo(PASSWORD);
        assertThat(output.getUser().getUserId()).isEqualTo(USER_ID);

    }

}