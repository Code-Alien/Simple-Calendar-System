package com.event_calendar.mapper;

import com.event_calendar.dto.EventDTO;
import com.event_calendar.entity.EventEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class EventMapperTest {

  private EventMapper eventMapper;
  private EventDTO eventDTO;
  private EventEntity eventEntity;
  private LocalDateTime startDateTime;
  private LocalDateTime endDateTime;
  private Instant startInstant;
  private Instant endInstant;
  private ZoneId utcZone;

  @BeforeEach
  void setUp() {
    eventMapper = new EventMapper();
    utcZone = ZoneId.of("UTC");

    startDateTime = LocalDateTime.now();
    endDateTime = startDateTime.plusHours(2);

    startInstant = startDateTime.atZone(utcZone)
            .toInstant();
    endInstant = endDateTime.atZone(utcZone)
            .toInstant();

    eventDTO = new EventDTO();
    eventDTO.setId(1L);
    eventDTO.setTitle("Test Event");
    eventDTO.setDescription("Test Description");
    eventDTO.setStartDateTime(startDateTime);
    eventDTO.setEndDateTime(endDateTime);
    eventDTO.setLocation("Test Location");

    eventEntity = new EventEntity();
    eventEntity.setId(1L);
    eventEntity.setTitle("Test Event");
    eventEntity.setDescription("Test Description");
    eventEntity.setStartDateTime(startInstant);
    eventEntity.setEndDateTime(endInstant);
    eventEntity.setLocation("Test Location");
    eventEntity.setCreatedAt(Instant.now());
    eventEntity.setUpdatedAt(Instant.now());
  }

  @Test
  void toEntity_WithValidDTO_ShouldMapCorrectly() {
    EventEntity result = eventMapper.toEntity(eventDTO, utcZone);

    assertNotNull(result);
    assertEquals(eventDTO.getTitle(), result.getTitle());
    assertEquals(eventDTO.getDescription(), result.getDescription());
    assertEquals(eventDTO.getLocation(), result.getLocation());
    assertNotNull(result.getStartDateTime());
    assertNotNull(result.getEndDateTime());
  }

  @Test
  void toEntity_WithNullDTO_ShouldReturnNull() {
    EventEntity result = eventMapper.toEntity(null, utcZone);

    assertNull(result);
  }

  @Test
  void toEntity_WithAllFields_ShouldMapAllFields() {
    EventDTO dto = new EventDTO();
    dto.setTitle("Conference");
    dto.setDescription("Annual Tech Conference");
    dto.setStartDateTime(LocalDateTime.of(2024, 12, 15, 9, 0));
    dto.setEndDateTime(LocalDateTime.of(2024, 12, 15, 17, 0));
    dto.setLocation("Convention Center");

    EventEntity result = eventMapper.toEntity(dto, utcZone);

    assertNotNull(result);
    assertEquals("Conference", result.getTitle());
    assertEquals("Annual Tech Conference", result.getDescription());
    assertEquals("Convention Center", result.getLocation());
    assertNotNull(result.getStartDateTime());
    assertNotNull(result.getEndDateTime());
  }

  @Test
  void toEntity_WithNullableFields_ShouldHandleNulls() {
    EventDTO dto = new EventDTO();
    dto.setTitle("Test Event");
    dto.setDescription(null);
    dto.setStartDateTime(startDateTime);
    dto.setEndDateTime(endDateTime);
    dto.setLocation(null);

    EventEntity result = eventMapper.toEntity(dto, utcZone);

    assertNotNull(result);
    assertEquals("Test Event", result.getTitle());
    assertNull(result.getDescription());
    assertNull(result.getLocation());
    assertNotNull(result.getStartDateTime());
    assertNotNull(result.getEndDateTime());
  }

  @Test
  void toEntity_WithDifferentTimezone_ShouldConvertCorrectly() {
    ZoneId tokyo = ZoneId.of("Asia/Tokyo");

    EventEntity result = eventMapper.toEntity(eventDTO, tokyo);

    assertNotNull(result);
    assertEquals(eventDTO.getTitle(), result.getTitle());
    assertNotNull(result.getStartDateTime());
    assertNotNull(result.getEndDateTime());
  }

  @Test
  void toResponseDTO_WithValidEntity_ShouldMapCorrectly() {
    ZoneId zoneId = ZoneId.of("UTC");

    EventDTO result = eventMapper.toResponseDTO(eventEntity, zoneId);

    assertNotNull(result);
    assertEquals(eventEntity.getId(), result.getId());
    assertEquals(eventEntity.getTitle(), result.getTitle());
    assertEquals(eventEntity.getDescription(), result.getDescription());
    assertEquals(eventEntity.getLocation(), result.getLocation());
    assertNotNull(result.getStartDateTime());
    assertNotNull(result.getEndDateTime());
  }

  @Test
  void toResponseDTO_WithNullEntity_ShouldReturnNull() {
    EventDTO result = eventMapper.toResponseDTO(null, ZoneId.of("UTC"));

    assertNull(result);
  }

  @Test
  void toResponseDTO_WithUTCTimezone_ShouldConvertCorrectly() {
    ZoneId utc = ZoneId.of("UTC");
    Instant now = Instant.now();
    eventEntity.setStartDateTime(now);
    eventEntity.setEndDateTime(now.plus(2, ChronoUnit.HOURS));

    EventDTO result = eventMapper.toResponseDTO(eventEntity, utc);

    assertNotNull(result);
    assertEquals(now.atZone(utc)
            .toLocalDateTime(), result.getStartDateTime());
    assertEquals(now.plus(2, ChronoUnit.HOURS)
            .atZone(utc)
            .toLocalDateTime(), result.getEndDateTime());
  }

  @Test
  void toResponseDTO_WithDifferentTimezone_ShouldConvertToTargetTimezone() {
    ZoneId tokyo = ZoneId.of("Asia/Tokyo");
    Instant now = Instant.now();
    eventEntity.setStartDateTime(now);
    eventEntity.setEndDateTime(now.plus(2, ChronoUnit.HOURS));

    EventDTO result = eventMapper.toResponseDTO(eventEntity, tokyo);

    assertNotNull(result);
    assertEquals(now.atZone(tokyo)
            .toLocalDateTime(), result.getStartDateTime());
    assertEquals(now.plus(2, ChronoUnit.HOURS)
            .atZone(tokyo)
            .toLocalDateTime(), result.getEndDateTime());
  }

  @Test
  void toResponseDTO_WithNewYorkTimezone_ShouldConvertCorrectly() {
    ZoneId newYork = ZoneId.of("America/New_York");
    Instant now = Instant.parse("2024-12-15T15:00:00Z");
    eventEntity.setStartDateTime(now);
    eventEntity.setEndDateTime(now.plus(3, ChronoUnit.HOURS));

    EventDTO result = eventMapper.toResponseDTO(eventEntity, newYork);

    assertNotNull(result);
    assertNotNull(result.getStartDateTime());
    assertNotNull(result.getEndDateTime());
    assertEquals(now.atZone(newYork)
            .toLocalDateTime(), result.getStartDateTime());
  }

  @Test
  void toResponseDTO_WithAllFields_ShouldMapAllFields() {
    ZoneId zoneId = ZoneId.of("UTC");
    Instant createdAt = Instant.now()
            .minus(7, ChronoUnit.DAYS);
    Instant updatedAt = Instant.now();

    eventEntity.setId(99L);
    eventEntity.setTitle("Annual Meeting");
    eventEntity.setDescription("Company annual meeting");
    eventEntity.setLocation("Headquarters");
    eventEntity.setCreatedAt(createdAt);
    eventEntity.setUpdatedAt(updatedAt);

    EventDTO result = eventMapper.toResponseDTO(eventEntity, zoneId);

    assertNotNull(result);
    assertEquals(99L, result.getId());
    assertEquals("Annual Meeting", result.getTitle());
    assertEquals("Company annual meeting", result.getDescription());
    assertEquals("Headquarters", result.getLocation());
  }

  @Test
  void toResponseDTO_WithNullableFields_ShouldHandleNulls() {
    ZoneId zoneId = ZoneId.of("UTC");
    eventEntity.setDescription(null);
    eventEntity.setLocation(null);

    EventDTO result = eventMapper.toResponseDTO(eventEntity, zoneId);

    assertNotNull(result);
    assertNull(result.getDescription());
    assertNull(result.getLocation());
    assertNotNull(result.getTitle());
    assertNotNull(result.getId());
  }

  @Test
  void roundTripConversion_ShouldPreserveData() {
    ZoneId utc = ZoneId.of("UTC");

    EventEntity entityFromDTO = eventMapper.toEntity(eventDTO, utc);
    entityFromDTO.setId(1L);
    entityFromDTO.setCreatedAt(Instant.now());
    entityFromDTO.setUpdatedAt(Instant.now());

    EventDTO dtoFromEntity = eventMapper.toResponseDTO(entityFromDTO, utc);

    assertNotNull(dtoFromEntity);
    assertEquals(eventDTO.getTitle(), dtoFromEntity.getTitle());
    assertEquals(eventDTO.getDescription(), dtoFromEntity.getDescription());
    assertEquals(eventDTO.getLocation(), dtoFromEntity.getLocation());
  }

  @Test
  void toEntity_WithPreciseDateTime_ShouldConvertAccurately() {
    LocalDateTime preciseDateTime = LocalDateTime.of(2024, 12, 25, 14, 30, 45, 123456789);
    eventDTO.setStartDateTime(preciseDateTime);
    eventDTO.setEndDateTime(preciseDateTime.plusHours(2));

    EventEntity result = eventMapper.toEntity(eventDTO, utcZone);

    assertNotNull(result);
    assertNotNull(result.getStartDateTime());
    assertNotNull(result.getEndDateTime());
  }

  @Test
  void toResponseDTO_WithMultipleTimezones_ShouldProduceDifferentLocalTimes() {
    Instant fixedInstant = Instant.parse("2024-12-15T12:00:00Z");
    eventEntity.setStartDateTime(fixedInstant);
    eventEntity.setEndDateTime(fixedInstant.plus(1, ChronoUnit.HOURS));

    EventDTO utcResult = eventMapper.toResponseDTO(eventEntity, ZoneId.of("UTC"));
    EventDTO tokyoResult = eventMapper.toResponseDTO(eventEntity, ZoneId.of("Asia/Tokyo"));
    EventDTO newYorkResult = eventMapper.toResponseDTO(eventEntity, ZoneId.of("America/New_York"));

    assertNotEquals(utcResult.getStartDateTime(), tokyoResult.getStartDateTime());
    assertNotEquals(utcResult.getStartDateTime(), newYorkResult.getStartDateTime());
    assertNotEquals(tokyoResult.getStartDateTime(), newYorkResult.getStartDateTime());
  }

  @Test
  void toEntity_DoesNotSetIdCreatedAtOrUpdatedAt() {
    EventEntity result = eventMapper.toEntity(eventDTO, utcZone);

    assertNotNull(result);
    assertNull(result.getId());
    assertNull(result.getCreatedAt());
    assertNull(result.getUpdatedAt());
  }

  @Test
  void toResponseDTO_PreservesEntityId() {
    ZoneId zoneId = ZoneId.of("UTC");
    eventEntity.setId(42L);

    EventDTO result = eventMapper.toResponseDTO(eventEntity, zoneId);

    assertNotNull(result);
    assertEquals(42L, result.getId());
  }
}