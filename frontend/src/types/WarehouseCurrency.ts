import { CurrencyStatus } from "@/enums/CurrencyStatus";

export interface WarehouseCurrency {
  id: number;
  currencyId: number;
  currencyCode: string;
  currencyName: string;
  symbol: string;
  decimalPlaces: number;
  defaultCurrency: boolean;
  status: CurrencyStatus;
  companyId: number;
  warehouseId: number;
}

export interface CreateWarehouseCurrencyRequest {
  currencyId: number;
  decimalPlaces?: number;
  defaultCurrency?: boolean;
  status: CurrencyStatus;
}

export interface UpdateWarehouseCurrencyRequest {
  currencyId?: number;
  decimalPlaces?: number;
  defaultCurrency?: boolean;
  status?: CurrencyStatus;
}
