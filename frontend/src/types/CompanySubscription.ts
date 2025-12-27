import { SubscriptionStatus } from "@/enums/SubscriptionStatus";
import { SubscriptionPlan } from "./SubscriptionPlan";

export interface CompanySubscription {
  id: number;
  companyId: number;
  subscriptionPlanId: number;
  plan?: SubscriptionPlan;
  startDate: string;
  endDate?: string | null;
  nextBillingDate?: string | null;
  renewalDate?: string | null;
  autoRenew: boolean;
  trialActive: boolean;
  trialEndDate?: string | null;
  status: SubscriptionStatus;
  billingReference?: string | null;
  createdBy: number;
  createdAt: string;
  updatedBy?: number | null;
  updatedAt?: string | null;
  isDeleted: boolean;
  version: number;
}

export interface CreateCompanySubscriptionRequest {
  companyId: number;
  subscriptionPlanId: number;
  startDate: string;
  endDate?: string;
  nextBillingDate?: string;
  renewalDate?: string;
  autoRenew?: boolean;
  trialActive?: boolean;
  trialEndDate?: string;
  billingReference?: string;
  createdBy: number;
}

export interface UpdateCompanySubscriptionRequest {
  subscriptionPlanId?: number;
  startDate?: string;
  endDate?: string;
  nextBillingDate?: string;
  renewalDate?: string;
  autoRenew?: boolean;
  status?: SubscriptionStatus;
  trialActive?: boolean;
  trialEndDate?: string;
  billingReference?: string;
  updatedBy: number;
}

export interface CompanySubscriptionResponse extends CompanySubscription {}
