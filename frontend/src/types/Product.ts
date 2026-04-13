// src/types/product.ts
import { ProductType } from "@/enums/productType";
import { ProductStatus } from "@/enums/productStatus";
import { ProductPriceResponse } from "./ProductPrice";
import { ProductStockResponse } from "./ProductStock";
import { TaxType } from "./TaxTypes";
import { MediaResponse } from "./Media";
import { ProductTaxResponse } from "./ProductTax";

// ------------------ Product Response from backend ------------------
export interface ProductResponse {
  id: number;

  // Basic info
  name: string;
  code: string;
  sku: string;
  barcode?: string;

  // Category info
  categoryId: number;
  categoryName?: string;

  // Brand info
  brandId?: number | null;
  brandName?: string;

  // Type and status
  productType: ProductType;
  status: ProductStatus;

  // Units
  productUnitId: number;
  productUnitName?: string;
  salesUnitId?: number | null;
  salesUnitName?: string;
  purchaseUnitId?: number | null;
  purchaseUnitName?: string;

  // Conversion
  unitConversionFactor?: string; // BigDecimal -> string

  // Flags
  isBatchManaged: boolean;
  isSerialized: boolean;
  isComposite: boolean;
  hasVariants: boolean;

  // Physical
  weight?: string;
  volume?: string;
  dimensions?: string;

  // Description / images
  description?: string;
  productImage?: string;
  imageUrls?: string[];
  mediaImages?: MediaResponse[];

  // Warehouse-specific (single)
  price?: ProductPriceResponse;
  stock?: ProductStockResponse;
  tax?: ProductTaxResponse;

  // Multi-warehouse (default)
  prices?: ProductPriceResponse[];
  stocks?: ProductStockResponse[];
  taxes?: ProductTaxResponse[];

  // System flags
  isDeleted: boolean;

  // Audit
  createdBy?: number;
  createdAt?: string;
  updatedBy?: number;
  updatedAt?: string | null;
}

// Related sub-responses
// export interface ProductPriceResponse {
//   id: number;
//   warehouseId: number;
//   currencyCode: string;
//   unitPrice: string;
// }

// export interface ProductStockResponse {
//   id: number;
//   warehouseId: number;
//   availableQty: string;
//   stockAlert?: string;
// }

// Create Product Request
export interface CreateProductRequest {
  name: string;
  code: string;
  sku: string;
  barcode?: string;

  categoryId: number;
  brandId?: number | null;

  productType: ProductType;
  status: ProductStatus;

  productUnitId: number;
  salesUnitId?: number | null;
  purchaseUnitId?: number | null;

  unitConversionFactor?: string;

  isBatchManaged: boolean;
  isSerialized: boolean;
  isComposite: boolean;
  hasVariants: boolean;

  weight?: string;
  volume?: string;
  dimensions?: string;
  description?: string;

  productImage?: string;
  imageUrls?: string[];

  isDeleted: boolean;
}

// Update Product Request
export interface UpdateProductRequest {
  id: number;

  name?: string;
  code?: string;
  sku?: string;
  barcode?: string;

  categoryId?: number;
  brandId?: number | null;

  productType?: ProductType;
  status?: ProductStatus;

  productUnitId?: number;
  salesUnitId?: number | null;
  purchaseUnitId?: number | null;

  unitConversionFactor?: string;

  isBatchManaged?: boolean;
  isSerialized?: boolean;
  isComposite?: boolean;
  hasVariants?: boolean;

  weight?: string;
  volume?: string;
  dimensions?: string;
  description?: string;

  productImage?: string;
  imageUrls?: string[];

  isDeleted?: boolean;
}

// Internal UI selection (like SelectedPurchaseProduct)
export interface SelectedProduct {
  productId: number;
  productName: string;
  code: string;
  sku: string;
  unitPrice: string; // for POS/transfer
  stock: number;
  quantity: number;
  subTotal: string;
}

export type Product = ProductResponse;

export interface ProductListItem {
  id: number;
  name: string;
  code: string;
  sku: string;
  barcode?: string;

  // Category info
  categoryId: number;
  categoryName?: string;

  // Brand info
  brandId?: number | null;
  brandName?: string;

  // Product unit
  productUnitId: number;
  productUnitName?: string;

  // Price and stock (from first warehouse or aggregated)
  unitPrice?: string;
  availableQty?: string;

  // Images
  productImage?: string; // Legacy field - single image URL
  mediaImages?: MediaResponse[]; // New field - array of media objects

  // Other fields you might need
  status: ProductStatus;
  productType: ProductType;

  // System flags
  isDeleted: boolean;
}
