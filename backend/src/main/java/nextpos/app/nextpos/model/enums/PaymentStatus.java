package nextpos.app.nextpos.model.enums;

public enum PaymentStatus {
    PENDING, // Payment initiated, but not yet completed
    AUTHORIZED, // Amount authorized but not captured (common in card payments)
    CAPTURED, // Funds successfully captured
    PAID, // Payment completed successfully (alias for CAPTURED in some cases)
    PARTIALLY_PAID, // Only a portion of the total amount has been paid
    UNPAID, // No payment made
    FAILED, // Payment attempt failed (e.g. declined, timeout)
    CANCELLED, // Payment cancelled by user or system
    REFUNDED, // Full refund issued
    PARTIALLY_REFUNDED // Partial refund issued
}