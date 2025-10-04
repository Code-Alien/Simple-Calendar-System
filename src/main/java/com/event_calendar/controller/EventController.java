package com.event_calendar.controller;

import com.event_calendar.dto.EventDTO;
import com.event_calendar.mapper.EventMapper;
import com.event_calendar.service.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

  private final EventService eventService;

  @Autowired
  public EventController(EventService eventService, EventMapper eventMapper) {
    this.eventService = eventService;
  }

  @PostMapping
  public ResponseEntity<EventDTO> createEvent(@Valid @RequestBody EventDTO eventDTO, @RequestHeader(value = "X-Timezone") String timezoneHeader) {
    ZoneId zoneId = ZoneId.of(timezoneHeader);
    EventDTO createdEvent = eventService.createEvent(eventDTO, zoneId);
    return ResponseEntity.ok()
            .body(createdEvent);
  }

  @GetMapping
  public ResponseEntity<List<EventDTO>> getAllEvents(@RequestHeader(value = "X-Timezone") String timezoneHeader) {
    ZoneId zoneId = ZoneId.of(timezoneHeader);
    List<EventDTO> events = eventService.getAllEvents(zoneId);
    return ResponseEntity.ok(events);
  }

  @GetMapping("/{id}")
  public ResponseEntity<EventDTO> getEventById(@PathVariable Long id, @RequestHeader(value = "X-Timezone") String timezoneHeader) {
    ZoneId zoneId = ZoneId.of(timezoneHeader);
    EventDTO event = eventService.getEventById(id, zoneId);
    return ResponseEntity.ok(event);
  }

  @PutMapping("/{id}")
  public ResponseEntity<EventDTO> updateEvent(@PathVariable Long id, @Valid @RequestBody EventDTO eventDTO,
          @RequestHeader(value = "X-Timezone") String timezoneHeader) {
    ZoneId zoneId = ZoneId.of(timezoneHeader);
    EventDTO updatedEvent = eventService.updateEvent(id, eventDTO, zoneId);
    return ResponseEntity.ok(updatedEvent);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
    eventService.deleteEvent(id);
    return ResponseEntity.noContent()
            .build();
  }
}
