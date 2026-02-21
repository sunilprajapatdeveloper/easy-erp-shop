import api from "./api";
import type {
  WarehouseListItem,
  WarehouseDetail,
  CreateWarehouseRequest,
  UpdateWarehouseRequest,
} from "@/types/Warehouse";

export const getWarehouses = (params?: { active?: boolean }) =>
  api.get<WarehouseListItem[]>("/warehouses", { params });

export const getWarehouseById = (id: number) =>
  api.get<WarehouseDetail>(`/warehouses/${id}`);

export const createWarehouse = (data: CreateWarehouseRequest) =>
  api.post<WarehouseDetail>("/warehouses", data);

export const updateWarehouse = (id: number, data: UpdateWarehouseRequest) =>
  api.put<WarehouseDetail>(`/warehouses/${id}`, data);

export const deleteWarehouse = (id: number) =>
  api.delete<void>(`/warehouses/${id}`);
