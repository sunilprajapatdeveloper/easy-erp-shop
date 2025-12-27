export enum AdjustmentType {
  ADD = "ADD",
  DEDUCT = "DEDUCT",
}

export interface Adjustment {
  id: number;
  warehouse: {
    id: number;
    name: string;
  };
  date: string;
  note?: string;
  products: AdjustmentProduct[];
  createdBy?: number;
  createdAt?: string;
  updatedBy?: number;
  updatedAt?: string | null;
  companyId?: number;
}

export interface AdjustmentProduct {
  id: number; // Product ID
  code: string;
  name: string;
  currentQty: number;
  adjustedQty: number;
  stockEffect: AdjustmentType;
}

// Used in internal form state, not backend response
export interface SelectedProduct {
  productId: number;
  productName: string;
  code: string;
  stock: number;
  adjustedQty: number;
  stockEffect: AdjustmentType;
}

// Matches backend CreateAdjustmentRequest DTO
export type CreateAdjustmentRequest = {
  warehouseId: number;
  date: string;
  note?: string;
  products: {
    productId: number;
    adjustedQty: number;
    stockEffect: AdjustmentType;
  }[];
};
