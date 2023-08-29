package com.bol.kahala.controller.exception.handler;

import com.bol.kahala.controller.exception.model.ErrorResponse;
import com.bol.kahala.controller.exception.model.MethodArgumentNotValidErrorResponse;
import lombok.extern.slf4j.Slf4j;
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

/**
 * This class serves as a global exception handler for common exceptions that can occur
 * across the application. It provides handling for validation errors, invalid format errors,
 * and runtime exceptions.
 */
@ControllerAdvice
@Order
@Slf4j
public class CommonExceptionHandler {

    // Constants for error messages
    public static final String AN_ERROR_HAPPENED_PLEASE_TRY_AGAIN_LATER = "An error happened, please try again later.";
    public static final String UNDEFINED = "undefined";
    public static final String INVALID_JSON_BODY = "Invalid JSON body.";

    /**
     * Utility method to extract error details from an ObjectError and create a
     * MethodArgumentNotValidErrorResponse instance.
     */
    private static MethodArgumentNotValidErrorResponse getErrorDto(ObjectError error) {
        String field = error instanceof FieldError fieldError ? fieldError.getField() : null;
        String rejectedValue = error instanceof FieldError fieldError ? String.valueOf(fieldError.getRejectedValue()) : null;

        return new MethodArgumentNotValidErrorResponse(
                error.getObjectName(),
                field,
                rejectedValue,
                error.getDefaultMessage());
    }

    /**
     * Handles MethodArgumentNotValidException by returning a MethodArgumentNotValidErrorResponse.
     * This response includes details about the validation errors.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public MethodArgumentNotValidErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        Optional<MethodArgumentNotValidErrorResponse> firstResponse = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(CommonExceptionHandler::getErrorDto)
                .findFirst();

        return firstResponse.orElseGet(() -> new MethodArgumentNotValidErrorResponse(UNDEFINED,
                UNDEFINED, UNDEFINED, AN_ERROR_HAPPENED_PLEASE_TRY_AGAIN_LATER));
    }

    /**
     * Handles HttpMessageNotReadableException by returning an ErrorResponse.
     * This response includes a general error message for invalid format errors.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ErrorResponse handleInvalidFormatExceptions(HttpMessageNotReadableException ex) {
        log.error("An error occurred:", ex);
        return new ErrorResponse(INVALID_JSON_BODY);
    }

    /**
     * Handles RuntimeException by returning an ErrorResponse.
     * This response includes a general error message for runtime exceptions.
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ErrorResponse handleValidationExceptions(RuntimeException ex) {
        log.error("An error occurred:", ex);
        return new ErrorResponse(AN_ERROR_HAPPENED_PLEASE_TRY_AGAIN_LATER);
    }
}

