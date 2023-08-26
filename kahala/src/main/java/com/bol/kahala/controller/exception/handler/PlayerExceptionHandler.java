package com.bol.kahala.controller.exception.handler;

import com.bol.kahala.controller.exception.model.ErrorResponse;
import com.bol.kahala.service.exception.InvalidPlayerException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Order(0)
public class PlayerExceptionHandler {

    @ExceptionHandler(InvalidPlayerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ErrorResponse invalidPlayerExceptionHandler(InvalidPlayerException ex) {
        return new ErrorResponse(ex.getMessage());
    }

}
