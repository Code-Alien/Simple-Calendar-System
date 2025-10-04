package com.event_calendar.service.impl;

import com.event_calendar.dto.EventDTO;
import com.event_calendar.entity.EventEntity;
import com.event_calendar.exception.EventNotFoundException;
import com.event_calendar.mapper.EventMapper;
import com.event_calendar.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

  @Mock
  private EventRepository eventRepository;

  @Mock
  private EventMapper eventMapper;

  @InjectMocks
  private EventServiceImpl eventService;

  private EventDTO eventDTO;
  private EventEntity eventEntity;
  private EventEntity savedEventEntity;
  private ZoneId zoneId;
  private LocalDateTime localNow;
  private Instant now;

  @BeforeEach
  void setUp() {
    localNow = LocalDateTime.now();
    now = Instant.now();
    zoneId = ZoneId.of("UTC");

    eventDTO = new EventDTO();
    eventDTO.setTitle("Test Event");
    eventDTO.setDescription("Test Description");
    eventDTO.setStartDateTime(localNow);
    eventDTO.setEndDateTime(localNow.plusHours(2));
    eventDTO.setLocation("Test Location");

    eventEntity = new EventEntity();
    eventEntity.setTitle("Test Event");
    eventEntity.setDescription("Test Description");
    eventEntity.setStartDateTime(now);
    eventEntity.setEndDateTime(now.plus(2, ChronoUnit.HOURS));
    eventEntity.setLocation("Test Location");

    savedEventEntity = new EventEntity();
    savedEventEntity.setId(1L);
    savedEventEntity.setTitle("Test Event");
    savedEventEntity.setDescription("Test Description");
    savedEventEntity.setStartDateTime(now);
    savedEventEntity.setEndDateTime(now.plus(2, ChronoUnit.HOURS));
    savedEventEntity.setLocation("Test Location");
    savedEventEntity.setCreatedAt(now);
    savedEventEntity.setUpdatedAt(now);
  }

  @Test
  void createEvent_WithValidDTO_ReturnsCreatedEvent() {
    EventDTO responseDTO = new EventDTO();
    responseDTO.setId(1L);
    responseDTO.setTitle("Test Event");
    responseDTO.setDescription("Test Description");
    responseDTO.setStartDateTime(localNow);
    responseDTO.setEndDateTime(localNow.plusHours(2));
    responseDTO.setLocation("Test Location");

    when(eventMapper.toEntity(any(EventDTO.class), any(ZoneId.class))).thenReturn(eventEntity);
    when(eventRepository.save(any(EventEntity.class))).thenReturn(savedEventEntity);
    when(eventMapper.toResponseDTO(any(EventEntity.class), any(ZoneId.class))).thenReturn(responseDTO);

    EventDTO result = eventService.createEvent(eventDTO, zoneId);

    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals("Test Event", result.getTitle());
    assertEquals("Test Description", result.getDescription());
    assertEquals("Test Location", result.getLocation());
    verify(eventMapper, times(1)).toEntity(eventDTO, zoneId);
    verify(eventRepository, times(1)).save(eventEntity);
    verify(eventMapper, times(1)).toResponseDTO(savedEventEntity, zoneId);
  }

  @Test
  void createEvent_WithDifferentTimezone_UsesProvidedTimezone() {
    ZoneId tokyo = ZoneId.of("Asia/Tokyo");
    EventDTO responseDTO = new EventDTO();
    responseDTO.setId(1L);
    responseDTO.setTitle("Test Event");

    when(eventMapper.toEntity(any(EventDTO.class), eq(tokyo))).thenReturn(eventEntity);
    when(eventRepository.save(any(EventEntity.class))).thenReturn(savedEventEntity);
    when(eventMapper.toResponseDTO(any(EventEntity.class), eq(tokyo))).thenReturn(responseDTO);

    EventDTO result = eventService.createEvent(eventDTO, tokyo);

    assertNotNull(result);
    verify(eventMapper, times(1)).toEntity(eventDTO, tokyo);
    verify(eventMapper, times(1)).toResponseDTO(savedEventEntity, tokyo);
  }

  @Test
  void getAllEvents_ReturnsListOfEvents() {
    List<EventEntity> entities = Arrays.asList(savedEventEntity);
    EventDTO responseDTO = new EventDTO();
    responseDTO.setId(1L);
    responseDTO.setTitle("Test Event");

    when(eventRepository.findAll()).thenReturn(entities);
    when(eventMapper.toResponseDTO(any(EventEntity.class), any(ZoneId.class))).thenReturn(responseDTO);

    List<EventDTO> result = eventService.getAllEvents(zoneId);

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(1L, result.get(0)
            .getId());
    assertEquals("Test Event", result.get(0)
            .getTitle());
    verify(eventRepository, times(1)).findAll();
    verify(eventMapper, times(1)).toResponseDTO(savedEventEntity, zoneId);
  }

  @Test
  void getAllEvents_WithEmptyList_ReturnsEmptyList() {
    when(eventRepository.findAll()).thenReturn(Collections.emptyList());

    List<EventDTO> result = eventService.getAllEvents(zoneId);

    assertNotNull(result);
    assertTrue(result.isEmpty());
    verify(eventRepository, times(1)).findAll();
    verify(eventMapper, never()).toResponseDTO(any(EventEntity.class), any(ZoneId.class));
  }

  @Test
  void getAllEvents_WithMultipleEvents_ReturnsAllEvents() {
    EventEntity event1 = new EventEntity();
    event1.setId(1L);
    event1.setTitle("Event 1");

    EventEntity event2 = new EventEntity();
    event2.setId(2L);
    event2.setTitle("Event 2");

    List<EventEntity> entities = Arrays.asList(event1, event2);

    EventDTO dto1 = new EventDTO();
    dto1.setId(1L);
    dto1.setTitle("Event 1");

    EventDTO dto2 = new EventDTO();
    dto2.setId(2L);
    dto2.setTitle("Event 2");

    when(eventRepository.findAll()).thenReturn(entities);
    when(eventMapper.toResponseDTO(event1, zoneId)).thenReturn(dto1);
    when(eventMapper.toResponseDTO(event2, zoneId)).thenReturn(dto2);

    List<EventDTO> result = eventService.getAllEvents(zoneId);

    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("Event 1", result.get(0)
            .getTitle());
    assertEquals("Event 2", result.get(1)
            .getTitle());
    verify(eventRepository, times(1)).findAll();
    verify(eventMapper, times(2)).toResponseDTO(any(EventEntity.class), eq(zoneId));
  }

  @Test
  void getEventById_WithExistingId_ReturnsEvent() {
    EventDTO responseDTO = new EventDTO();
    responseDTO.setId(1L);
    responseDTO.setTitle("Test Event");

    when(eventRepository.findById(1L)).thenReturn(Optional.of(savedEventEntity));
    when(eventMapper.toResponseDTO(any(EventEntity.class), any(ZoneId.class))).thenReturn(responseDTO);

    EventDTO result = eventService.getEventById(1L, zoneId);

    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals("Test Event", result.getTitle());
    verify(eventRepository, times(1)).findById(1L);
    verify(eventMapper, times(1)).toResponseDTO(savedEventEntity, zoneId);
  }

  @Test
  void getEventById_WithNonExistingId_ThrowsEventNotFoundException() {
    when(eventRepository.findById(999L)).thenReturn(Optional.empty());

    EventNotFoundException exception = assertThrows(EventNotFoundException.class, () -> {
      eventService.getEventById(999L, zoneId);
    });

    assertEquals("Event with id 999 not found", exception.getMessage());
    verify(eventRepository, times(1)).findById(999L);
    verify(eventMapper, never()).toResponseDTO(any(EventEntity.class), any(ZoneId.class));
  }

  @Test
  void getEventById_WithDifferentTimezone_UsesProvidedTimezone() {
    ZoneId newYork = ZoneId.of("America/New_York");
    EventDTO responseDTO = new EventDTO();
    responseDTO.setId(1L);

    when(eventRepository.findById(1L)).thenReturn(Optional.of(savedEventEntity));
    when(eventMapper.toResponseDTO(savedEventEntity, newYork)).thenReturn(responseDTO);

    EventDTO result = eventService.getEventById(1L, newYork);

    assertNotNull(result);
    verify(eventMapper, times(1)).toResponseDTO(savedEventEntity, newYork);
  }

  @Test
  void updateEvent_WithValidData_ReturnsUpdatedEvent() {
    EventDTO updateDTO = new EventDTO();
    updateDTO.setTitle("Updated Title");
    updateDTO.setDescription("Updated Description");
    updateDTO.setStartDateTime(localNow.plusDays(1));
    updateDTO.setEndDateTime(localNow.plusDays(1)
            .plusHours(3));
    updateDTO.setLocation("Updated Location");

    EventEntity updatedEntity = new EventEntity();
    updatedEntity.setId(1L);
    updatedEntity.setTitle("Updated Title");
    updatedEntity.setDescription("Updated Description");
    updatedEntity.setStartDateTime(now.plus(1, ChronoUnit.DAYS));
    updatedEntity.setEndDateTime(now.plus(1, ChronoUnit.DAYS)
            .plus(3, ChronoUnit.HOURS));
    updatedEntity.setLocation("Updated Location");

    EventDTO responseDTO = new EventDTO();
    responseDTO.setId(1L);
    responseDTO.setTitle("Updated Title");
    responseDTO.setDescription("Updated Description");
    responseDTO.setStartDateTime(localNow.plusDays(1));
    responseDTO.setEndDateTime(localNow.plusDays(1)
            .plusHours(3));
    responseDTO.setLocation("Updated Location");

    when(eventRepository.findById(1L)).thenReturn(Optional.of(savedEventEntity));
    when(eventRepository.save(any(EventEntity.class))).thenReturn(updatedEntity);
    when(eventMapper.toResponseDTO(any(EventEntity.class), any(ZoneId.class))).thenReturn(responseDTO);

    EventDTO result = eventService.updateEvent(1L, updateDTO, zoneId);

    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals("Updated Title", result.getTitle());
    assertEquals("Updated Description", result.getDescription());
    assertEquals("Updated Location", result.getLocation());
    verify(eventRepository, times(1)).findById(1L);
    verify(eventRepository, times(1)).save(any(EventEntity.class));
    verify(eventMapper, times(1)).toResponseDTO(any(EventEntity.class), eq(zoneId));
  }

  @Test
  void updateEvent_WithNonExistingId_ThrowsEventNotFoundException() {
    when(eventRepository.findById(999L)).thenReturn(Optional.empty());

    EventNotFoundException exception = assertThrows(EventNotFoundException.class, () -> {
      eventService.getEventById(999L, zoneId);
    });

    assertEquals("Event with id 999 not found", exception.getMessage());
    verify(eventRepository, times(1)).findById(999L);
    verify(eventRepository, never()).save(any(EventEntity.class));
    verify(eventMapper, never()).toResponseDTO(any(EventEntity.class), any(ZoneId.class));
  }

  @Test
  void updateEvent_UpdatesAllFields() {
    EventDTO updateDTO = new EventDTO();
    updateDTO.setTitle("New Title");
    updateDTO.setDescription("New Description");
    updateDTO.setStartDateTime(localNow.plusHours(5));
    updateDTO.setEndDateTime(localNow.plusHours(7));
    updateDTO.setLocation("New Location");

    EventDTO responseDTO = new EventDTO();
    responseDTO.setId(1L);
    responseDTO.setTitle("New Title");
    responseDTO.setDescription("New Description");
    responseDTO.setStartDateTime(localNow.plusHours(5));
    responseDTO.setEndDateTime(localNow.plusHours(7));
    responseDTO.setLocation("New Location");

    when(eventRepository.findById(1L)).thenReturn(Optional.of(savedEventEntity));
    when(eventRepository.save(any(EventEntity.class))).thenReturn(savedEventEntity);
    when(eventMapper.toResponseDTO(any(EventEntity.class), any(ZoneId.class))).thenReturn(responseDTO);

    EventDTO result = eventService.updateEvent(1L, updateDTO, zoneId);

    assertNotNull(result);
    assertEquals("New Title", result.getTitle());
    assertEquals("New Description", result.getDescription());
    assertEquals("New Location", result.getLocation());
    verify(eventRepository).save(argThat(entity -> entity.getTitle()
            .equals("New Title") && entity.getDescription()
            .equals("New Description") && entity.getLocation()
            .equals("New Location")));
  }

  @Test
  void updateEvent_WithDifferentTimezone_ConvertsDateTimesCorrectly() {
    ZoneId paris = ZoneId.of("Europe/Paris");
    EventDTO updateDTO = new EventDTO();
    updateDTO.setTitle("Paris Event");
    updateDTO.setDescription("Event in Paris");
    updateDTO.setStartDateTime(localNow);
    updateDTO.setEndDateTime(localNow.plusHours(2));
    updateDTO.setLocation("Paris");

    EventDTO responseDTO = new EventDTO();
    responseDTO.setId(1L);
    responseDTO.setTitle("Paris Event");

    when(eventRepository.findById(1L)).thenReturn(Optional.of(savedEventEntity));
    when(eventRepository.save(any(EventEntity.class))).thenReturn(savedEventEntity);
    when(eventMapper.toResponseDTO(any(EventEntity.class), eq(paris))).thenReturn(responseDTO);

    EventDTO result = eventService.updateEvent(1L, updateDTO, paris);

    assertNotNull(result);
    verify(eventMapper, times(1)).toResponseDTO(any(EventEntity.class), eq(paris));
  }

  @Test
  void deleteEvent_WithExistingId_DeletesEvent() {
    when(eventRepository.findById(1L)).thenReturn(Optional.of(savedEventEntity));
    doNothing().when(eventRepository)
            .delete(any(EventEntity.class));

    eventService.deleteEvent(1L);

    verify(eventRepository, times(1)).findById(1L);
    verify(eventRepository, times(1)).delete(savedEventEntity);
  }

  @Test
  void deleteEvent_WithNonExistingId_ThrowsEventNotFoundException() {
    when(eventRepository.findById(999L)).thenReturn(Optional.empty());

    EventNotFoundException exception = assertThrows(EventNotFoundException.class, () -> {
      eventService.getEventById(999L, zoneId);
    });

    assertEquals("Event with id 999 not found", exception.getMessage());
    verify(eventRepository, times(1)).findById(999L);
    verify(eventRepository, never()).delete(any(EventEntity.class));
  }

  @Test
  void deleteEvent_VerifiesEventExistsBeforeDeleting() {
    when(eventRepository.findById(1L)).thenReturn(Optional.of(savedEventEntity));
    doNothing().when(eventRepository)
            .delete(savedEventEntity);

    eventService.deleteEvent(1L);

    verify(eventRepository, times(1)).findById(1L);
    verify(eventRepository, times(1)).delete(savedEventEntity);
  }

  @Test
  void createEvent_MapsEntityCorrectly() {
    EventDTO responseDTO = new EventDTO();
    responseDTO.setId(1L);

    when(eventMapper.toEntity(eventDTO, zoneId)).thenReturn(eventEntity);
    when(eventRepository.save(eventEntity)).thenReturn(savedEventEntity);
    when(eventMapper.toResponseDTO(savedEventEntity, zoneId)).thenReturn(responseDTO);

    eventService.createEvent(eventDTO, zoneId);

    verify(eventMapper, times(1)).toEntity(eventDTO, zoneId);
    verify(eventRepository, times(1)).save(eventEntity);
  }

  @Test
  void getAllEvents_WithSpecificTimezone_MapsCorrectly() {
    ZoneId sydney = ZoneId.of("Australia/Sydney");
    List<EventEntity> entities = Arrays.asList(savedEventEntity);
    EventDTO responseDTO = new EventDTO();
    responseDTO.setId(1L);

    when(eventRepository.findAll()).thenReturn(entities);
    when(eventMapper.toResponseDTO(savedEventEntity, sydney)).thenReturn(responseDTO);

    List<EventDTO> result = eventService.getAllEvents(sydney);

    assertNotNull(result);
    verify(eventMapper, times(1)).toResponseDTO(savedEventEntity, sydney);
  }
}