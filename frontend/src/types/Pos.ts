import { ShipmentStatus } from "@/enums/shipmentStatus";
import { PaymentStatus } from "@/enums/paymentStatus";
import { TaxCategory } from "@/enums/TaxCategory";
import { PaymentSourceType } from "@/enums/paymentSourceType";

export interface Pos {
  id: number;
  referenceNumber: string;
  date: string;
  customerId: number | null;
  warehouseId: number;
  products: PosProduct[];
  orderTax: number;
  discount: number;
  shippingCost: number;
  grandTotal: number;
  status: ShipmentStatus;
  note: string;
  amountPaid: number;
  amountDue: number;
  payments: CreatePaymentRequest[];
  paymentStatus: PaymentStatus;
  createdBy?: number;
  createdAt?: string;
  updatedBy?: number;
  updatedAt?: string | null;
  companyId?: number;
}

export interface PosProduct {
  productId: number;
  productName: string;
  productCode: string;
  productUnitPrice: number;
  productStock: number;
  saleQty: number;
  productDiscount: number;
  productTax: number;
  subTotal: number;
}

export interface SelectedPosProduct {
  productId: number;
  productName: string;
  code: string;
  price: number;
  discount: number;
  tax: number;
  taxCategory: TaxCategory;
  subTotal: number;
  saleQty: number;
}

export interface CreatePosRequest {
  date: string;
  customerId?: number | null;
  warehouseId: number;
  orderTax: number;
  discount: number;
  shippingCost: number;
  status: ShipmentStatus;
  note: string;
  products: {
    productId: number;
    productUnitPrice: number;
    saleQty: number;
    productDiscount: number;
    productTax: number;
    subTotal: number;
  }[];
  payments?: CreatePaymentRequest[];
  currencyId: number;
  exchangeRate: number;
}

export interface CreatePaymentRequest {
  referenceType: PaymentSourceType;
  referenceId?: number;
  referenceNumber?: string;
  paymentType: "INCOMING";
  amount: number;
  paymentMethod: "CASH" | "CARD" | "UPI" | "CHEQUE" | "PAYPAL" | "GIFT_CARD";
  paymentData?: string;
  status: PaymentStatus;
  paymentDate: string;
  note?: string;
  transactionReference?: string;
  idempotencyKey?: string;
  currencyCode?: string;
}

export interface UpdatePosRequest {
  saleStatus: "PENDING" | "PROCESSING" | "COMPLETED";
}
