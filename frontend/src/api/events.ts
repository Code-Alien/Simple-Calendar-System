import apiClient from './client';
import type {Event, EventRequest} from '../types/event';

const ENDPOINTS = {
  EVENTS: '/events',
  EVENT_BY_ID: (id: number) => `/events/${id}`,
} as const;

export const createEvent = async (event: EventRequest): Promise<Event> => {
  try {
    const response = await apiClient.post<Event>(ENDPOINTS.EVENTS, event);
    return response.data;
  } catch (error) {
    console.error('Error creating event:', error);
    throw error;
  }
};

export const getAllEvents = async (): Promise<Event[]> => {
  try {
    const response = await apiClient.get<Event[]>(ENDPOINTS.EVENTS);
    return response.data;
  } catch (error) {
    console.error('Error fetching events:', error);
    throw error;
  }
};

export const getEventById = async (id: number): Promise<Event> => {
  try {
    const response = await apiClient.get<Event>(ENDPOINTS.EVENT_BY_ID(id));
    return response.data;
  } catch (error) {
    console.error(`Error fetching event ${id}:`, error);
    throw error;
  }
};

export const updateEvent = async (id: number, event: EventRequest): Promise<Event> => {
  try {
    const response = await apiClient.put<Event>(ENDPOINTS.EVENT_BY_ID(id), event);
    return response.data;
  } catch (error) {
    console.error(`Error updating event ${id}:`, error);
    throw error;
  }
};

export const deleteEvent = async (id: number): Promise<void> => {
  try {
    await apiClient.delete(ENDPOINTS.EVENT_BY_ID(id));
  } catch (error) {
    console.error(`Error deleting event ${id}:`, error);
    throw error;
  }
};

export const eventsApi = {
  createEvent,
  getAllEvents,
  getEventById,
  updateEvent,
  deleteEvent,
};
