import {useNavigate, useParams} from 'react-router-dom';
import {useEffect, useState} from 'react';
import {eventsApi} from '../../api/events';
import type {Event, EventRequest} from '../../types/event';
import {EventForm} from '../../components/EventForm/EventForm';
import {ArrowLeft, Edit2, Trash2, Clock, MapPin, Calendar, FileText, X} from 'lucide-react';
import styles from './EventDetailsPage.module.css';

export function EventDetailsPage() {
  const {id} = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [event, setEvent] = useState<Event | null>(null);
  const [loading, setLoading] = useState(true);
  const [editing, setEditing] = useState(false);
  const [deleting, setDeleting] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);

  const fetchEvent = async () => {
    if (!id) return;

    try {
      setLoading(true);
      setError(null);
      const eventData = await eventsApi.getEventById(parseInt(id));
      setEvent(eventData);
    } catch (err: any) {
      setError(err.message || 'Failed to fetch event details');
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = () => {
    setEditing(true);
  };

  const handleCancelEdit = () => {
    setEditing(false);
    fetchEvent();
  };

  const handleSave = async (data: EventRequest) => {
    if (!id) return;

    try {
      setLoading(true);
      setError(null);
      const updatedEvent = await eventsApi.updateEvent(parseInt(id), data);
      setEvent(updatedEvent);
      setEditing(false);
    } catch (err: any) {
      setError(err.message || 'Failed to update event');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async () => {
    if (!id) return;

    try {
      setDeleting(true);
      setError(null);
      await eventsApi.deleteEvent(parseInt(id));
      navigate('/calendar');
    } catch (err: any) {
      setError(err.message || 'Failed to delete event');
    } finally {
      setDeleting(false);
      setDeleteDialogOpen(false);
    }
  };

  const handleBack = () => {
    navigate('/calendar');
  };

  useEffect(() => {
    fetchEvent();
  }, [id]);

  if (loading && !event) {
    return (
      <div className={styles.loading}>
        <div className={styles.loadingSpinner}></div>
        <p>Loading event details...</p>
      </div>
    );
  }

  if (error && !event) {
    return (
      <div className={styles.errorPage}>
        <div className={styles.error}>
          {error}
        </div>
        <button onClick={handleBack} className={styles.backButton}>
          <ArrowLeft size={16} />
          Back to Calendar
        </button>
      </div>
    );
  }

  if (!event) {
    return (
      <div className={styles.notFound}>
        <h2>Event not found</h2>
        <button onClick={handleBack} className={styles.backButton}>
          <ArrowLeft size={16} />
          Back to Calendar
        </button>
      </div>
    );
  }

  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <div className={styles.headerContent}>
          <button 
            onClick={handleBack} 
            className={styles.backButton}
            disabled={deleteDialogOpen}
          >
            <ArrowLeft size={20} />
            Back
          </button>
          <div className={styles.titleSection}>
            <Calendar size={32} className={styles.titleIcon} />
            <h1 className={styles.title}>Event Details</h1>
          </div>
          {!editing && (
            <div className={styles.actionButtons}>
              <button 
                onClick={handleEdit} 
                className={styles.editButton}
                disabled={deleteDialogOpen}
              >
                <Edit2 size={16} />
                Edit
              </button>
              <button 
                onClick={() => setDeleteDialogOpen(true)} 
                className={styles.deleteButton}
                disabled={deleteDialogOpen}
              >
                <Trash2 size={16} />
                Delete
              </button>
            </div>
          )}
        </div>
      </div>

      <div className={styles.content}>
        {error && (
          <div className={styles.error}>
            {error}
          </div>
        )}

        {editing ? (
          <EventForm
            initialData={{
              title: event.title,
              description: event.description || '',
              startDateTime: event.startDateTime,
              endDateTime: event.endDateTime,
              location: event.location || ''
            }}
            onSubmit={handleSave}
            onCancel={handleCancelEdit}
            loading={loading}
            isEditing={true}
          />
        ) : (
          <div className={styles.eventCard}>
            <div className={styles.eventHeader}>
              <h2 className={styles.eventTitle}>{event.title}</h2>
            </div>

            <div className={styles.eventContent}>
              {event.description && (
                <div className={styles.field}>
                  <div className={styles.fieldHeader}>
                    <FileText size={20} className={styles.fieldIcon} />
                    <span className={styles.fieldLabel}>Description</span>
                  </div>
                  <p className={styles.fieldValue}>{event.description}</p>
                </div>
              )}

              <div className={styles.field}>
                <div className={styles.fieldHeader}>
                  <Clock size={20} className={styles.fieldIcon} />
                  <span className={styles.fieldLabel}>Start Date & Time</span>
                </div>
                <p className={styles.fieldValue}>
                  {new Date(event.startDateTime).toLocaleString()}
                </p>
              </div>

              <div className={styles.field}>
                <div className={styles.fieldHeader}>
                  <Clock size={20} className={styles.fieldIcon} />
                  <span className={styles.fieldLabel}>End Date & Time</span>
                </div>
                <p className={styles.fieldValue}>
                  {new Date(event.endDateTime).toLocaleString()}
                </p>
              </div>

              {event.location && (
                <div className={styles.field}>
                  <div className={styles.fieldHeader}>
                    <MapPin size={20} className={styles.fieldIcon} />
                    <span className={styles.fieldLabel}>Location</span>
                  </div>
                  <p className={styles.fieldValue}>{event.location}</p>
                </div>
              )}
            </div>
          </div>
        )}

        {/* Delete Confirmation Dialog */}
        {deleteDialogOpen && (
          <div className={styles.dialogOverlay}>
            <div className={styles.dialog}>
              <div className={styles.dialogHeader}>
                <h3>Delete Event</h3>
                <button 
                  onClick={() => setDeleteDialogOpen(false)} 
                  className={styles.closeButton}
                >
                  <X size={20} />
                </button>
              </div>
              <div className={styles.dialogContent}>
                <p>Are you sure you want to delete "{event.title}"? This action cannot be undone.</p>
              </div>
              <div className={styles.dialogActions}>
                <button 
                  onClick={() => setDeleteDialogOpen(false)} 
                  className={styles.cancelButton}
                >
                  Cancel
                </button>
                <button
                  onClick={handleDelete}
                  disabled={deleting}
                  className={styles.confirmDeleteButton}
                >
                  {deleting ? 'Deleting...' : 'Delete'}
                </button>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
