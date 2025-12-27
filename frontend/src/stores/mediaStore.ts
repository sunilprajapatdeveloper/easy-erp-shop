import { defineStore } from "pinia";
import type {
  MediaResponse,
  MediaUploadRequest,
  MoveMediaRequest,
  PaginatedMediaResponse,
  StorageUsageResponse,
} from "@/types/Media";
import {
  uploadFile,
  uploadFiles,
  getMedia,
  getMediaByEntity,
  getCompanyMedia,
  getMediaUrl,
  getSignedUrl,
  deleteMedia,
  deleteMediaByEntity,
  moveMedia,
  getStorageUsage,
} from "@/services/mediaService";
import { useUserStore } from "@/stores/userStore";

export const useMediaStore = defineStore("media", {
  state: () => ({
    mediaList: [] as MediaResponse[],
    companyMedia: {
      content: [] as MediaResponse[],
      totalPages: 0,
      totalElements: 0,
      size: 20,
      number: 0,
    } as PaginatedMediaResponse,
    currentMedia: null as MediaResponse | null,
    loading: false,
    uploading: false,
    error: null as string | null,
    storageUsage: null as StorageUsageResponse | null,
  }),

  getters: {
    getMediaById: (state) => (id: string) => {
      return state.mediaList.find((media) => media.id === id);
    },

    getMediaByEntityType: (state) => (entityType: string, entityId: number) => {
      return state.mediaList.filter(
        (media) =>
          media.entityType === entityType && media.entityId === entityId
      );
    },

    getPrimaryMedia: (state) => (entityType: string, entityId: number) => {
      return state.mediaList.find(
        (media) =>
          media.entityType === entityType &&
          media.entityId === entityId &&
          media.isPublic // Adjust based on your actual primary flag logic
      );
    },

    isLoading: (state) => state.loading || state.uploading,

    totalStorageUsed(): number {
      return this.storageUsage?.usedSpace || 0;
    },

    storageUsagePercentage(): number {
      return this.storageUsage?.usagePercentage || 0;
    },
  },

  actions: {
    /**
     * Upload single file
     */
    async uploadSingleFile(
      file: File,
      request: Omit<MediaUploadRequest, "companyId">
    ) {
      this.uploading = true;
      this.error = null;
      try {
        const userStore = useUserStore();
        const companyId = userStore.currentUser?.companyId;
        const userId = userStore.currentUser?.id;

        if (!companyId || !userId) {
          throw new Error("User or company information is missing.");
        }

        const uploadRequest: MediaUploadRequest = {
          ...request,
          companyId,
        };

        const res = await uploadFile(file, uploadRequest, userId);
        this.mediaList.push(res.data);
        return res.data;
      } catch (err: any) {
        console.error("Upload failed:", err);
        this.error = err.message ?? "Failed to upload file";
        throw err;
      } finally {
        this.uploading = false;
      }
    },

    /**
     * Upload multiple files
     */
    async uploadMultipleFiles(
      files: File[],
      request: Omit<MediaUploadRequest, "companyId">
    ) {
      this.uploading = true;
      this.error = null;
      try {
        const userStore = useUserStore();
        const companyId = userStore.currentUser?.companyId;
        const userId = userStore.currentUser?.id;

        if (!companyId || !userId) {
          throw new Error("User or company information is missing.");
        }

        const uploadRequest: MediaUploadRequest = {
          ...request,
          companyId,
        };

        const res = await uploadFiles(files, uploadRequest, userId);
        this.mediaList.push(...res.data);
        return res.data;
      } catch (err: any) {
        console.error("Upload failed:", err);
        this.error = err.message ?? "Failed to upload files";
        throw err;
      } finally {
        this.uploading = false;
      }
    },

    /**
     * Fetch media by entity
     */
    async fetchMediaByEntity(entityType: string, entityId: number) {
      this.loading = true;
      this.error = null;
      try {
        const userStore = useUserStore();
        const companyId = userStore.currentUser?.companyId;

        if (!companyId) {
          throw new Error("Company information is missing.");
        }

        const res = await getMediaByEntity(entityType, entityId, companyId);
        this.mediaList = res.data;
      } catch (err: any) {
        console.error("Fetch failed:", err);
        this.error = err.message ?? "Failed to fetch media by entity";
      } finally {
        this.loading = false;
      }
    },

    /**
     * Fetch company media with pagination
     */
    async fetchCompanyMedia(pageable?: {
      page?: number;
      size?: number;
      sort?: string;
    }) {
      this.loading = true;
      this.error = null;
      try {
        const userStore = useUserStore();
        const companyId = userStore.currentUser?.companyId;

        if (!companyId) {
          throw new Error("Company information is missing.");
        }

        const res = await getCompanyMedia(companyId, pageable);
        this.companyMedia = res.data;
      } catch (err: any) {
        console.error("Fetch failed:", err);
        this.error = err.message ?? "Failed to fetch company media";
      } finally {
        this.loading = false;
      }
    },

    /**
     * Fetch media by ID
     */
    async fetchMediaById(mediaId: string): Promise<MediaResponse | null> {
      this.loading = true;
      this.error = null;
      try {
        const userStore = useUserStore();
        const companyId = userStore.currentUser?.companyId;

        if (!companyId) {
          throw new Error("Company information is missing.");
        }

        const res = await getMedia(mediaId, companyId);
        this.currentMedia = res.data;
        return res.data;
      } catch (err: any) {
        console.error("Fetch failed:", err);
        this.error = err.message ?? "Failed to fetch media";
        return null;
      } finally {
        this.loading = false;
      }
    },

    /**
     * Get media URL
     */
    async getMediaUrl(mediaId: string): Promise<string | null> {
      try {
        const userStore = useUserStore();
        const companyId = userStore.currentUser?.companyId;

        if (!companyId) {
          throw new Error("Company information is missing.");
        }

        const res = await getMediaUrl(mediaId, companyId);
        return res.data.url;
      } catch (err: any) {
        console.error("Failed to get media URL:", err);
        return null;
      }
    },

    /**
     * Get signed URL
     */
    async getSignedUrl(
      mediaId: string,
      expiryMinutes: number = 60
    ): Promise<string | null> {
      try {
        const userStore = useUserStore();
        const companyId = userStore.currentUser?.companyId;

        if (!companyId) {
          throw new Error("Company information is missing.");
        }

        const res = await getSignedUrl(mediaId, companyId, expiryMinutes);
        return res.data.url;
      } catch (err: any) {
        console.error("Failed to get signed URL:", err);
        return null;
      }
    },

    /**
     * Delete media
     */
    async removeMedia(mediaId: string) {
      try {
        const userStore = useUserStore();
        const companyId = userStore.currentUser?.companyId;

        if (!companyId) {
          throw new Error("Company information is missing.");
        }

        await deleteMedia(mediaId, companyId);

        // Remove from lists
        this.mediaList = this.mediaList.filter((media) => media.id !== mediaId);
        this.companyMedia.content = this.companyMedia.content.filter(
          (media) => media.id !== mediaId
        );

        if (this.currentMedia?.id === mediaId) {
          this.currentMedia = null;
        }
      } catch (err: any) {
        console.error("Delete failed:", err);
        throw err;
      }
    },

    /**
     * Delete media by entity
     */
    async removeMediaByEntity(entityType: string, entityId: number) {
      try {
        const userStore = useUserStore();
        const companyId = userStore.currentUser?.companyId;

        if (!companyId) {
          throw new Error("Company information is missing.");
        }

        await deleteMediaByEntity(entityType, entityId, companyId);

        // Remove from lists
        this.mediaList = this.mediaList.filter(
          (media) =>
            !(media.entityType === entityType && media.entityId === entityId)
        );
        this.companyMedia.content = this.companyMedia.content.filter(
          (media) =>
            !(media.entityType === entityType && media.entityId === entityId)
        );
      } catch (err: any) {
        console.error("Delete failed:", err);
        throw err;
      }
    },

    /**
     * Move media to another entity
     */
    async moveMediaToEntity(
      mediaId: string,
      newEntityType: string,
      newEntityId: number
    ) {
      try {
        const userStore = useUserStore();
        const companyId = userStore.currentUser?.companyId;

        if (!companyId) {
          throw new Error("Company information is missing.");
        }

        const request: MoveMediaRequest = {
          newEntityType,
          newEntityId,
          companyId,
        };

        const res = await moveMedia(mediaId, request);

        // Update in lists
        const index = this.mediaList.findIndex((media) => media.id === mediaId);
        if (index !== -1) {
          this.mediaList[index] = res.data;
        }

        const companyIndex = this.companyMedia.content.findIndex(
          (media) => media.id === mediaId
        );
        if (companyIndex !== -1) {
          this.companyMedia.content[companyIndex] = res.data;
        }

        if (this.currentMedia?.id === mediaId) {
          this.currentMedia = res.data;
        }

        return res.data;
      } catch (err: any) {
        console.error("Move failed:", err);
        throw err;
      }
    },

    /**
     * Fetch storage usage
     */
    async fetchStorageUsage() {
      try {
        const userStore = useUserStore();
        const companyId = userStore.currentUser?.companyId;

        if (!companyId) {
          throw new Error("Company information is missing.");
        }

        const res = await getStorageUsage(companyId);
        this.storageUsage = res.data;
        return res.data;
      } catch (err: any) {
        console.error("Failed to fetch storage usage:", err);
        throw err;
      }
    },

    /**
     * Clear current media
     */
    clearCurrentMedia() {
      this.currentMedia = null;
    },

    /**
     * Clear all media
     */
    clearAllMedia() {
      this.mediaList = [];
      this.companyMedia = {
        content: [],
        totalPages: 0,
        totalElements: 0,
        size: 20,
        number: 0,
      };
      this.currentMedia = null;
    },
  },
});
