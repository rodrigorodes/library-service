package com.library.book.commons.validators;

import java.time.Year;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class YearLessThanOrEqualToCurrentValidator implements ConstraintValidator<YearLessThanOrEqualToCurrent, Integer> {

    @Override
    public void initialize(YearLessThanOrEqualToCurrent constraintAnnotation) {
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; 
        }

        int currentYear = Year.now().getValue();
        return value <= currentYear;
    }
}
