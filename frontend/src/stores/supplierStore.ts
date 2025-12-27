import { defineStore } from "pinia";
import { ref, computed } from "vue";
import type {
  Supplier,
  CreateSupplierRequest,
  UpdateSupplierRequest,
} from "@/types/Supplier";
import {
  getSuppliers,
  getSupplierById,
  createSupplier,
  updateSupplier,
  deleteSupplier,
} from "@/services/supplierService";

export const useSupplierStore = defineStore("suppliers", () => {
  const suppliers = ref<Supplier[]>([]);
  const selectedSupplier = ref<Supplier | null>(null);
  const loading = ref(false);
  const isSubmitting = ref(false);

  const supplierMap = computed<Record<number, string>>(() => {
    const map: Record<number, string> = {};
    suppliers.value.forEach((s) => {
      if (s.id !== undefined) {
        map[s.id] = s.name;
      }
    });
    return map;
  });

  // Fetch all suppliers
  const fetchSuppliers = async (): Promise<Supplier[]> => {
    loading.value = true;
    try {
      const res = await getSuppliers();
      suppliers.value = res.data;
      return suppliers.value;
    } finally {
      loading.value = false;
    }
  };

  // Fetch supplier by ID
  const fetchSupplierById = async (id: number) => {
    loading.value = true;
    try {
      const res = await getSupplierById(id);
      selectedSupplier.value = res.data;
    } catch (error) {
      console.error("Failed to fetch supplier:", error);
      throw error;
    } finally {
      loading.value = false;
    }
  };

  // Add new supplier
  const addSupplier = async (
    data: CreateSupplierRequest,
    fetchAfter = true
  ) => {
    isSubmitting.value = true;
    try {
      await createSupplier(data);
      if (fetchAfter) await fetchSuppliers();
    } catch (error) {
      console.error("Failed to create supplier:", error);
      throw error;
    } finally {
      isSubmitting.value = false;
    }
  };

  // Update existing supplier
  const editSupplier = async (id: number, data: UpdateSupplierRequest) => {
    isSubmitting.value = true;
    try {
      const res = await updateSupplier(id, data);
      const index = suppliers.value.findIndex((s) => s.id === id);
      if (index !== -1) suppliers.value[index] = res.data;
    } catch (error) {
      console.error("Failed to update supplier:", error);
      throw error;
    } finally {
      isSubmitting.value = false;
    }
  };

  // Delete supplier
  const removeSupplier = async (id: number) => {
    try {
      await deleteSupplier(id);
      suppliers.value = suppliers.value.filter((s) => s.id !== id);
    } catch (error) {
      console.error("Failed to delete supplier:", error);
      throw error;
    }
  };

  return {
    suppliers,
    supplierMap,
    selectedSupplier,
    loading,
    isSubmitting,
    fetchSuppliers,
    fetchSupplierById,
    addSupplier,
    editSupplier,
    removeSupplier,
  };
});
