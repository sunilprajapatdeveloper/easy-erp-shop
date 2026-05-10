import api from "./api";
import type {
  CreatePromotionRequest,
  UpdatePromotionRequest,
  PromotionResponse,
  CouponValidationRequest,
  CouponValidationResponse,
} from "@/types/promotion";
import type { PaginationRequest, PaginationResponse } from "@/types/pagination";

export interface ApiResponse<T> {
  success: boolean;
  message: string | null;
  data: T;
}

// Get paginated promotions
export const getPromotionsPaginated = (params: PaginationRequest) =>
  api.get<ApiResponse<PaginationResponse<PromotionResponse>>>(
    "/api/v1/promotions",
    { params },
  );

// Get single promotion by ID
export const getPromotionById = (id: number) =>
  api.get<ApiResponse<PromotionResponse>>(`/api/v1/promotions/${id}`);

// Create promotion
export const createPromotion = (data: CreatePromotionRequest) =>
  api.post<ApiResponse<PromotionResponse>>("/api/v1/promotions", data);

// Update promotion
export const updatePromotion = (id: number, data: UpdatePromotionRequest) =>
  api.put<ApiResponse<PromotionResponse>>(`/api/v1/promotions/${id}`, data);

// Delete promotion
export const deletePromotion = (id: number) =>
  api.delete<void>(`/api/v1/promotions/${id}`);

// Toggle promotion active status
export const togglePromotion = (id: number) =>
  api.patch<void>(`/api/v1/promotions/${id}/toggle`);

// Validate coupon (check discount)
export const validateCoupon = (data: CouponValidationRequest) =>
  api.post<ApiResponse<CouponValidationResponse>>(
    "/api/v1/promotions/validate",
    data,
  );
