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
  list(companyId: number) {
    return api.get<CompanyCurrency[]>("/company-currencies", {
      headers: { "X-Company-Id": companyId },
    });
  },

  /**
   * Get a single company currency by ID
   */
  get(id: number, companyId: number) {
    return api.get<CompanyCurrency>(`/company-currencies/${id}`, {
      headers: { "X-Company-Id": companyId },
    });
  },

  /**
   * Create a new company currency
   */
  create(companyId: number, payload: CreateCompanyCurrencyRequest) {
    return api.post<CompanyCurrency>("/company-currencies", payload, {
      headers: { "X-Company-Id": companyId },
    });
  },

  /**
   * Update an existing company currency
   */
  update(id: number, companyId: number, payload: UpdateCompanyCurrencyRequest) {
    return api.put<CompanyCurrency>(
      `/company-currencies/${id}`,
      payload,
      {
        headers: { "X-Company-Id": companyId },
      }
    );
  },

  /**
   * Delete a company currency
   */
  delete(id: number, companyId: number) {
    return api.delete(`/company-currencies/${id}`, {
      headers: { "X-Company-Id": companyId },
    });
  },
};
