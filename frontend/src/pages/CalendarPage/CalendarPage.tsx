import {Calendar} from '../../components/Calendar/Calendar';
import {Link} from 'react-router-dom';
import {Plus} from 'lucide-react';
import styles from './CalendarPage.module.css';

export function CalendarPage() {
  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <div className={styles.headerContent}>
          <h1 className={styles.title}>Event Calendar</h1>
          <Link to="/add-event" className={styles.addButton}>
            <Plus size={20} />
            Add Event
          </Link>
        </div>
      </div>
      <div className={styles.calendarWrapper}>
        <Calendar/>
      </div>
    </div>
  );
}
