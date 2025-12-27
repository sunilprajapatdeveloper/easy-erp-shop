import { defineStore } from "pinia";
import { ref } from "vue";
import type {
  Expenses,
  CreateExpensesRequest,
  UpdateExpensesRequest,
} from "@/types/Expenses";
import {
  getExpenses,
  getExpenseById,
  getAllExpenses,
  createExpense,
  updateExpense,
  deleteExpense,
} from "@/services/expensesService";

export const useExpensesStore = defineStore("expenses", () => {
  const expenses = ref<Expenses[]>([]);
  const selectedExpense = ref<Expenses | null>(null);
  const loading = ref(false);
  const isSubmitting = ref(false);

  // Fetch all expenses (user scoped)
  const fetchExpenses = async () => {
    loading.value = true;
    try {
      const res = await getExpenses();
      expenses.value = res.data;
    } catch (error) {
      console.error("Failed to fetch user expenses:", error);
    } finally {
      loading.value = false;
    }
  };

  // Fetch all expenses (company scoped)
  const fetchAllExpenses = async () => {
    loading.value = true;
    try {
      const res = await getAllExpenses();
      expenses.value = res.data;
    } catch (error) {
      console.error("Failed to fetch company expenses:", error);
    } finally {
      loading.value = false;
    }
  };

  // Fetch single expense by ID
  const fetchExpenseById = async (id: number) => {
    loading.value = true;
    try {
      const res = await getExpenseById(id);
      selectedExpense.value = res.data;
    } catch (error) {
      console.error("Failed to fetch expense:", error);
      throw error;
    } finally {
      loading.value = false;
    }
  };

  // Create expense
  const addExpense = async (data: CreateExpensesRequest, fetchAfter = true) => {
    isSubmitting.value = true;
    try {
      await createExpense(data);
      if (fetchAfter) await fetchExpenses();
    } catch (error) {
      console.error("Failed to create expense:", error);
      throw error;
    } finally {
      isSubmitting.value = false;
    }
  };

  // Update expense
  const editExpense = async (id: number, data: UpdateExpensesRequest) => {
    isSubmitting.value = true;
    try {
      const res = await updateExpense(id, data);
      const index = expenses.value.findIndex((e) => e.id === id);
      if (index !== -1) {
        expenses.value[index] = res.data;
      }
    } catch (error) {
      console.error("Failed to update expense:", error);
      throw error;
    } finally {
      isSubmitting.value = false;
    }
  };

  // Delete expense
  const removeExpense = async (id: number) => {
    try {
      await deleteExpense(id);
      expenses.value = expenses.value.filter((e) => e.id !== id);
    } catch (error) {
      console.error("Failed to delete expense:", error);
      throw error;
    }
  };

  return {
    expenses,
    selectedExpense,
    loading,
    isSubmitting,
    fetchExpenses,
    fetchAllExpenses,
    fetchExpenseById,
    addExpense,
    editExpense,
    removeExpense,
  };
});
