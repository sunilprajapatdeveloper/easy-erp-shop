import api from "./api";
import type {
  TaxSetting,
  CreateTaxSettingRequest,
  UpdateTaxSettingRequest,
} from "@/types/TaxSetting";

const BASE_URL = "/api/tax-settings";

export const taxSettingService = {
  async list(companyId: number): Promise<TaxSetting[]> {
    const response = await api.get(BASE_URL, {
      headers: { "X-Company-Id": companyId },
    });
    return response.data;
  },

  async get(id: number, companyId: number): Promise<TaxSetting> {
    const response = await api.get(`${BASE_URL}/${id}`, {
      headers: { "X-Company-Id": companyId },
    });
    return response.data;
  },

  async create(
    request: CreateTaxSettingRequest,
    companyId: number
  ): Promise<TaxSetting> {
    const response = await api.post(BASE_URL, request, {
      headers: { "X-Company-Id": companyId },
    });
    return response.data;
  },

  async update(
    id: number,
    request: UpdateTaxSettingRequest,
    companyId: number
  ): Promise<TaxSetting> {
    const response = await api.put(`${BASE_URL}/${id}`, request, {
      headers: { "X-Company-Id": companyId },
    });
    return response.data;
  },

  async delete(id: number, companyId: number): Promise<void> {
    await api.delete(`${BASE_URL}/${id}`, {
      headers: { "X-Company-Id": companyId },
    });
  },
};
