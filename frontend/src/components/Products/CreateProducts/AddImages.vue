<!-- src/components/products/AddImages.vue -->
<template>
  <div class="form-group w-xxl-5">
    <label class="d-block fs-14 text-black mb-2">Product Images</label>
    <div class="card upload-gallery border-0 rounded-1 p-xxl-30 p-4">
      <!-- File Upload Area -->
      <div class="file-upload text-center position-relative mb-20">
        <img src="../../../assets/img/icons/upload-files.svg" alt="Upload" />
        <span class="d-block text-optional fs-14 mt-30">
          Drag & Drop Multiple Images here or
          <span class="text_secondary fw-semibold position-relative">
            Browse Files
          </span>
        </span>
        <input type="file" multiple accept="image/*"
          class="d-block w-100 shadow-none border-0 position-absolute start-0 end-0 top-0 bottom-0 z-1 opacity-0"
          @change="handleFileSelect" ref="fileInput" />
      </div>

      <!-- Selected Files Preview -->
      <div v-if="selectedFiles.length > 0" class="mb-20">
        <h6 class="fs-14 text-black mb-3">Selected Files ({{ selectedFiles.length }})</h6>
        <div class="d-flex flex-wrap gap-3">
          <div v-for="(file, index) in selectedFiles" :key="index" class="position-relative">
            <div class="preview-thumbnail">
              <img :src="file.preview" :alt="file.name" class="img-fluid rounded"
                style="width: 80px; height: 80px; object-fit: cover;" />
              <button type="button" class="btn btn-danger btn-sm position-absolute top-0 end-0 translate-middle"
                @click="removeSelectedFile(index)" style="width: 20px; height: 20px; padding: 0; font-size: 10px;">
                ×
              </button>
              <small class="d-block mt-1 text-truncate" style="width: 80px; font-size: 11px;">
                {{ file.name }}
              </small>
            </div>
          </div>
        </div>
      </div>

      <!-- Existing Images -->
      <div v-if="existingImages.length > 0" class="mb-20">
        <h6 class="fs-14 text-black mb-3">Existing Images ({{ existingImages.length }})</h6>
        <div class="d-flex flex-wrap gap-3">
          <div v-for="media in existingImages" :key="media.id" class="position-relative">
            <div class="existing-thumbnail">
              <img :src="getImageUrl(media)" :alt="media.originalFilename" class="img-fluid rounded"
                style="width: 80px; height: 80px; object-fit: cover;" />
              <button type="button" class="btn btn-danger btn-sm position-absolute top-0 end-0 translate-middle"
                @click="removeExistingImage(media.id)" style="width: 20px; height: 20px; padding: 0; font-size: 10px;">
                ×
              </button>
              <small class="d-block mt-1 text-truncate" style="width: 80px; font-size: 11px;">
                {{ media.originalFilename }}
              </small>
              <small class="d-block text-muted" style="font-size: 9px;">
                {{ formatFileSize(media.fileSize) }}
              </small>
            </div>
          </div>
        </div>
      </div>

      <!-- Upload Progress -->
      <div v-if="uploading" class="mb-20">
        <div class="progress" style="height: 6px;">
          <div class="progress-bar progress-bar-striped progress-bar-animated" :style="{ width: uploadProgress + '%' }">
          </div>
        </div>
        <small class="text-muted d-block mt-1 text-center">Uploading... {{ uploadProgress }}%</small>
      </div>

      <!-- Upload Button -->
      <button v-if="selectedFiles.length > 0 && !uploading" type="button" class="btn btn-primary btn-sm w-100"
        @click="uploadImages" :disabled="uploading || !productId">
        <i class="fas fa-upload me-2"></i>
        Upload {{ selectedFiles.length }} Image{{ selectedFiles.length > 1 ? 's' : '' }}
      </button>

      <!-- Info Messages -->
      <div v-if="!productId" class="alert alert-warning mt-3 mb-0 p-2">
        <small>
          <i class="fas fa-info-circle me-2"></i>
          Save the product first to upload images
        </small>
      </div>

      <div v-if="existingImages.length === 0 && selectedFiles.length === 0" class="alert alert-info mt-3 mb-0 p-2">
        <small>
          <i class="fas fa-info-circle me-2"></i>
          No images added yet. Select images to upload.
        </small>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, watch } from 'vue';
import { useMediaStore } from '@/stores/mediaStore';
import { useUserStore } from '@/stores/userStore';
import type { MediaResponse } from '@/types/Media';

interface SelectedFile {
  file: File;
  name: string;
  preview: string;
  size: number;
}

