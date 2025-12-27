import { defineStore } from "pinia";
import { ref } from "vue";
import type {
  CompanyCurrency,
  CreateCompanyCurrencyRequest,
  UpdateCompanyCurrencyRequest,
} from "@/types/CompanyCurrency";
import { companyCurrencyService } from "@/services/companyCurrencyService";

export const useCompanyCurrencyStore = defineStore("companyCurrency", () => {
  const list = ref<CompanyCurrency[]>([]);
  const current = ref<CompanyCurrency | null>(null);

  /**
   * Fetch all company currencies for a company
   */
  const fetchAll = async (companyId: number) => {
    const response = await companyCurrencyService.list(companyId);
    list.value = response.data;
  };

  /**
   * Fetch a single currency by ID
   */
  const fetchOne = async (id: number, companyId: number) => {
    const response = await companyCurrencyService.get(id, companyId);
    current.value = response.data;
  };

  /**
   * Create a new company currency
   */
  const create = async (
    companyId: number,
    payload: CreateCompanyCurrencyRequest
  ) => {
    const response = await companyCurrencyService.create(companyId, payload);
    list.value.push(response.data);
    return response.data;
  };

  /**
   * Update an existing company currency
   */
  const update = async (
    id: number,
    companyId: number,
    payload: UpdateCompanyCurrencyRequest
  ) => {
    const response = await companyCurrencyService.update(
      id,
      companyId,
      payload
    );

    // Update list
    const index = list.value.findIndex((item) => item.id === id);
    if (index !== -1) list.value[index] = response.data;

    // Update current if matching
    if (current.value?.id === id) current.value = response.data;

    return response.data;
  };

  /**
   * Delete a currency
   */
  const remove = async (id: number, companyId: number) => {
    await companyCurrencyService.delete(id, companyId);
    list.value = list.value.filter((item) => item.id !== id);

    if (current.value?.id === id) current.value = null;
  };

  return {
    list,
    current,
    fetchAll,
    fetchOne,
    create,
    update,
    remove,
  };
});
