package com.event_calendar.validator;

import com.event_calendar.dto.EventDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EventDateTimeValidator implements ConstraintValidator<ValidEventDateTime, EventDTO> {

  @Override
  public void initialize(ValidEventDateTime constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(EventDTO event, ConstraintValidatorContext context) {
    if (event == null) {
      return false;
    }

    if (event.getStartDateTime() == null || event.getEndDateTime() == null) {
      return false;
    }

    return event.getEndDateTime()
            .isAfter(event.getStartDateTime());
  }
}
