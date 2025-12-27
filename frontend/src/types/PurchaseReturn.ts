import { ShipmentStatus } from "@/enums/shipmentStatus";
import { PurchaseStatus } from "@/enums/purchaseStatus";

/**
 * Response product detail (from backend PurchaseReturnResponse.ProductDetail)
 */
export interface PurchaseReturnProduct {
  productId: number;
  productName: string;
  productCode: string;
  productUnitCost: string;
  returnQty: number;
  productDiscount: string;
  productTax: string;
  subTotal: string;
}

/**
 * Full response from backend (PurchaseReturnResponse)
 */
export interface PurchaseReturn {
  id: number;
  referenceNumber: string;
  date: string;
  originalPurchaseId: number;
  supplierId: number;
  warehouseId: number;
  products: PurchaseReturnProduct[];
  returnTax: string;
  returnDiscount: string;
  shippingCost: string;
  totalRefundAmount?: string;
  shipmentStatus: ShipmentStatus;
  returnStatus: PurchaseStatus;
  note?: string;
  createdBy: number;
  createdAt: string;
  updatedBy?: number;
  updatedAt?: string | null;
  companyId: number;
}

/**
 * Selected product in UI (internal only)
 */
export interface SelectedPurchaseReturnProduct {
  productId: number;
  productName: string;
  productCode: string;
  stock: number;
  unitCost: string;
  discount: string;
  tax: string;
  subTotal: string;
  returnQty: number;
}

/**
 * Create request (CreatePurchaseReturnRequest)
 */
export interface CreatePurchaseReturnRequest {
  date: string;
  originalPurchaseId: number;
  supplierId: number;
  warehouseId: number;
  products: {
    productId: number;
    productUnitCost: string;
    returnQty: number;
    productDiscount: string;
    productTax: string;
    subTotal: string;
  }[];
  orderTax: string;
  discount: string;
  shippingCost: string;
  totalAmount: string;
  shipmentStatus: ShipmentStatus;
  returnStatus: PurchaseStatus;
  note?: string;
}

/**
 * Update request (UpdatePurchaseReturnRequest)
 */
export interface UpdatePurchaseReturnRequest {
  date?: string;
  originalPurchaseId?: number;
  supplierId?: number;
  warehouseId?: number;
  products?: {
    productId: number;
    productUnitCost: string;
    returnQty: number;
    productDiscount: string;
    productTax: string;
    subTotal: string;
  }[];
  orderTax?: string;
  discount?: string;
  shippingCost?: string;
  shipmentStatus?: ShipmentStatus;
  returnStatus?: PurchaseStatus;
  note?: string;
}
