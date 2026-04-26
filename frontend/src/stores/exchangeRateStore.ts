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
  rateCache: Map<string, ExchangeRateDetail>;
}

export const useExchangeRateStore = defineStore("exchangeRate", {
  state: (): ExchangeRateState => ({
    exchangeRates: [],
    currentExchangeRate: null,
    loading: false,
    error: null,
    rateCache: new Map(),
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
        this.rateCache.clear();
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
          r.id === id ? updated : r,
        );
        if (this.currentExchangeRate?.id === id) {
          this.currentExchangeRate = updated;
        }
        this.rateCache.clear();
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
        this.rateCache.clear();
      } catch (err: any) {
        this.error = err.message || "Failed to delete exchange rate";
        throw err;
      } finally {
        this.loading = false;
      }
    },

    /**
     * Find the best matching exchange rate for a currency pair within a scope.
     * Caches results by a key composed of baseCurrencyId_targetCurrencyId_companyId_warehouseId.
     *
     * @param baseCurrencyId - Product's original currency ID
     * @param targetCurrencyId - Sale currency ID
     * @param companyId - Company ID (must be provided)
     * @param warehouseId - Warehouse ID (optional, used for hierarchy)
     * @returns ExchangeRateDetail
     */
    async fetchRateByCurrencies(
      baseCurrencyId: number,
      targetCurrencyId: number,
      companyId: number,
      warehouseId?: number,
    ): Promise<ExchangeRateDetail> {
      const cacheKey = `${baseCurrencyId}_${targetCurrencyId}_${companyId}_${
        warehouseId ?? "null"
      }`;
      const cached = this.rateCache.get(cacheKey);
      if (cached) {
        return cached;
      }

      this.loading = true;
      try {
        const rate = await exchangeRateService.find({
          baseCurrencyId,
          targetCurrencyId,
          companyId,
          warehouseId,
        });
        this.rateCache.set(cacheKey, rate);
        return rate;
      } catch (err: any) {
        this.error = err.message || "Failed to fetch exchange rate";
        throw err;
      } finally {
        this.loading = false;
      }
    },

    /**
     * Clear the entire rate cache (useful after rate updates or logout)
     */
    clearRateCache() {
      this.rateCache.clear();
    },
  },
});
