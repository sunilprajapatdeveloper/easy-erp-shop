package nextpos.app.nextpos.model.enums;

/**
 * Represents the lifecycle status of a company's subscription.
 * Covers all typical enterprise SaaS subscription scenarios.
 */
public enum SubscriptionStatus {

    /** Subscription is active and billing is current. */
    ACTIVE,

    /** Company is in a free trial period. */
    TRIAL,

    /** Subscription has expired due to non-renewal or non-payment. */
    EXPIRED,

    /** Subscription was manually cancelled by the company or admin. */
    CANCELLED,

    /** Subscription is pending activation (e.g., payment not yet confirmed). */
    PENDING_ACTIVATION,

    /**
     * Subscription is suspended temporarily (e.g., payment failed, compliance
     * issue).
     */
    SUSPENDED,

    /**
     * Subscription is scheduled to be cancelled at the end of the billing cycle.
     */
    PENDING_CANCELLATION
}
