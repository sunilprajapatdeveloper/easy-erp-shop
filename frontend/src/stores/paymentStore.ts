import { defineStore } from "pinia";
import { ref } from "vue";
import type {
  Payment,
  CreatePaymentRequest,
  UpdatePaymentRequest,
} from "@/types/Payment";
import {
  getPaymentsByReference,
  createPayment,
  updatePayment,
  deletePayment,
  getPaymentById,
  getPaymentStatus,
} from "@/services/paymentService";

export const usePaymentStore = defineStore("payments", () => {
  const payments = ref<Payment[]>([]);
  const selectedPayment = ref<Payment | null>(null);
  const tempPayment = ref<Payment | null>(null);
  const loading = ref(false);
  const isSubmitting = ref(false);

  let pollingInterval: ReturnType<typeof setInterval> | null = null;

  /** Fetch payments for a specific reference type and ID */
  const fetchPaymentsForReference = async (
    referenceType: string,
    referenceId: number
  ) => {
    loading.value = true;
    try {
      const res = await getPaymentsByReference(referenceType, referenceId);
      payments.value = res.data;
    } catch (error) {
      console.error("Failed to fetch payments:", error);
    } finally {
      loading.value = false;
    }
  };

  /** Fetch single payment by ID */
  const fetchPaymentById = async (id: number) => {
    loading.value = true;
    try {
      const res = await getPaymentById(id);
      selectedPayment.value = res.data;
    } catch (error) {
      console.error("Failed to fetch payment:", error);
    } finally {
      loading.value = false;
    }
  };

  /** Create a new payment */
  const addPayment = async (data: CreatePaymentRequest) => {
    isSubmitting.value = true;
    try {
      // Just pass data as-is; let the API layer handle serialization if needed
      const res = await createPayment(data);
      payments.value.push(res.data);
      tempPayment.value = res.data;
      localStorage.setItem("lastPayment", JSON.stringify(res.data));
      return res.data;
    } catch (error) {
      console.error("Failed to create payment:", error);
      throw error;
    } finally {
      isSubmitting.value = false;
    }
  };

  /** Update an existing payment */
  const editPayment = async (id: number, data: UpdatePaymentRequest) => {
    isSubmitting.value = true;
    try {
      // Serialize paymentData if paymentMetadata is present
      if (data.paymentData && typeof data.paymentData !== "string") {
        data.paymentData = JSON.stringify(data.paymentData);
      }

      const res = await updatePayment(id, data);
      const index = payments.value.findIndex((p) => p.id === id);
      if (index !== -1) payments.value[index] = res.data;

      if (selectedPayment.value?.id === id) selectedPayment.value = res.data;
      return res.data;
    } catch (error) {
      console.error("Failed to update payment:", error);
      throw error;
    } finally {
      isSubmitting.value = false;
    }
  };

  /** Delete a payment */
  const removePayment = async (id: number) => {
    try {
      await deletePayment(id);
      payments.value = payments.value.filter((p) => p.id !== id);
      if (selectedPayment.value?.id === id) selectedPayment.value = null;
    } catch (error) {
      console.error("Failed to delete payment:", error);
      throw error;
    }
  };

  /** Temp payment helper */
  const setTempPayment = (payment: Payment | null) => {
    tempPayment.value = payment;
  };
  const clearTempPayment = () => {
    tempPayment.value = null;
  };

  /** Poll payment status until PAID */
  const startPollingPaymentStatus = (
    id: number,
    onPaid: (status: string) => void
  ) => {
    stopPollingPaymentStatus();
    pollingInterval = setInterval(async () => {
      try {
        const res = await getPaymentStatus(id);
        if (res.data.status === "PAID") {
          onPaid(res.data.status);
          stopPollingPaymentStatus();
        }
      } catch (error) {
        console.error("Polling payment status failed:", error);
      }
    }, 2000);
  };

  const stopPollingPaymentStatus = () => {
    if (pollingInterval) {
      clearInterval(pollingInterval);
      pollingInterval = null;
    }
  };

  return {
    payments,
    selectedPayment,
    tempPayment,
    loading,
    isSubmitting,
    fetchPaymentsForReference,
    fetchPaymentById,
    addPayment,
    editPayment,
    removePayment,
    setTempPayment,
    clearTempPayment,
    startPollingPaymentStatus,
    stopPollingPaymentStatus,
  };
});
