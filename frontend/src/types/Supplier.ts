// types/Supplier.ts

// Supplier entity from backend
export interface Supplier {
  id: number;
  name: string;
  email?: string;
  phone?: string;
  country?: string;
  city?: string;
  address?: string;
  taxNumber?: string;
  createdBy: number;
  createdAt: string;
  updatedBy?: number;
  updatedAt?: string | null;
  companyId: number;
  isActive?: boolean;
  externalCode?: string;
}

// Create Supplier Request
export interface CreateSupplierRequest {
  name: string;
  email?: string;
  phone?: string;
  country?: string;
  city?: string;
  address?: string;
  taxNumber?: string;
}

// Update Supplier Request
export interface UpdateSupplierRequest {
  name?: string;
  email?: string;
  phone?: string;
  country?: string;
  city?: string;
  address?: string;
  taxNumber?: string;
}
