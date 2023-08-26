package com.bol.kahala.repository.impl;

import com.bol.kahala.model.domain.User;
import com.bol.kahala.repository.UserRepository;
import com.bol.kahala.service.exception.UserNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {

    private final Map<String, User> userMap = new HashMap<>();

    @Override
    public User createUser(User user) {
        user.setUserId(UUID.randomUUID().toString());
        userMap.put(user.getUserId(), user);
        return user;
    }

    @Override
    public User findUserById(String userId) throws UserNotFoundException {
        if (!userMap.containsKey(userId)) {
            throw new UserNotFoundException();
        }
        return userMap.get(userId);
    }

    @Override
    public User findUserByUserName(String userName) {
        for (User user : userMap.values()) {
            if (user.getUserName().equals(userName)) {
                return user;
            }
        }
        return null;
    }

    public Map<String, User> getUsers() {
        return userMap;
    }
}
