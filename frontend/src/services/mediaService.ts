import api from "./api";
import { useUserStore } from "@/stores/userStore";
import type {
  MediaResponse,
  MediaUploadRequest,
  MoveMediaRequest,
  PaginatedMediaResponse,
  StorageUsageResponse,
} from "@/types/Media";

// Helper: build headers
const getHeaders = () => {
  const userStore = useUserStore();
  const userId = userStore.currentUser?.id;

  if (!userId) {
    throw new Error("User information is missing.");
  }

  return {
    "X-User-Id": userId,
  };
};

/**
 * Upload single file
 */
export const uploadFile = (
  file: File,
  request: MediaUploadRequest,
  userId: number
) => {
  const formData = new FormData();
  formData.append("file", file);
  formData.append("companyId", request.companyId.toString());
  formData.append("entityType", request.entityType);
  formData.append("entityId", request.entityId.toString());
  if (request.isPrimary !== undefined) {
    formData.append("isPrimary", request.isPrimary.toString());
  }

  return api.post<MediaResponse>("/media/upload", formData, {
    headers: {
      ...getHeaders(),
      "Content-Type": "multipart/form-data",
    },
  });
};

/**
 * Upload multiple files
 */
export const uploadFiles = (
  files: File[],
  request: MediaUploadRequest,
  userId: number
) => {
  const formData = new FormData();
  files.forEach((file) => {
    formData.append("files", file);
  });
  formData.append("companyId", request.companyId.toString());
  formData.append("entityType", request.entityType);
  formData.append("entityId", request.entityId.toString());
  if (request.isPrimary !== undefined) {
    formData.append("isPrimary", request.isPrimary.toString());
  }

  return api.post<MediaResponse[]>("/media/upload/multiple", formData, {
    headers: {
      ...getHeaders(),
      "Content-Type": "multipart/form-data",
    },
  });
};

/**
 * Get media by ID
 */
export const getMedia = (mediaId: string, companyId: number) =>
  api.get<MediaResponse>(`/media/${mediaId}`, {
    params: { companyId },
    headers: getHeaders(),
  });

/**
 * Get media by entity
 */
export const getMediaByEntity = (
  entityType: string,
  entityId: number,
  companyId: number
) =>
  api.get<MediaResponse[]>(`/media/entity/${entityType}/${entityId}`, {
    params: { companyId },
    headers: getHeaders(),
  });

/**
 * Get company media with pagination
 */
export const getCompanyMedia = (
  companyId: number,
  pageable?: {
    page?: number;
    size?: number;
    sort?: string;
  }
) => {
  const params: any = { companyId };
  if (pageable) {
    if (pageable.page !== undefined) params.page = pageable.page;
    if (pageable.size !== undefined) params.size = pageable.size;
    if (pageable.sort) params.sort = pageable.sort;
  }

  return api.get<PaginatedMediaResponse>("/media/company", {
    params,
    headers: getHeaders(),
  });
};

/**
 * Get public URL for media
 */
export const getMediaUrl = (mediaId: string, companyId: number) =>
  api.get<{ url: string }>(`/media/${mediaId}/url`, {
    params: { companyId },
    headers: getHeaders(),
  });

/**
 * Get signed URL for media
 */
export const getSignedUrl = (
  mediaId: string,
  companyId: number,
  expiryMinutes: number = 60
) =>
  api.get<{ url: string }>(`/media/${mediaId}/signed-url`, {
    params: { companyId, expiryMinutes },
    headers: getHeaders(),
  });

/**
 * Delete media
 */
export const deleteMedia = (mediaId: string, companyId: number) =>
  api.delete<void>(`/media/${mediaId}`, {
    params: { companyId },
    headers: getHeaders(),
  });

/**
 * Delete media by entity
 */
export const deleteMediaByEntity = (
  entityType: string,
  entityId: number,
  companyId: number
) =>
  api.delete<void>(`/media/entity/${entityType}/${entityId}`, {
    params: { companyId },
    headers: getHeaders(),
  });

/**
 * Move media to another entity
 */
export const moveMedia = (mediaId: string, request: MoveMediaRequest) =>
  api.put<MediaResponse>(`/media/${mediaId}/move`, null, {
    params: {
      newEntityType: request.newEntityType,
      newEntityId: request.newEntityId,
      companyId: request.companyId,
    },
    headers: getHeaders(),
  });

/**
 * Get storage usage
 */
export const getStorageUsage = (companyId: number) =>
  api.get<StorageUsageResponse>("/media/storage/usage", {
    params: { companyId },
    headers: getHeaders(),
  });

/**
 * Helper to get local file URL
 */
export const getLocalMediaUrl = (
  companyId: number,
  filename: string,
  thumb: boolean = false
) => {
  return `/api/v1/media/local/${companyId}/${filename}?thumb=${thumb}`;
};

/**
 * Helper to get media file URL by ID
 */
export const getMediaFileUrl = (
  mediaId: string,
  companyId: number,
  thumb: boolean = false
) => {
  return `/api/v1/media/${mediaId}/file?companyId=${companyId}&thumb=${thumb}`;
};

/**
 * Helper to serve file directly
 */
export const getDirectFileUrl = (
  companyId: number,
  entityType: string,
  entityId: number,
  filename: string,
  thumb: boolean = false
) => {
  return `/api/v1/media/serve/${companyId}/${entityType}/${entityId}/${filename}?thumb=${thumb}`;
};
