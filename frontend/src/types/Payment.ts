import { PaymentType } from "@/enums/paymentType";
import { PaymentSourceType } from "@/enums/paymentSourceType";
import { PaymentStatus } from "@/enums/paymentStatus";
import { PaymentGatewayProvider } from "@/enums/PaymentGatewayProvider";
import { PaymentMethod } from "@/enums/paymentMethods";
import { ExchangeRateSource } from "@/enums/ExchangeRateSource";

export interface PaymentResponse {
  id: number;
  referenceNumber: string | null;
  referenceType: PaymentSourceType;
  referenceId: number;
  paymentType: PaymentType;
  amountTxnCurrency: number;
  paymentMethod: PaymentMethod;
  gatewayProvider: PaymentGatewayProvider | null;
  transactionReference: string | null;
  status: PaymentStatus;
  paymentDate: string;
  currencyCode: string;
  exchangeRate: number;
  amountBaseCurrency: number;
  idempotencyKey: string;
  paymentMetadata: Record<string, any> | null;
  referenceCurrencyCode: string | null;
  referenceAmount: number | null;
  warehouseId: number | null;
  posTerminalId: string | null;
  exchangeRateSource: ExchangeRateSource | null;
  createdBy: number;
  createdAt: string;
  updatedBy: number | null;
  updatedAt: string | null;
  companyId: number;
}

export interface CreatePaymentRequest {
  referenceType: PaymentSourceType;
  referenceId: number;
  referenceNumber?: string;
  paymentType: PaymentType;
  amountTxnCurrency: number;
  paymentMethod: PaymentMethod;
  gatewayProvider?: PaymentGatewayProvider;
  paymentMetadata?: string;
  status: PaymentStatus;
  paymentDate: string;
  transactionReference?: string;
  idempotencyKey: string;
  currencyCode: string;
  exchangeRate: number;
  amountBaseCurrency?: number;
  referenceCurrencyCode?: string;
  referenceAmount?: number;
  warehouseId?: number;
  posTerminalId?: string;
  exchangeRateSource?: ExchangeRateSource;
}

export interface UpdatePaymentRequest {
  amountTxnCurrency?: number;
  paymentMethod?: PaymentMethod;
  gatewayProvider?: PaymentGatewayProvider;
  status?: PaymentStatus;
  paymentDate?: string;
  transactionReference?: string;
  currencyCode?: string;
  exchangeRate?: number;
  amountBaseCurrency?: number;
  idempotencyKey?: string;
  referenceCurrencyCode?: string;
  referenceAmount?: number;
  warehouseId?: number;
  posTerminalId?: string;
  exchangeRateSource?: ExchangeRateSource;
}
