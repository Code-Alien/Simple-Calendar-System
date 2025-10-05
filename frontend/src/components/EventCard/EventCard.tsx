import {Clock, MapPin} from 'lucide-react';
import styles from './EventCard.module.css';
import type {EventInput} from "@fullcalendar/core";

export function EventCard({event}: EventInput) {
  const startTime = event.start ? new Date(event.start).toLocaleTimeString([], {
    hour: '2-digit',
    minute: '2-digit'
  }) : '';

  return (
    <div className={styles.eventCard}>
      <div className={styles.eventHeader}>
        <div className={styles.title}>
          {event.title}
        </div>
        {startTime && (
          <div className={styles.time}>
            <Clock size={12}/>
            {startTime}
          </div>
        )}
      </div>

      {event.extendedProps?.location && (
        <div className={styles.location}>
          <MapPin size={12}/>
          <span>{event.extendedProps.location}</span>
        </div>
      )}
    </div>
  );
}
