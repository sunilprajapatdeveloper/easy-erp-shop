import { defineStore } from "pinia";
import { ref } from "vue";
import type {
  TaxSetting,
  CreateTaxSettingRequest,
  UpdateTaxSettingRequest,
} from "@/types/TaxSetting";
import { taxSettingService } from "@/services/taxSettingService";

export const useTaxSettingStore = defineStore("taxSetting", () => {
  const taxSettingsList = ref<TaxSetting[]>([]);
  const taxSetting = ref<TaxSetting | null>(null);

  const fetchList = async (companyId: number) => {
    taxSettingsList.value = await taxSettingService.list(companyId);
  };

  const fetch = async (id: number, companyId: number) => {
    taxSetting.value = await taxSettingService.get(id, companyId);
  };

  const create = async (
    request: CreateTaxSettingRequest,
    companyId: number
  ) => {
    const created = await taxSettingService.create(request, companyId);
    taxSetting.value = created;
    await fetchList(companyId);
    return created;
  };

  const update = async (
    id: number,
    request: UpdateTaxSettingRequest,
    companyId: number
  ) => {
    const updated = await taxSettingService.update(id, request, companyId);
    taxSetting.value = updated;
    await fetchList(companyId);
    return updated;
  };

  const remove = async (id: number, companyId: number) => {
    await taxSettingService.delete(id, companyId);
    taxSetting.value = null;
    await fetchList(companyId);
  };

  return {
    taxSettingsList,
    taxSetting,
    fetchList,
    fetch,
    create,
    update,
    remove,
  };
});
