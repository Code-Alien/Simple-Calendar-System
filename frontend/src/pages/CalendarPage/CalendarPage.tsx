import {Calendar} from '../../components/Calendar/Calendar';
import {Link} from 'react-router-dom';
import {Button} from '@mui/material';
import {Add as AddIcon} from '@mui/icons-material';
import styles from './CalendarPage.module.css';

export function CalendarPage() {
  return (
    <div>
      <div className={styles.header}>
        <h1>Event Calendar</h1>
        <Button
          variant="contained"
          startIcon={<AddIcon/>}
          component={Link}
          to="/add-event"
        >
          Add Event
        </Button>
      </div>
      <Calendar/>
    </div>
  );
}
