package com.library.book.commons.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = YearLessThanOrEqualToCurrentValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface YearLessThanOrEqualToCurrent {
    String message() default "The year must be less than or equal to the current year";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

