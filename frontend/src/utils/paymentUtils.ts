import { usePosStore } from "@/stores/posStore";
import { PaymentSourceType } from "@/enums/paymentSourceType";
import { PaymentType } from "@/enums/paymentType";
import { PaymentStatus } from "@/enums/paymentStatus";
import { PaymentMethod } from "@/enums/paymentMethods";
import { CreatePaymentRequest } from "@/types/Payment";

/** Returns today's date in YYYY-MM-DD format */
const getTodayDate = (): string => new Date().toISOString().split("T")[0];

/**
 * Builds a CreatePaymentRequest for SALE payments.
 */
export function buildSalePaymentRequest(params: {
  amount: number;
  paymentMethod: PaymentMethod;
  status?: PaymentStatus;
  currencyCode?: string;
  exchangeRate?: number;
  baseCurrencyAmount?: number;
  paymentData?: Record<string, any>;
  note?: string;
  transactionReference?: string;
  idempotencyKey?: string;
}): CreatePaymentRequest {
  const posStore = usePosStore();

  // Prefer store data for faster access, fallback to localStorage
  const saleData =
    posStore.temporarySale ??
    (() => {
      const stored = localStorage.getItem("temporarySale");
      return stored ? JSON.parse(stored) : null;
    })();

  if (!saleData?.id) {
    throw new Error("Missing sale ID for payment request.");
  }

  if (!saleData?.referenceNumber) {
    throw new Error("Missing sale reference number for payment request.");
  }

  return {
    referenceType: PaymentSourceType.SALE,
    referenceId: saleData.id,
    referenceNumber: saleData.referenceNumber,
    paymentType: PaymentType.INCOMING,
    amount: params.amount,
    baseCurrencyAmount: params.baseCurrencyAmount ?? params.amount, // default to same as txn amount
    currencyCode: params.currencyCode ?? "INR",
    exchangeRate: params.exchangeRate ?? 1,
    paymentMethod: params.paymentMethod,
    gatewayProvider: undefined,
    paymentData: params.paymentData,
    status: params.status ?? PaymentStatus.PENDING,
    paymentDate: getTodayDate(),
    note: params.note,
    transactionReference: params.transactionReference,
    idempotencyKey: params.idempotencyKey,
  };
}
