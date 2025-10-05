export interface Event {
  id: number;
  title: string;
  description?: string;
  startDateTime: string;
  endDateTime: string;
  location?: string;
}

export interface EventRequest {
  title: string;
  description?: string;
  startDateTime: string;
  endDateTime: string;
  location?: string;
}

export interface ApiResponse<T> {
  data: T;
  message?: string;
  status: number;
}

export interface ApiError {
  message?: string;
  status: number;
  errors?: string[];
  error?: string;
}
