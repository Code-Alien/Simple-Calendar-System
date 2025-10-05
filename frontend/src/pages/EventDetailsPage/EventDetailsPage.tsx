import {useNavigate, useParams} from 'react-router-dom';
import {useEffect, useState} from 'react';
import {eventsApi} from '../../api/events';
import type {Event, EventRequest} from '../../types/event';
import {EventForm} from '../../components/EventForm/EventForm';
import {Alert, Box, Button, Card, CardContent, CircularProgress, Dialog, DialogActions, DialogContent, DialogTitle, Typography} from '@mui/material';
import {ArrowBack as BackIcon, Delete as DeleteIcon, Edit as EditIcon} from '@mui/icons-material';
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
      <Box className={styles.loading}>
        <CircularProgress/>
      </Box>
    );
  }

  if (error && !event) {
    return (
      <Box className={styles.errorPage}>
        <Alert severity="error" className={styles.error}>
          {error}
        </Alert>
        <Button variant="outlined" onClick={handleBack}>
          <BackIcon sx={{mr: 1}}/>
          Back to Calendar
        </Button>
      </Box>
    );
  }

  if (!event) {
    return (
      <Box className={styles.notFound}>
        <Typography variant="h5" gutterBottom>
          Event not found
        </Typography>
        <Button variant="outlined" onClick={handleBack}>
          <BackIcon sx={{mr: 1}}/>
          Back to Calendar
        </Button>
      </Box>
    );
  }

  return (
    <Box className={styles.container}>
      <Box className={styles.header}>
        <Typography variant="h4" component="h1">
          Event Details
        </Typography>
        <Box className={styles.buttonGroup}>
          <Button
            variant="outlined"
            startIcon={<BackIcon/>}
            onClick={handleBack}
          >
            Back
          </Button>
          {!editing && (
            <>
              <Button
                variant="contained"
                startIcon={<EditIcon/>}
                onClick={handleEdit}
              >
                Edit
              </Button>
              <Button
                variant="outlined"
                color="error"
                startIcon={<DeleteIcon/>}
                onClick={() => setDeleteDialogOpen(true)}
              >
                Delete
              </Button>
            </>
          )}
        </Box>
      </Box>

      {error && (
        <Alert severity="error" className={styles.error}>
          {error}
        </Alert>
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
        <Card>
          <CardContent>
            <Typography variant="h5" gutterBottom>
              {event.title}
            </Typography>

            {event.description && (
              <Box className={styles.field}>
                <Typography variant="subtitle1" className={styles.fieldLabel} gutterBottom>
                  Description:
                </Typography>
                <Typography variant="body1" className={styles.fieldValue}>
                  {event.description}
                </Typography>
              </Box>
            )}

            <Box className={styles.field}>
              <Typography variant="subtitle1" className={styles.fieldLabel} gutterBottom>
                Start Date & Time:
              </Typography>


              <Typography variant="body1" className={styles.fieldValue}>
                {new Date(event.startDateTime).toLocaleString()}
              </Typography>
            </Box>

            <Box className={styles.field}>
              <Typography variant="subtitle1" className={styles.fieldLabel} gutterBottom>
                End Date & Time:
              </Typography>
              <Typography variant="body1" className={styles.fieldValue}>
                {new Date(event.endDateTime).toLocaleString()}
              </Typography>
            </Box>

            {event.location && (
              <Box className={styles.field}>
                <Typography variant="subtitle1" className={styles.fieldLabel} gutterBottom>
                  Location:
                </Typography>
                <Typography variant="body1" className={styles.fieldValue}>
                  {event.location}
                </Typography>
              </Box>
            )}
          </CardContent>
        </Card>
      )}

      {/* Delete Confirmation Dialog */}
      <Dialog
        open={deleteDialogOpen}
        onClose={() => setDeleteDialogOpen(false)}
        maxWidth="sm"
        fullWidth
      >
        <DialogTitle>Delete Event</DialogTitle>
        <DialogContent>
          <Typography>
            Are you sure you want to delete "{event.title}"? This action cannot be undone.
          </Typography>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDeleteDialogOpen(false)}>
            Cancel
          </Button>
          <Button
            onClick={handleDelete}
            color="error"
            variant="contained"
            disabled={deleting}
          >
            {deleting ? 'Deleting...' : 'Delete'}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}
