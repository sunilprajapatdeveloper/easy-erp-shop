import { PaymentSourceType } from "@/enums/paymentSourceType";
import api from "./api";
import type {
  CreatePaymentRequest,
  UpdatePaymentRequest,
  PaymentResponse,
} from "@/types/Payment";

export interface ApiResponse<T> {
  success: boolean;
  message: string | null;
  data: T;
}

export const createPayment = (data: CreatePaymentRequest) =>
  api.post<ApiResponse<PaymentResponse>>("/payments", data);

export const updatePayment = (id: number, data: UpdatePaymentRequest) =>
  api.put<ApiResponse<PaymentResponse>>(`/payments/${id}`, data);

export const deletePayment = (id: number) =>
  api.delete<void>(`/payments/${id}`);

export const getPaymentById = (id: number) =>
  api.get<ApiResponse<PaymentResponse>>(`/payments/${id}`);

export const getPaymentsByReference = (
  type: PaymentSourceType,
  referenceId: number,
) =>
  api.get<ApiResponse<PaymentResponse[]>>("/payments/reference", {
    params: { type, referenceId },
  });

export const getPaymentStatus = (id: number) =>
  api.get<ApiResponse<{ status: string }>>(`/payments/${id}/status`);
