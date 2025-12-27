<template>
  <div class="modal fade" id="createModal" tabindex="-1" aria-labelledby="createModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-rl modal-dialog-centered">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title text-title" id="createModalLabel">
            Create Brand
          </h5>
          <button type="button" class="btn-close p-0" id="brandModalCloseButton" data-bs-dismiss="modal"
            aria-label="Close">
            <img src="../../../assets/img/icons/close-circle-2.svg" alt="Image" />
          </button>
        </div>

        <div class="modal-body">
          <form @submit.prevent="submit">
            <!-- Brand Name -->
            <div class="form-group mb-15">
              <label class="d-block fs-14 text-black mb-10">Brand Name</label>
              <input v-model="form.name" type="text"
                class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-black border-0 placeholder-1"
                placeholder="Enter Brand Name" required />
            </div>

            <!-- Brand Description -->
            <div class="form-group mb-15">
              <label class="d-block fs-14 text-black mb-10">Brand Description</label>
              <textarea v-model="form.description" cols="30" rows="3"
                class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-black border-0 placeholder-1"
                placeholder="Enter Brand Description"></textarea>
            </div>

            <!-- Brand Image -->
            <!-- <div class="form-group mb-15">
              <label class="d-block fs-14 text-black mb-10">Brand Image</label>
              <input
                type="file"
                class="form-control bg-transparent ps-0 fs-14"
                @change="handleImageUpload"
              />
            </div> -->

            <!-- Submit -->
            <button type="submit" class="btn style-five w-100 d-block">
              Submit
            </button>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, defineProps, defineEmits } from 'vue';
import type { Brand } from '@/types/Brand';

const props = defineProps<{
  initialData: Brand | null;
  isEditing: boolean;
}>();

const emit = defineEmits<{
  (e: 'submit', data: Omit<Brand, 'id'>): void;
}>();

const form = ref<Omit<Brand, 'id'>>({
  name: '',
  description: '',
  image: ''
});

const resetForm = () => {
  form.value = {
    name: '',
    description: '',
    image: ''
  };
};

// Move watch AFTER resetForm is defined
watch(
  () => props.initialData,
  (brand) => {
    if (brand) {
      form.value = {
        name: brand.name || '',
        description: brand.description || '',
        image: brand.image || '',
      };
    } else {
      resetForm();
    }
  },
  { immediate: true }
);

const submit = async () => {
  try {
    emit('submit', form.value);
    resetForm();

    // Close modal
    const closeButton = document.querySelector<HTMLButtonElement>('#brandModalCloseButton');
    closeButton?.click();
  } catch (error: any) {
    console.error('Failed to submit brand:', error);
    alert('Failed to submit brand: ' + error?.response?.data?.error || error.message);
  }
};
</script>