import api from "./api";
import type {
  CompanyCurrency,
  CreateCompanyCurrencyRequest,
  UpdateCompanyCurrencyRequest,
} from "@/types/CompanyCurrency";

export const companyCurrencyService = {
  /**
   * List all company currencies
   */
  list(_companyId: number, onboardingToken?: string) {
    return api.get<CompanyCurrency[]>("/company-currencies", {
      headers: onboardingToken ? { "X-Onboarding-Token": onboardingToken } : {},
    });
  },

  /**
   * Get a single company currency by ID
   */
  get(id: number, _companyId: number) {
    return api.get<CompanyCurrency>(`/company-currencies/${id}`);
  },

  /**
   * Create a new company currency
   */
  create(_companyId: number, payload: CreateCompanyCurrencyRequest, onboardingToken?: string) {
    return api.post<CompanyCurrency>("/company-currencies", payload, {
      headers: onboardingToken ? { "X-Onboarding-Token": onboardingToken } : {},
    });
  },

  /**
   * Update an existing company currency
   */
  update(id: number, _companyId: number, payload: UpdateCompanyCurrencyRequest) {
    return api.put<CompanyCurrency>(
      `/company-currencies/${id}`,
      payload,
      {}
    );
  },

  /**
   * Delete a company currency
   */
  delete(id: number, _companyId: number) {
    return api.delete(`/company-currencies/${id}`);
  },
};
