import { defineStore } from "pinia";
import { socialMediaSettingsService } from "@/services/socialMediaSettingsService";
import type {
  SocialMediaSettings,
  CreateSocialMediaSettingsRequest,
  UpdateSocialMediaSettingsRequest,
} from "@/types/SocialMediaSettings";

export const useSocialMediaSettingsStore = defineStore("socialMediaSettings", {
  state: () => ({
    settings: [] as SocialMediaSettings[],
    loading: false,
    error: null as string | null,
  }),

  actions: {
    async fetchAll(companyId: number) {
      this.loading = true;
      this.error = null;
      try {
        this.settings = await socialMediaSettingsService.listByCompany(
          companyId
        );
        return this.settings;
      } catch (err: any) {
        this.error = err?.message || "Failed to fetch social media settings";
        throw err;
      } finally {
        this.loading = false;
      }
    },

    async fetchById(id: number, companyId: number) {
      this.loading = true;
      this.error = null;
      try {
        const setting = await socialMediaSettingsService.getById(id, companyId);
        const idx = this.settings.findIndex((s) => s.id === id);
        if (idx !== -1) this.settings[idx] = setting;
        else this.settings.push(setting);
        return setting;
      } catch (err: any) {
        this.error = err?.message || "Failed to fetch social media setting";
        throw err;
      } finally {
        this.loading = false;
      }
    },

    async create(payload: CreateSocialMediaSettingsRequest, createdBy: number) {
      this.loading = true;
      this.error = null;
      try {
        const created = await socialMediaSettingsService.create(
          payload,
          createdBy
        );
        this.settings.push(created);
        return created;
      } catch (err: any) {
        this.error = err?.message || "Failed to create social media setting";
        throw err;
      } finally {
        this.loading = false;
      }
    },

    async update(
      id: number,
      companyId: number,
      payload: UpdateSocialMediaSettingsRequest,
      updatedBy: number
    ) {
      this.loading = true;
      this.error = null;
      try {
        const updated = await socialMediaSettingsService.update(
          id,
          companyId,
          payload,
          updatedBy
        );
        const idx = this.settings.findIndex((s) => s.id === id);
        if (idx !== -1) this.settings[idx] = updated;
        return updated;
      } catch (err: any) {
        this.error = err?.message || "Failed to update social media setting";
        throw err;
      } finally {
        this.loading = false;
      }
    },

    clear() {
      this.settings = [];
      this.error = null;
    },
  },
});
