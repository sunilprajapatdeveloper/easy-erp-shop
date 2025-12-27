import api from "./api";
import type { Category } from "@/types/Category";

// Get all categories
export const getCategories = () => api.get<Category[]>("/categories");

// Create a new category
export const createCategory = (data: Omit<Category, "id">) =>
  api.post<Category>("/categories", data);

// Update a category
export const updateCategory = (id: number, data: Omit<Category, "id">) =>
  api.put<Category>(`/categories/${id}`, data);

// Delete a category
export const deleteCategory = (id: number) =>
  api.delete<void>(`/categories/${id}`);
