import { defineStore } from "pinia";
import { securitySettingsService } from "@/services/securitySettingsService";
import type {
  SecuritySettings,
  CreateSecuritySettingsRequest,
  UpdateSecuritySettingsRequest,
} from "@/types/SecuritySettings";

interface SecuritySettingsState {
  settings: SecuritySettings | null;
  allSettings: SecuritySettings[];
  loading: boolean;
  error: string | null;
}

export const useSecuritySettingsStore = defineStore("securitySettings", {
  state: (): SecuritySettingsState => ({
    settings: null,
    allSettings: [],
    loading: false,
    error: null,
  }),

  actions: {
    async fetchSettings(companyId: number) {
      this.loading = true;
      this.error = null;
      try {
        this.settings = await securitySettingsService.getSettings(companyId);
      } catch (err: any) {
        this.error = err.message || "Failed to fetch security settings";
      } finally {
        this.loading = false;
      }
    },

    async createSettings(
      companyId: number,
      payload: CreateSecuritySettingsRequest,
      createdBy: number
    ) {
      this.loading = true;
      this.error = null;
      try {
        this.settings = await securitySettingsService.createSettings(
          companyId,
          payload,
          createdBy
        );
        return this.settings;
      } catch (err: any) {
        this.error = err.message || "Failed to create security settings";
        throw err;
      } finally {
        this.loading = false;
      }
    },

    async updateSettings(
      companyId: number,
      payload: UpdateSecuritySettingsRequest,
      updatedBy: number
    ) {
      this.loading = true;
      this.error = null;
      try {
        this.settings = await securitySettingsService.updateSettings(
          companyId,
          payload,
          updatedBy
        );
        return this.settings;
      } catch (err: any) {
        this.error = err.message || "Failed to update security settings";
        throw err;
      } finally {
        this.loading = false;
      }
    },

    async fetchAllSettings() {
      this.loading = true;
      this.error = null;
      try {
        this.allSettings = await securitySettingsService.listAllSettings();
      } catch (err: any) {
        this.error = err.message || "Failed to fetch all security settings";
      } finally {
        this.loading = false;
      }
    },
  },
});
