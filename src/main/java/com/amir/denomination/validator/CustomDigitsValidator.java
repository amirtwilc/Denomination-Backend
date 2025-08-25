package com.amir.denomination.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;

public class CustomDigitsValidator implements ConstraintValidator<CustomDigits, BigDecimal> {

    @Value("${app.max-decimal-digits}")
    protected int maxDecimalDigits;

    @Override
    public void initialize(CustomDigits constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(BigDecimal number, ConstraintValidatorContext constraintValidatorContext) {
        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate(
                "Value cannot have more than " + maxDecimalDigits + " fraction digits")
                .addConstraintViolation();
        return Math.max(0, number.stripTrailingZeros().scale()) <= maxDecimalDigits;
    }
}
