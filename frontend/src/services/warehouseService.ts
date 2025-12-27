import api from "./api";
import { useUserStore } from "@/stores/userStore";
import type {
  WarehouseListItem,
  WarehouseDetail,
  CreateWarehouseRequest,
  UpdateWarehouseRequest,
} from "@/types/Warehouse";

const getHeaders = () => {
  const userStore = useUserStore();
  const companyId = userStore.currentUser?.companyId;
  const userId = userStore.currentUser?.id;

  if (!companyId || !userId) throw new Error("User or company info missing");
  return { "X-Company-Id": companyId, "X-User-Id": userId };
};

export const getWarehouses = (params?: { active?: boolean }) =>
  api.get<WarehouseListItem[]>("/warehouses", {
    headers: getHeaders(),
    params,
  });

export const getWarehouseById = (id: number) =>
  api.get<WarehouseDetail>(`/warehouses/${id}`, { headers: getHeaders() });

export const createWarehouse = (data: CreateWarehouseRequest) =>
  api.post<WarehouseDetail>("/warehouses", data, { headers: getHeaders() });

export const updateWarehouse = (id: number, data: UpdateWarehouseRequest) =>
  api.put<WarehouseDetail>(`/warehouses/${id}`, data, {
    headers: getHeaders(),
  });

export const deleteWarehouse = (id: number) =>
  api.delete<void>(`/warehouses/${id}`, { headers: getHeaders() });
