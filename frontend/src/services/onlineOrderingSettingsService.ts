import api from "./api";
import { useUserStore } from "@/stores/userStore";
import type {
  OnlineOrderingSettings,
  CreateOnlineOrderingSettingsRequest,
  UpdateOnlineOrderingSettingsRequest,
} from "@/types/OnlineOrderingSettings";

const getHeaders = () => {
  const userStore = useUserStore();
  const userId = userStore.currentUser?.id;
  return {
    "X-User-Id": userId?.toString() || "",
  };
};

export const onlineOrderingSettingsService = {
  async getSettings(companyId: number): Promise<OnlineOrderingSettings> {
    const { data } = await api.get(`/online-ordering-settings/${companyId}`, {
      headers: getHeaders(),
    });
    return data;
  },

  async createSettings(
    payload: CreateOnlineOrderingSettingsRequest
  ): Promise<OnlineOrderingSettings> {
    const { data } = await api.post(`/online-ordering-settings`, payload, {
      headers: getHeaders(),
    });
    return data;
  },

  async updateSettings(
    companyId: number,
    payload: UpdateOnlineOrderingSettingsRequest
  ): Promise<OnlineOrderingSettings> {
    const { data } = await api.put(
      `/online-ordering-settings/${companyId}`,
      payload,
      {
        headers: getHeaders(),
      }
    );
    return data;
  },
};
