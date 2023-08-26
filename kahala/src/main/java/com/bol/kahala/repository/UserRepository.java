package com.bol.kahala.repository;

import com.bol.kahala.model.domain.User;
import com.bol.kahala.service.exception.UserNotFoundException;

public interface UserRepository {
    User createUser(User user);

    User findUserById(String userId) throws UserNotFoundException;

    User findUserByUserName(String userName);
}
