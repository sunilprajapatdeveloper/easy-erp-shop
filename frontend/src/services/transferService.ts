import api from "./api";
import type { Transfer, CreateTransferRequest } from "@/types/Transfer";

export const getTransfers = () => api.get<Transfer[]>("/transfers");

export const createTransfer = (data: CreateTransferRequest) =>
  api.post<Transfer>("/transfers", data);

export const updateTransfer = (id: number, data: CreateTransferRequest) =>
  api.put<Transfer>(`/transfers/${id}`, data);

export const deleteTransfer = (id: number) =>
  api.delete<void>(`/transfers/${id}`);
