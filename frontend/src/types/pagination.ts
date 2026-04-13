export interface PaginationRequest {
  page: number;
  size: number;
  sort?: string;
  search?: string;
  categoryId?: number;
  brandId?: number;
  status?: string;
  productType?: string;
  warehouseId?: number;
  includePrice?: boolean;
  includeStock?: boolean;
  includeTax?: boolean;
}

export interface PaginationResponse<T> {
  data: T[];
  pagination: {
    page: number;
    size: number;
    totalElements: number;
    totalPages: number;
    hasNext: boolean;
    hasPrevious: boolean;
  };
}
