import { defineStore } from "pinia";
import { ref } from "vue";
import type {
  CompanySubscription,
  CreateCompanySubscriptionRequest,
  UpdateCompanySubscriptionRequest,
} from "@/types/CompanySubscription";
import { companySubscriptionService } from "@/services/companySubscriptionService";

export const useCompanySubscriptionStore = defineStore(
  "companySubscription",
  () => {
    const list = ref<CompanySubscription[]>([]);
    const current = ref<CompanySubscription | null>(null);

    const fetchByCompany = async (companyId: number) => {
      const response = await companySubscriptionService.listByCompany(
        companyId
      );
      list.value = response.data;
    };

    const fetchActive = async (companyId: number) => {
      const response = await companySubscriptionService.getActive(companyId);
      current.value = response.data;
    };

    const create = async (
      payload: CreateCompanySubscriptionRequest,
      userId: number
    ) => {
      const response = await companySubscriptionService.create(payload, userId);
      list.value.push(response.data);
      return response.data;
    };

    const update = async (
      id: number,
      payload: UpdateCompanySubscriptionRequest,
      userId: number
    ) => {
      const response = await companySubscriptionService.update(
        id,
        payload,
        userId
      );
      const index = list.value.findIndex((item) => item.id === id);
      if (index !== -1) list.value[index] = response.data;
      if (current.value?.id === id) current.value = response.data;
      return response.data;
    };

    const remove = async (id: number, userId: number) => {
      await companySubscriptionService.delete(id, userId);
      list.value = list.value.filter((item) => item.id !== id);
      if (current.value?.id === id) current.value = null;
    };

    return {
      list,
      current,
      fetchByCompany,
      fetchActive,
      create,
      update,
      remove,
    };
  }
);
