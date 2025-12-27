import { ShipmentStatus } from "@/enums/shipmentStatus";
import { PurchaseStatus } from "@/enums/purchaseStatus";
import { TaxType } from "./TaxTypes";

// Response Purchase Product
export interface PurchaseProduct {
  productId: number;
  productName: string;
  productCode: string;
  productUnitCost: string;
  productStock: number;
  purchaseQty: number;
  productDiscount: string;
  productTax: string;
  subTotal: string;
}

// Full Purchase Response from backend
export interface Purchase {
  id: number;
  referenceNumber: string;
  date: string; // LocalDate -> string
  supplierId: number;
  warehouseId: number;
  products: PurchaseProduct[];
  orderTax: string;
  discount: string;
  shippingCost: string;
  totalAmount: string;
  shippingStatus: ShipmentStatus;
  purchaseStatus: PurchaseStatus;
  note: string;
  createdBy?: number;
  createdAt?: string;
  updatedBy?: number;
  updatedAt?: string | null;
  companyId?: number;
}

// Selected product in the POS (internal UI use)
export interface SelectedPurchaseProduct {
  productId: number;
  productName: string;
  code: string;
  cost: string; // cost per unit
  discount: string;
  stock: number;
  tax: string;
  taxType: TaxType | "EXCLUSIVE";
  subTotal: string;
  purchaseQty: number;
}

// Create Purchase Request sent to backend
export interface CreatePurchaseRequest {
  date: string;
  supplierId: number;
  warehouseId: number;
  orderTax: string;
  discount: string;
  shippingCost: string;
  totalAmount: string;
  shippingStatus: ShipmentStatus;
  purchaseStatus: PurchaseStatus;
  note?: string;
  products: {
    productId: number;
    productUnitCost: string;
    purchaseQty: number;
    productDiscount: string;
    productTax: string;
    subTotal: string;
  }[];
}

// Update Purchase Request sent to backend
export interface UpdatePurchaseRequest {
  date: string;
  supplierId: number;
  warehouseId: number;
  products: UpdatePurchaseProductRequest[];
  orderTax: string;
  discount: string;
  shippingCost: string;
  totalAmount: string;
  shippingStatus: ShipmentStatus;
  purchaseStatus: PurchaseStatus;
  note: string;
}

export interface UpdatePurchaseProductRequest {
  productId: number;
  productUnitCost: string;
  purchaseQty: number;
  productDiscount: string;
  productTax: string;
  subTotal: string;
}