import { defineStore } from "pinia";
import { ref } from "vue";
import type { Purchase, CreatePurchaseRequest } from "@/types/Purchase";
import {
  getPurchases,
  getPurchaseById,
  getAllPurchases,
  createPurchase,
  updatePurchase,
  deletePurchase,
} from "@/services/purchaseService";

export const usePurchaseStore = defineStore("purchases", () => {
  const purchases = ref<Purchase[]>([]);
  const selectedPurchase = ref<Purchase | null>(null);
  const loading = ref(false);
  const isSubmitting = ref(false);

  const fetchPurchases = async () => {
    loading.value = true;
    try {
      const res = await getPurchases();
      purchases.value = res.data;
    } catch (error) {
      console.error("Failed to fetch user purchases:", error);
    } finally {
      loading.value = false;
    }
  };

  const fetchAllPurchases = async () => {
    loading.value = true;
    try {
      const res = await getAllPurchases();
      purchases.value = res.data;
    } catch (error) {
      console.error("Failed to fetch company purchases:", error);
    } finally {
      loading.value = false;
    }
  };

  const fetchPurchaseById = async (id: number) => {
    loading.value = true;
    try {
      const res = await getPurchaseById(id);
      selectedPurchase.value = res.data;
    } catch (error) {
      console.error("Failed to fetch purchase:", error);
      throw error;
    } finally {
      loading.value = false;
    }
  };

  const addPurchase = async (
    data: CreatePurchaseRequest,
    fetchAfter = true
  ) => {
    isSubmitting.value = true;
    try {
      await createPurchase(data);
      if (fetchAfter) await fetchPurchases();
    } catch (error) {
      console.error("Failed to create purchase:", error);
      throw error;
    } finally {
      isSubmitting.value = false;
    }
  };

  const editPurchase = async (id: number, data: CreatePurchaseRequest) => {
    isSubmitting.value = true;
    try {
      const res = await updatePurchase(id, data);
      const index = purchases.value.findIndex((p) => p.id === id);
      if (index !== -1) {
        purchases.value[index] = res.data;
      }
    } catch (error) {
      console.error("Failed to update purchase:", error);
      throw error;
    } finally {
      isSubmitting.value = false;
    }
  };

  const removePurchase = async (id: number) => {
    try {
      await deletePurchase(id);
      purchases.value = purchases.value.filter((p) => p.id !== id);
    } catch (error) {
      console.error("Failed to delete purchase:", error);
      throw error;
    }
  };

  return {
    purchases,
    selectedPurchase,
    loading,
    isSubmitting,
    fetchPurchases,
    fetchAllPurchases,
    fetchPurchaseById,
    addPurchase,
    editPurchase,
    removePurchase,
  };
});
