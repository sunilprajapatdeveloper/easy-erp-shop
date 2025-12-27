import { defineStore } from "pinia";
import type { Currency, CreateCurrencyRequest } from "@/types/Currency";
import {
  getCurrencies,
  createCurrency,
  updateCurrency,
  deleteCurrency,
} from "@/services/currencyService";

// Strongly typed state interface
interface CurrencyState {
  currencies: Currency[];
  loading: boolean;
  error: string | null;
}

export const useCurrencyStore = defineStore("currency", {
  state: (): CurrencyState => ({
    currencies: [],
    loading: false,
    error: null,
  }),

  actions: {
    async fetchCurrencies(): Promise<Currency[]> {
      this.loading = true;
      this.error = null;
      try {
        const res = await getCurrencies();
        this.currencies = res.data;
        return this.currencies;
      } catch (err: any) {
        this.error = err.message ?? "Failed to fetch currencies";
        return [];
      } finally {
        this.loading = false;
      }
    },

    async addCurrency(data: CreateCurrencyRequest) {
      this.loading = true;
      this.error = null;
      try {
        const res = await createCurrency(data);
        this.currencies.push(res.data);
        return res.data;
      } catch (err: any) {
        this.error = err.message ?? "Failed to add currency";
        throw err;
      } finally {
        this.loading = false;
      }
    },

    async updateCurrency(id: number, data: CreateCurrencyRequest) {
      this.loading = true;
      this.error = null;
      try {
        const res = await updateCurrency(id, data);
        const index = this.currencies.findIndex((c) => c.id === id);
        if (index !== -1) {
          this.currencies[index] = res.data;
        }
        return res.data;
      } catch (err: any) {
        this.error = err.message ?? "Failed to update currency";
        throw err;
      } finally {
        this.loading = false;
      }
    },

    async removeCurrency(id: number) {
      this.loading = true;
      this.error = null;
      try {
        await deleteCurrency(id);
        this.currencies = this.currencies.filter((c) => c.id !== id);
      } catch (err: any) {
        this.error = err.message ?? "Failed to delete currency";
        throw err;
      } finally {
        this.loading = false;
      }
    },
  },
});
