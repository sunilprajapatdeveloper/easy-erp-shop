export interface PaginationRequest {
  page: number; // zero‑based page number
  size: number; // page size
  sort?: string; // e.g., "name,asc" or "createdAt,desc"
  search?: string; // optional search term
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
