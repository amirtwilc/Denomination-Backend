package com.amir.denomination.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {CustomDecimalValidator.class})
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomDecimalMin {
    String message() default "Number value is too low"; //message to be returned on validation failure

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
