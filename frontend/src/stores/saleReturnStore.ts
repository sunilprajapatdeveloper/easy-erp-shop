import { defineStore } from "pinia";
import { ref } from "vue";
import type { SaleReturn, CreateSaleReturnRequest, UpdateSaleReturnRequest } from "@/types/saleReturn";
import {
  getSaleReturns,
  getSaleReturnById,
  getAllSaleReturns,
  createSaleReturn,
  updateSaleReturn,
  deleteSaleReturn,
} from "@/services/saleReturnService";

export const useSaleReturnStore = defineStore("saleReturn", () => {
  const saleReturns = ref<SaleReturn[]>([]);
  const selectedSaleReturn = ref<SaleReturn | null>(null);
  const loading = ref(false);
  const isSubmitting = ref(false);

  const fetchSaleReturns = async () => {
    loading.value = true;
    try {
      const res = await getSaleReturns();
      saleReturns.value = res.data;
    } catch (error) {
      console.error("Failed to fetch user sale returns:", error);
    } finally {
      loading.value = false;
    }
  };

  const fetchAllSaleReturns = async () => {
    loading.value = true;
    try {
      const res = await getAllSaleReturns();
      saleReturns.value = res.data;
    } catch (error) {
      console.error("Failed to fetch company sale returns:", error);
    } finally {
      loading.value = false;
    }
  };

  const fetchSaleReturnById = async (id: number): Promise<SaleReturn | null> => {
    loading.value = true;
    try {
      const res = await getSaleReturnById(id);
      selectedSaleReturn.value = res.data;
      return res.data;
    } catch (error) {
      console.error("Failed to fetch sale return:", error);
      throw error;
    } finally {
      loading.value = false;
    }
  };

  const addSaleReturn = async (
    data: CreateSaleReturnRequest,
    fetchAfter = true
  ) => {
    isSubmitting.value = true;
    try {
      await createSaleReturn(data);
      if (fetchAfter) await fetchSaleReturns();
    } catch (error) {
      console.error("Failed to create sale return:", error);
      throw error;
    } finally {
      isSubmitting.value = false;
    }
  };

  const editSaleReturn = async (id: number, data: UpdateSaleReturnRequest): Promise<SaleReturn> => {
    isSubmitting.value = true;
    try {
      const res = await updateSaleReturn(id, data);
      const index = saleReturns.value.findIndex((s) => s.id === id);
      if (index !== -1) {
        saleReturns.value[index] = res.data;
      }
      // update selectedSaleReturn if editing currently selected
      if (selectedSaleReturn.value?.id === id) {
        selectedSaleReturn.value = res.data;
      }
      return res.data;
    } catch (error) {
      console.error("Failed to update sale return:", error);
      throw error;
    } finally {
      isSubmitting.value = false;
    }
  };

  const removeSaleReturn = async (id: number) => {
    try {
      await deleteSaleReturn(id);
      saleReturns.value = saleReturns.value.filter((s) => s.id !== id);
    } catch (error) {
      console.error("Failed to delete sale return:", error);
      throw error;
    }
  };

  return {
    saleReturns,
    selectedSaleReturn,
    loading,
    isSubmitting,
    fetchSaleReturns,
    fetchAllSaleReturns,
    fetchSaleReturnById,
    addSaleReturn,
    editSaleReturn,
    removeSaleReturn,
  };
});
