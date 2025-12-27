// Response from backend
export interface ProductPriceResponse {
  id: number;

  productId: number;
  productCode?: string;
  productName?: string;

  warehouseId?: number;
  warehouseName?: string;

  priceList: string;
  channel?: string;
  customerGroup?: string;

  price: number;
  cost?: number;
  minPrice?: number;
  maxPrice?: number;

  currencyId: number;
  currencyCode?: string;
  currencySymbol?: string;

  isActive: boolean;

  validFrom?: string;
  validTo?: string;

  minQuantity: number;
  maxQuantity?: number;

  createdAt?: string;
  updatedAt?: string;
}

// Create Request
export interface CreateProductPriceRequest {
  productId: number;
  warehouseId?: number;
  priceList?: string;
  channel?: string;
  customerGroup?: string;
  price: number;
  cost?: number;
  minPrice?: number;
  maxPrice?: number;
  currencyId: number;
  isActive?: boolean;
  validFrom?: string;
  validTo?: string;
  minQuantity?: number;
  maxQuantity?: number;
}

// Update Request
export interface UpdateProductPriceRequest {
  id: number;
  productId: number;
  warehouseId?: number;
  priceList?: string;
  channel?: string;
  customerGroup?: string;
  price: number;
  cost?: number;
  minPrice?: number;
  maxPrice?: number;
  currencyId: number;
  isActive?: boolean;
  validFrom?: string;
  validTo?: string;
  minQuantity?: number;
  maxQuantity?: number;
}

export type ProductPrice = ProductPriceResponse;
