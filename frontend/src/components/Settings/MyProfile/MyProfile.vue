<template>
  <form @submit.prevent="handleSubmit" class="pb-60">
    <div class="row">
      <!-- Profile Image -->
      <div class="col-xxl-3 col-xl-4 col-lg-4">
        <div class="form-group">
          <label for="profileImage" class="d-block fs-14 text-black mb-2">
            {{ form.profileId ? 'Change Profile Image' : 'Add Profile Image' }}
          </label>

          <div class="profile-upload-card border-0 rounded-3 p-4">

            <!-- Profile Preview Container -->
            <div class="profile-preview-wrapper">

              <!-- Actual Image -->
              <img v-if="previewImage || form.profileImageUrl" :src="previewImage || form.profileImageUrl"
                class="profile-preview-img" alt="Profile" />

              <!-- Placeholder -->
              <div v-else class="profile-placeholder">
                <i class="bi bi-person-circle placeholder-icon"></i>
                <span class="text-muted mt-2 d-block">No Image</span>
              </div>

              <!-- Hover Overlay -->
              <div class="profile-overlay" style="user-select: none;">
                <span>Change Image</span>
              </div>

              <!-- File Input -->
              <input id="profileImage" type="file" accept="image/*" @change="onFileChange" :disabled="uploading" />
            </div>

            <!-- Remove Button -->
            <button v-if="previewImage || form.profileImageUrl" type="button"
              class="btn btn-sm btn-outline-danger w-100 mt-3" @click="clearImage" :disabled="uploading">
              <i class="bi bi-trash me-1"></i> Remove Image
            </button>

            <!-- Upload progress -->
            <div v-if="uploading" class="upload-progress mt-3">
              <div class="progress" style="height: 6px;">
                <div class="progress-bar progress-bar-striped progress-bar-animated" role="progressbar"
                  :style="{ width: uploadProgress + '%' }"></div>
              </div>
              <small class="text-muted">{{ uploadProgress }}% uploading...</small>
            </div>

            <div class="alert alert-info mt-3 py-2">
              <small>
                <i class="bi bi-info-circle me-1"></i>
                Max file size: 5MB • Formats: JPG, PNG, GIF, WebP
              </small>
            </div>

          </div>
        </div>
      </div>

      <!-- Profile Fields -->
      <div class="col-xxl-9 col-xl-8 col-lg-8">
        <div class="row gx-xxl-6">
          <div class="col-lg-6">
            <div class="form-group mb-25">
              <label for="firstname" class="d-block fs-14 text-black mb-2">
                First Name <span class="text-danger">*</span>
              </label>
              <input id="firstname" v-model="form.firstname" type="text"
                class="form-control shadow-none fs-14 bg-white rounded-1 text-title"
                :class="{ 'is-invalid': errors.firstname }" required />
              <div v-if="errors.firstname" class="invalid-feedback d-block">
                {{ errors.firstname }}
              </div>
            </div>
          </div>

          <div class="col-lg-6">
            <div class="form-group mb-25">
              <label for="lastname" class="d-block fs-14 text-black mb-2">Last Name</label>
              <input id="lastname" v-model="form.lastname" type="text"
                class="form-control shadow-none fs-14 bg-white rounded-1 text-title"
                :class="{ 'is-invalid': errors.lastname }" />
              <div v-if="errors.lastname" class="invalid-feedback d-block">
                {{ errors.lastname }}
              </div>
            </div>
          </div>

          <div class="col-lg-6">
            <div class="form-group mb-25">
              <label for="username" class="d-block fs-14 text-black mb-2">
                Username <span class="text-danger">*</span>
              </label>
              <input id="username" v-model="form.username" type="text"
                class="form-control shadow-none fs-14 bg-white rounded-1 text-title"
                :class="{ 'is-invalid': errors.username }" required />
              <div v-if="errors.username" class="invalid-feedback d-block">
                {{ errors.username }}
              </div>
              <!-- <small class="text-muted">This will be used for login</small> -->
            </div>
          </div>

          <div class="col-lg-6">
            <div class="form-group mb-25">
              <label for="phone" class="d-block fs-14 text-black mb-2">Phone Number</label>
              <input id="phone" v-model="form.phone" type="tel"
                class="form-control shadow-none fs-14 bg-white rounded-1 text-title"
                :class="{ 'is-invalid': errors.phone }" />
              <div v-if="errors.phone" class="invalid-feedback d-block">
                {{ errors.phone }}
              </div>
            </div>
          </div>

          <div class="col-lg-6">
            <div class="form-group mb-25">
              <label for="email" class="d-block fs-14 text-black mb-2">Email</label>
              <input id="email" v-model="form.email" type="email"
                class="form-control shadow-none fs-14 bg-white rounded-1 text-title" readonly
                style="background-color: #f8f9fa; cursor: not-allowed;" />
              <!-- <div class="small text-muted mt-1">Email cannot be changed</div> -->
            </div>
          </div>

          <div class="col-lg-6">
            <div class="form-group mb-25">
              <label for="role" class="d-block fs-14 text-black mb-2">Role</label>
              <input id="role" :value="userRole" type="text"
                class="form-control shadow-none fs-14 bg-white rounded-1 text-title" readonly
                style="background-color: #f8f9fa; cursor: not-allowed;" />
            </div>
          </div>

          <!-- <div class="col-lg-12">
            <div class="form-group mb-25">
              <label for="bio" class="d-block fs-14 text-black mb-2">Bio</label>
              <textarea id="bio" v-model="form.profile" cols="30" rows="3" placeholder="Tell us about yourself..."
                class="form-control bg-white border rounded-1 resize-none fs-14 text-title"
                :class="{ 'is-invalid': errors.profile }" />
              <div v-if="errors.profile" class="invalid-feedback d-block">
                {{ errors.profile }}
              </div>
              <small class="text-muted">Maximum 500 characters</small>
            </div>
          </div> -->

          <div class="col-12">
            <div class="d-flex gap-3">
              <button class="btn style-one transition border-0 fw-medium text-white rounded-1 fs-md-15 fs-lg-16 px-4"
                type="submit" :disabled="loading || uploading">
                <span v-if="loading">
                  <i class="bi bi-arrow-repeat spin me-2"></i> Updating...
                </span>
                <span v-else>
                  <i class="bi bi-check-circle me-2"></i> Update Profile
                </span>
              </button>

              <button type="button" class="btn btn-outline-secondary rounded-1 fs-md-15 fs-lg-16 px-4"
                @click="resetForm" :disabled="loading || uploading">
                <i class="bi bi-arrow-counterclockwise me-2"></i> Reset
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </form>
</template>

