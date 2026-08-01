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
  amountTxnCurrency: number;
  paymentMethod: PaymentMethod;
  status?: PaymentStatus;
  currencyCode?: string;
  exchangeRate?: number;
  amountBaseCurrency?: number;
  paymentMetadata?: Record<string, any> | string;
  transactionReference?: string;
  idempotencyKey?: string;
}): CreatePaymentRequest {
  const posStore = usePosStore();

  // Retrieve sale data (store or localStorage fallback)
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

  // Ensure idempotency key is present (required by backend)
  const idempotencyKey =
    params.idempotencyKey ?? `sale_${saleData.id}_${Date.now()}`;

  // Serialize paymentMetadata if it's an object
  let paymentMetadata: string | undefined;
  if (params.paymentMetadata) {
    paymentMetadata =
      typeof params.paymentMetadata === "object"
        ? JSON.stringify(params.paymentMetadata)
        : params.paymentMetadata;
  }

  return {
    referenceType: PaymentSourceType.SALE,
    referenceId: saleData.id,
    referenceNumber: saleData.referenceNumber,
    paymentType: PaymentType.INCOMING,
    amountTxnCurrency: params.amountTxnCurrency,
    amountBaseCurrency: params.amountBaseCurrency ?? params.amountTxnCurrency,
    currencyCode: params.currencyCode ?? "INR",
    exchangeRate: params.exchangeRate ?? 1,
    paymentMethod: params.paymentMethod,
    gatewayProvider: undefined,
    paymentMetadata,
    status: params.status ?? PaymentStatus.PENDING,
    paymentDate: getTodayDate(),
    transactionReference: params.transactionReference,
    idempotencyKey,
  };
}
