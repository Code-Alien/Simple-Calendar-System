package com.event_calendar.dto;

import com.event_calendar.validator.ValidEventDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ValidEventDateTime
public class EventDTO {

  private Long id;
  @NotBlank(message = "Title is required")
  @Size(max = 255, message = "Title must be less than 255 characters")
  private String title;

  @Size(max = 5000, message = "Description must be less than 5000 characters")
  private String description;

  @NotNull(message = "Start date time is required")
  private LocalDateTime startDateTime;

  @NotNull(message = "End date time is required")
  private LocalDateTime endDateTime;

  @Size(max = 255, message = "Location must be less than 255 characters")
  private String location;
}
