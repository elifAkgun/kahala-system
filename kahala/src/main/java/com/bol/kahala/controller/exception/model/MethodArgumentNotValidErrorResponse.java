package com.bol.kahala.controller.exception.model;

public record MethodArgumentNotValidErrorResponse(
        String objectName,
        String fieldName,
        String rejectedValue,
        String errorMessage
) {
}
