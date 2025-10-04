package com.event_calendar.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "event")

@Data
public class EventEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  @NotBlank(message = "Title is required")
  private String title;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(nullable = false)
  private Instant startDateTime;

  @Column(nullable = false)
  private Instant endDateTime;

  @Column(columnDefinition = "TEXT")
  private String location;

  @CreationTimestamp
  private Instant createdAt;

  @UpdateTimestamp
  private Instant updatedAt;

  @AssertTrue(message = "Start date must be before end date")
  public boolean isStartBeforeEnd() {
    return startDateTime.isBefore(endDateTime);
  }

}
