import { BillingCycle } from "@/enums/BillingCycle";
import { PlanStatus } from "@/enums/PlanStatus";

/** Represents a subscription plan and its enterprise-level metadata */
export interface SubscriptionPlan {
  id: number;
  name: string;
  description?: string;
  price: number;
  currency: string;
  billingCycle: BillingCycle;

  trialAvailable: boolean;
  trialDays: number;
  maxUsers?: number;
  maxBranches?: number;

  features: Record<string, string>;
  availableRegions: string[];

  status: PlanStatus;
  isDeleted: boolean;

  createdBy: number;
  updatedBy?: number;
  createdAt: string;
  updatedAt: string;

  version: number;
}

/** Request payloads for creating/updating subscription plans */
export interface CreateSubscriptionPlanRequest {
  name: string;
  description?: string;
  price: number;
  currency: string;
  billingCycle: BillingCycle;

  trialAvailable?: boolean;
  trialDays?: number;
  maxUsers?: number;
  maxBranches?: number;

  features?: Record<string, string>;
  availableRegions?: string[];
}

export interface UpdateSubscriptionPlanRequest {
  name?: string;
  description?: string;
  price?: number;
  currency?: string;
  billingCycle?: BillingCycle;

  trialAvailable?: boolean;
  trialDays?: number;
  maxUsers?: number;
  maxBranches?: number;

  features?: Record<string, string>;
  availableRegions?: string[];

  status?: PlanStatus;
}
