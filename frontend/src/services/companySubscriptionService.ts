import api from "./api";
import type {
  CompanySubscription,
  CreateCompanySubscriptionRequest,
  UpdateCompanySubscriptionRequest,
} from "@/types/CompanySubscription";

export const companySubscriptionService = {
  listByCompany(companyId: number) {
    return api.get<CompanySubscription[]>(
      `/company-subscriptions/company/${companyId}`
    );
  },

  getActive(companyId: number) {
    return api.get<CompanySubscription>(
      `/company-subscriptions/company/${companyId}/active`
    );
  },

  create(payload: CreateCompanySubscriptionRequest, userId: number) {
    return api.post<CompanySubscription>(
      `/company-subscriptions`,
      payload,
      {
        headers: { "X-User-Id": userId },
      }
    );
  },

  update(
    id: number,
    payload: UpdateCompanySubscriptionRequest,
    userId: number
  ) {
    return api.put<CompanySubscription>(
      `/company-subscriptions/${id}`,
      payload,
      {
        headers: { "X-User-Id": userId },
      }
    );
  },

  delete(id: number, userId: number) {
    return api.delete(`/company-subscriptions/${id}`, {
      headers: { "X-User-Id": userId },
    });
  },
};
