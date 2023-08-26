package com.bol.kahala.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IntegerValueValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface IntegerValue {
    String message() default "Invalid integer value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}



