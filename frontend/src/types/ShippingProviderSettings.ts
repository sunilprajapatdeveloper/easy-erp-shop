import { ShippingProvider } from "@/enums/ShippingProvider";

export interface ShippingProviderSettings {
  id: number;
  companyId: number;
  warehouseId: number;
  providerName: ShippingProvider;
  accountId: string;
  apiKey?: string;
  apiSecret?: string;
  apiEndpoint?: string;
  enabled: boolean;
  serviceRegions?: string;
  providerConfig?: Record<string, any>;
  createdBy?: number;
  createdAt?: string;
  updatedBy?: number;
  updatedAt?: string;
}

export interface CreateShippingProviderSettingsRequest {
  companyId: number;
  warehouseId: number;
  providerName: ShippingProvider;
  accountId: string;
  apiKey?: string;
  apiSecret?: string;
  apiEndpoint?: string;
  enabled?: boolean;
  serviceRegions?: string;
  providerConfig?: Record<string, any>;
}

export interface UpdateShippingProviderSettingsRequest {
  providerName?: ShippingProvider;
  accountId?: string;
  apiKey?: string;
  apiSecret?: string;
  apiEndpoint?: string;
  enabled?: boolean;
  serviceRegions?: string;
  providerConfig?: Record<string, any>;
}
