import api from "./api";
import { useUserStore } from "@/stores/userStore";
import type {
  POSGeneralSettingsResponse,
  CreatePOSGeneralSettingsRequest,
  UpdatePOSGeneralSettingsRequest,
} from "@/types/POSGeneralSettings";

const getHeaders = (warehouseId?: number) => {
  const userStore = useUserStore();
  const companyId = userStore.currentUser?.companyId;
  const userId = userStore.currentUser?.id;

  if (!companyId || !userId) throw new Error("User or company info missing");
  const headers: Record<string, number> = {
    "X-Company-Id": companyId,
    "X-User-Id": userId,
  };
  if (warehouseId) headers["X-Warehouse-Id"] = warehouseId;
  return headers;
};

export const createPOSSettings = (
  warehouseId: number,
  data: CreatePOSGeneralSettingsRequest
) =>
  api.post<POSGeneralSettingsResponse>("/pos/settings", data, {
    headers: getHeaders(warehouseId),
  });

export const getPOSSettingsByWarehouse = (warehouseId: number) =>
  api.get<POSGeneralSettingsResponse>("/pos/settings", {
    headers: getHeaders(warehouseId),
  });

export const updatePOSSettings = (
  warehouseId: number,
  id: number,
  data: UpdatePOSGeneralSettingsRequest
) =>
  api.put<POSGeneralSettingsResponse>(`/pos/settings/${id}`, data, {
    headers: getHeaders(warehouseId),
  });

export const deletePOSSettings = (warehouseId: number, id: number) =>
  api.delete<void>(`/pos/settings/${id}`, { headers: getHeaders(warehouseId) });
