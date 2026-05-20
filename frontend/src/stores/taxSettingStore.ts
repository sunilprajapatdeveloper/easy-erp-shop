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

  /*
   * Main tax configuration used during sale calculation/POS flow
   */
  const activeTaxSetting = ref<TaxSetting | null>(null);

  const fetchList = async () => {
    taxSettingsList.value = await taxSettingService.list();
  };

  const fetch = async (id: number) => {
    taxSetting.value = await taxSettingService.get(id);
  };

  /*
   * Fetch warehouse/company active tax configuration
   */
  const fetchActive = async (warehouseId?: number) => {
    activeTaxSetting.value = await taxSettingService.getActive(warehouseId);

    return activeTaxSetting.value;
  };

  const create = async (request: CreateTaxSettingRequest) => {
    const created = await taxSettingService.create(request);

    taxSetting.value = created;

    await fetchList();

    return created;
  };

  const update = async (id: number, request: UpdateTaxSettingRequest) => {
    const updated = await taxSettingService.update(id, request);

    taxSetting.value = updated;

    await fetchList();

    return updated;
  };

  const remove = async (id: number) => {
    await taxSettingService.delete(id);

    taxSetting.value = null;

    await fetchList();
  };

  return {
    taxSettingsList,
    taxSetting,
    activeTaxSetting,
    fetchList,
    fetch,
    fetchActive,
    create,
    update,
    remove,
  };
});
