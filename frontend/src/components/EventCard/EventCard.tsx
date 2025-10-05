import styles from './EventCard.module.css';
import type {EventInput} from "@fullcalendar/core";


export function EventCard({event}: EventInput) {
  const startTime = event.start ? new Date(event.start).toLocaleTimeString([], {
    hour: '2-digit',
    minute: '2-digit'
  }) : '';

  return (
    <div className={styles.eventCard}>
      <div className={styles.time}>
        {startTime}
      </div>
      <div className={styles.title}>
        {event.title}
      </div>
      {event.extendedProps?.description && (
        <div className={styles.description}>
          {event.extendedProps.description}
        </div>
      )}
      {event.extendedProps?.location && (
        <div className={styles.location}>
          📍 {event.extendedProps.location}
        </div>
      )}
    </div>
  );
}
