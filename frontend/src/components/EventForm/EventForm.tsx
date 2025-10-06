import {Controller, useForm} from 'react-hook-form';
import {Box, Button, FormControl, FormGroup, Stack, TextField} from '@mui/material';
import {DateTimePicker} from '@mui/x-date-pickers/DateTimePicker';
import {LocalizationProvider} from '@mui/x-date-pickers/LocalizationProvider';
import {AdapterDayjs} from '@mui/x-date-pickers/AdapterDayjs';
import dayjs from 'dayjs';
import utc from 'dayjs/plugin/utc';
import timezone from 'dayjs/plugin/timezone';
import styles from './EventForm.module.css';
import type {EventRequest} from '../../types/event';
import "dayjs/locale/fr";
import "dayjs/locale/de";
import "dayjs/locale/ja";
import "dayjs/locale/vi";
import 'dayjs/locale/en';
import 'dayjs/locale/uk';
import 'dayjs/locale/es';
import 'dayjs/locale/it';
import 'dayjs/locale/pt';

dayjs.extend(utc);
dayjs.extend(timezone);

interface EventFormProps {
  onSubmit: (data: EventRequest) => void;
  onCancel: () => void;
  loading?: boolean;
  initialData?: Partial<EventRequest>;
  isEditing?: boolean;
}

export function EventForm({onSubmit, onCancel, loading = false, initialData, isEditing = false}: Readonly<EventFormProps>) {
  const {
    register,
    handleSubmit,
    control,
    formState: {errors},
    watch
  } = useForm<EventRequest>({
    defaultValues: initialData || {
      title: '',
      description: '',
      startDateTime: '',
      endDateTime: '',
      location: ''
    }
  });

  const minDate = dayjs('1970-01-02T00:00:00Z');
  const maxDate = dayjs('2038-01-18T03:10:00Z');

  const startDateTime = watch('startDateTime');
  const languageCode = navigator.language?.split('-')[0];
  let submitButtonText: string = isEditing ? 'Save Changes' : 'Create Event';

  return (
    <LocalizationProvider dateAdapter={AdapterDayjs} adapterLocale={languageCode}>
      <div className={styles.formContainer}>
        <Box component="form" onSubmit={handleSubmit(onSubmit)} className={styles.form}>
          <FormControl component="fieldset" className={styles.fieldset}>
            <FormGroup className={styles.formGroup}>
              <Stack spacing={3}>
                <TextField
                  fullWidth
                  label="Title *"
                  className={styles.textField}
                  {...register('title', {
                    required: 'Title is required',
                    validate: (value) =>
                      value.trim() !== '' || 'Title must not be empty or only spaces',
                    minLength: {value: 1, message: 'Title must not be empty'}
                  })}
                  error={!!errors.title}
                  helperText={errors.title?.message}
                  disabled={loading}
                  inputProps={{maxLength: 255}}
                />

                <TextField
                  fullWidth
                  label="Description"
                  multiline
                  rows={4}
                  className={styles.textField}
                  {...register('description')}
                  disabled={loading}
                  inputProps={{maxLength: 5000}}
                />

                <Stack className={styles.dateTimeRow}>
                  <Controller
                    name="startDateTime"
                    control={control}
                    rules={{
                      required: 'Start date and time is required'
                    }}
                    render={({field, fieldState: {error}}) => (
                      <DateTimePicker
                        {...field}
                        label="Start Date & Time *"
                        value={field.value ? dayjs(field.value) : null}
                        onChange={(date) => {
                          if (date) {
                            field.onChange(date.format('YYYY-MM-DDTHH:mm:ss'));
                          } else {
                            field.onChange(null);
                          }
                        }}
                        minDate={minDate}
                        maxDate={maxDate}
                        disabled={loading}
                        slotProps={{
                          textField: {
                            fullWidth: true,
                            className: styles.textField,
                            error: !!error,
                            helperText: error?.message
                          }
                        }}
                      />
                    )}
                  />

                  <Controller
                    name="endDateTime"
                    control={control}
                    rules={{
                      required: 'End date and time is required',
                      validate: (value) => {
                        if (!value) return 'End date and time is required';
                        if (startDateTime && !dayjs(value).isAfter(dayjs(startDateTime))) {
                          return 'End date and time must be after start date and time';
                        }
                        return true;
                      }
                    }}
                    render={({field, fieldState: {error}}) => (
                      <DateTimePicker
                        {...field}
                        label="End Date & Time *"
                        value={field.value ? dayjs(field.value) : null}
                        onChange={(date) => {
                          if (date) {
                            field.onChange(date.format('YYYY-MM-DDTHH:mm:ss'));
                          } else {
                            field.onChange(null);
                          }
                        }}
                        disabled={loading}
                        minDateTime={startDateTime ? dayjs(startDateTime) : minDate}
                        maxDate={maxDate}
                        slotProps={{
                          textField: {
                            fullWidth: true,
                            className: styles.textField,
                            error: !!error,
                            helperText: error?.message
                          }
                        }}
                      />
                    )}
                  />
                </Stack>

                <TextField
                  fullWidth
                  label="Location"
                  className={styles.textField}
                  {...register('location')}
                  disabled={loading}
                  inputProps={{maxLength: 255}}
                />

                <Box className={styles.buttonGroup}>
                  <Button
                    variant="outlined"
                    onClick={onCancel}
                    disabled={loading}
                    className={styles.cancelButton}
                  >
                    Cancel
                  </Button>
                  <Button
                    type="submit"
                    variant="contained"
                    disabled={loading}
                    className={styles.submitButton}
                  >
                    {submitButtonText}
                  </Button>
                </Box>
              </Stack>
            </FormGroup>
          </FormControl>
        </Box>
      </div>
    </LocalizationProvider>
  );
}
