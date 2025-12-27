export interface POSGeneralSettingsResponse {
  id: number;

  // Warehouse
  warehouseId: number;
  warehouseName: string;

  // Company
  companyId: number;
  companyName: string;

  // Default customer
  defaultCustomerId?: number;
  defaultCustomerName?: string;

  // Default currency (warehouse-level)
  defaultCurrencyId: number;
  defaultCurrencyCode: string;
  defaultCurrencySymbol: string;

  // Default payment method
  defaultPaymentMethod?: string;

  // Tax inclusive
  defaultTaxInclusive: boolean;

  // Audit fields
  createdBy?: number;
  createdAt: string;
  updatedBy?: number;
  updatedAt?: string | null;
}

export interface CreatePOSGeneralSettingsRequest {
  defaultCustomerId?: number;
  defaultCurrencyId: number;
  defaultPaymentMethod?: string;
  defaultTaxInclusive?: boolean;
}

export interface UpdatePOSGeneralSettingsRequest {
  defaultCustomerId?: number;
  defaultCurrencyId?: number;
  defaultPaymentMethod?: string;
  defaultTaxInclusive?: boolean;
}
