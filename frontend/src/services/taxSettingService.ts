import api from "./api";
import type {
  TaxSetting,
  CreateTaxSettingRequest,
  UpdateTaxSettingRequest,
} from "@/types/TaxSetting";

const BASE_URL = "/api/tax-settings";

export const taxSettingService = {
  async list(): Promise<TaxSetting[]> {
    const response = await api.get(BASE_URL);
    return response.data;
  },

  async get(id: number): Promise<TaxSetting> {
    const response = await api.get(`${BASE_URL}/${id}`);
    return response.data;
  },

  async getActive(warehouseId?: number): Promise<TaxSetting> {
    const response = await api.get(`${BASE_URL}/active`, {
      params: {
        warehouseId,
      },
    });

    return response.data;
  },

  async create(request: CreateTaxSettingRequest): Promise<TaxSetting> {
    const response = await api.post(BASE_URL, request);
    return response.data;
  },

  async update(
    id: number,
    request: UpdateTaxSettingRequest,
  ): Promise<TaxSetting> {
    const response = await api.put(`${BASE_URL}/${id}`, request);

    return response.data;
  },

  async delete(id: number): Promise<void> {
    await api.delete(`${BASE_URL}/${id}`);
  },
};
