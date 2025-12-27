import { defineStore } from "pinia";
import { ref } from "vue";
import type {
  BrandingSettings,
  CreateBrandingSettingsRequest,
  UpdateBrandingSettingsRequest,
} from "@/types/BrandingSettings";
import { brandingSettingsService } from "@/services/brandingSettingsService";

export const useBrandingSettingsStore = defineStore("brandingSettings", () => {
  const brandingSettingsList = ref<BrandingSettings[]>([]);
  const brandingSettings = ref<BrandingSettings | null>(null);

  const fetchList = async (companyId: number) => {
    brandingSettingsList.value = await brandingSettingsService.list(companyId);
  };

  const fetch = async (id: number, companyId: number) => {
    brandingSettings.value = await brandingSettingsService.get(id, companyId);
  };

  const create = async (
    request: CreateBrandingSettingsRequest,
    companyId: number,
    createdBy: number
  ) => {
    const created = await brandingSettingsService.create(
      request,
      companyId,
      createdBy
    );
    brandingSettings.value = created;
    await fetchList(companyId);
    return created;
  };

  const update = async (
    id: number,
    request: UpdateBrandingSettingsRequest,
    companyId: number,
    updatedBy: number
  ) => {
    const updated = await brandingSettingsService.update(
      id,
      request,
      companyId,
      updatedBy
    );
    brandingSettings.value = updated;
    await fetchList(companyId);
    return updated;
  };

  const remove = async (id: number, companyId: number, deletedBy?: number) => {
    await brandingSettingsService.delete(id, companyId, deletedBy);
    brandingSettings.value = null;
    await fetchList(companyId);
  };

  return {
    brandingSettingsList,
    brandingSettings,
    fetchList,
    fetch,
    create,
    update,
    remove,
  };
});
