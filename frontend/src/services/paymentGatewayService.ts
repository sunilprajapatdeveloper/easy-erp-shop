import api from "./api";
import { useUserStore } from "@/stores/userStore";
import type {
  PaymentGatewaySettings,
  CreatePaymentGatewaySettingsRequest,
  UpdatePaymentGatewaySettingsRequest,
  PaginatedPaymentGatewaySettings,
} from "@/types/PaymentGateway";

// Helper to inject authentication & tenant headers (same pattern as userService)
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

export const paymentGatewayService = {
  // ---------- Company‑level ----------
  createForCompany: async (
    payload: CreatePaymentGatewaySettingsRequest,
  ): Promise<PaymentGatewaySettings> => {
    const res = await api.post<PaymentGatewaySettings>(
      "/settings/payment-gateway/company",
      payload,
      getHeaders(),
    );
    return res.data;
  },

  updateForCompany: async (
    payload: UpdatePaymentGatewaySettingsRequest,
  ): Promise<PaymentGatewaySettings> => {
    const res = await api.put<PaymentGatewaySettings>(
      "/settings/payment-gateway/company",
      payload,
      getHeaders(),
    );
    return res.data;
  },

  deleteForCompany: async (id: number): Promise<void> => {
    await api.delete(`/settings/payment-gateway/company/${id}`, getHeaders());
  },

  getForCompanyById: async (id: number): Promise<PaymentGatewaySettings> => {
    const res = await api.get<PaymentGatewaySettings>(
      `/settings/payment-gateway/company/${id}`,
      getHeaders(),
    );
    return res.data;
  },

  getAllForCompany: async (): Promise<PaymentGatewaySettings[]> => {
    const res = await api.get<PaymentGatewaySettings[]>(
      "/settings/payment-gateway/company",
      getHeaders(),
    );
    return res.data;
  },

  getPaginatedForCompany: async (
    page: number,
    size: number,
    sort?: string,
  ): Promise<PaginatedPaymentGatewaySettings> => {
    const params: any = { page, size };
    if (sort) params.sort = sort;
    const res = await api.get<PaginatedPaymentGatewaySettings>(
      "/settings/payment-gateway/company/paginated",
      {
        ...getHeaders(),
        params,
      },
    );
    return res.data;
  },

  // ---------- System‑level (super admin only) ----------
  createSystem: async (
    payload: CreatePaymentGatewaySettingsRequest,
  ): Promise<PaymentGatewaySettings> => {
    const res = await api.post<PaymentGatewaySettings>(
      "/settings/payment-gateway/system",
      payload,
      getHeaders(),
    );
    return res.data;
  },

  updateSystem: async (
    payload: UpdatePaymentGatewaySettingsRequest,
  ): Promise<PaymentGatewaySettings> => {
    const res = await api.put<PaymentGatewaySettings>(
      "/settings/payment-gateway/system",
      payload,
      getHeaders(),
    );
    return res.data;
  },

  deleteSystem: async (id: number): Promise<void> => {
    await api.delete(`/settings/payment-gateway/system/${id}`, getHeaders());
  },

  getSystemById: async (id: number): Promise<PaymentGatewaySettings> => {
    const res = await api.get<PaymentGatewaySettings>(
      `/settings/payment-gateway/system/${id}`,
      getHeaders(),
    );
    return res.data;
  },

  getAllSystem: async (): Promise<PaymentGatewaySettings[]> => {
    const res = await api.get<PaymentGatewaySettings[]>(
      "/settings/payment-gateway/system",
      getHeaders(),
    );
    return res.data;
  },
};
