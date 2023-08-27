package com.bol.kahala.validation;

import com.bol.kahala.validation.constraint.IntegerValue;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * A custom validator for validating if a value is an instance of Integer.
 */
public class IntegerValueValidator implements ConstraintValidator<IntegerValue, Integer> {

    /**
     * Initializes the validator.
     *
     * @param constraintAnnotation The annotation instance associated with the validator.
     */
    @Override
    public void initialize(IntegerValue constraintAnnotation) {
        // Do nothing as no specific initialization is needed.
    }

    /**
     * Validates if the given value is an instance of Integer.
     *
     * @param value   The value to be validated.
     * @param context The context in which the constraint is evaluated.
     * @return true if the value is an instance of Integer, false otherwise.
     */
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value instanceof Integer;
    }
}

