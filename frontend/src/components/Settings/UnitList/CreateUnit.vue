<template>
  <div class="modal fade" id="createModal" tabindex="-1" aria-labelledby="createModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-rl modal-dialog-centered">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title text-title" id="createModalLabel">
            {{ isEditing ? 'Update' : 'Create' }} Unit
          </h5>
          <button type="button" class="btn-close p-0" data-bs-dismiss="modal" aria-label="Close">
            <img src="../../../assets/img/icons/close-circle-2.svg" alt="Image" />
          </button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="handleSubmit">
            <div class="form-group mb-15">
              <label class="d-block fs-14 text-black mb-10">Name</label>
              <input v-model="form.name" required type="text"
                class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-black border-0 placeholder-1"
                placeholder="Enter Unit Name" />
            </div>
            <div class="form-group mb-15">
              <label class="d-block fs-14 text-black mb-10">Short Name</label>
              <input v-model="form.shortName" required type="text"
                class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-black border-0 placeholder-1"
                placeholder="Enter Unit Short Name" />
            </div>
            <div class="form-group mb-15">
              <label class="d-block fs-14 text-black mb-10">Base Unit</label>
              <input v-model="form.baseUnit" required type="text"
                class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-black border-0 placeholder-1"
                placeholder="Enter Base Unit" />
            </div>
            <div class="form-group mb-15">
              <label class="d-block fs-14 text-black mb-10">Operator</label>
              <select v-model="form.operator" required
                class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-black border-0">
                <option value="*">*</option>
                <option value="/">/</option>
                <option value="+">+</option>
                <option value="-">-</option>
              </select>
            </div>
            <div class="form-group mb-15">
              <label class="d-block fs-14 text-black mb-10">Operator Value</label>
              <input v-model.number="form.operatorValue" required type="number" min="0.0001" step="0.0001"
                class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-black border-0 placeholder-1"
                placeholder="Enter Operator Value" />
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
import { ref, watch, defineProps, defineEmits } from "vue";
import type { Unit, CreateUnitRequest } from "@/types/Unit";

const props = defineProps<{
  initialData: Unit | null;
  isEditing: boolean;
}>();

const emit = defineEmits<{
  (e: "submit", data: CreateUnitRequest): void;
}>();

const form = ref<CreateUnitRequest>({
  name: "",
  shortName: "",
  baseUnit: "",
  operator: "*",
  operatorValue: 1,
});

const resetForm = () => {
  form.value = {
    name: "",
    shortName: "",
    baseUnit: "",
    operator: "*",
    operatorValue: 1,
  };
};

watch(
  () => props.initialData,
  (unit) => {
    if (unit) {
      form.value = {
        name: unit.name || "",
        shortName: unit.shortName || "",
        baseUnit: unit.baseUnit || "",
        operator: unit.operator || "*",
        operatorValue: unit.operatorValue ?? 1,
      };
    } else {
      resetForm();
    }
  },
  { immediate: true }
);

const handleSubmit = () => {
  emit("submit", { ...form.value });
  document.querySelector<HTMLButtonElement>("#createModal .btn-close")?.click();
};
</script>