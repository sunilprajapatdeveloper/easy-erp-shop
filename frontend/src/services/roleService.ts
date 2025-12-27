import api from "./api";
import type { Role } from "@/types/Role";

// Get all roles
export const getRoles = () => api.get<Role[]>("/roles");

// Get role by ID
export const getRoleById = (id: number) => api.get<Role>(`/roles/${id}`);

// Create a new role
export const createRole = (data: Omit<Role, "id">) =>
  api.post<Role>("/roles", data);

// Update a role
export const updateRole = (id: number, data: Omit<Role, "id">) =>
  api.put<Role>(`/roles/${id}`, data);

// Delete a role
export const deleteRole = (id: number) => api.delete<void>(`/roles/${id}`);
