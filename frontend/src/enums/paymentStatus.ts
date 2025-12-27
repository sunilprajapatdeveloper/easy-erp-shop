export enum PaymentStatus {
  PENDING = "PENDING", // Payment initiated, not completed
  AUTHORIZED = "AUTHORIZED", // Amount authorized but not captured
  CAPTURED = "CAPTURED", // Funds successfully captured
  PAID = "PAID", // Payment completed successfully
  PARTIALLY_PAID = "PARTIALLY_PAID", // Partial payment made
  UNPAID = "UNPAID", // No payment made
  FAILED = "FAILED", // Payment attempt failed
  CANCELLED = "CANCELLED", // Cancelled by user/system
  REFUNDED = "REFUNDED", // Full refund issued
  PARTIALLY_REFUNDED = "PARTIALLY_REFUNDED", // Partial refund issued
}

export const PaymentStatusLabels: Record<PaymentStatus, string> = {
  [PaymentStatus.PENDING]: "Pending",
  [PaymentStatus.AUTHORIZED]: "Authorized",
  [PaymentStatus.CAPTURED]: "Captured",
  [PaymentStatus.PAID]: "Paid",
  [PaymentStatus.PARTIALLY_PAID]: "Partially Paid",
  [PaymentStatus.UNPAID]: "Unpaid",
  [PaymentStatus.FAILED]: "Failed",
  [PaymentStatus.CANCELLED]: "Cancelled",
  [PaymentStatus.REFUNDED]: "Refunded",
  [PaymentStatus.PARTIALLY_REFUNDED]: "Partially Refunded",
};