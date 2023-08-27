package com.bol.kahala.validation.constraint;

import com.bol.kahala.validation.IntegerValueValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * This annotation is used to validate if an integer value meets certain criteria.
 * It is typically applied to fields or parameters that need to be validated for specific integer constraints.
 */
@Documented
@Constraint(validatedBy = IntegerValueValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface IntegerValue {

    /**
     * Returns the default error message when the validation fails.
     *
     * @return The default error message.
     */
    String message() default "Invalid integer value";

    /**
     * Specifies the validation groups to which this constraint belongs.
     * By default, no group is specified, meaning this constraint belongs to the default group.
     *
     * @return The validation groups to which this constraint belongs.
     */
    Class<?>[] groups() default {};

    /**
     * Specifies custom payloads for the validation.
     * These payloads can be used to indicate different error levels or other custom handling.
     *
     * @return The custom payloads for the validation.
     */
    Class<? extends Payload>[] payload() default {};
}




