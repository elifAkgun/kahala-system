package com.bol.kahala.controller.exception.handler;

import com.bol.kahala.controller.exception.model.ErrorResponse;
import com.bol.kahala.controller.exception.model.MethodArgumentNotValidErrorResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Optional;

@ControllerAdvice
@Order
public class CommonExceptionHandler {

    public static final String AN_ERROR_HAPPENED_PLEASE_TRY_AGAIN_LATER = "An error happened, please try again later.";
    public static final String UNDEFINED = "undefined";

    private static MethodArgumentNotValidErrorResponse getErrorDto(ObjectError error) {
        String field = error instanceof FieldError fieldError ? fieldError.getField() : null;
        String rejectedValue = error instanceof FieldError fieldError ? String.valueOf(fieldError.getRejectedValue()) : null;

        return new MethodArgumentNotValidErrorResponse(
                error.getObjectName(),
                field,
                rejectedValue,
                error.getDefaultMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public MethodArgumentNotValidErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        Optional<MethodArgumentNotValidErrorResponse> firstResponse = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(CommonExceptionHandler::getErrorDto)
                .findFirst();
        if (firstResponse.isPresent()) {
            return firstResponse.get();
        }
        return new MethodArgumentNotValidErrorResponse(UNDEFINED,
                UNDEFINED, UNDEFINED, AN_ERROR_HAPPENED_PLEASE_TRY_AGAIN_LATER);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ErrorResponse handleInvalidFormatExceptions(HttpMessageNotReadableException ex) {
        ex.printStackTrace();
        return new ErrorResponse(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ErrorResponse handleValidationExceptions(RuntimeException ex) {
        ex.printStackTrace();
        return new ErrorResponse(AN_ERROR_HAPPENED_PLEASE_TRY_AGAIN_LATER);
    }
}
