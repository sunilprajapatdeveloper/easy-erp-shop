import { defineStore } from "pinia";
import { paymentGatewayService } from "@/services/paymentGatewayService";
import type {
  PaymentGatewaySettings,
  CreatePaymentGatewaySettingsRequest,
  UpdatePaymentGatewaySettingsRequest,
  PaginatedPaymentGatewaySettings,
} from "@/types/PaymentGateway";

interface PaymentGatewayState {
  companySettings: PaymentGatewaySettings[];
  systemSettings: PaymentGatewaySettings[];
  currentCompanySetting: PaymentGatewaySettings | null;
  currentSystemSetting: PaymentGatewaySettings | null;
  paginatedCompanySettings: PaginatedPaymentGatewaySettings | null;
  loading: boolean;
  error: string | null;
}

export const usePaymentGatewayStore = defineStore("paymentGateway", {
  state: (): PaymentGatewayState => ({
    companySettings: [],
    systemSettings: [],
    currentCompanySetting: null,
    currentSystemSetting: null,
    paginatedCompanySettings: null,
    loading: false,
    error: null,
  }),

  persist: true, // optional – works like userStore

  getters: {
    // Quick access to enabled settings (example)
    enabledCompanySettings: (state) =>
      state.companySettings.filter((s) => s.enabled),
    enabledSystemSettings: (state) =>
      state.systemSettings.filter((s) => s.enabled),
  },

  actions: {
    // ---------- Company‑level ----------
    async fetchAllCompanySettings() {
      this.loading = true;
      this.error = null;
      try {
        this.companySettings = await paymentGatewayService.getAllForCompany();
      } catch (err: any) {
        this.error = err?.message || "Failed to fetch company payment settings";
        throw err;
      } finally {
        this.loading = false;
      }
    },

    async fetchCompanySettingById(id: number) {
      this.loading = true;
      this.error = null;
      try {
        this.currentCompanySetting =
          await paymentGatewayService.getForCompanyById(id);
      } catch (err: any) {
        this.error = err?.message || "Failed to fetch company payment setting";
        throw err;
      } finally {
        this.loading = false;
      }
    },

    async createCompanySetting(payload: CreatePaymentGatewaySettingsRequest) {
      this.loading = true;
      this.error = null;
      try {
        const created = await paymentGatewayService.createForCompany(payload);
        this.companySettings.push(created);
        return created;
      } catch (err: any) {
        this.error = err?.message || "Failed to create company payment setting";
        throw err;
      } finally {
        this.loading = false;
      }
    },

    async updateCompanySetting(
      id: number,
      payload: UpdatePaymentGatewaySettingsRequest,
    ) {
      this.loading = true;
      this.error = null;
      try {
        const updated = await paymentGatewayService.updateForCompany(payload);
        const index = this.companySettings.findIndex((s) => s.id === id);
        if (index !== -1) this.companySettings[index] = updated;
        if (this.currentCompanySetting?.id === id)
          this.currentCompanySetting = updated;
        return updated;
      } catch (err: any) {
        this.error = err?.message || "Failed to update company payment setting";
        throw err;
      } finally {
        this.loading = false;
      }
    },

    async deleteCompanySetting(id: number) {
      this.loading = true;
      this.error = null;
      try {
        await paymentGatewayService.deleteForCompany(id);
        this.companySettings = this.companySettings.filter((s) => s.id !== id);
        if (this.currentCompanySetting?.id === id)
          this.currentCompanySetting = null;
      } catch (err: any) {
        this.error = err?.message || "Failed to delete company payment setting";
        throw err;
      } finally {
        this.loading = false;
      }
    },

    async fetchPaginatedCompanySettings(
      page: number,
      size: number,
      sort?: string,
    ) {
      this.loading = true;
      this.error = null;
      try {
        this.paginatedCompanySettings =
          await paymentGatewayService.getPaginatedForCompany(page, size, sort);
      } catch (err: any) {
        this.error =
          err?.message || "Failed to fetch paginated company settings";
        throw err;
      } finally {
        this.loading = false;
      }
    },

    // ---------- System‑level ----------
    async fetchAllSystemSettings() {
      this.loading = true;
      this.error = null;
      try {
        this.systemSettings = await paymentGatewayService.getAllSystem();
      } catch (err: any) {
        this.error = err?.message || "Failed to fetch system payment settings";
        throw err;
      } finally {
        this.loading = false;
      }
    },

    async fetchSystemSettingById(id: number) {
      this.loading = true;
      this.error = null;
      try {
        this.currentSystemSetting = await paymentGatewayService.getSystemById(
          id,
        );
      } catch (err: any) {
        this.error = err?.message || "Failed to fetch system payment setting";
        throw err;
      } finally {
        this.loading = false;
      }
    },

    async createSystemSetting(payload: CreatePaymentGatewaySettingsRequest) {
      this.loading = true;
      this.error = null;
      try {
        const created = await paymentGatewayService.createSystem(payload);
        this.systemSettings.push(created);
        return created;
      } catch (err: any) {
        this.error = err?.message || "Failed to create system payment setting";
        throw err;
      } finally {
        this.loading = false;
      }
    },

    async updateSystemSetting(
      id: number,
      payload: UpdatePaymentGatewaySettingsRequest,
    ) {
      this.loading = true;
      this.error = null;
      try {
        const updated = await paymentGatewayService.updateSystem(payload);
        const index = this.systemSettings.findIndex((s) => s.id === id);
        if (index !== -1) this.systemSettings[index] = updated;
        if (this.currentSystemSetting?.id === id)
          this.currentSystemSetting = updated;
        return updated;
      } catch (err: any) {
        this.error = err?.message || "Failed to update system payment setting";
        throw err;
      } finally {
        this.loading = false;
      }
    },

    async deleteSystemSetting(id: number) {
      this.loading = true;
      this.error = null;
      try {
        await paymentGatewayService.deleteSystem(id);
        this.systemSettings = this.systemSettings.filter((s) => s.id !== id);
        if (this.currentSystemSetting?.id === id)
          this.currentSystemSetting = null;
      } catch (err: any) {
        this.error = err?.message || "Failed to delete system payment setting";
        throw err;
      } finally {
        this.loading = false;
      }
    },
  },
});
