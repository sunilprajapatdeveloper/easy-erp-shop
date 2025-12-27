export type LoyaltyType = "POINTS" | "CASHBACK" | "TIERED";

export interface LoyaltySettings {
  enabled: boolean;
  programName?: string;
  loyaltyType: LoyaltyType;
  pointsPerCurrency?: number;
  currencyPerPoint?: number;
  pointsExpiryDays?: number;
  cashbackPercentage?: number;
  minOrderAmountForCashback?: number;
  tierRules?: Record<string, any>; // Flexible JSON for tiers
  minPointsToRedeem?: number;
  maxDiscountPercentage?: number;
  extraSettings?: Record<string, any>; // Extra flexible JSON
  isActive?: boolean;
  createdAt?: string; // ISO string
  updatedAt?: string; // ISO string
}

export interface CreateLoyaltySettingsRequest {
  enabled?: boolean;
  programName?: string;
  loyaltyType: LoyaltyType;
  pointsPerCurrency?: number;
  currencyPerPoint?: number;
  pointsExpiryDays?: number;
  cashbackPercentage?: number;
  minOrderAmountForCashback?: number;
  tierRules?: Record<string, any>;
  minPointsToRedeem?: number;
  maxDiscountPercentage?: number;
  extraSettings?: Record<string, any>;
}

export interface UpdateLoyaltySettingsRequest {
  enabled?: boolean;
  programName?: string;
  loyaltyType?: LoyaltyType;
  pointsPerCurrency?: number;
  currencyPerPoint?: number;
  pointsExpiryDays?: number;
  cashbackPercentage?: number;
  minOrderAmountForCashback?: number;
  tierRules?: Record<string, any>;
  minPointsToRedeem?: number;
  maxDiscountPercentage?: number;
  extraSettings?: Record<string, any>;
  isActive?: boolean;
}
