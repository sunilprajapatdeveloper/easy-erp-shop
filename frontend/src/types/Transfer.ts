import { TaxType } from "./TaxTypes";

export enum ShipmentStatus {
  PENDING = "PENDING",
  PROCESSING = "PROCESSING",
  SHIPPED = "SHIPPED",
  IN_TRANSIT = "IN_TRANSIT",
  DELIVERED = "DELIVERED",
  RETURNED = "RETURNED",
}

export const ShipmentStatusLabels: Record<ShipmentStatus, string> = {
  [ShipmentStatus.PENDING]: "Pending",
  [ShipmentStatus.PROCESSING]: "Processing",
  [ShipmentStatus.SHIPPED]: "Shipped",
  [ShipmentStatus.IN_TRANSIT]: "In Transit",
  [ShipmentStatus.DELIVERED]: "Delivered",
  [ShipmentStatus.RETURNED]: "Returned",
};

export interface Transfer {
  id: number;
  fromWarehouse: number;
  toWarehouse: number;
  date: string;
  orderTax: string;
  discount: string;
  shippingCost: string;
  grandTotal: string;
  status: ShipmentStatus;
  note?: string;
  products: TransferProduct[];
  createdBy?: number;
  createdAt?: string;
  updatedBy?: number;
  updatedAt?: string | null;
  companyId?: number;
}

export interface TransferProduct {
  productId: number;
  productCode: string;
  productUnitCost: string;
  productStock: number;
  transferredQty: number;
  productDiscount: string;
  productTax: string;
  subTotal: string;
}

export interface SelectedTransferProduct {
  productId: number;
  productName: string;
  code: string;
  stock: number;
  cost: string;
  discount: string;
  tax: string;
  taxType: TaxType | "EXCLUSIVE";
  subTotal: string;
  transferredQty: number;
}

export interface CreateTransferRequest {
  fromWarehouse: number;
  toWarehouse: number;
  date: string;
  orderTax: string;
  discount: string;
  shippingCost: string;
  status: ShipmentStatus;
  note?: string;
  products: {
    productId: number;
    productCode: string;
    productUnitCost: string;
    productStock: number;
    transferredQty: number;
    productDiscount: string;
    productTax: string;
    subTotal: string;
  }[];
}
