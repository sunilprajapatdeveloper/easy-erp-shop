export interface AdjustmentType {
  id: number;
  name: string;
  description: string;
  stockEffect: "ADD" | "DEDUCT";
  createdBy?: number;
  createdAt?: string;
  updatedBy?: number;
  updatedAt?: string | null;
  companyId?: number;
}
