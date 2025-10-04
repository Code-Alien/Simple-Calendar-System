package com.event_calendar.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateUtils {

  private DateUtils() {
  }

  public static Instant toInstant(LocalDateTime dateTime, ZoneId zoneId) {
    return dateTime.atZone(zoneId)
            .toInstant();
  }

}
