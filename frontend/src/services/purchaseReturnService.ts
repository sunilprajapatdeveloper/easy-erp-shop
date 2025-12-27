import api from "./api";
import type {
  PurchaseReturn,
  CreatePurchaseReturnRequest,
  UpdatePurchaseReturnRequest,
} from "@/types/PurchaseReturn";

export const getPurchaseReturns = () =>
  api.get<PurchaseReturn[]>("/purchase-returns");

export const getPurchaseReturnById = (id: number) =>
  api.get<PurchaseReturn>(`/purchase-returns/${id}`);

export const getAllPurchaseReturns = () =>
  api.get<PurchaseReturn[]>("/purchase-returns/company");

export const createPurchaseReturn = (data: CreatePurchaseReturnRequest) =>
  api.post<PurchaseReturn>("/purchase-returns", data);

export const updatePurchaseReturn = (
  id: number,
  data: UpdatePurchaseReturnRequest
) => api.put<PurchaseReturn>(`/purchase-returns/${id}`, data);

export const deletePurchaseReturn = (id: number) =>
  api.delete<void>(`/purchase-returns/${id}`);
