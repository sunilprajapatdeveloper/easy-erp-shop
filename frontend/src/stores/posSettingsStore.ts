import { defineStore } from "pinia";
import type {
  POSGeneralSettingsResponse,
  CreatePOSGeneralSettingsRequest,
  UpdatePOSGeneralSettingsRequest,
} from "@/types/POSGeneralSettings";
import {
  createPOSSettings,
  getPOSSettingsByWarehouse,
  updatePOSSettings,
  deletePOSSettings,
} from "@/services/posGeneralSettingsService";

interface POSSettingsState {
  posSettings?: POSGeneralSettingsResponse;
  loading: boolean;
  error: string | null;
}

export const usePOSSettingsStore = defineStore("posSettings", {
  state: (): POSSettingsState => ({
    posSettings: undefined,
    loading: false,
    error: null,
  }),

  actions: {
    async fetchPOSSettings(warehouseId: number) {
      this.loading = true;
      this.error = null;
      try {
        const res = await getPOSSettingsByWarehouse(warehouseId);
        this.posSettings = res.data;
        return res.data;
      } catch (err: any) {
        this.error = err.message ?? "Failed to fetch POS settings";
        throw err;
      } finally {
        this.loading = false;
      }
    },

    // New helper to load POS settings and return default customer + currency
    async loadPOSSettings(warehouseId: number) {
      try {
        const data: POSGeneralSettingsResponse = await this.fetchPOSSettings(
          warehouseId
        );

        // Return a clean object with the defaults
        return {
          defaultCustomerId: data?.defaultCustomerId ?? null,
          defaultCurrencyId: data?.defaultCurrencyId ?? null,
          defaultPaymentMethod: data?.defaultPaymentMethod ?? "",
          defaultTaxInclusive: data?.defaultTaxInclusive ?? false,
        };
      } catch (err: any) {
        console.error("Failed to load POS settings:", err);
        return {
          defaultCustomerId: null,
          defaultCurrencyId: null,
          defaultPaymentMethod: "",
          defaultTaxInclusive: false,
        };
      }
    },

    async createPOSSettings(
      warehouseId: number,
      data: CreatePOSGeneralSettingsRequest
    ) {
      this.loading = true;
      this.error = null;
      try {
        const res = await createPOSSettings(warehouseId, data);
        this.posSettings = res.data;
        return res.data;
      } catch (err: any) {
        this.error = err.message ?? "Failed to create POS settings";
        throw err;
      } finally {
        this.loading = false;
      }
    },

    async updatePOSSettings(
      warehouseId: number,
      id: number,
      data: UpdatePOSGeneralSettingsRequest
    ) {
      this.loading = true;
      this.error = null;
      try {
        const res = await updatePOSSettings(warehouseId, id, data);
        this.posSettings = res.data;
        return res.data;
      } catch (err: any) {
        this.error = err.message ?? "Failed to update POS settings";
        throw err;
      } finally {
        this.loading = false;
      }
    },

    async deletePOSSettings(warehouseId: number, id: number) {
      this.loading = true;
      this.error = null;
      try {
        await deletePOSSettings(warehouseId, id);
        this.posSettings = undefined;
      } catch (err: any) {
        this.error = err.message ?? "Failed to delete POS settings";
        throw err;
      } finally {
        this.loading = false;
      }
    },
  },
});
