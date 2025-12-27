import api from "./api";
import { useUserStore } from "@/stores/userStore";
import type {
  ShippingProviderSettings,
  CreateShippingProviderSettingsRequest,
  UpdateShippingProviderSettingsRequest,
} from "@/types/ShippingProviderSettings";

const getHeaders = () => {
  const store = useUserStore();
  const headers: Record<string, string> = {};
  if (store.currentUser?.id)
    headers["X-User-Id"] = String(store.currentUser.id);
  if (store.currentUser?.companyId)
    headers["X-Company-Id"] = String(store.currentUser.companyId);
  if (store.token) headers["Authorization"] = `Bearer ${store.token}`;
  return { headers };
};

export const shippingProviderSettingsService = {
  create: async (
    payload: CreateShippingProviderSettingsRequest,
    createdBy: number
  ): Promise<ShippingProviderSettings> => {
    const res = await api.post<ShippingProviderSettings>(
      "/settings/shipping-provider",
      payload,
      { ...getHeaders(), params: { createdBy } }
    );
    return res.data;
  },

  update: async (
    id: number,
    companyId: number,
    warehouseId: number,
    payload: UpdateShippingProviderSettingsRequest,
    updatedBy: number
  ): Promise<ShippingProviderSettings> => {
    const res = await api.put<ShippingProviderSettings>(
      `/settings/shipping-provider/${id}`,
      payload,
      { ...getHeaders(), params: { companyId, warehouseId, updatedBy } }
    );
    return res.data;
  },

  getById: async (
    id: number,
    companyId: number,
    warehouseId: number
  ): Promise<ShippingProviderSettings> => {
    const res = await api.get<ShippingProviderSettings>(
      `/settings/shipping-provider/${id}`,
      { ...getHeaders(), params: { companyId, warehouseId } }
    );
    return res.data;
  },

  listByCompany: async (
    companyId: number
  ): Promise<ShippingProviderSettings[]> => {
    const res = await api.get<ShippingProviderSettings[]>(
      `/settings/shipping-provider/company/${companyId}`,
      getHeaders()
    );
    return res.data;
  },

  listByWarehouse: async (
    companyId: number,
    warehouseId: number
  ): Promise<ShippingProviderSettings[]> => {
    const res = await api.get<ShippingProviderSettings[]>(
      `/settings/shipping-provider/warehouse`,
      { ...getHeaders(), params: { companyId, warehouseId } }
    );
    return res.data;
  },
};
