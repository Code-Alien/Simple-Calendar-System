package com.event_calendar.service.impl;

import com.event_calendar.dto.EventDTO;
import com.event_calendar.entity.EventEntity;
import com.event_calendar.exception.EventNotFoundException;
import com.event_calendar.mapper.EventMapper;
import com.event_calendar.repository.EventRepository;
import com.event_calendar.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

  private final EventRepository eventRepository;
  private final EventMapper eventMapper;

  @Autowired
  public EventServiceImpl(EventRepository eventRepository, EventMapper eventMapper) {
    this.eventRepository = eventRepository;
    this.eventMapper = eventMapper;
  }

  @Transactional
  @Override
  public EventDTO createEvent(EventDTO event, ZoneId zoneId) {
    EventEntity save = eventRepository.save(eventMapper.toEntity(event, zoneId));
    return eventMapper.toResponseDTO(save, zoneId);
  }

  @Transactional
  @Override
  public List<EventDTO> getAllEvents(ZoneId zoneId) {
    return eventRepository.findAll()
            .stream()
            .map(event -> eventMapper.toResponseDTO(event, zoneId))
            .toList();
  }

  @Transactional
  @Override
  public EventDTO getEventById(Long id, ZoneId zoneId) {
    EventEntity eventEntity = eventRepository.findById(id)
            .orElseThrow(() -> new EventNotFoundException(id));
    return eventMapper.toResponseDTO(eventEntity, zoneId);
  }

  @Transactional
  @Override
  public EventDTO updateEvent(Long id, EventDTO eventDetails, ZoneId zoneId) {
    EventEntity existingEvent = eventRepository.findById(id)
            .orElseThrow(() -> new EventNotFoundException(id));
    eventMapper.updateEntityFromDto(eventDetails, zoneId, existingEvent);
    EventEntity updatedEvent = eventRepository.save(existingEvent);
    return eventMapper.toResponseDTO(updatedEvent, zoneId);
  }

  @Transactional
  @Override
  public void deleteEvent(Long id) {
    EventEntity eventEntity = this.eventRepository.findById(id)
            .orElseThrow(() -> new EventNotFoundException(id));
    eventRepository.delete(eventEntity);
  }
}
