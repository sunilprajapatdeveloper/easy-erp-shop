// src/services/api.ts
import axios from "axios";
import { getAuthToken } from "./authService";

const api = axios.create({
  baseURL: "https://liberal-tick-quiet.ngrok-free.app/api/v1",
  timeout: 30000, // Increased timeout for scanner operations
  headers: {
    "ngrok-skip-browser-warning": "true",
    "Content-Type": "application/json",
  },
});

// Request interceptor
api.interceptors.request.use(
  (config) => {
    const token = getAuthToken();
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    // Add company ID from user store if available
    const userData = localStorage.getItem("user");
    if (userData) {
      try {
        const user = JSON.parse(userData);
        if (user.companyId) {
          config.headers["X-Company-Id"] = user.companyId;
        }
      } catch (e) {
        console.warn("Failed to parse user data from localStorage");
      }
    }

    return config;
  },
  (error) => Promise.reject(error)
);

// Response interceptor
api.interceptors.response.use(
  (response) => response,
  (error) => {
    console.error("API Error:", error.response?.data || error.message);

    if (error.response?.status === 401) {
      localStorage.removeItem("authToken");
      localStorage.removeItem("authTokenExpiry");
      localStorage.removeItem("user");
      window.location.href = "/login";
    }

    return Promise.reject(error);
  }
);

export default api;
