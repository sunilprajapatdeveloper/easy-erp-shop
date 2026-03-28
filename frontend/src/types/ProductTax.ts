import { TaxType } from "@/enums/TaxType";

export interface ProductTaxResponse {
  id: number;
  productId: number;
  warehouseId?: number;
  taxCode: string;
  taxName: string;
  taxType: TaxType;
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
  taxType: TaxType;
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
  taxType?: TaxType;
  taxRate?: number;
  isInclusive?: boolean;
  isCompound?: boolean;
  isActive?: boolean;
}

// Convenience type
export type ProductTax = ProductTaxResponse;
