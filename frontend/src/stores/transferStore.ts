import { defineStore } from "pinia";
import { ref } from "vue";
import type { Transfer, CreateTransferRequest } from "@/types/Transfer";
import {
  getTransfers,
  createTransfer,
  updateTransfer,
  deleteTransfer,
} from "@/services/transferService";

export const useTransferStore = defineStore("transfers", () => {
  const transfers = ref<Transfer[]>([]);
  const loading = ref(false);
  const isSubmitting = ref(false);

  const fetchTransfers = async () => {
    loading.value = true;
    try {
      const res = await getTransfers();
      transfers.value = res.data;
    } catch (error) {
      console.error("Failed to fetch transfers:", error);
    } finally {
      loading.value = false;
    }
  };

  const addTransfer = async (
    data: CreateTransferRequest,
    fetchAfter = true
  ) => {
    isSubmitting.value = true;
    try {
      await createTransfer(data);
      if (fetchAfter) {
        await fetchTransfers();
      }
    } catch (error) {
      console.error("Failed to create transfer:", error);
      throw error;
    } finally {
      isSubmitting.value = false;
    }
  };

  const editTransfer = async (id: number, data: CreateTransferRequest) => {
    isSubmitting.value = true;
    try {
      const res = await updateTransfer(id, data);
      const index = transfers.value.findIndex((t) => t.id === id);
      if (index !== -1) {
        transfers.value[index] = res.data;
      }
    } catch (error) {
      console.error("Failed to update transfer:", error);
    } finally {
      isSubmitting.value = false;
    }
  };

  const removeTransfer = async (id: number) => {
    await deleteTransfer(id);
    transfers.value = transfers.value.filter((t) => t.id !== id);
  };

  return {
    transfers,
    loading,
    isSubmitting,
    fetchTransfers,
    addTransfer,
    editTransfer,
    removeTransfer,
  };
});
