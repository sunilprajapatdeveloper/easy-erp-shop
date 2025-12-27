export interface Customer {
  id: number;
  name: string;
  email: string;
  phone: string;
  country: string;
  city: string;
  createdBy?: number;
  createdAt?: string;
  updatedBy?: number;
  updatedAt?: string | null;
  companyId?: number;
}

export interface CreateCustomerRequest {
  name: string;
  email: string;
  phone?: string;
  country?: string;
  city?: string;
}
