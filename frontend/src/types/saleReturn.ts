import { ShipmentStatus } from "@/enums/shipmentStatus";
import { SaleStatus } from "@/enums/saleStatus";

/**
 * Full Sale Return Response (matches SaleReturnResponse DTO)
 */
export interface SaleReturn {
  id: number;
  referenceNumber: string;
  date: string;

  originalSaleId: number;
  customerId: number;
  warehouseId: number;

  products: SaleReturnProduct[];

  returnTax: string;
  returnDiscount: string;
  shippingCost: string;

  shipmentStatus: ShipmentStatus;
  returnStatus: SaleStatus;

  note: string;

  createdBy?: number;
  createdAt?: string;
  updatedBy?: number;
  updatedAt?: string | null;

  companyId?: number;
}

/**
 * Sale Return Product (from SaleReturnResponse.ProductDetail)
 */
export interface SaleReturnProduct {
  productId: number;
  productName: string;
  productCode: string;
  productUnitPrice: string;
  returnQty: number;
  returnDiscount: string;
  returnTax: string;
}

/**
 * Selected product in the POS (internal UI use only, for return workflow)
 */
export interface SelectedSaleReturnProduct {
  productId: number;
  productName: string;
  code: string;
  price: string;
  discount: string;
  stock: number;
  tax: string;
  // taxType: TaxType | "EXCLUSIVE";
  subTotal: string;
  returnQty: number;
}

/**
 * Create Sale Return Request (matches backend CreateSaleReturnRequest)
 */
export interface CreateSaleReturnRequest {
  date: string;
  originalSaleId: number;
  customerId: number | null;
  warehouseId: number | null;

  products: {
    productId: number;
    productUnitPrice: string;
    returnQty: number;
    returnDiscount: string;
    returnTax: string;
  }[];

  returnTax: string;
  returnDiscount: string;
  shippingCost: string;
  shipmentStatus: ShipmentStatus;
  returnStatus: SaleStatus;
  note: string;
}

/**
 * Update Sale Return Request (matches backend UpdateSaleReturnRequest)
 */
export interface UpdateSaleReturnRequest {
  date?: string;
  originalSaleId?: number;
  customerId?: number | null;
  warehouseId?: number | null;

  products?: {
    productId: number;
    productUnitPrice: string;
    returnQty: number;
    returnDiscount: string;
    returnTax: string;
  }[];

  returnTax?: string;
  returnDiscount?: string;
  shippingCost?: string;
  shipmentStatus?: ShipmentStatus;
  returnStatus?: SaleStatus;
  note?: string;
}
