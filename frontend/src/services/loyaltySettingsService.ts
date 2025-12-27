import api from "./api";
import type {
  LoyaltySettings,
  CreateLoyaltySettingsRequest,
  UpdateLoyaltySettingsRequest,
} from "@/types/LoyaltySettings";

const BASE_URL = "/api/v1/loyalty-settings";

export const loyaltySettingsService = {
  async list(companyId: number): Promise<LoyaltySettings[]> {
    const response = await api.get(`${BASE_URL}`, {
      headers: { "X-Company-Id": companyId },
    });
    return response.data;
  },

  async get(id: number, companyId: number): Promise<LoyaltySettings> {
    const response = await api.get(`${BASE_URL}/${id}`, {
      headers: { "X-Company-Id": companyId },
    });
    return response.data;
  },

  async create(
    request: CreateLoyaltySettingsRequest,
    companyId: number,
    createdBy: number
  ): Promise<LoyaltySettings> {
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
    request: UpdateLoyaltySettingsRequest,
    companyId: number,
    updatedBy: number
  ): Promise<LoyaltySettings> {
    const response = await api.put(`${BASE_URL}/${id}`, request, {
      headers: {
        "X-Company-Id": companyId,
        "X-User-Id": updatedBy,
      },
    });
    return response.data;
  },
};
