package com.bol.kahala.repository.impl;

import com.bol.kahala.model.domain.User;
import com.bol.kahala.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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
    public User save(User user) {
        user.setUserId(UUID.randomUUID().toString());
        userMap.put(user.getUserId(), user);
        return user;
    }

    @Override
    public <S extends User> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param userId The unique identifier of the user to retrieve.
     * @return The retrieved user.
     * @throws UserNotFoundException If the user with the specified ID is not found.
     */
    @Override
    public Optional<User> findById(String userId) {
        if (!userMap.containsKey(userId)) {
            return Optional.empty();
        }
        return Optional.of(userMap.get(userId));
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public Iterable<User> findAll() {
        return null;
    }

    @Override
    public Iterable<User> findAllById(Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(User entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends User> entities) {

    }

    @Override
    public void deleteAll() {

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
