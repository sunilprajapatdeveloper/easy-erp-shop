import { TaxApplicationOrder } from "@/enums/TaxApplicationOrder";
import { TaxCategory } from "@/enums/TaxCategory";
import { TaxInclusionType } from "@/enums/TaxInclusionType";

export interface ProductTaxResponse {
  id: number;
  productId: number;
  warehouseId?: number;
  taxCode: string;
  taxName: string;
  taxCategory: TaxCategory;
  taxRate: number;
  overrideInclusionType?: TaxInclusionType;
  overrideApplicationOrder?: TaxApplicationOrder;
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
  overrideInclusionType?: TaxInclusionType;
  overrideApplicationOrder?: TaxApplicationOrder;
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
  overrideInclusionType?: TaxInclusionType;
  overrideApplicationOrder?: TaxApplicationOrder;
  isCompound?: boolean;
  isActive?: boolean;
}

export type ProductTax = ProductTaxResponse;
