package com.amir.denomination.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {CustomDigitsValidator.class})
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomDigits {
    String message() default "numeric value out of bounds"; //message to be returned on validation failure

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
