package com.bol.kahala.repository.impl;

import static org.junit.jupiter.api.Assertions.*;

import com.bol.kahala.model.domain.User;
import com.bol.kahala.repository.UserRepository;
import com.bol.kahala.service.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.mockito.Mockito.when;

class InMemoryUserRepositoryImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private InMemoryUserRepositoryImpl inMemoryUserRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test creating a new user")
    void givenNewUser_whenCreateUser_thenUserIsCreated() {
        User newUser = new User();
        inMemoryUserRepository.createUser(newUser);

        assertNotNull(newUser.getUserId());
        assertTrue(inMemoryUserRepository.getUsers().containsKey(newUser.getUserId()));
        assertEquals(newUser, inMemoryUserRepository.getUsers().get(newUser.getUserId()));
    }

    @Test
    @DisplayName("Test finding an existing user by ID")
    void givenExistingUserId_whenFindUserById_thenUserIsFound() throws UserNotFoundException {
        String userId = UUID.randomUUID().toString();
        User user = new User();
        user.setUserId(userId);
        inMemoryUserRepository.getUsers().put(userId, user);

        User foundUser = inMemoryUserRepository.findUserById(userId);
        assertEquals(user, foundUser);
    }

    @Test
    @DisplayName("Test finding a non-existing user by ID")
    void givenNonExistingUserId_whenFindUserById_thenThrowUserNotFoundException() {
        String nonExistingUserId = UUID.randomUUID().toString();

        assertThrows(UserNotFoundException.class, () -> {
            inMemoryUserRepository.findUserById(nonExistingUserId);
        });
    }

    @Test
    @DisplayName("Test finding an existing user by username")
    void givenExistingUserName_whenFindUserByUserName_thenUserIsFound() {
        String userName = "testUser";
        User user = new User();
        user.setUserId(UUID.randomUUID().toString());
        user.setUserName(userName);
        inMemoryUserRepository.getUsers().put(user.getUserId(), user);

        User foundUser = inMemoryUserRepository.findUserByUserName(userName);
        assertNotNull(foundUser);
        assertEquals(user, foundUser);
    }

    @Test
    @DisplayName("Test finding a non-existing user by username")
    void givenNonExistingUserName_whenFindUserByUserName_thenReturnNull() {
        String nonExistingUserName = "nonExistingUser";

        User foundUser = inMemoryUserRepository.findUserByUserName(nonExistingUserName);
        assertNull(foundUser);
    }
}
