import { defineStore } from "pinia";
import { ref } from "vue";
import { useUserStore } from "./userStore";
import type {
  WarehouseCurrency,
  CreateWarehouseCurrencyRequest,
  UpdateWarehouseCurrencyRequest,
} from "@/types/WarehouseCurrency";
import { warehouseCurrencyService } from "@/services/warehouseCurrencyService";

export const useWarehouseCurrencyStore = defineStore(
  "warehouseCurrency",
  () => {
    const list = ref<WarehouseCurrency[]>([]);
    const current = ref<WarehouseCurrency | null>(null);
    const loading = ref(false);
    const error = ref<string | null>(null);

    const userStore = useUserStore();

    /**
     * Helper to get companyId
     */
    const resolveCompanyId = (companyId?: number) => {
      return companyId ?? userStore.currentUser?.companyId ?? 0;
    };

    /**
     * Fetch all warehouse currencies
     */
    const fetchAll = async (warehouseId: number, companyId?: number) => {
      try {
        loading.value = true;
        const resolvedCompanyId = resolveCompanyId(companyId);
        const response = await warehouseCurrencyService.list(
          resolvedCompanyId,
          warehouseId
        );
        list.value = response.data;
      } catch (err: any) {
        error.value = err.message || "Failed to fetch warehouse currencies";
      } finally {
        loading.value = false;
      }
    };

    /**
     * Fetch a single currency
     */
    const fetchOne = async (
      id: number,
      warehouseId: number,
      companyId?: number
    ) => {
      try {
        loading.value = true;
        const resolvedCompanyId = resolveCompanyId(companyId);
        const response = await warehouseCurrencyService.get(
          id,
          resolvedCompanyId,
          warehouseId
        );
        current.value = response.data;
      } catch (err: any) {
        error.value = err.message || "Failed to fetch currency";
      } finally {
        loading.value = false;
      }
    };

    /**
     * Fetch default warehouse currency
     */
    const fetchDefault = async (warehouseId: number, companyId?: number) => {
      try {
        loading.value = true;
        const resolvedCompanyId = resolveCompanyId(companyId);
        const response = await warehouseCurrencyService.getDefault(
          resolvedCompanyId,
          warehouseId
        );
        current.value = response.data;
        return response.data;
      } catch (err: any) {
        error.value = err.message || "Failed to fetch default currency";
        return null;
      } finally {
        loading.value = false;
      }
    };

    /**
     * Create a new warehouse currency
     */
    const create = async (
      warehouseId: number,
      payload: CreateWarehouseCurrencyRequest,
      companyId?: number
    ) => {
      try {
        const resolvedCompanyId = resolveCompanyId(companyId);
        const response = await warehouseCurrencyService.create(
          resolvedCompanyId,
          warehouseId,
          payload
        );
        list.value.push(response.data);
        return response.data;
      } catch (err: any) {
        error.value = err.message || "Failed to create currency";
        throw err;
      }
    };

    /**
     * Update an existing warehouse currency
     */
    const update = async (
      id: number,
      warehouseId: number,
      payload: UpdateWarehouseCurrencyRequest,
      companyId?: number
    ) => {
      try {
        const resolvedCompanyId = resolveCompanyId(companyId);
        const response = await warehouseCurrencyService.update(
          id,
          resolvedCompanyId,
          warehouseId,
          payload
        );

        const index = list.value.findIndex((item) => item.id === id);
        if (index !== -1) list.value[index] = response.data;
        if (current.value?.id === id) current.value = response.data;

        return response.data;
      } catch (err: any) {
        error.value = err.message || "Failed to update currency";
        throw err;
      }
    };

    /**
     * Delete a currency
     */
    const remove = async (
      id: number,
      warehouseId: number,
      companyId?: number
    ) => {
      try {
        const resolvedCompanyId = resolveCompanyId(companyId);
        await warehouseCurrencyService.delete(
          id,
          resolvedCompanyId,
          warehouseId
        );
        list.value = list.value.filter((item) => item.id !== id);
        if (current.value?.id === id) current.value = null;
      } catch (err: any) {
        error.value = err.message || "Failed to delete currency";
        throw err;
      }
    };

    const setSelectedCurrency = (currencyId: number) => {
      const selected = list.value.find((c) => c.currencyId === currencyId);
      if (selected) current.value = selected;
    };

    return {
      list,
      current,
      loading,
      error,
      fetchAll,
      fetchOne,
      fetchDefault,
      create,
      update,
      remove,
      setSelectedCurrency,
    };
  }
);
