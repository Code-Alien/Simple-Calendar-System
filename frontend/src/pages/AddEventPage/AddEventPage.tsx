import {useNavigate} from 'react-router-dom';
import {useState} from 'react';
import {eventsApi} from '../../api/events';
import type {EventRequest} from '../../types/event';
import {EventForm} from '../../components/EventForm/EventForm';
import {ArrowLeft, CalendarPlus} from 'lucide-react';
import styles from './AddEventPage.module.css';

export function AddEventPage() {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (data: EventRequest) => {
    setLoading(true);
    setError(null);

    try {
      await eventsApi.createEvent(data);
      navigate('/calendar');
    } catch (err: any) {
      setError(err.message || 'Failed to create event');
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = () => {
    navigate('/calendar');
  };

  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <div className={styles.headerContent}>
          <button onClick={handleCancel} className={styles.backButton}>
            <ArrowLeft size={20} />
            Back
          </button>
          <div className={styles.titleSection}>
            <CalendarPlus size={32} className={styles.titleIcon} />
            <h1 className={styles.title}>Add New Event</h1>
          </div>
        </div>
      </div>
      
      <div className={styles.content}>
        {error && (
          <div className={styles.error}>
            {error}
          </div>
        )}
        <EventForm
          onSubmit={handleSubmit}
          onCancel={handleCancel}
          loading={loading}
        />
      </div>
    </div>
  );
}
