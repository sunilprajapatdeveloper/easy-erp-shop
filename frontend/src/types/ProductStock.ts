// ProductStock Response from backend
export interface ProductStockResponse {
  id: number;
  productId: number;
  warehouseId: number;

  quantity: number;
  reservedQuantity: number;
  inTransitQuantity: number;
  committedQuantity: number;

  minStockLevel?: number;
  maxStockLevel?: number;
  reorderLevel?: number;

  stockAlert?: boolean;
  averageCost?: string;

  lastCountDate?: string;
  nextCountDate?: string;

  availableQuantity: number;
}

// Create ProductStock Request
export interface CreateProductStockRequest {
  productId: number;
  warehouseId: number;
  quantity?: number;
  reservedQuantity?: number;
  inTransitQuantity?: number;
  committedQuantity?: number;
  minStockLevel?: number;
  maxStockLevel?: number;
  reorderLevel?: number;
  averageCost?: string;
  lastCountDate?: string;
  nextCountDate?: string;
}

// Update ProductStock Request
export interface UpdateProductStockRequest {
  id: number;
  productId: number;
  warehouseId: number;
  quantity?: number;
  reservedQuantity?: number;
  inTransitQuantity?: number;
  committedQuantity?: number;
  minStockLevel?: number;
  maxStockLevel?: number;
  reorderLevel?: number;
  stockAlert?: boolean;
  averageCost?: string;
  lastCountDate?: string;
  nextCountDate?: string;
}

export type ProductStock = ProductStockResponse;
