import { defineStore } from "pinia";
import { ref } from "vue";
import type { Sale, CreateSaleRequest, UpdateSaleRequest } from "@/types/Sale";
import {
  getSales,
  getSaleById,
  getAllSales,
  createSale,
  updateSale,
  deleteSale,
} from "@/services/saleService";

export const useSaleStore = defineStore("sales", () => {
  const sales = ref<Sale[]>([]);
  const selectedSale = ref<Sale | null>(null);
  const loading = ref(false);
  const isSubmitting = ref(false);

  const fetchSales = async () => {
    loading.value = true;
    try {
      const res = await getSales();
      sales.value = res.data;
    } catch (error) {
      console.error("Failed to fetch user sales:", error);
    } finally {
      loading.value = false;
    }
  };

  const fetchAllSales = async () => {
    loading.value = true;
    try {
      const res = await getAllSales();
      sales.value = res.data;
    } catch (error) {
      console.error("Failed to fetch company sales:", error);
    } finally {
      loading.value = false;
    }
  };

  const fetchSaleById = async (id: number) => {
    loading.value = true;
    try {
      const res = await getSaleById(id);
      selectedSale.value = res.data;
    } catch (error) {
      console.error("Failed to fetch sale:", error);
      throw error;
    } finally {
      loading.value = false;
    }
  };

  const addSale = async (data: CreateSaleRequest, fetchAfter = true) => {
    isSubmitting.value = true;
    try {
      await createSale(data);
      if (fetchAfter) await fetchSales();
    } catch (error) {
      console.error("Failed to create sale:", error);
      throw error;
    } finally {
      isSubmitting.value = false;
    }
  };

  const editSale = async (id: number, data: UpdateSaleRequest) => {
    isSubmitting.value = true;
    try {
      const res = await updateSale(id, data);
      const index = sales.value.findIndex((s) => s.id === id);
      if (index !== -1) {
        sales.value[index] = res.data;
      }
    } catch (error) {
      console.error("Failed to update sale:", error);
      throw error;
    } finally {
      isSubmitting.value = false;
    }
  };

  const removeSale = async (id: number) => {
    try {
      await deleteSale(id);
      sales.value = sales.value.filter((s) => s.id !== id);
    } catch (error) {
      console.error("Failed to delete sale:", error);
      throw error;
    }
  };

  return {
    sales,
    selectedSale,
    loading,
    isSubmitting,
    fetchSales,
    fetchAllSales,
    fetchSaleById,
    addSale,
    editSale,
    removeSale,
  };
});
