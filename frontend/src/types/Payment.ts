import { PaymentMethod } from "@/enums/paymentMethods";
import { PaymentGatewayProvider } from "@/enums/PaymentGatewayProvider";
import { ExchangeRateSource } from "@/enums/ExchangeRateSource";
import { PaymentStatus } from "@/enums/paymentStatus";
import { PaymentSourceType } from "@/enums/paymentSourceType";
import { PaymentType } from "@/enums/paymentType";

export interface PaymentResponse {
  id: number;
  referenceNumber?: string;
  referenceType: PaymentSourceType;
  referenceId: number;
  paymentType: PaymentType;
  amountTxnCurrency: number;
  paymentMethod: PaymentMethod;
  gatewayProvider?: PaymentGatewayProvider;
  transactionReference?: string;
  status: PaymentStatus;
  paymentDate: string;
  currencyCode: string;
  exchangeRate: number;
  amountBaseCurrency: number;
  idempotencyKey: string;
  paymentMetadata?: Record<string, any>;
  referenceCurrencyCode?: string;
  referenceAmount?: number;
  warehouseId?: number;
  posTerminalId?: string;
  exchangeRateSource?: ExchangeRateSource;
  createdBy: number;
  createdAt: string;
  updatedBy?: number;
  updatedAt?: string;
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
