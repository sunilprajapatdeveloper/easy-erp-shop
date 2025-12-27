import api from "./api";
import type {
  ProductStock,
  ProductStockResponse,
  CreateProductStockRequest,
  UpdateProductStockRequest,
} from "@/types/ProductStock";
import { useUserStore } from "@/stores/userStore";

// Helper to get headers with company/user info
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

// ------------------ API Calls ------------------
export const getProductStocks = () =>
  api.get<ProductStockResponse[]>("/products/product-stocks", {
    headers: getHeaders(),
  });

export const getProductStockById = (id: number) =>
  api.get<ProductStockResponse>(`/products/product-stocks/${id}`, {
    headers: getHeaders(),
  });

export const getByProductAndWarehouse = (
  productId: number,
  warehouseId: number
) =>
  api.get<ProductStockResponse>(
    "/products/product-stocks/by-product-warehouse",
    {
      headers: getHeaders(),
      params: { productId, warehouseId },
    }
  );

export const createProductStock = (data: CreateProductStockRequest) =>
  api.post<ProductStockResponse>("/products/product-stocks", data, {
    headers: getHeaders(),
  });

export const updateProductStock = (
  id: number,
  data: UpdateProductStockRequest
) =>
  api.put<ProductStockResponse>(`/products/product-stocks/${id}`, data, {
    headers: getHeaders(),
  });

export const adjustProductStock = (
  productId: number,
  warehouseId: number,
  delta: number
) =>
  api.patch<ProductStockResponse>("/products/product-stocks/adjust", null, {
    headers: getHeaders(),
    params: { productId, warehouseId, delta },
  });

export const deleteProductStock = (id: number) =>
  api.delete<void>(`/products/product-stocks/${id}`, {
    headers: getHeaders(),
  });
