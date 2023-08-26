package com.bol.kahala.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class IntegerValueValidator implements ConstraintValidator<IntegerValue, Integer> {
    @Override
    public void initialize(IntegerValue constraintAnnotation) {
        //do nothing because there is no initialize value
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value instanceof Integer;
    }
}