<script setup lang="ts">
import { reactive, ref, computed, onMounted } from "vue";
import { useUserStore } from "@/stores/userStore";
import { useMediaStore } from "@/stores/mediaStore";
import type { User, UpdateUserRequest } from "@/types/User";

// Stores
const userStore = useUserStore();
const mediaStore = useMediaStore();

// State
const loading = ref(false);
const uploading = ref(false);
const uploadProgress = ref(0);
const previewImage = ref<string | null>(null);
const selectedFile = ref<File | null>(null);
const originalUser = ref<User | null>(null);

// Form and errors
const form = reactive({
  firstname: "",
  lastname: "",
  username: "",
  phone: "",
  email: "",
  profile: "",
  profileImageUrl: "",
  profileId: "",
});

const errors = reactive({
  firstname: "",
  lastname: "",
  username: "",
  phone: "",
  profile: "",
});

// Computed
const userRole = computed(() => {
  const roles = ["Admin", "Manager", "Employee", "Viewer"];
  return originalUser.value ? roles[originalUser.value.roleId - 1] || "Unknown" : "Unknown";
});

const hasChanges = computed(() => {
  if (!originalUser.value) return false;

  return (
    form.firstname !== originalUser.value.firstname ||
    form.lastname !== originalUser.value.lastname ||
    form.username !== originalUser.value.username ||
    form.phone !== originalUser.value.phone ||
    form.profile !== originalUser.value.profile ||
    selectedFile.value !== null ||
    (originalUser.value.profileImageUrl && !form.profileImageUrl)
  );
});

// Lifecycle
onMounted(() => {
  loadUserProfile();
});

