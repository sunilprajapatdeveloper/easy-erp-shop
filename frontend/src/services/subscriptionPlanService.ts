import api from "./api";
import type {
  SubscriptionPlan,
  CreateSubscriptionPlanRequest,
  UpdateSubscriptionPlanRequest,
} from "@/types/SubscriptionPlan";

const URL = "/subscription-plans";

export const subscriptionPlanService = {
  async list(): Promise<SubscriptionPlan[]> {
    const response = await api.get(URL);
    return response.data;
  },

  async get(id: number): Promise<SubscriptionPlan> {
    const response = await api.get(`${URL}/${id}`);
    return response.data;
  },

  async create(
    data: CreateSubscriptionPlanRequest,
    userId: number
  ): Promise<SubscriptionPlan> {
    const response = await api.post(URL, data, {
      headers: { "X-User-Id": userId },
    });
    return response.data;
  },

  async update(
    id: number,
    data: UpdateSubscriptionPlanRequest,
    userId: number
  ): Promise<SubscriptionPlan> {
    const response = await api.put(`${URL}/${id}`, data, {
      headers: { "X-User-Id": userId },
    });
    return response.data;
  },

  async delete(id: number, userId: number): Promise<void> {
    await api.delete(`${URL}/${id}`, { headers: { "X-User-Id": userId } });
  },
};
