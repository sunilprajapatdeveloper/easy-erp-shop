import { ShipmentStatus } from "@/enums/shipmentStatus";
import { TaxType } from "./TaxTypes";
import { SaleStatus } from "@/enums/saleStatus";
import { Payment, CreatePaymentRequest } from "@/types/Payment";

/**
 * Full Sale Response from backend
 */
export interface Sale {
  id: number;
  referenceNumber: string;
  invoiceNumber?: string;
  receiptNumber?: string;
  date: string;
  customerId?: number;
  warehouseId: number;
  products: SaleProduct[];
  orderTax: string;
  discount: string;
  shippingCost: string;
  totalAmountTxnCurrency: string;
  dueAmountTxnCurrency: string;
  totalAmountBaseCurrency: string;
  dueAmountBaseCurrency: string;
  exchangeRate: string;
  currencyId: number;
  currencyCode: string;
  shipmentStatus: ShipmentStatus;
  saleStatus: SaleStatus;
  note?: string;
  source: "WEB" | "POS" | string;
  isRefund: boolean;
  posTerminalId?: string;
  cashierId?: number;
  payments: Payment[];
  createdBy?: number;
  createdAt?: string;
  updatedBy?: number;
  updatedAt?: string;
  companyId?: number;
}

/**
 * Sale Product in Sale Response
 */
export interface SaleProduct {
  productId: number;
  productName: string;
  productCode: string;
  productUnitPrice: string;
  saleQty: number;
  productDiscount: string;
  productTax: string;
  lineTotalTxnCurrency: string; // computed in backend
}

/**
 * Selected product in POS UI
 */
export interface SelectedSaleProduct {
  productId: number;
  productName: string;
  code: string;
  price: string;
  discount: string;
  stock: number;
  tax: string;
  taxType: TaxType | "EXCLUSIVE";
  subTotal: string;
  saleQty: number;
}

/**
 * Request to create a new sale
 */
export interface CreateSaleRequest {
  date: string; // ISO string
  customerId?: number | null;
  warehouseId: number;
  orderTax: string;
  discount: string;
  shippingCost: string;
  saleStatus: SaleStatus;
  shipmentStatus: ShipmentStatus;
  note?: string;
  source?: "WEB" | "POS" | string;
  products: {
    productId: number;
    productUnitPrice: string;
    saleQty: number;
    productDiscount: string;
    productTax: string;
  }[];
  payments?: CreatePaymentRequest[];
  currencyId: number;
  exchangeRate: string;
}

/**
 * Request to update a sale
 */
export interface UpdateSaleRequest {
  date?: string;
  customerId?: number;
  warehouseId?: number;
  orderTax?: string;
  discount?: string;
  shippingCost?: string;
  saleStatus?: SaleStatus;
  shipmentStatus?: ShipmentStatus;
  note?: string;
  products?: {
    productId: number;
    productUnitPrice?: string;
    saleQty?: number;
    productDiscount?: string;
    productTax?: string;
  }[];
  payments?: CreatePaymentRequest[];
  currencyId?: number;
  exchangeRate?: string;
}
