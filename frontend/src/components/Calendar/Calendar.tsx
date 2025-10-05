import FullCalendar from '@fullcalendar/react'
import dayGridPlugin from '@fullcalendar/daygrid'
import {useEffect, useState} from 'react'
import {eventsApi} from '../../api'
import type {EventClickArg, EventContentArg, EventInput} from '@fullcalendar/core/index.js'
import {useNavigate} from 'react-router-dom'
import {EventCard} from '../EventCard/EventCard'
import styles from './Calendar.module.css'

export function Calendar() {
  const navigate = useNavigate()
  const [fullCalendarEvents, setFullCalendarEvents] = useState<EventInput[]>([])
  const [loading, setLoading] = useState(true)

  const handleEventAdd = () => {
    navigate('/add-event')
  }

  const handleEventClick = (info: EventClickArg) => {
    const eventId = info.event.id
    if (eventId) {
      navigate(`/event/${eventId}`)
    }
  }

  const renderEventContent = (eventInfo: EventContentArg) => {
    return <EventCard event={eventInfo.event}/>
  }


  const fetchEvents = async () => {
    try {
      const events = await eventsApi.getAllEvents()
      setFullCalendarEvents(events.map(event => ({
        id: event.id.toString(),
        title: event.title,
        start: event.startDateTime,
        end: event.endDateTime,
        extendedProps: {
          location: event.location,
          description: event.description
        }
      })))
    } catch (error) {
      console.error('Error fetching events:', error)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchEvents()
  }, [])

  if (loading) {
    return <div>Loading...</div>
  }

  return (
    <div className={styles.calendarContainer}>
      <FullCalendar
        plugins={[dayGridPlugin]}
        initialView='dayGridMonth'
        weekends={true}
        events={fullCalendarEvents}
        eventContent={renderEventContent}
        themeSystem="solar"
        customButtons={{
          addEvent: {
            text: 'Add Event',
            click: handleEventAdd
          }
        }}
        headerToolbar={{
          left: 'title',
          right: 'addEvent, today, prev,next'
        }}
        editable={true}
        selectable={true}
        selectMirror={true}
        dayMaxEvents={true}
        height='100%'
        eventClick={handleEventClick}
        eventAdd={handleEventAdd}
      />
    </div>
  )
}