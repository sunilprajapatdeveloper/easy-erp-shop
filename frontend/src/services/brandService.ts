import api from "./api";
import type { Brand } from "@/types/Brand";

// Get all brands
export const getBrands = () => api.get<Brand[]>("/brands");

// Create a new brand
export const createBrand = (data: Omit<Brand, "id">) =>
  api.post<Brand>("/brands", data);

// Update a brand
export const updateBrand = (id: number, data: Omit<Brand, "id">) =>
  api.put<Brand>(`/brands/${id}`, data);

// Delete a brand
export const deleteBrand = (id: number) => api.delete<void>(`/brands/${id}`);
