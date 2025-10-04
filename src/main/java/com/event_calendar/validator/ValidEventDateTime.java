package com.event_calendar.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EventDateTimeValidator.class)
@Documented
public @interface ValidEventDateTime {

  String message() default "End date time must be after start date time";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}