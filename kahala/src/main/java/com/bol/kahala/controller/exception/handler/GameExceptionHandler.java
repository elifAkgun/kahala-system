package com.bol.kahala.controller.exception.handler;

import com.bol.kahala.controller.exception.model.ErrorResponse;
import com.bol.kahala.service.exception.InvalidGameException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This class serves as an exception handler for specific exceptions related to game operations.
 * It handles the InvalidGameException by returning an ErrorResponse with a bad request status.
 */
@ControllerAdvice
@Order(0)
public class GameExceptionHandler {

    /**
     * Handles InvalidGameException by returning an ErrorResponse.
     * This response includes the error message from the exception.
     */
    @ExceptionHandler(InvalidGameException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @Order(0)
    public ErrorResponse gameNotFoundHandler(InvalidGameException ex) {
        return new ErrorResponse(
                ex.getMessage());
    }

}

