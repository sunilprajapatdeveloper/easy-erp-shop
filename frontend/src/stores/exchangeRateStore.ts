import { defineStore } from "pinia";
import { exchangeRateService } from "@/services/exchangeRateService";
import type {
  ExchangeRateListItem,
  ExchangeRateDetail,
  CreateExchangeRateRequest,
  UpdateExchangeRateRequest,
} from "@/types/ExchangeRate";

interface ExchangeRateState {
  exchangeRates: ExchangeRateListItem[];
  currentExchangeRate: ExchangeRateDetail | null;
  loading: boolean;
  error: string | null;
}

export const useExchangeRateStore = defineStore("exchangeRate", {
  state: (): ExchangeRateState => ({
    exchangeRates: [],
    currentExchangeRate: null,
    loading: false,
    error: null,
  }),

  actions: {
    async fetchAll() {
      this.loading = true;
      try {
        this.exchangeRates = await exchangeRateService.getAll();
      } catch (err: any) {
        this.error = err.message || "Failed to load exchange rates";
      } finally {
        this.loading = false;
      }
    },

    async fetchById(id: number) {
      this.loading = true;
      try {
        this.currentExchangeRate = await exchangeRateService.getById(id);
      } catch (err: any) {
        this.error = err.message || "Failed to load exchange rate details";
      } finally {
        this.loading = false;
      }
    },

    async create(payload: CreateExchangeRateRequest) {
      this.loading = true;
      try {
        const created = await exchangeRateService.create(payload);
        this.exchangeRates.push(created);
        return created;
      } catch (err: any) {
        this.error = err.message || "Failed to create exchange rate";
        throw err;
      } finally {
        this.loading = false;
      }
    },

    async update(id: number, payload: UpdateExchangeRateRequest) {
      this.loading = true;
      try {
        const updated = await exchangeRateService.update(id, payload);
        this.exchangeRates = this.exchangeRates.map((r) =>
          r.id === id ? updated : r
        );
        if (this.currentExchangeRate?.id === id) {
          this.currentExchangeRate = updated;
        }
        return updated;
      } catch (err: any) {
        this.error = err.message || "Failed to update exchange rate";
        throw err;
      } finally {
        this.loading = false;
      }
    },

    async delete(id: number) {
      this.loading = true;
      try {
        await exchangeRateService.delete(id);
        this.exchangeRates = this.exchangeRates.filter((r) => r.id !== id);
        if (this.currentExchangeRate?.id === id) {
          this.currentExchangeRate = null;
        }
      } catch (err: any) {
        this.error = err.message || "Failed to delete exchange rate";
        throw err;
      } finally {
        this.loading = false;
      }
    },
  },
});
