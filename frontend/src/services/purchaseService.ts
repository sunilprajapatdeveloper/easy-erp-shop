import api from "./api";
import type { Purchase, CreatePurchaseRequest } from "@/types/Purchase";

export const getPurchases = () => api.get<Purchase[]>("/purchases");

export const getPurchaseById = (id: number) =>
  api.get<Purchase>(`/purchases/${id}`);

export const getAllPurchases = () => api.get<Purchase[]>("/purchases/company");

export const createPurchase = (data: CreatePurchaseRequest) =>
  api.post<Purchase>("/purchases", data);

export const updatePurchase = (id: number, data: CreatePurchaseRequest) =>
  api.put<Purchase>(`/purchases/${id}`, data);

export const deletePurchase = (id: number) =>
  api.delete<void>(`/purchases/${id}`);
