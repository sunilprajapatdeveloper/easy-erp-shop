import api from "./api";
import type {
  ProductPrice,
  ProductPriceResponse,
  CreateProductPriceRequest,
  UpdateProductPriceRequest,
} from "@/types/ProductPrice";
import { useUserStore } from "@/stores/userStore";

const getHeaders = () => {
  const userStore = useUserStore();
  const companyId = userStore.currentUser?.companyId;
  const userId = userStore.currentUser?.id;
  if (!companyId || !userId)
    throw new Error("User or company information is missing.");
  return { "X-Company-Id": companyId, "X-User-Id": userId };
};

export const getProductPrices = () =>
  api.get<ProductPriceResponse[]>("/products/product-prices", {
    headers: getHeaders(),
  });

export const getProductPriceById = (id: number) =>
  api.get<ProductPriceResponse>(`/products/product-prices/${id}`, {
    headers: getHeaders(),
  });

export const getPricesByProduct = (productId: number) =>
  api.get<ProductPriceResponse[]>(
    `/products/product-prices/product/${productId}`,
    { headers: getHeaders() }
  );

export const getPricesByWarehouse = (warehouseId: number) =>
  api.get<ProductPriceResponse[]>(
    `/products/product-prices/warehouse/${warehouseId}`,
    { headers: getHeaders() }
  );

export const getByProductAndWarehouse = (
  productId: number,
  warehouseId: number
) =>
  api.get<ProductPriceResponse>(
    "/products/product-prices/by-product-warehouse",
    {
      headers: getHeaders(),
      params: { productId, warehouseId },
    }
  );

export const createProductPrice = (data: CreateProductPriceRequest) =>
  api.post<ProductPriceResponse>("/products/product-prices", data, {
    headers: getHeaders(),
  });

export const updateProductPrice = (
  id: number,
  data: UpdateProductPriceRequest
) =>
  api.put<ProductPriceResponse>(`/products/product-prices/${id}`, data, {
    headers: getHeaders(),
  });

export const deleteProductPrice = (id: number) =>
  api.delete<void>(`/products/product-prices/${id}`, { headers: getHeaders() });
