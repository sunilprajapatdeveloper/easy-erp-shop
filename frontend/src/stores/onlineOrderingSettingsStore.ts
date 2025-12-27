import { defineStore } from "pinia";
import { onlineOrderingSettingsService } from "@/services/onlineOrderingSettingsService";
import type {
  OnlineOrderingSettings,
  CreateOnlineOrderingSettingsRequest,
  UpdateOnlineOrderingSettingsRequest,
} from "@/types/OnlineOrderingSettings";

interface OnlineOrderingSettingsState {
  settings: OnlineOrderingSettings | null;
  loading: boolean;
  error: string | null;
}

export const useOnlineOrderingSettingsStore = defineStore(
  "onlineOrderingSettings",
  {
    state: (): OnlineOrderingSettingsState => ({
      settings: null,
      loading: false,
      error: null,
    }),

    actions: {
      async fetchSettings(companyId: number) {
        this.loading = true;
        this.error = null;
        try {
          this.settings = await onlineOrderingSettingsService.getSettings(
            companyId
          );
        } catch (err: any) {
          this.error =
            err.message || "Failed to fetch online ordering settings";
        } finally {
          this.loading = false;
        }
      },

      async createSettings(payload: CreateOnlineOrderingSettingsRequest) {
        this.loading = true;
        this.error = null;
        try {
          this.settings = await onlineOrderingSettingsService.createSettings(
            payload
          );
          return this.settings;
        } catch (err: any) {
          this.error =
            err.message || "Failed to create online ordering settings";
          throw err;
        } finally {
          this.loading = false;
        }
      },

      async updateSettings(
        companyId: number,
        payload: UpdateOnlineOrderingSettingsRequest
      ) {
        this.loading = true;
        this.error = null;
        try {
          this.settings = await onlineOrderingSettingsService.updateSettings(
            companyId,
            payload
          );
          return this.settings;
        } catch (err: any) {
          this.error =
            err.message || "Failed to update online ordering settings";
          throw err;
        } finally {
          this.loading = false;
        }
      },
    },
  }
);
