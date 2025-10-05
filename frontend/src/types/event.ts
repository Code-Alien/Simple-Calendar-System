// Event types based on Java entity fields
export interface Event {
  id: number;
  title: string;
  description?: string;
  startDateTime: string; // ISO string format for Instant
  endDateTime: string; // ISO string format for Instant
  location?: string;
}

// Request type for creating/updating events (without id)
export interface EventRequest {
  title: string;
  description?: string;
  startDateTime: string; // ISO string format
  endDateTime: string; // ISO string format
  location?: string;
}

// Response type for API calls
export interface ApiResponse<T> {
  data: T;
  message?: string;
  status: number;
}

// Error response type
export interface ApiError {
  message: string;
  status: number;
  errors?: string[];
}
