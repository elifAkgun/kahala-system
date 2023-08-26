package com.bol.kahala.controller.exception.handler;

import com.bol.kahala.controller.exception.model.ErrorResponse;
import com.bol.kahala.service.exception.InvalidUserException;
import com.bol.kahala.service.exception.UserAlreadyExistException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Order(0)
public class UserExceptionHandler {

    @ExceptionHandler(UserAlreadyExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ErrorResponse userAlreadyExistExceptionHandler(UserAlreadyExistException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(InvalidUserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ErrorResponse invalidUserExceptionHandler(InvalidUserException ex) {
        return new ErrorResponse(ex.getMessage());
    }
}