// Methods
async function loadUserProfile() {
  try {
    loading.value = true;
    const user = userStore.currentUser;

    if (!user) {
      originalUser.value = userStore.currentUser;
    } else {
      originalUser.value = user;
    }

    if (originalUser.value) {
      // Populate form
      form.firstname = originalUser.value.firstname || "";
      form.lastname = originalUser.value.lastname || "";
      form.username = originalUser.value.username || "";
      form.phone = originalUser.value.phone || "";
      form.email = originalUser.value.email || "";
      form.profile = originalUser.value.profile || "";
      form.profileImageUrl = originalUser.value.profileImageUrl || "";
      form.profileId = originalUser.value.profileId || "";

      // Load profile image if exists
      if (originalUser.value.profileId) {
        await loadProfileImage();
      } else if (originalUser.value.profileImageUrl) {
        previewImage.value = originalUser.value.profileImageUrl;
      }
    }
  } catch (error) {
    console.error("Failed to load user profile:", error);
    notify("error", "Failed to load profile data");
  } finally {
    loading.value = false;
  }
}

async function loadProfileImage() {
  if (!originalUser.value?.profileId) return;

  try {
    const media = await mediaStore.fetchMediaById(originalUser.value.profileId);
    if (media) {
      previewImage.value = media.url;
      form.profileImageUrl = media.url;
    }
  } catch (error) {
    console.error("Failed to load profile image:", error);
    // Fallback to URL if media not found
    if (originalUser.value?.profileImageUrl) {
      previewImage.value = originalUser.value.profileImageUrl;
    }
  }
}

function onFileChange(e: Event) {
  const target = e.target as HTMLInputElement;
  const file = target.files?.[0];
  if (!file) return;

  selectedFile.value = file;

  const reader = new FileReader();
  reader.onload = (ev) => {
    previewImage.value = ev.target?.result as string;
  };
  reader.readAsDataURL(file);
}

function validateImageFile(file: File): boolean {
  const maxSize = 5 * 1024 * 1024; // 5MB
  const allowedTypes = ['image/jpeg', 'image/png', 'image/gif', 'image/webp'];

  // Check file type
  if (!allowedTypes.includes(file.type)) {
    notify("error", "Only JPG, PNG, GIF, and WebP images are allowed");
    return false;
  }

  // Check file size
  if (file.size > maxSize) {
    notify("error", "Image size must be less than 5MB");
    return false;
  }

  return true;
}

async function handleSubmit() {
  if (!originalUser.value || !hasChanges.value) {
    notify("info", "No changes to save");
    return;
  }

  // Validate form
  if (!validateForm()) return;

  try {
    loading.value = true;
    uploading.value = true;

    // Simulate upload progress (in real app, you'd use actual progress events)
    uploadProgress.value = 0;
    const progressInterval = setInterval(() => {
      if (uploadProgress.value < 90) {
        uploadProgress.value += 10;
      }
    }, 100);

    // Prepare update data
    const updateData: UpdateUserRequest = {
      id: originalUser.value.id,
      firstname: form.firstname,
      lastname: form.lastname,
      username: form.username,
      phone: form.phone,
      profileImageUrl: form.profile,
      roleId: originalUser.value.roleId,
      companyId: originalUser.value.companyId,
    };

    // Handle image upload if new file selected
    if (selectedFile.value) {
      // Option 1: Using the media service directly
      try {
        const mediaResponse = await mediaStore.uploadSingleFile(selectedFile.value, {
          entityType: "USER",
          entityId: originalUser.value.id,
          isPrimary: true,
        });

        updateData.profileImageUrl = mediaResponse.url;
        updateData.profileId = mediaResponse.id;

        // If there was a previous image, you might want to delete it
        if (originalUser.value.profileId && originalUser.value.profileId !== mediaResponse.id) {
          // Optionally delete old media (commented out for safety)
          // await mediaStore.removeMedia(originalUser.value.profileId);
        }
      } catch (uploadError) {
        console.error("Image upload failed:", uploadError);
        notify("error", "Failed to upload profile image");
        clearInterval(progressInterval);
        uploading.value = false;
        return;
      }
    } else if (!form.profileImageUrl && originalUser.value.profileId) {
      // Image was removed
      updateData.profileImageUrl = "";
      updateData.profileId = "";

      // Optionally delete the media from storage
      // await mediaStore.removeMedia(originalUser.value.profileId);
    }

    // Update user profile
    await userStore.updateUser(originalUser.value.id, updateData);

    // Update original user reference
    originalUser.value = userStore.currentUser;

    clearInterval(progressInterval);
    uploadProgress.value = 100;

    notify("success", "Profile updated successfully!");

    // Reset file selection
    selectedFile.value = null;

    // Small delay to show 100% progress
    setTimeout(() => {
      uploading.value = false;
      uploadProgress.value = 0;
    }, 500);

  } catch (error: any) {
    console.error("Profile update failed:", error);

    let errorMessage = "Failed to update profile";
    if (error.response?.data?.message) {
      errorMessage = error.response.data.message;
    } else if (error.message) {
      errorMessage = error.message;
    }

    notify("error", errorMessage);
  } finally {
    loading.value = false;
    uploading.value = false;
    uploadProgress.value = 0;
  }
}

