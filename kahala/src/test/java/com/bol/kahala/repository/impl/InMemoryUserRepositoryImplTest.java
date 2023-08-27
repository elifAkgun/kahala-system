package com.bol.kahala.repository.impl;

import com.bol.kahala.model.domain.User;
import com.bol.kahala.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

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
        inMemoryUserRepository.save(newUser);

        assertNotNull(newUser.getUserId());
        assertTrue(inMemoryUserRepository.getUsers().containsKey(newUser.getUserId()));
        assertEquals(newUser, inMemoryUserRepository.getUsers().get(newUser.getUserId()));
    }

    @Test
    @DisplayName("Test finding an existing user by ID")
    void givenExistingUserId_whenFindUserById_thenUserIsFound() {
        String userId = UUID.randomUUID().toString();
        User user = new User();
        user.setUserId(userId);
        inMemoryUserRepository.getUsers().put(userId, user);

        User foundUser = inMemoryUserRepository.findById(userId).get();
        assertEquals(user, foundUser);
    }
}
