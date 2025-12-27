import { defineStore } from "pinia";
import { ref } from "vue";
import type { Adjustment, CreateAdjustmentRequest } from "@/types/Adjustment";
import {
  createAdjustment,
  getAdjustments,
  updateAdjustment,
  deleteAdjustment,
} from "@/services/adjustmentService";

export const useAdjustmentStore = defineStore("adjustments", () => {
  const adjustments = ref<Adjustment[]>([]);
  const loading = ref(false);
  const isSubmitting = ref(false);

  const fetchAdjustments = async () => {
    loading.value = true;
    try {
      const res = await getAdjustments();
      adjustments.value = res.data;
    } finally {
      loading.value = false;
    }
  };

  const addAdjustment = async (data: CreateAdjustmentRequest) => {
    isSubmitting.value = true;
    try {
      await createAdjustment(data);
      await fetchAdjustments(); // refresh list after creation
    } finally {
      isSubmitting.value = false;
    }
  };

  const editAdjustment = async (id: number, data: CreateAdjustmentRequest) => {
    isSubmitting.value = true;
    try {
      const res = await updateAdjustment(id, data);
      const index = adjustments.value.findIndex((a) => a.id === id);
      if (index !== -1) adjustments.value[index] = res.data;
    } finally {
      isSubmitting.value = false;
    }
  };

  const removeAdjustment = async (id: number) => {
    await deleteAdjustment(id);
    adjustments.value = adjustments.value.filter((a) => a.id !== id);
  };

  return {
    adjustments,
    loading,
    isSubmitting,
    fetchAdjustments,
    addAdjustment,
    editAdjustment,
    removeAdjustment,
  };
});
