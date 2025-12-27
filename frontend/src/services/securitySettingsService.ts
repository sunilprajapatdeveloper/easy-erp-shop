import api from "./api";
import type {
  SecuritySettings,
  CreateSecuritySettingsRequest,
  UpdateSecuritySettingsRequest,
} from "@/types/SecuritySettings";

export const securitySettingsService = {
  async getSettings(companyId: number): Promise<SecuritySettings> {
    const { data } = await api.get(`/settings/security/company/${companyId}`);
    return data;
  },

  async createSettings(
    companyId: number,
    payload: CreateSecuritySettingsRequest,
    createdBy: number
  ): Promise<SecuritySettings> {
    const { data } = await api.post(
      `/settings/security/company/${companyId}?createdBy=${createdBy}`,
      payload
    );
    return data;
  },

  async updateSettings(
    companyId: number,
    payload: UpdateSecuritySettingsRequest,
    updatedBy: number
  ): Promise<SecuritySettings> {
    const { data } = await api.put(
      `/settings/security/company/${companyId}?updatedBy=${updatedBy}`,
      payload
    );
    return data;
  },

  async listAllSettings(): Promise<SecuritySettings[]> {
    const { data } = await api.get(`/settings/security/all`);
    return data;
  },
};
