import api from "./api";
import type { Adjustment, CreateAdjustmentRequest } from "@/types/Adjustment";

export const getAdjustments = () => api.get<Adjustment[]>("/adjustments");

export const createAdjustment = (data: CreateAdjustmentRequest) =>
  api.post<Adjustment>("/adjustments", data);

export const updateAdjustment = (id: number, data: CreateAdjustmentRequest) =>
  api.put<Adjustment>(`/adjustments/${id}`, data);

export const deleteAdjustment = (id: number) =>
  api.delete<void>(`/adjustments/${id}`);
