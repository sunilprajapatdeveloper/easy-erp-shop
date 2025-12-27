import api from "./api";
import type {
  BrandingSettings,
  CreateBrandingSettingsRequest,
  UpdateBrandingSettingsRequest,
} from "@/types/BrandingSettings";

const BASE_URL = "/api/v1/branding-settings";

export const brandingSettingsService = {
  async list(companyId: number): Promise<BrandingSettings[]> {
    const response = await api.get(BASE_URL, {
      headers: { "X-Company-Id": companyId },
    });
    return response.data;
  },

  async get(id: number, companyId: number): Promise<BrandingSettings> {
    const response = await api.get(`${BASE_URL}/${id}`, {
      headers: { "X-Company-Id": companyId },
    });
    return response.data;
  },

  async create(
    request: CreateBrandingSettingsRequest,
    companyId: number,
    createdBy: number
  ): Promise<BrandingSettings> {
    const response = await api.post(BASE_URL, request, {
      headers: {
        "X-Company-Id": companyId,
        "X-User-Id": createdBy,
      },
    });
    return response.data;
  },

  async update(
    id: number,
    request: UpdateBrandingSettingsRequest,
    companyId: number,
    updatedBy: number
  ): Promise<BrandingSettings> {
    const response = await api.put(`${BASE_URL}/${id}`, request, {
      headers: {
        "X-Company-Id": companyId,
        "X-User-Id": updatedBy,
      },
    });
    return response.data;
  },

  async delete(
    id: number,
    companyId: number,
    deletedBy?: number
  ): Promise<void> {
    await api.delete(`${BASE_URL}/${id}`, {
      headers: {
        "X-Company-Id": companyId,
        "X-User-Id": deletedBy ?? 0,
      },
    });
  },
};
