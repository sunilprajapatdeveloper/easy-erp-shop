export interface MediaResponse {
  id: string;
  originalFilename: string;
  storedFilename: string;
  url: string;
  thumbnailUrl: string;
  fileSize: number;
  mimeType: string;
  extension: string;
  storageProvider: string;
  isPublic: boolean;
  entityType: string;
  entityId: number;
  companyId: number;
  warehouseId: number | null;
  uploadedBy: number;
  uploadedAt: string;
  createdAt: string;
  updatedAt: string;
  metadata: string | null;
}

// Request types for creating/updating media
export interface CreateMediaRequest {
  companyId: number;
  entityType: string;
  entityId: number;
  file: File;
  isPrimary?: boolean;
}

export interface UpdateMediaRequest {
  id: string;
  isPrimary?: boolean;
}

// Request type for upload (form data)
export interface MediaUploadRequest {
  companyId: number;
  entityType: string;
  entityId: number;
  isPrimary?: boolean;
}

// Request for moving media
export interface MoveMediaRequest {
  newEntityType: string;
  newEntityId: number;
  companyId: number;
}

// Storage usage response
export interface StorageUsageResponse {
  totalSpace: number;
  usedSpace: number;
  availableSpace: number;
  usagePercentage: number;
  totalFiles: number;
}

// Pageable interface
export interface Pageable {
  page?: number;
  size?: number;
  sort?: string;
}

// Paginated response
export interface PaginatedMediaResponse {
  content: MediaResponse[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
}

// For UI representation
export interface SelectedMedia {
  id: string;
  originalFilename: string;
  url: string;
  thumbnailUrl: string;
  fileSize: number;
  mimeType: string;
  isPrimary: boolean;
  entityType: string;
  entityId: number;
}

export type Media = MediaResponse;
