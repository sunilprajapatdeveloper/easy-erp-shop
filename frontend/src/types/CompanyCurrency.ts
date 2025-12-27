import { CurrencyStatus } from "@/enums/CurrencyStatus";

export interface CompanyCurrency {
  id: number;
  currencyId: number;
  currencyCode: string;
  currencyName: string;
  symbol: string;
  decimalPlaces: number;
  defaultCurrency: boolean;
  status: CurrencyStatus;
  companyId: number;
}

export interface CreateCompanyCurrencyRequest {
  currencyId: number;
  decimalPlaces?: number;
  defaultCurrency?: boolean;
  status: CurrencyStatus;
}

export interface UpdateCompanyCurrencyRequest {
  currencyId?: number;
  decimalPlaces?: number;
  defaultCurrency?: boolean;
  status?: CurrencyStatus;
}
