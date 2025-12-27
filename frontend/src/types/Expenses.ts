export interface Expenses {
  id: number;
  warehouseId: number;
  categoryId: number;
  date: string;
  amount: number;
  details?: string;
  createdBy?: number;
  createdAt?: string;
  updatedBy?: number;
  updatedAt?: string;
  companyId: number;
}

export interface CreateExpensesRequest {
  warehouseId: number;
  categoryId: number;
  date: string;
  amount: number;
  details?: string;
}

export interface UpdateExpensesRequest {
  warehouseId?: number;
  categoryId?: number;
  date?: string;
  amount?: number;
  details?: string;
}
