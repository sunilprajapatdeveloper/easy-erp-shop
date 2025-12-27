import api from "./api";
import type { SaleReturn, CreateSaleReturnRequest, UpdateSaleReturnRequest } from "@/types/saleReturn";

// Get all sale returns for current user
export const getSaleReturns = () => api.get<SaleReturn[]>("/sales-return");

// Get sale return by id
export const getSaleReturnById = (id: number) =>
  api.get<SaleReturn>(`/sales-return/${id}`);

// Get all sale returns for company
export const getAllSaleReturns = () =>
  api.get<SaleReturn[]>("/sales-return/company");

// Create a sale return
export const createSaleReturn = (data: CreateSaleReturnRequest) =>
  api.post<SaleReturn>("/sales-return", data);

// Update a sale return
export const updateSaleReturn = (id: number, data: UpdateSaleReturnRequest) =>
  api.put<SaleReturn>(`/sales-return/${id}`, data);

// Delete a sale return
export const deleteSaleReturn = (id: number) =>
  api.delete<void>(`/sales-return/${id}`);
