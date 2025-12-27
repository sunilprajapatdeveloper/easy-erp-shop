import api from "./api";
import { useUserStore } from "@/stores/userStore";
import type {
  ExchangeRateListItem,
  ExchangeRateDetail,
  CreateExchangeRateRequest,
  UpdateExchangeRateRequest,
} from "@/types/ExchangeRate";

const getHeaders = () => {
  const userStore = useUserStore();
  const headers: Record<string, string> = {};
  if (userStore.currentUser?.id) {
    headers["X-User-Id"] = String(userStore.currentUser.id);
  }
  if (userStore.currentUser?.companyId) {
    headers["X-Company-Id"] = String(userStore.currentUser.companyId);
  }
  return { headers };
};

export const exchangeRateService = {
  getAll: async () => {
    const res = await api.get<ExchangeRateListItem[]>(
      "/exchange-rates",
      getHeaders()
    );
    return res.data;
  },

  getById: async (id: number) => {
    const res = await api.get<ExchangeRateDetail>(
      `/exchange-rates/${id}`,
      getHeaders()
    );
    return res.data;
  },

  create: async (payload: CreateExchangeRateRequest) => {
    const res = await api.post<ExchangeRateDetail>(
      "/exchange-rates",
      payload,
      getHeaders()
    );
    return res.data;
  },

  update: async (id: number, payload: UpdateExchangeRateRequest) => {
    const res = await api.put<ExchangeRateDetail>(
      `/exchange-rates/${id}`,
      payload,
      getHeaders()
    );
    return res.data;
  },

  delete: async (id: number) => {
    await api.delete<void>(`/exchange-rates/${id}`, getHeaders());
  },

  find: async (params: {
    baseCurrencyId: number;
    targetCurrencyId: number;
    companyId?: number;
    warehouseId?: number;
  }) => {
    const res = await api.get<ExchangeRateDetail>("/exchange-rates/find", {
      ...getHeaders(),
      params,
    });
    return res.data;
  },
};
