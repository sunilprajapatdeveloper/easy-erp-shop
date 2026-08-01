import { ShipmentStatus } from "@/enums/shipmentStatus";
import { SaleStatus } from "@/enums/saleStatus";
import { TaxCategory } from "@/enums/TaxCategory";
import { TaxInclusionType } from "@/enums/TaxInclusionType";
import { TaxApplicationOrder } from "@/enums/TaxApplicationOrder";
import { SaleSource } from "@/enums/SaleSource";
import { DiscountType } from "@/enums/discountType";
import { PaymentStatus } from "@/enums/paymentStatus";
import { PromotionType } from "@/enums/promotionType";
import { DiscountSource } from "@/enums/DiscountSource";

export interface Sale {
  id: number;
  referenceNumber: string;
  invoiceNumber?: string;
  receiptNumber?: string;
  date: string;
  customerId?: number;
  customerName?: string;
  warehouseId: number;
  warehouseName?: string;
  products: SaleProduct[];
  subtotalAmountTxnCurrency: number;
  totalTaxAmount: number;
  orderDiscount: number;
  orderDiscountValue?: number;
  orderDiscountType?: DiscountType;
  discountSource?: DiscountSource;
  appliedDiscountId?: number;
  discountName?: string;
  discountCode?: string;
  discountDescription?: string;
  promotionDiscountAmount?: number;
  promotionDiscountType?: DiscountType;
  promotionDiscountValue?: number;
  promotionCouponCode?: string;
  promotionName?: string;
  promotionCode?: string;
  promotionDescription?: string;
  promotionType?: PromotionType;
  totalDiscountAmount: number;
  shippingCost: number;
  roundingAmount: number;
  totalAmountTxnCurrency: number;
  grandTotalTxnCurrency: number;
  paidAmountTxnCurrency: number;
  dueAmountTxnCurrency: number;
  exchangeRate: number;
  currencyId: number;
  currencyCode?: string;
  subtotalAmountBaseCurrency: number;
  totalAmountBaseCurrency: number;
  grandTotalBaseCurrency: number;
  paidAmountBaseCurrency: number;
  dueAmountBaseCurrency: number;
  shipmentStatus: ShipmentStatus;
  saleStatus: SaleStatus;
  paymentStatus?: PaymentStatus;
  source: SaleSource;
  posTerminalId?: string;
  cashierId?: number;
  dueDate?: string;
  note?: string;
  createdBy?: number;
  createdAt?: string;
  updatedBy?: number;
  updatedAt?: string;
  companyId?: number;
}

export interface SaleProduct {
  id?: number;
  productId: number;
  productName?: string;
  productCode?: string;
  productUnitPrice: number;
  quantity: number;
  lineDiscountAmount: number;
  lineNetAmount: number;
  lineTaxAmount: number;
  lineGrossAmount: number;
  taxName: string;
  taxCategory: TaxCategory;
  taxRate: number;
  taxInclusionType: TaxInclusionType;
  taxApplicationOrder: TaxApplicationOrder;
}

export interface SelectedSaleProduct {
  productId: number;
  productName: string;
  code: string;
  productUnitPrice: number;
  quantity: number;
  stock: number;
  taxName: string;
  taxCategory: TaxCategory;
  taxRate: number;
  taxInclusionType: TaxInclusionType;
  taxApplicationOrder: TaxApplicationOrder;
  lineDiscountAmount?: number;
  lineNetAmount?: number;
  lineTaxAmount?: number;
  lineGrossAmount?: number;
}

export interface CreateSaleRequest {
  date: string;
  customerId?: number | null;
  warehouseId: number;
  products: CreateSaleProductRequest[];
  currencyId: number;
  exchangeRate: number;
  manualDiscountValue?: number;
  manualDiscountType?: DiscountType;
  manualDiscountReason?: string;
  appliedDiscountId?: number;
  couponCode?: string;
  shippingCost?: number;
  roundingAmount?: number;
  paidAmountTxnCurrency?: number;
  shipmentStatus?: ShipmentStatus;
  saleStatus?: SaleStatus;
  paymentStatus?: PaymentStatus;
  source?: SaleSource;
  posTerminalId?: string;
  cashierId?: number;
  dueDate?: string;
  note?: string;
}

export interface CreateSaleProductRequest {
  productId: number;
  quantity: number;
  unitPriceOverride?: number;
}

export interface UpdateSaleRequest {
  date?: string;
  customerId?: number | null;
  warehouseId?: number;
  products?: UpdateSaleProductRequest[];
  manualDiscountValue?: number;
  manualDiscountType?: DiscountType;
  manualDiscountReason?: string;
  appliedDiscountId?: number;
  couponCode?: string;
  shippingCost?: number;
  roundingAmount?: number;
  posTerminalId?: string;
  cashierId?: number;
  dueDate?: string;
  shipmentStatus?: ShipmentStatus;
  saleStatus?: SaleStatus;
  paymentStatus?: PaymentStatus;
  source?: SaleSource;
  note?: string;
  currencyId?: number;
  exchangeRate?: number;
}

export interface UpdateSaleProductRequest {
  productId: number;
  quantity?: number;
  unitPriceOverride?: number;
}
