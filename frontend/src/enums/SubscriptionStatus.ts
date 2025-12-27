export enum SubscriptionStatus {
  ACTIVE = "ACTIVE",
  TRIAL = "TRIAL",
  EXPIRED = "EXPIRED",
  CANCELLED = "CANCELLED",
  PENDING_ACTIVATION = "PENDING_ACTIVATION",
  SUSPENDED = "SUSPENDED",
  PENDING_CANCELLATION = "PENDING_CANCELLATION",
}

// Optional: friendly labels for display in UI
export const SubscriptionStatusLabels: Record<SubscriptionStatus, string> = {
  [SubscriptionStatus.ACTIVE]: "Active",
  [SubscriptionStatus.TRIAL]: "Trial",
  [SubscriptionStatus.EXPIRED]: "Expired",
  [SubscriptionStatus.CANCELLED]: "Cancelled",
  [SubscriptionStatus.PENDING_ACTIVATION]: "Pending Activation",
  [SubscriptionStatus.SUSPENDED]: "Suspended",
  [SubscriptionStatus.PENDING_CANCELLATION]: "Pending Cancellation",
};