export default defineComponent({
  name: 'AddImages',
  props: {
    productId: {
      type: Number,
      default: null
    },
    existingMedia: {
      type: Array as () => MediaResponse[],
      default: () => []
    }
  },
  emits: ['images-uploaded', 'image-deleted'],

  setup(props, { emit }) {
    const mediaStore = useMediaStore();
    const userStore = useUserStore();

    const fileInput = ref<HTMLInputElement | null>(null);
    const selectedFiles = ref<SelectedFile[]>([]);
    const existingImages = ref<MediaResponse[]>([]);
    const uploading = ref(false);
    const uploadProgress = ref(0);

    // Helper to get the best image URL
    const getImageUrl = (media: MediaResponse): string => {
      return media.thumbnailUrl || media.url;
    };

    // Helper to format file size
    const formatFileSize = (bytes: number): string => {
      if (bytes === 0) return '0 Bytes';
      const k = 1024;
      const sizes = ['Bytes', 'KB', 'MB', 'GB'];
      const i = Math.floor(Math.log(bytes) / Math.log(k));
      return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
    };

    // Watch for changes in existingMedia prop
    watch(() => props.existingMedia, (newMedia) => {
      existingImages.value = newMedia || [];
    }, { immediate: true });

    const handleFileSelect = (event: Event) => {
      const input = event.target as HTMLInputElement;
      if (!input.files?.length) return;

      const files = Array.from(input.files);

      files.forEach(file => {
        // Validate file type
        if (!file.type.startsWith('image/')) {
          alert(`File "${file.name}" is not an image. Please select image files only.`);
          return;
        }

        // Validate file size (max 5MB)
        if (file.size > 5 * 1024 * 1024) {
          alert(`File "${file.name}" exceeds 5MB limit.`);
          return;
        }

        // Create preview
        const preview = URL.createObjectURL(file);

        selectedFiles.value.push({
          file,
          name: file.name,
          preview,
          size: file.size
        });
      });

      // Reset file input to allow selecting same files again
      if (fileInput.value) {
        fileInput.value.value = '';
      }
    };

    const removeSelectedFile = (index: number) => {
      // Revoke object URL to prevent memory leaks
      if (selectedFiles.value[index]?.preview) {
        URL.revokeObjectURL(selectedFiles.value[index].preview);
      }
      selectedFiles.value.splice(index, 1);
    };

    const removeExistingImage = async (mediaId: string) => {
      if (!confirm('Are you sure you want to delete this image?')) {
        return;
      }

      try {
        await mediaStore.removeMedia(mediaId);
        existingImages.value = existingImages.value.filter(img => img.id !== mediaId);
        emit('image-deleted', mediaId);

        alert('Image deleted successfully');
      } catch (error) {
        console.error('Failed to delete image:', error);
        alert('Failed to delete image');
      }
    };

    const uploadImages = async () => {
      if (!props.productId) {
        alert('Please save the product first before uploading images');
        return;
      }

      if (selectedFiles.value.length === 0) return;

      uploading.value = true;
      uploadProgress.value = 0;

      try {
        const companyId = userStore.currentUser?.companyId;
        if (!companyId) {
          throw new Error('Company information is missing');
        }

        const files = selectedFiles.value.map(sf => sf.file);

        // Upload files - NO isPrimary parameter
        const uploadedMedia = await mediaStore.uploadMultipleFiles(files, {
          entityType: 'PRODUCT',
          entityId: props.productId,
          // No isPrimary here
        });

        // Clear selected files
        selectedFiles.value.forEach(sf => {
          URL.revokeObjectURL(sf.preview);
        });
        selectedFiles.value = [];

        // Add new images to existing list
        existingImages.value = [...existingImages.value, ...uploadedMedia];

        emit('images-uploaded', uploadedMedia);

        alert(`Successfully uploaded ${uploadedMedia.length} image(s)`);
      } catch (error: any) {
        console.error('Upload failed:', error);
        alert(`Upload failed: ${error.message || 'Unknown error'}`);
      } finally {
        uploading.value = false;
        uploadProgress.value = 0;
      }
    };

    // Clean up object URLs when component is destroyed
    const cleanup = () => {
      selectedFiles.value.forEach(sf => {
        URL.revokeObjectURL(sf.preview);
      });
    };

    return {
      fileInput,
      selectedFiles,
      existingImages,
      uploading,
      uploadProgress,
      getImageUrl,
      formatFileSize,
      handleFileSelect,
      removeSelectedFile,
      removeExistingImage,
      uploadImages,
      cleanup
    };
  },

  beforeUnmount() {
    this.cleanup();
  }
});
</script>

<style lang="scss" scoped>
.upload-gallery {
  .file-upload {
    padding: 40px 20px;
    border: 1.5px dashed #a8acb2;
    text-align: center;
    border-radius: 4px;
    cursor: pointer;
    transition: border-color 0.3s ease;

    &:hover {
      border-color: #007bff;
    }

    input {
      width: 100%;
      height: 100%;
      cursor: pointer;
    }
  }
}

.preview-thumbnail,
.existing-thumbnail {
  transition: transform 0.2s ease;

  &:hover {
    transform: scale(1.05);
  }
}

@media only screen and (min-width: 1400px) {
  .upload-gallery {
    .file-upload {
      padding: 40px 75px 35px;
    }
  }
}
</style>