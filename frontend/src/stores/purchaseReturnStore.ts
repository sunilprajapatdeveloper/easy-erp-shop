import { defineStore } from "pinia";
import { ref } from "vue";
import type {
  PurchaseReturn,
  CreatePurchaseReturnRequest,
  UpdatePurchaseReturnRequest,
} from "@/types/PurchaseReturn";
import {
  getPurchaseReturns,
  getPurchaseReturnById,
  getAllPurchaseReturns,
  createPurchaseReturn,
  updatePurchaseReturn,
  deletePurchaseReturn,
} from "@/services/purchaseReturnService";

export const usePurchaseReturnStore = defineStore("purchaseReturns", () => {
  const purchaseReturns = ref<PurchaseReturn[]>([]);
  const selectedPurchaseReturn = ref<PurchaseReturn | null>(null);
  const loading = ref(false);
  const isSubmitting = ref(false);

  // Fetch all purchase returns (user scoped)
  const fetchPurchaseReturns = async () => {
    loading.value = true;
    try {
      const res = await getPurchaseReturns();
      purchaseReturns.value = res.data;
    } catch (error) {
      console.error("Failed to fetch purchase returns:", error);
    } finally {
      loading.value = false;
    }
  };

  // Fetch all purchase returns (company scoped)
  const fetchAllPurchaseReturns = async () => {
    loading.value = true;
    try {
      const res = await getAllPurchaseReturns();
      purchaseReturns.value = res.data;
    } catch (error) {
      console.error("Failed to fetch company purchase returns:", error);
    } finally {
      loading.value = false;
    }
  };

  // Fetch a single purchase return by ID
  const fetchPurchaseReturnById = async (
    id: number
  ): Promise<PurchaseReturn | null> => {
    loading.value = true;
    try {
      const res = await getPurchaseReturnById(id);
      selectedPurchaseReturn.value = res.data;
      return res.data;
    } catch (error) {
      console.error("Failed to fetch purchase return:", error);
      return null;
    } finally {
      loading.value = false;
    }
  };

  // Create new purchase return
  const addPurchaseReturn = async (
    data: CreatePurchaseReturnRequest,
    fetchAfter = true
  ) => {
    isSubmitting.value = true;
    try {
      await createPurchaseReturn(data);
      if (fetchAfter) await fetchPurchaseReturns();
    } catch (error) {
      console.error("Failed to create purchase return:", error);
      throw error;
    } finally {
      isSubmitting.value = false;
    }
  };

  // Update purchase return
  const editPurchaseReturn = async (
    id: number,
    data: UpdatePurchaseReturnRequest
  ) => {
    isSubmitting.value = true;
    try {
      const res = await updatePurchaseReturn(id, data);
      const index = purchaseReturns.value.findIndex((pr) => pr.id === id);
      if (index !== -1) {
        purchaseReturns.value[index] = res.data;
      }
    } catch (error) {
      console.error("Failed to update purchase return:", error);
      throw error;
    } finally {
      isSubmitting.value = false;
    }
  };

  // Delete purchase return
  const removePurchaseReturn = async (id: number) => {
    try {
      await deletePurchaseReturn(id);
      purchaseReturns.value = purchaseReturns.value.filter(
        (pr) => pr.id !== id
      );
    } catch (error) {
      console.error("Failed to delete purchase return:", error);
      throw error;
    }
  };

  return {
    purchaseReturns,
    selectedPurchaseReturn,
    loading,
    isSubmitting,
    fetchPurchaseReturns,
    fetchAllPurchaseReturns,
    fetchPurchaseReturnById,
    addPurchaseReturn,
    editPurchaseReturn,
    removePurchaseReturn,
  };
});
