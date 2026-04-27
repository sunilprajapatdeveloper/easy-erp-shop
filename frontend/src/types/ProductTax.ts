import { TaxCategory } from "@/enums/TaxCategory";

export interface ProductTaxResponse {
  id: number;
  productId: number;
  warehouseId?: number;
  taxCode: string;
  taxName: string;
  taxCategory: TaxCategory;
  taxRate: number;
  isInclusive: boolean;
  isCompound: boolean;
  isActive: boolean;
}

export interface CreateProductTaxRequest {
  productId: number;
  warehouseId?: number;
  taxCode: string;
  taxName: string;
  taxCategory: TaxCategory;
  taxRate: number;
  isInclusive?: boolean;
  isCompound?: boolean;
  isActive?: boolean;
}

export interface UpdateProductTaxRequest {
  productId?: number;
  warehouseId?: number;
  taxCode?: string;
  taxName?: string;
  taxCategory?: TaxCategory;
  taxRate?: number;
  isInclusive?: boolean;
  isCompound?: boolean;
  isActive?: boolean;
}

// Convenience type
export type ProductTax = ProductTaxResponse;
