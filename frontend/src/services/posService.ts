import api from "./api";
import type { Pos, CreatePosRequest, UpdatePosRequest } from "@/types/Pos";

export const getAllPos = () => api.get<Pos[]>("/pos");

export const getPosById = (id: number) => api.get<Pos>(`/pos/${id}`);

export const createPos = (data: CreatePosRequest) =>
  api.post<Pos>("/pos/sale", data);

export const updatePos = (id: number, data: UpdatePosRequest) =>
  api.put<Pos>(`/pos/sale/${id}`, data);

export const deletePos = (id: number) => api.delete<void>(`/pos/${id}`);