function validateForm(): boolean {
  let isValid = true;

  // Clear previous errors
  Object.keys(errors).forEach(key => {
    errors[key as keyof typeof errors] = "";
  });

  // Validate firstname
  if (!form.firstname.trim()) {
    errors.firstname = "First name is required";
    isValid = false;
  } else if (form.firstname.length < 2) {
    errors.firstname = "First name must be at least 2 characters";
    isValid = false;
  }

  // Validate username
  if (!form.username.trim()) {
    errors.username = "Username is required";
    isValid = false;
  } else if (form.username.length < 3) {
    errors.username = "Username must be at least 3 characters";
    isValid = false;
  }

  // Validate phone (if provided)
  if (form.phone && !/^[\d\s\-\+\(\)]{10,15}$/.test(form.phone)) {
    errors.phone = "Please enter a valid phone number";
    isValid = false;
  }

  // Validate bio length
  if (form.profile && form.profile.length > 500) {
    errors.profile = "Bio must be less than 500 characters";
    isValid = false;
  }

  return isValid;
}

function resetForm() {
  if (!originalUser.value) return;

  // Reset form to original values
  form.firstname = originalUser.value.firstname || "";
  form.lastname = originalUser.value.lastname || "";
  form.username = originalUser.value.username || "";
  form.phone = originalUser.value.phone || "";
  form.profile = originalUser.value.profile || "";
  form.profileImageUrl = originalUser.value.profileImageUrl || "";
  form.profileId = originalUser.value.profileId || "";

  // Reset image preview
  selectedFile.value = null;
  if (originalUser.value.profileImageUrl) {
    previewImage.value = originalUser.value.profileImageUrl;
  } else {
    previewImage.value = null;
  }

  // Clear errors
  Object.keys(errors).forEach(key => {
    errors[key as keyof typeof errors] = "";
  });

  notify("info", "Form reset to original values");
}

function clearImage() {
  previewImage.value = null;
  selectedFile.value = null;
  form.profileImageUrl = "";

  const input = document.getElementById("profileImage") as HTMLInputElement;
  if (input) input.value = "";
}

function notify(type: "success" | "error" | "info" | "warning", message: string) {
  // Using Bootstrap toast notification (requires Bootstrap 5)
  const toastContainer = document.getElementById('toast-container') || createToastContainer();

  const toastId = 'toast-' + Date.now();
  const toast = document.createElement('div');
  toast.className = `toast align-items-center text-bg-${type} border-0`;
  toast.id = toastId;
  toast.setAttribute('role', 'alert');
  toast.setAttribute('aria-live', 'assertive');
  toast.setAttribute('aria-atomic', 'true');

  toast.innerHTML = `
    <div class="d-flex">
      <div class="toast-body">
        <i class="bi ${getIconForType(type)} me-2"></i>
        ${message}
      </div>
      <button type="button" class="btn-close btn-close-white me-2 m-auto" 
              data-bs-dismiss="toast" aria-label="Close"></button>
    </div>
  `;

  toastContainer.appendChild(toast);

  // Initialize and show toast
  const bsToast = new (window as any).bootstrap.Toast(toast, { delay: 3000 });
  bsToast.show();

  // Remove toast after it's hidden
  toast.addEventListener('hidden.bs.toast', () => {
    toast.remove();
  });
}

