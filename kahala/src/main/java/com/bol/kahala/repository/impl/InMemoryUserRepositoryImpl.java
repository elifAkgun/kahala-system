package com.bol.kahala.repository.impl;

import com.bol.kahala.model.domain.User;
import com.bol.kahala.repository.UserRepository;
import com.bol.kahala.service.exception.UserNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * This class implements the UserRepository interface and provides an in-memory storage for user data.
 */
@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {

    // A map to store users using their unique IDs as keys
    private final Map<String, User> userMap = new HashMap<>();

    /**
     * Creates and saves a new user in the repository.
     *
     * @param user The user object to be created and saved.
     * @return The created user.
     */
    @Override
    public User createUser(User user) {
        user.setUserId(UUID.randomUUID().toString());
        userMap.put(user.getUserId(), user);
        return user;
    }

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param userId The unique identifier of the user to retrieve.
     * @return The retrieved user.
     * @throws UserNotFoundException If the user with the specified ID is not found.
     */
    @Override
    public User findUserById(String userId) throws UserNotFoundException {
        if (!userMap.containsKey(userId)) {
            throw new UserNotFoundException();
        }
        return userMap.get(userId);
    }

    /**
     * Finds a user by their username.
     *
     * @param userName The username of the user to find.
     * @return The retrieved user, or null if not found.
     */
    @Override
    public User findUserByUserName(String userName) {
        for (User user : userMap.values()) {
            if (user.getUserName().equals(userName)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Returns the map containing user data.
     *
     * @return The map of users.
     */
    public Map<String, User> getUsers() {
        return userMap;
    }
}
