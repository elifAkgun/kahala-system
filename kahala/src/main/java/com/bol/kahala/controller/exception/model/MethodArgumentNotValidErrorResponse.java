package com.bol.kahala.controller.exception.model;

/**
 * This class represents an error response for method argument validation errors.
 * It contains information about the object name, field name, rejected value, and error message.
 */
public record MethodArgumentNotValidErrorResponse(
        String objectName,
        String fieldName,
        String rejectedValue,
        String errorMessage
) {
    /**
     * Constructs a MethodArgumentNotValidErrorResponse with the provided information.
     *
     * @param objectName    The name of the object associated with the validation error.
     * @param fieldName     The name of the field associated with the validation error.
     * @param rejectedValue The rejected value that caused the validation error.
     * @param errorMessage  The error message associated with the validation error.
     */
    public MethodArgumentNotValidErrorResponse {
        // Constructor body is empty due to the usage of record syntax.
        // The fields are provided as constructor parameters.
    }

    /**
     * Returns the name of the object associated with the validation error.
     *
     * @return The object name.
     */
    public String getObjectName() {
        return objectName;
    }

    /**
     * Returns the name of the field associated with the validation error.
     *
     * @return The field name.
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Returns the rejected value that caused the validation error.
     *
     * @return The rejected value.
     */
    public String getRejectedValue() {
        return rejectedValue;
    }

    /**
     * Returns the error message associated with the validation error.
     *
     * @return The error message.
     */
    public String getErrorMessage() {
        return errorMessage;
    }
}