function getIconForType(type: string): string {
  switch (type) {
    case 'success': return 'bi-check-circle-fill';
    case 'error': return 'bi-exclamation-circle-fill';
    case 'warning': return 'bi-exclamation-triangle-fill';
    case 'info': return 'bi-info-circle-fill';
    default: return 'bi-info-circle-fill';
  }
}

function createToastContainer(): HTMLElement {
  const container = document.createElement('div');
  container.id = 'toast-container';
  container.className = 'toast-container position-fixed bottom-0 end-0 p-3';
  container.style.zIndex = '1060';
  document.body.appendChild(container);
  return container;
}
</script>

<style lang="scss" scoped>
.upload-gallery {
  .file-upload {
    padding: 40px 20px;
    border: 1.5px dashed #a8acb2;
    text-align: center;
    border-radius: 8px;
    background-color: #f8f9fa;
    transition: all 0.3s ease;

    &:hover {
      border-color: #0d6efd;
      background-color: #f0f8ff;
    }

    input {
      width: 100%;
      height: 100%;
      cursor: pointer;
    }
  }

  .profile-image-container {
    .profile-image {
      width: 120px;
      height: 120px;
      object-fit: cover;
      border: 3px solid #fff;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    }

    .profile-placeholder {
      width: 120px;
      height: 120px;
      margin: 0 auto;
      background-color: #e9ecef;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;

      img {
        width: 48px;
        height: 48px;
        opacity: 0.5;
      }
    }
  }

  .upload-progress {
    margin-top: 15px;

    .progress {
      background-color: #e9ecef;

      .progress-bar {
        background-color: #0d6efd;
      }
    }
  }
}

.form-control {
  &:read-only {
    background-color: #f8f9fa !important;
  }

  &.is-invalid {
    border-color: #dc3545;

    &:focus {
      box-shadow: 0 0 0 0.25rem rgba(220, 53, 69, 0.25);
    }
  }
}

.btn {
  min-width: 140px;

  &[disabled] {
    opacity: 0.65;
    cursor: not-allowed;
  }
}

.style-one {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;

  &:hover:not([disabled]) {
    background: linear-gradient(135deg, #764ba2 0%, #667eea 100%);
    transform: translateY(-1px);
    box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
  }
}

.invalid-feedback {
  font-size: 12px;
}

@media only screen and (min-width: 1400px) {
  .upload-gallery {
    .file-upload {
      padding: 40px 75px 35px;
    }
  }
}

@media (max-width: 768px) {
  .upload-gallery {
    .profile-image-container {

      .profile-image,
      .profile-placeholder {
        width: 100px;
        height: 100px;
      }
    }
  }

  .d-flex.gap-3 {
    flex-direction: column;
    gap: 10px !important;

    .btn {
      width: 100%;
    }
  }
}

// Spin animation for loading
.spin {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }

  to {
    transform: rotate(360deg);
  }
}

.profile-upload-card {
  // background: #ffffff;
  border: 1px solid #e7e7e7;
}

.profile-preview-wrapper {
  width: 150px;
  height: 150px;
  margin: 0 auto;
  position: relative;
  border-radius: 50%;
  overflow: hidden;
  cursor: pointer;
  transition: 0.3s ease;

  &:hover .profile-overlay {
    opacity: 1;
    visibility: visible;
  }

  input[type="file"] {
    position: absolute;
    inset: 0;
    opacity: 0;
    cursor: pointer;
    width: 100%;
    height: 100%;
  }
}

.profile-preview-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.profile-placeholder {
  width: 100%;
  height: 100%;
  background: #eef1f5;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  .placeholder-icon {
    font-size: 60px;
    color: #adb5bd;
  }
}

.profile-overlay {
  position: absolute;
  inset: 0;
  background: rgba(0, 0, 0, 0.45);
  color: #fff;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 14px;
  opacity: 0;
  visibility: hidden;
  transition: 0.3s ease;
}

.upload-progress small {
  font-size: 12px;
}
</style>