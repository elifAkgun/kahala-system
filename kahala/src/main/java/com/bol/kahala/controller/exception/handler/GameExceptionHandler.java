package com.bol.kahala.controller.exception.handler;

import com.bol.kahala.controller.exception.model.ErrorResponse;
import com.bol.kahala.service.exception.InvalidGameException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Order(0)
public class GameExceptionHandler {
    @ExceptionHandler(InvalidGameException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @Order(0)
    public ErrorResponse gameNotFoundHandler(InvalidGameException ex) {
        return new ErrorResponse(
                ex.getMessage());
    }

}
