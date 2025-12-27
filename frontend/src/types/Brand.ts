export interface Brand {
  id: number;
  name: string;
  image?: string;
  description?: string;
  createdBy?: number;
  createdAt?: string;
  updatedBy?: number;
  updatedAt?: string | null;
  companyId?: number;
}

export type CreateBrandRequest = Omit<
  Brand,
  "id" | "createdBy" | "createdAt" | "updatedBy" | "updatedAt" | "companyId"
>;
