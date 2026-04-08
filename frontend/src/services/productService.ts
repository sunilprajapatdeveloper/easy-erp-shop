import api from "./api";
import { useUserStore } from "@/stores/userStore";
import type { Product } from "@/types/Product";

// Request/Response DTOs (keep aligned with backend)
export type CreateProductRequest = Omit<
  Product,
  "id" | "createdAt" | "updatedAt"
>;
export type UpdateProductRequest = Partial<CreateProductRequest>;
export type ProductResponse = Product;

// Helper: build headers
const getHeaders = () => {
  const userStore = useUserStore();
  const companyId = userStore.currentUser?.companyId;
  const userId = userStore.currentUser?.id;

  if (!companyId || !userId) {
    throw new Error("User or company information is missing.");
  }

  return {
    "X-Company-Id": companyId,
    "X-User-Id": userId,
  };
};

/**
 * Get products with optional filters
 */
export const getProducts = (params?: {
  warehouseId?: number;
  userId?: number;
  includePrice?: boolean;
  includeStock?: boolean;
  includeTax?: boolean;
}) =>
  api.get<ProductResponse[]>("/products", {
    headers: getHeaders(),
    params,
  });

export const getProductById = (id: number) =>
  api.get<ProductResponse>(`/products/${id}`, { headers: getHeaders() });

export const getProductByCode = (code: string) =>
  api.get<ProductResponse>(`/products/code/${code}`, { headers: getHeaders() });

export const getProductByBarcode = (barcode: string) =>
  api.get<ProductResponse>(`/products/barcode/${barcode}`, {
    headers: getHeaders(),
  });

export const createProduct = (data: CreateProductRequest) =>
  api.post<ProductResponse>("/products", data, { headers: getHeaders() });

export const updateProduct = (id: number, data: UpdateProductRequest) =>
  api.put<ProductResponse>(`/products/${id}`, data, { headers: getHeaders() });

export const deleteProduct = (id: number) =>
  api.delete<void>(`/products/${id}`, { headers: getHeaders() });

export const searchProducts = (query: string, page = 0, size = 20) =>
  api.get<ProductResponse[]>(`/products/search`, {
    headers: getHeaders(),
    params: { q: query, page, size },
  });
