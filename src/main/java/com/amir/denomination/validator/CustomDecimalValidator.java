package com.amir.denomination.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

public class CustomDecimalValidator implements ConstraintValidator<CustomDecimalMin, BigDecimal> {

    @Value("${app.supported-denominations}")
    protected List<BigDecimal> supportedDenominations;

    @Override
    public void initialize(CustomDecimalMin constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(BigDecimal number, ConstraintValidatorContext constraintValidatorContext) {
        BigDecimal minSupported = supportedDenominations
                .stream()
                .min(Comparator.naturalOrder())
                .orElse(BigDecimal.ZERO);
        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate("Number must be larger than " + minSupported)
                .addConstraintViolation();
        return number.compareTo(minSupported) >= 0;
    }
}
