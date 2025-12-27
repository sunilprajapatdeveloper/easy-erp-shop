import { defineStore } from "pinia";
import { shippingProviderSettingsService } from "@/services/shippingProviderSettingsService";
import type {
  ShippingProviderSettings,
  CreateShippingProviderSettingsRequest,
  UpdateShippingProviderSettingsRequest,
} from "@/types/ShippingProviderSettings";

export const useShippingProviderSettingsStore = defineStore(
  "shippingProviderSettings",
  {
    state: () => ({
      settings: [] as ShippingProviderSettings[],
      loading: false,
      error: null as string | null,
    }),

    actions: {
      async fetchAllByCompany(companyId: number) {
        this.loading = true;
        this.error = null;
        try {
          this.settings = await shippingProviderSettingsService.listByCompany(
            companyId
          );
          return this.settings;
        } catch (err: any) {
          this.error = err?.message || "Failed to fetch settings";
          throw err;
        } finally {
          this.loading = false;
        }
      },

      async fetchAllByWarehouse(companyId: number, warehouseId: number) {
        this.loading = true;
        this.error = null;
        try {
          this.settings = await shippingProviderSettingsService.listByWarehouse(
            companyId,
            warehouseId
          );
          return this.settings;
        } catch (err: any) {
          this.error = err?.message || "Failed to fetch settings";
          throw err;
        } finally {
          this.loading = false;
        }
      },

      async fetchById(id: number, companyId: number, warehouseId: number) {
        this.loading = true;
        this.error = null;
        try {
          const setting = await shippingProviderSettingsService.getById(
            id,
            companyId,
            warehouseId
          );
          const idx = this.settings.findIndex((s) => s.id === id);
          if (idx !== -1) this.settings[idx] = setting;
          else this.settings.push(setting);
          return setting;
        } catch (err: any) {
          this.error = err?.message || "Failed to fetch setting";
          throw err;
        } finally {
          this.loading = false;
        }
      },

      async create(
        payload: CreateShippingProviderSettingsRequest,
        createdBy: number
      ) {
        this.loading = true;
        this.error = null;
        try {
          const created = await shippingProviderSettingsService.create(
            payload,
            createdBy
          );
          this.settings.push(created);
          return created;
        } catch (err: any) {
          this.error = err?.message || "Failed to create setting";
          throw err;
        } finally {
          this.loading = false;
        }
      },

      async update(
        id: number,
        companyId: number,
        warehouseId: number,
        payload: UpdateShippingProviderSettingsRequest,
        updatedBy: number
      ) {
        this.loading = true;
        this.error = null;
        try {
          const updated = await shippingProviderSettingsService.update(
            id,
            companyId,
            warehouseId,
            payload,
            updatedBy
          );
          const idx = this.settings.findIndex((s) => s.id === id);
          if (idx !== -1) this.settings[idx] = updated;
          return updated;
        } catch (err: any) {
          this.error = err?.message || "Failed to update setting";
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
  }
);
