import axios from "axios";
import { getAuthToken } from "./authService";

const api = axios.create({
  baseURL: "http://localhost:9091/api/v1",
  timeout: 30000, // Increased timeout for scanner operations
  headers: {
    "ngrok-skip-browser-warning": "true",
    "Content-Type": "application/json",
  },
});

// Define public endpoints that should not receive authentication headers
const publicPaths = [
  "/verifications",
  "/auth/login",
  "/auth/register",
  "/auth/forgot-password",
  "/users/register",
  "/subscription-plans"
];

// Request interceptor
api.interceptors.request.use(
  (config) => {
    const isPublic = publicPaths.some((path) => config.url?.startsWith(path));

    // Only add auth token for non‑public requests
    if (!isPublic) {
      const token = getAuthToken();
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
    }

    // Add company ID from user store if available
    if (!isPublic) {
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
    }

    return config;
  },
  (error) => Promise.reject(error),
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
