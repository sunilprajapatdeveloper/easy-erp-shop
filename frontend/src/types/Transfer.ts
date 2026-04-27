import { ShipmentStatus } from "@/enums/shipmentStatus";
import { TaxInclusionType } from "@/enums/TaxInclusionType";

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
  inclusionType: TaxInclusionType;
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
