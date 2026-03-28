import api from "./api";
import type {
  ProductTaxResponse,
  CreateProductTaxRequest,
  UpdateProductTaxRequest,
} from "@/types/ProductTax";
import { useUserStore } from "@/stores/userStore";

const getHeaders = () => {
  const userStore = useUserStore();
  const companyId = userStore.currentUser?.companyId;
  const userId = userStore.currentUser?.id;
  if (!companyId || !userId) {
    throw new Error("User or company information is missing.");
  }
  return { "X-Company-Id": companyId, "X-User-Id": userId };
};

// Get all taxes
export const getAllProductTaxes = () =>
  api.get<ProductTaxResponse[]>("/products/product-taxes", {
    headers: getHeaders(),
  });

// Get tax by ID
export const getProductTaxById = (taxId: number) =>
  api.get<ProductTaxResponse>(`/products/product-taxes/${taxId}`, {
    headers: getHeaders(),
  });

// Get taxes by product
export const getTaxesByProduct = (productId: number) =>
  api.get<ProductTaxResponse[]>(
    `/products/product-taxes/product/${productId}`,
    {
      headers: getHeaders(),
    },
  );

// Get taxes by warehouse
export const getTaxesByWarehouse = (warehouseId: number) =>
  api.get<ProductTaxResponse[]>(
    `/products/product-taxes/warehouse/${warehouseId}`,
    {
      headers: getHeaders(),
    },
  );

// Create tax
export const createProductTax = (data: CreateProductTaxRequest) =>
  api.post<ProductTaxResponse>("/products/product-taxes", data, {
    headers: getHeaders(),
  });

// Update tax
export const updateProductTax = (
  taxId: number,
  data: UpdateProductTaxRequest,
) =>
  api.put<ProductTaxResponse>(`/products/product-taxes/${taxId}`, data, {
    headers: getHeaders(),
  });

// Delete tax
export const deleteProductTax = (taxId: number) =>
  api.delete<void>(`/products/product-taxes/${taxId}`, {
    headers: getHeaders(),
  });

// Get effective tax for product (optionally by warehouse and taxCode)
export const getEffectiveTax = (
  productId: number,
  taxCode: string,
  warehouseId?: number,
) =>
  api.get<ProductTaxResponse>("/products/product-taxes/effective", {
    headers: getHeaders(),
    params: { productId, warehouseId, taxCode },
  });
