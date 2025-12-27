<template>
  <div class="modal fade" id="createModal" tabindex="-1" aria-labelledby="createModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-rl modal-dialog-centered">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title text-title" id="createModalLabel">
            {{ isEditing ? 'Edit Category' : 'Create Category' }}
          </h5>
          <button type="button" class="btn-close p-0" id="categoryModalCloseButton" data-bs-dismiss="modal"
            aria-label="Close">
            <img src="../../../assets/img/icons/close-circle-2.svg" alt="Image" />
          </button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="handleSubmit">
            <div class="form-group mb-15">
              <label class="d-block fs-14 text-black mb-10">Category Name</label>
              <input v-model="categoryName" type="text"
                class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-black border-0 placeholder-1"
                placeholder="Enter Category Name" />
            </div>
            <div class="form-group mb-15">
              <label class="d-block fs-14 text-black mb-10">Category Code</label>
              <input v-model="categoryCode" type="text"
                class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-black border-0 placeholder-1"
                placeholder="Enter Category Code" />
            </div>
            <button type="submit" class="btn style-five w-100 d-block">
              {{ isEditing ? 'Update' : 'Submit' }}
            </button>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, defineProps, defineEmits } from 'vue';
import type { Category } from '@/types/Category';

const props = defineProps<{ initialData: Category | null; isEditing: boolean }>();
const emit = defineEmits<{
  (e: 'submit', data: Omit<Category, 'id'>): void;
}>();

const categoryName = ref('');
const categoryCode = ref('');

watch(
  () => props.initialData,
  (val) => {
    if (val) {
      categoryName.value = val.name;
      categoryCode.value = val.code ?? '';
    } else {
      categoryName.value = '';
      categoryCode.value = '';
    }
  },
  { immediate: true }
);

const handleSubmit = () => {
  if (!categoryName.value.trim() || !categoryCode.value.trim()) {
    alert('Both category name and code are required.');
    return;
  }

  emit('submit', {
    name: categoryName.value.trim(),
    code: categoryCode.value.trim(),
  });

  document.getElementById('categoryModalCloseButton')?.click();
};
</script>
