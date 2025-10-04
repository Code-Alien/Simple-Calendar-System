package com.event_calendar.service;

import com.event_calendar.dto.EventDTO;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.List;

public interface EventService {

  @Transactional
  EventDTO createEvent(EventDTO event, ZoneId zoneId);

  @Transactional
  List<EventDTO> getAllEvents(ZoneId zoneId);

  @Transactional
  EventDTO getEventById(Long id, ZoneId zoneId);

  @Transactional
  EventDTO updateEvent(Long id, EventDTO eventDetails, ZoneId zoneId);

  @Transactional
  void deleteEvent(Long id);
}
