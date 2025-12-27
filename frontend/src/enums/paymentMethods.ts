export enum PaymentMethod {
  CASH = "CASH",
  CARD = "CARD",
  UPI = "UPI",
  PAYPAL = "PAYPAL",
  CHEQUE = "CHEQUE",
  GIFT_CARD = "GIFT_CARD",
  WALLET = "WALLET",
  BANK_TRANSFER = "BANK_TRANSFER",
  VOUCHER = "VOUCHER",
  MULTIPLE = "MULTIPLE",
}

export const PaymentMethodLabels: Record<PaymentMethod, string> = {
  [PaymentMethod.CASH]: "Cash",
  [PaymentMethod.CARD]: "Card",
  [PaymentMethod.UPI]: "Upi",
  [PaymentMethod.PAYPAL]: "Paypal",
  [PaymentMethod.CHEQUE]: "Cheque",
  [PaymentMethod.GIFT_CARD]: "Gift card",
  [PaymentMethod.WALLET]: "Wallet",
  [PaymentMethod.BANK_TRANSFER]: "Bank Transfer",
  [PaymentMethod.VOUCHER]: "Voucher",
  [PaymentMethod.MULTIPLE]: "Multiple",
};

export const PaymentMethodMap: Record<string, PaymentMethod> = {
  card: PaymentMethod.CARD,
  cash: PaymentMethod.CASH,
  upi: PaymentMethod.UPI,
  paypal: PaymentMethod.PAYPAL,
  cheque: PaymentMethod.CHEQUE,
  giftcard: PaymentMethod.GIFT_CARD,
} as const;

export type PaymentMethodKey = keyof typeof PaymentMethodMap;
export type PaymentMethodValue = typeof PaymentMethodMap[PaymentMethodKey];