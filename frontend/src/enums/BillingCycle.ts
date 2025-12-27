export enum BillingCycle {
  DAILY = "DAILY",
  WEEKLY = "WEEKLY",
  MONTHLY = "MONTHLY",
  QUARTERLY = "QUARTERLY",
  YEARLY = "YEARLY",
}

export const BillingCycleLabels: Record<BillingCycle, string> = {
  [BillingCycle.DAILY]: "Daily",
  [BillingCycle.WEEKLY]: "Weekly",
  [BillingCycle.MONTHLY]: "Monthly",
  [BillingCycle.QUARTERLY]: "Quarterly",
  [BillingCycle.YEARLY]: "Yearly",
};
