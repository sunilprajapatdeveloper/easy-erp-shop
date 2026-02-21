import api from "./api";
import { useUserStore } from "@/stores/userStore";
import type {
  UserResponse,
  UserListItem,
  CreateUserRequest,
  UpdateUserRequest,
  UserRegisterRequest,
  UpdatePasswordRequest,
  LoginRequest,
  JwtResponse,
} from "@/types/User";

const getHeaders = () => {
  const store = useUserStore();
  const headers: Record<string, string> = {};
  if (store.currentUser?.id)
    headers["X-User-Id"] = String(store.currentUser.id);
  if (store.currentUser?.companyId)
    headers["X-Company-Id"] = String(store.currentUser.companyId);
  if (store.token) headers["Authorization"] = `Bearer ${store.token}`;
  return { headers };
};

const getCompanyHeaders = (companyId: number) => ({
  headers: {
    "X-Company-Id": String(companyId),
  },
});

export const userService = {
  getAllUsers: async (): Promise<UserListItem[]> => {
    const res = await api.get<UserListItem[]>("/users", getHeaders());
    return res.data;
  },

  getUserById: async (id: number): Promise<UserResponse> => {
    const res = await api.get<UserResponse>(`/users/${id}`, getHeaders());
    return res.data;
  },

  createUser: async (payload: CreateUserRequest): Promise<UserResponse> => {
    const res = await api.post<UserResponse>("/users", payload, getHeaders());
    return res.data;
  },

  updateUser: async (
    id: number,
    payload: UpdateUserRequest,
  ): Promise<UserResponse> => {
    const res = await api.put<UserResponse>(
      `/users/${id}`,
      payload,
      getHeaders(),
    );
    return res.data;
  },

  deleteUser: async (id: number): Promise<void> => {
    await api.delete(`/users/${id}`, getHeaders());
  },

  register: async (
    payload: UserRegisterRequest,
    companyId: number,
  ): Promise<UserResponse> => {
    const res = await api.post<UserResponse>(
      "/users/register",
      payload,
      getCompanyHeaders(companyId),
    );
    return res.data;
  },

  login: async (payload: LoginRequest): Promise<JwtResponse> => {
    const res = await api.post<JwtResponse>("/users/login", payload);
    return res.data;
  },

  updatePassword: async (
    payload: UpdatePasswordRequest & { userId?: number }
  ): Promise<void> => {
    await api.post("/users/update-password", payload, getHeaders());
  },
};
