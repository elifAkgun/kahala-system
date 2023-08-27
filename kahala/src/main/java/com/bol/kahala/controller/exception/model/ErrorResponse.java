package com.bol.kahala.controller.exception.model;

/**
 * This class represents an error response that can be returned when handling exceptions.
 * It contains an error message field to provide information about the error.
 */
public record ErrorResponse(String errorMessage) {
    /**
     * Constructs an ErrorResponse with the provided error message.
     *
     * @param errorMessage The error message to be included in the ErrorResponse.
     */
    public ErrorResponse {
        // Constructor body is empty due to the usage of record syntax.
        // The error message is provided as a constructor parameter.
    }

    /**
     * Returns the error message contained in this ErrorResponse.
     *
     * @return The error message.
     */
    public String getErrorMessage() {
        return errorMessage;
    }
}

