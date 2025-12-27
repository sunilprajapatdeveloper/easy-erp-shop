import api from "./api";
import type {
  Payment,
  CreatePaymentRequest,
  UpdatePaymentRequest,
} from "@/types/Payment";

/**
 * Get all payments by reference
 * @param type PaymentSourceType (SALE, PURCHASE, etc.)
 * @param referenceId Entity ID (Sale.id, Purchase.id, etc.)
 */
export const getPaymentsByReference = (type: string, referenceId: number) =>
  api.get<Payment[]>(
    `/payments/reference?type=${type}&referenceId=${referenceId}`
  );

/**
 * Create a new payment
 */
export const createPayment = (data: CreatePaymentRequest) =>
  api.post<Payment>("/payments", data);

/**
 * Update an existing payment
 */
export const updatePayment = (id: number, data: UpdatePaymentRequest) =>
  api.put<Payment>(`/payments/${id}`, data);

/**
 * Delete a payment
 */
export const deletePayment = (id: number) =>
  api.delete<void>(`/payments/${id}`);

/**
 * Get payment by ID
 */
export const getPaymentById = (id: number) =>
  api.get<Payment>(`/payments/${id}`);

/**
 * Get payment status only
 */
export const getPaymentStatus = (id: number) =>
  api.get<{ status: string }>(`/payments/${id}/status`);
