import api from "./api";
import type {
  SubscriptionPlan,
  CreateSubscriptionPlanRequest,
  UpdateSubscriptionPlanRequest,
} from "@/types/SubscriptionPlan";

const BASE_URL = "/api/v1/subscription-plans";

export const subscriptionPlanService = {
  async list(): Promise<SubscriptionPlan[]> {
    const response = await api.get(BASE_URL);
    return response.data;
  },

  async get(id: number): Promise<SubscriptionPlan> {
    const response = await api.get(`${BASE_URL}/${id}`);
    return response.data;
  },

  async create(
    data: CreateSubscriptionPlanRequest,
    userId: number
  ): Promise<SubscriptionPlan> {
    const response = await api.post(BASE_URL, data, {
      headers: { "X-User-Id": userId },
    });
    return response.data;
  },

  async update(
    id: number,
    data: UpdateSubscriptionPlanRequest,
    userId: number
  ): Promise<SubscriptionPlan> {
    const response = await api.put(`${BASE_URL}/${id}`, data, {
      headers: { "X-User-Id": userId },
    });
    return response.data;
  },

  async delete(id: number, userId: number): Promise<void> {
    await api.delete(`${BASE_URL}/${id}`, { headers: { "X-User-Id": userId } });
  },
};
