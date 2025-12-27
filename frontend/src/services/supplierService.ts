// services/supplierService.ts
import api from "./api";
import type {
  Supplier,
  CreateSupplierRequest,
  UpdateSupplierRequest,
} from "@/types/Supplier";

// Get all suppliers
export const getSuppliers = () => api.get<Supplier[]>("/suppliers");

// Get supplier by ID
export const getSupplierById = (id: number) =>
  api.get<Supplier>(`/suppliers/${id}`);

// Create supplier
export const createSupplier = (data: CreateSupplierRequest) =>
  api.post<Supplier>("/suppliers", data);

// Update supplier
export const updateSupplier = (id: number, data: UpdateSupplierRequest) =>
  api.put<Supplier>(`/suppliers/${id}`, data);

// Delete supplier (soft delete if backend supports)
export const deleteSupplier = (id: number) =>
  api.delete<void>(`/suppliers/${id}`);
