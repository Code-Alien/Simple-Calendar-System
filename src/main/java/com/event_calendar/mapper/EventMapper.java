package com.event_calendar.mapper;

import com.event_calendar.dto.EventDTO;
import com.event_calendar.entity.EventEntity;
import com.event_calendar.util.DateUtils;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
public class EventMapper {

  public EventEntity toEntity(EventDTO dto, ZoneId zoneId) {
    if (dto == null) {
      return null;
    }
    EventEntity entity = new EventEntity();
    entity.setTitle(dto.getTitle());
    entity.setDescription(dto.getDescription());
    entity.setStartDateTime(DateUtils.toInstant(dto.getStartDateTime(), zoneId));
    entity.setEndDateTime(DateUtils.toInstant(dto.getEndDateTime(), zoneId));
    entity.setLocation(dto.getLocation());
    return entity;
  }

  public EventDTO toResponseDTO(EventEntity entity, ZoneId zoneId) {
    if (entity == null) {
      return null;
    }
    EventDTO dto = new EventDTO();
    dto.setId(entity.getId());
    dto.setTitle(entity.getTitle());
    dto.setDescription(entity.getDescription());
    //todo: test dates with js
    dto.setStartDateTime(entity.getStartDateTime()
            .atZone(zoneId)
            .toLocalDateTime());
    dto.setEndDateTime(entity.getEndDateTime()
            .atZone(zoneId)
            .toLocalDateTime());
    dto.setLocation(entity.getLocation());
    return dto;
  }

  public void updateEntityFromDto(EventDTO eventDetails, ZoneId zoneId, EventEntity existingEvent) {
    existingEvent.setTitle(eventDetails.getTitle());
    existingEvent.setDescription(eventDetails.getDescription());
    existingEvent.setStartDateTime(DateUtils.toInstant(eventDetails.getStartDateTime(), zoneId));
    existingEvent.setEndDateTime(DateUtils.toInstant(eventDetails.getEndDateTime(), zoneId));
    existingEvent.setLocation(eventDetails.getLocation());
  }

}