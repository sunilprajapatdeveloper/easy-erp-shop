import type { Currency } from "./Currency";
import type { CompanyListItem } from "./Company";
import type { WarehouseListItem } from "./Warehouse";

export enum ExchangeRateLevel {
  GLOBAL = "GLOBAL",
  COMPANY = "COMPANY",
  WAREHOUSE = "WAREHOUSE",
}

export const ExchangeRateLevelLabels: Record<ExchangeRateLevel, string> = {
  [ExchangeRateLevel.GLOBAL]: "Global",
  [ExchangeRateLevel.COMPANY]: "Company",
  [ExchangeRateLevel.WAREHOUSE]: "Warehouse",
};

export interface ExchangeRateListItem {
  id: number;
  baseCurrency: Currency;
  targetCurrency: Currency;
  rate: string;
  level: ExchangeRateLevel;
  company?: CompanyListItem;
  warehouse?: WarehouseListItem;
  isManualOverride: boolean;
  validFrom: string;
  validTo?: string;
}

export interface ExchangeRateDetail extends ExchangeRateListItem {
  bidRate?: string;
  askRate?: string;
  spreadPercentage?: string;
  overrideReason?: string;
  rateSource: string;
  providerName?: string;
  providerReferenceId?: string;
  createdAt: string;
  updatedAt: string;
  version: number;
}

export interface CreateExchangeRateRequest {
  baseCurrencyId: number;
  targetCurrencyId: number;
  rate: string;
  bidRate?: string;
  askRate?: string;
  level: ExchangeRateLevel;
  companyId?: number;
  warehouseId?: number;
  rateSource: string;
  providerName?: string;
  providerReferenceId?: string;
  spreadPercentage?: string;
  isManualOverride?: boolean;
  overrideReason?: string;
  validFrom: string;
  validTo?: string;
}

export type UpdateExchangeRateRequest = Partial<CreateExchangeRateRequest>;
