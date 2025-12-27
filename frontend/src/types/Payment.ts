import { PaymentType } from "@/enums/paymentType";
import { PaymentSourceType } from "@/enums/paymentSourceType";
import { PaymentStatus } from "@/enums/paymentStatus";
import { PaymentGatewayProvider } from "@/enums/PaymentGatewayProvider";
import { PaymentMethod } from "@/enums/paymentMethods";

/**
 * Payment entity returned from the backend
 */
export interface Payment {
  id: number;
  referenceNumber?: string;
  referenceType: PaymentSourceType;
  referenceId: number;
  paymentType: PaymentType;
  amountTxnCurrency: number;
  amountBaseCurrency: number;
  currencyCode: string;
  exchangeRate: number;
  paymentMethod: PaymentMethod;
  gatewayProvider?: PaymentGatewayProvider;
  transactionReference?: string;
  status: PaymentStatus;
  paymentDate: string;
  note?: string;
  idempotencyKey?: string;
  paymentMetadata?: Record<string, any>;
  createdBy?: number;
  createdAt?: string;
  updatedBy?: number;
  updatedAt?: string;
  companyId?: number;
}

/**
 * Request to create a new payment
 */
export interface CreatePaymentRequest {
  referenceType: PaymentSourceType;      // SALE, PURCHASE, SALE_RETURN, PURCHASE_RETURN, etc.
  referenceId?: number;                  // Optional, backend allows null
  referenceNumber?: string;              // Human-readable reference
  paymentType: PaymentType;              // INCOMING / OUTGOING
  amount: number;                        // Transaction currency amount
  paymentMethod: PaymentMethod;          // CASH, CARD, UPI, BANK_TRANSFER
  gatewayProvider?: PaymentGatewayProvider; // Optional (STRIPE, RAZORPAY, etc.)
  paymentData?: Record<string, any>;     // Optional JSON metadata
  status: PaymentStatus;                 // PENDING, SUCCESS, FAILED, PAID, REFUNDED, etc.
  paymentDate: string;                   // ISO date string
  note?: string;                          // Optional human note
  transactionReference?: string;         // External txn ID
  idempotencyKey?: string;               // Optional, max 100 chars
  currencyCode: string;                  // ISO 4217 currency code (USD, INR, EUR)
  exchangeRate: number;                  // Conversion rate relative to company base currency
  baseCurrencyAmount?: number;           // Optional: amount in company base currency
}

/**
 * Request to update an existing payment
 */
export interface UpdatePaymentRequest {
  amount?: number;                        // Transaction currency amount
  baseCurrencyAmount?: number;            // Optional: amount in company base currency
  currencyCode?: string;                  // ISO 4217 currency code (USD, INR, EUR)
  exchangeRate?: number;                  // Conversion rate relative to company base currency
  paymentMethod?: PaymentMethod;          // CASH, CARD, UPI, BANK_TRANSFER
  gatewayProvider?: PaymentGatewayProvider; // Optional (STRIPE, PAYPAL, etc.)
  paymentData?: string;                   // JSON metadata as string
  status?: PaymentStatus;                 // PENDING, SUCCESS, FAILED, PAID, REFUNDED, etc.
  paymentDate?: string;                   // ISO date string (YYYY-MM-DD)
  note?: string;                           // Optional human-readable note
  transactionReference?: string;          // External txn ID
  idempotencyKey?: string;                // Optional, max 100 chars
}
