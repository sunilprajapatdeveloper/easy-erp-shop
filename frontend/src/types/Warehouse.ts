export interface WarehouseListItem {
  id: number;
  name: string;
  city: string;
  country: string;
  headquarter: boolean;
  isDefault: boolean;
  currencyId: number;
}

export interface WarehouseDetail {
  id: number;
  name: string;
  phone?: string;
  email?: string;
  addressLine1?: string;
  addressLine2?: string;
  city: string;
  state?: string;
  country: string;
  zipCode?: string;

  headquarter: boolean;
  isDefault: boolean;
  currencyId: number;
  timezone: string;
  active: boolean;
  applyTax: boolean;
  applyTds: boolean;
  trackInventory: boolean;
  invoicePrefix?: string;

  companyId: number;
  createdBy?: number;
  createdAt: string;
  updatedBy?: number;
  updatedAt?: string | null;
  isDeleted: boolean;
}

export interface CreateWarehouseRequest {
  name: string;
  phone?: string;
  email?: string;
  addressLine1?: string;
  addressLine2?: string;
  city: string;
  state?: string;
  country: string;
  zipCode?: string;

  headquarter?: boolean;
  isDefault?: boolean;
  currencyId: number;
  timezone?: string;
  active?: boolean;
  applyTax?: boolean;
  applyTds?: boolean;
  trackInventory?: boolean;
  invoicePrefix?: string;

  companyId: number;
  createdBy?: number;
}

export interface UpdateWarehouseRequest {
  id: number; // required for update
  name?: string;
  phone?: string;
  email?: string;
  addressLine1?: string;
  addressLine2?: string;
  city?: string;
  state?: string;
  country?: string;
  zipCode?: string;

  headquarter?: boolean;
  isDefault?: boolean;
  currencyId?: number;
  timezone?: string;
  active?: boolean;
  applyTax?: boolean;
  applyTds?: boolean;
  trackInventory?: boolean;
  invoicePrefix?: string;

  companyId?: number;
  updatedBy?: number;
}

export type Warehouse = WarehouseDetail;
