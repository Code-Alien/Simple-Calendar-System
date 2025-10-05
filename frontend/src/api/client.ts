import type {AxiosError, AxiosInstance, AxiosResponse} from 'axios';
import axios from 'axios';
import type {ApiError} from '../types/event';

const apiClient: AxiosInstance = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
    'X-Timezone': Intl.DateTimeFormat().resolvedOptions().timeZone,
  },
});

apiClient.interceptors.request.use(
  (config) => {
    return config;
  },
  (error) => {
    console.error('Request error:', error);
    return Promise.reject(error);
  }
);

apiClient.interceptors.response.use(
  (response: AxiosResponse) => {
    return response;
  },
  (error: AxiosError) => {
    console.error('Response error:', error);

    const apiError: ApiError = {
      message: error.message || 'An unexpected error occurred',
      status: error.response?.status || 500,
      errors: (error.response?.data as any)?.errors || [],
    };

    return Promise.reject(apiError);
  }
);

export default apiClient;
