import api from "./api";
import type { Unit, CreateUnitRequest } from "@/types/Unit";

export const getUnits = () => api.get<Unit[]>("/units");

export const createUnit = (data: CreateUnitRequest) => api.post("/units", data);

export const updateUnit = (id: number, data: CreateUnitRequest) =>
  api.put(`/units/${id}`, data);

export const deleteUnit = (id: number) => api.delete(`/units/${id}`);
