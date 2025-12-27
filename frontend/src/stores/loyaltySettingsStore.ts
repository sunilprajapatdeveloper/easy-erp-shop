import { defineStore } from "pinia";
import { ref } from "vue";
import type {
  LoyaltySettings,
  CreateLoyaltySettingsRequest,
  UpdateLoyaltySettingsRequest,
} from "@/types/LoyaltySettings";
import { loyaltySettingsService } from "@/services/loyaltySettingsService";

export const useLoyaltySettingsStore = defineStore("loyaltySettings", () => {
  const loyaltySettingsList = ref<LoyaltySettings[]>([]);
  const loyaltySettings = ref<LoyaltySettings | null>(null);

  const fetchList = async (companyId: number) => {
    loyaltySettingsList.value = await loyaltySettingsService.list(companyId);
  };

  const fetch = async (id: number, companyId: number) => {
    loyaltySettings.value = await loyaltySettingsService.get(id, companyId);
  };

  const create = async (
    request: CreateLoyaltySettingsRequest,
    companyId: number,
    createdBy: number
  ) => {
    const created = await loyaltySettingsService.create(
      request,
      companyId,
      createdBy
    );
    loyaltySettings.value = created;
    await fetchList(companyId); // refresh list
    return created;
  };

  const update = async (
    id: number,
    request: UpdateLoyaltySettingsRequest,
    companyId: number,
    updatedBy: number
  ) => {
    const updated = await loyaltySettingsService.update(
      id,
      request,
      companyId,
      updatedBy
    );
    loyaltySettings.value = updated;
    await fetchList(companyId); // refresh list
    return updated;
  };

  return {
    loyaltySettingsList,
    loyaltySettings,
    fetchList,
    fetch,
    create,
    update,
  };
});
