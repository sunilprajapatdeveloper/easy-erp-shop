import { CreateDiscountRequest, DiscountResponse, UpdateDiscountRequest } from "@/types/Discount";
import api from "./api";
import type { PaginationRequest, PaginationResponse } from "@/types/pagination";

export interface ApiResponse<T> {
  success: boolean;
  message: string | null;
  data: T;
}

/** Get paginated discounts with optional filters */
export const getDiscountsPaginated = (
  params: PaginationRequest & {
    scope?: string;
    active?: boolean;
    warehouseId?: number;
  },
) =>
  api.get<ApiResponse<PaginationResponse<DiscountResponse>>>(
    "/api/v1/discounts",
    { params },
  );

/** Get single discount by ID */
export const getDiscountById = (id: number) =>
  api.get<ApiResponse<DiscountResponse>>(`/api/v1/discounts/${id}`);

/** Create discount */
export const createDiscount = (data: CreateDiscountRequest) =>
  api.post<ApiResponse<DiscountResponse>>("/api/v1/discounts", data);

/** Update discount */
export const updateDiscount = (id: number, data: UpdateDiscountRequest) =>
  api.put<ApiResponse<DiscountResponse>>(`/api/v1/discounts/${id}`, data);

/** Delete discount */
export const deleteDiscount = (id: number) =>
  api.delete<void>(`/api/v1/discounts/${id}`);

/** Toggle discount active status */
export const toggleDiscount = (id: number) =>
  api.patch<void>(`/api/v1/discounts/${id}/toggle`);

/**
 * Fetch all active order-level discounts for a warehouse.
 * Backend must support `scope`, `active` and `warehouseId` query parameters.
 * If not, we return all and filter on the frontend in the store.
 */
export const getActiveOrderDiscounts = (warehouseId: number) =>
  getDiscountsPaginated({
    page: 0,
    size: 100, // fetch many, will be filtered further in store
    scope: "ORDER",
    active: true,
    warehouseId,
  });
