package com.bol.kahala.service;

import com.bol.kahala.service.exception.UserAlreadyExistException;
import com.bol.kahala.service.input.CreateUserServiceInput;
import com.bol.kahala.service.input.UserServiceInput;
import com.bol.kahala.service.output.CreateUserServiceOutput;
import com.bol.kahala.service.output.UserServiceOutput;

public interface UserService {

    CreateUserServiceOutput createUser(CreateUserServiceInput input) throws UserAlreadyExistException;

    UserServiceOutput getUser(UserServiceInput userId);
}
