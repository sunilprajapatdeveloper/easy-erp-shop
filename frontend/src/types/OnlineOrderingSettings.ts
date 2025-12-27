export interface OnlineOrderingSettings {
  id: number;
  companyId: number;
  enabled: boolean;
  orderingUrl?: string;
  minOrderValue?: number;
  estimatedDeliveryTime?: number;
  selfPickupEnabled: boolean;
  deliveryEnabled: boolean;
  integrationKey?: string;
  customerNotes?: string;
  integrationConfig?: Record<string, any>;
  createdBy: number;
  createdAt: string;
  updatedBy?: number;
  updatedAt?: string;
}

export interface CreateOnlineOrderingSettingsRequest {
  companyId: number;
  enabled: boolean;
  orderingUrl?: string;
  minOrderValue?: number;
  estimatedDeliveryTime?: number;
  selfPickupEnabled: boolean;
  deliveryEnabled: boolean;
  integrationKey?: string;
  customerNotes?: string;
  integrationConfig?: Record<string, any>;
}

export interface UpdateOnlineOrderingSettingsRequest {
  companyId: number;
  enabled?: boolean;
  orderingUrl?: string;
  minOrderValue?: number;
  estimatedDeliveryTime?: number;
  selfPickupEnabled?: boolean;
  deliveryEnabled?: boolean;
  integrationKey?: string;
  customerNotes?: string;
  integrationConfig?: Record<string, any>;
}
