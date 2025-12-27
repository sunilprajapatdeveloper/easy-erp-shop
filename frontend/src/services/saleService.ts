import api from "./api";
import type { Sale, CreateSaleRequest } from "@/types/Sale";

export const getSales = () => api.get<Sale[]>("/sales");

export const getSaleById = (id: number) => api.get<Sale>(`/sales/${id}`);

export const getAllSales = () => api.get<Sale[]>("/sales/company");

export const createSale = (data: CreateSaleRequest) =>
  api.post<Sale>("/sales", data);

export const updateSale = (id: number, data: CreateSaleRequest) =>
  api.put<Sale>(`/sales/${id}`, data);

export const deleteSale = (id: number) => api.delete<void>(`/sales/${id}`);