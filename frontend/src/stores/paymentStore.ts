import { defineStore } from "pinia";
import { ref } from "vue";
import type {
  PaymentResponse,
  CreatePaymentRequest,
  UpdatePaymentRequest,
} from "@/types/Payment";
import { PaymentStatus } from "@/enums/paymentStatus";
import {
  getPaymentsByReference,
  createPayment,
  updatePayment,
  deletePayment,
  getPaymentById,
  getPaymentStatus,
} from "@/services/paymentService";
import { PaymentSourceType } from "@/enums/paymentSourceType";

export const usePaymentStore = defineStore("payments", () => {
  const payments = ref<PaymentResponse[]>([]);
  const selectedPayment = ref<PaymentResponse | null>(null);
  const tempPayment = ref<PaymentResponse | null>(null);
  const loading = ref(false);
  const isSubmitting = ref(false);

  let pollingInterval: ReturnType<typeof setInterval> | null = null;

  /** Fetch payments for a specific reference type and ID */
  const fetchPaymentsForReference = async (
    referenceType: PaymentSourceType,
    referenceId: number,
  ) => {
    loading.value = true;
    try {
      const res = await getPaymentsByReference(referenceType, referenceId);
      payments.value = res.data.data; // API response is wrapped in ApiResponse
    } catch (error) {
      console.error("Failed to fetch payments:", error);
      throw error;
    } finally {
      loading.value = false;
    }
  };

  /** Fetch single payment by ID */
  const fetchPaymentById = async (id: number) => {
    loading.value = true;
    try {
      const res = await getPaymentById(id);
      selectedPayment.value = res.data.data;
      return selectedPayment.value;
    } catch (error) {
      console.error("Failed to fetch payment:", error);
      throw error;
    } finally {
      loading.value = false;
    }
  };

  /** Create a new payment */
  const addPayment = async (
    data: CreatePaymentRequest,
  ): Promise<PaymentResponse> => {
    isSubmitting.value = true;
    try {
      // Make sure paymentMetadata is a string (JSON) if it exists as an object
      if (data.paymentMetadata && typeof data.paymentMetadata !== "string") {
        data.paymentMetadata = JSON.stringify(data.paymentMetadata);
      }

      // Generate an idempotency key if not provided
      if (!data.idempotencyKey) {
        data.idempotencyKey = `${data.referenceType}-${
          data.referenceId
        }-${Date.now()}`;
      }

      const res = await createPayment(data);
      const newPayment = res.data.data;

      // Add to list only if it belongs to the same reference we're currently viewing
      if (
        payments.value.length === 0 ||
        (payments.value[0].referenceType === newPayment.referenceType &&
          payments.value[0].referenceId === newPayment.referenceId)
      ) {
        payments.value.push(newPayment);
      }

      tempPayment.value = newPayment;
      localStorage.setItem("lastPayment", JSON.stringify(newPayment));
      return newPayment;
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
      const res = await updatePayment(id, data);
      const updated = res.data.data;

      const index = payments.value.findIndex((p) => p.id === id);
      if (index !== -1) payments.value[index] = updated;
      if (selectedPayment.value?.id === id) selectedPayment.value = updated;
      return updated;
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
      if (tempPayment.value?.id === id) tempPayment.value = null;
    } catch (error) {
      console.error("Failed to delete payment:", error);
      throw error;
    }
  };

  /** Temp payment helpers */
  const setTempPayment = (payment: PaymentResponse | null) => {
    tempPayment.value = payment;
  };
  const clearTempPayment = () => {
    tempPayment.value = null;
  };

  /** Poll payment status until it reaches a target status (default "PAID") */
  const startPollingPaymentStatus = (
    id: number,
    onComplete: (status: string) => void,
    targetStatus: PaymentStatus = PaymentStatus.PAID,
  ) => {
    stopPollingPaymentStatus();
    pollingInterval = setInterval(async () => {
      try {
        const res = await getPaymentStatus(id);
        const status = res.data.data.status;
        if (status === targetStatus) {
          onComplete(status);
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
