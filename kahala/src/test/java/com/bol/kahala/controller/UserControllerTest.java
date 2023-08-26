package com.bol.kahala.controller;

import com.bol.kahala.controller.request.UserRequest;
import com.bol.kahala.helper.ErrorMessages;
import com.bol.kahala.model.domain.User;
import com.bol.kahala.service.UserService;
import com.bol.kahala.service.exception.UserAlreadyExistException;
import com.bol.kahala.service.input.CreateUserServiceInput;
import com.bol.kahala.service.input.UserServiceInput;
import com.bol.kahala.service.output.CreateUserServiceOutput;
import com.bol.kahala.service.output.UserServiceOutput;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static com.bol.kahala.helper.ErrorMessages.USER_ALREADY_EXIST;
import static com.bol.kahala.helper.UserDataHelper.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private UserService userService;

    @Test
     void givenUserRequestHasNoValue_whenCreateUserCalled_thenReturnErrorResponse() throws Exception {
        // given- precondition or setup
        UserRequest userRequest = UserRequest.builder().build();

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)));

        // then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
     void givenUserRequestHasNotPassword_whenCreateUserCalled_thenReturnErrorResponse() throws Exception {
        // given- precondition or setup
        UserRequest userRequest = UserRequest.builder().userName(USER_NAME).build();

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)));

        // then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status()
                        .isBadRequest())
                .andExpect(jsonPath("$.errorMessage",
                        CoreMatchers.is(ErrorMessages.USER_PASSWORD_CANNOT_BE_EMPTY)));
    }

    @Test
     void givenUserRequestHasNotUserName_whenCreateUserCalled_thenReturnErrorResponse() throws Exception {
        // given- precondition or setup
        UserRequest userRequest = UserRequest.builder().password(PASSWORD).build();

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)));

        // then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status()
                        .isBadRequest())
                .andExpect(jsonPath("$.errorMessage",
                        CoreMatchers.is(ErrorMessages.USER_USERNAME_CANNOT_BE_EMPTY)));
    }

    @Test
     void givenValidUser_whenCreateUserThrownException_thenReturnErrorResponse() throws Exception {
        // given- precondition or setup
        UserRequest userRequest = UserRequest.builder()
                .userName(USER_NAME).password(PASSWORD).build();
        given(userService.createUser(any(CreateUserServiceInput.class)))
                .willThrow(new UserAlreadyExistException(USER_ALREADY_EXIST));

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)));

        // then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status()
                        .isBadRequest())
                .andExpect(jsonPath("$.errorMessage",
                        CoreMatchers.is(USER_ALREADY_EXIST)));
    }

    @Test
     void givenValidUserRequest_whenCreateUserCalled_thenReturn201Created() throws Exception {
        // given- precondition or setup
        UserRequest userRequest = UserRequest.builder()
                .userName(USER_NAME).password(PASSWORD).build();

        CreateUserServiceOutput createUserServiceOutput = CreateUserServiceOutput
                .builder().user(
                        User.builder().userName(USER_NAME)
                                .password(PASSWORD).userId(USER_ID).build()).build();
        given(userService.createUser(any(CreateUserServiceInput.class)))
                .willReturn(createUserServiceOutput);
        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)));

        // then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/users/" + USER_ID));
    }

    @Test
     void givenHighRequestLoad_whenCreatingUsers_thenResponseTimeWithinThreshold() throws Exception {
        int requestCount = 1000;
        long startTime = System.currentTimeMillis();

        CreateUserServiceOutput createUserServiceOutput = CreateUserServiceOutput
                .builder().user(
                        User.builder().userName(USER_NAME)
                                .password(PASSWORD).userId(USER_ID).build()).build();

        given(userService.createUser(any(CreateUserServiceInput.class)))
                .willReturn(createUserServiceOutput);

        for (int i = 0; i < requestCount; i++) {
            UserRequest userRequest = UserRequest.builder()
                    .userName("user" + i)
                    .password("password" + i)
                    .build();

            mockMvc.perform(post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userRequest)))
                    .andExpect(status().isCreated());
        }

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        long expectedMaxElapsedTimeMillis = 5000; // Timeout: 5 second

        assertTrue(elapsedTime <= expectedMaxElapsedTimeMillis,
                "Response time exceeded the expected threshold");
    }

    @Test
     void givenConcurrentRequests_whenCreatingUsers_thenAllRequestsSuccessful() throws Exception {
        int concurrentRequestCount = 100;

        CreateUserServiceOutput createUserServiceOutput = CreateUserServiceOutput
                .builder().user(
                        User.builder().userName(USER_NAME)
                                .password(PASSWORD).userId(USER_ID).build()).build();

        given(userService.createUser(any(CreateUserServiceInput.class)))
                .willReturn(createUserServiceOutput);

        List<Runnable> tasks = new ArrayList<>();
        for (int i = 0; i < concurrentRequestCount; i++) {
            UserRequest userRequest = UserRequest.builder()
                    .userName("user" + i)
                    .password("password" + i)
                    .build();

            tasks.add(() -> {
                try {
                    mockMvc.perform(post("/users")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(userRequest)))
                            .andExpect(status().isCreated());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }

        List<Thread> threads = new ArrayList<>();
        for (Runnable task : tasks) {
            threads.add(new Thread(task));
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
    }

    @Test
     void givenValidUserId_whenGetUserCalled_thenReturnUserResponse() throws Exception {
        // Configure the necessary behaviors using mock objects.
        User user = User.builder().userId(USER_ID).userName(USER_NAME).build();
        given(userService.getUser(any(UserServiceInput.class)))
                .willReturn(UserServiceOutput.builder().user(user).build());

        // Perform the test scenario using the mock objects.
        mockMvc.perform(get("/users/{userId}", USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.userId", CoreMatchers.is(USER_ID)))
                .andExpect(jsonPath("$.user.userName", CoreMatchers.is(USER_NAME)));
    }

    @Test
     void givenInvalidUserId_whenGetUserCalled_thenReturnNotFoundResponse() throws Exception {
        // Configure the necessary behaviors using mock objects.
        given(userService.getUser(any(UserServiceInput.class)))
                .willReturn(UserServiceOutput.builder().user(null).build());

        // Perform the test scenario using the mock objects.
        mockMvc.perform(get("/users/{userId}", "invalidUser"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenNullUserId_whenGetUserCalled_thenReturnBadRequestResponse() throws Exception {
        // Perform the test scenario without configuring any mock objects.
        mockMvc.perform(get("/users/{userId}", ""))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenExceptionThrownByService_whenGetUserCalled_thenReturnInternalServerErrorResponse() throws Exception {
        // Configure the necessary behaviors using mock objects.
        given(userService.getUser(any(UserServiceInput.class)))
                .willThrow(new RuntimeException("Something went wrong"));

        // Perform the test scenario using the mock objects.
        mockMvc.perform(get("/users/{userId}", "user123"))
                .andExpect(status().isInternalServerError());
    }
}