<template>
  <div class="modal fade" id="createModal" tabindex="-1" aria-labelledby="createModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-rl modal-dialog-centered">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title text-title" id="createModalLabel">
            {{ isEditing ? "Update" : "Create" }} Currency
          </h5>
          <button type="button" class="btn-close p-0" data-bs-dismiss="modal" aria-label="Close">
            <img src="../../../assets/img/icons/close-circle-2.svg" alt="Image" />
          </button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="handleSubmit">
            <div class="form-group mb-15">
              <label class="d-block fs-14 text-black mb-10">Currency Code</label>
              <input v-model="form.code" required type="text"
                class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-black border-0 placeholder-1"
                placeholder="Enter Currency Code" />
            </div>
            <div class="form-group mb-15">
              <label class="d-block fs-14 text-black mb-10">Currency Name</label>
              <input v-model="form.name" required type="text"
                class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-black border-0 placeholder-1"
                placeholder="Enter Currency Name" />
            </div>
            <div class="form-group mb-15">
              <label class="d-block fs-14 text-black mb-10">Symbol</label>
              <input v-model="form.symbol" required type="text"
                class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-black border-0 placeholder-1"
                placeholder="Enter Symbol" />
            </div>
            <button type="submit" class="btn style-five w-100 d-block">Submit</button>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, defineProps, defineEmits } from 'vue';
import type { CreateCurrencyRequest, Currency } from "@/types/Currency";

const props = defineProps<{
  initialData: Currency | null;
  isEditing: boolean;
}>();

const emit = defineEmits<{
  (e: "submit", data: CreateCurrencyRequest): void;
}>();

const form = ref<CreateCurrencyRequest>({
  name: "",
  code: "",
  symbol: "",
});

const resetForm = () => {
  form.value = {
    name: '',
    code: '',
    symbol: '',
  };
};

watch(
  () => props.initialData,
  (currency) => {
    if (currency) {
      form.value = {
        name: currency.name || '',
        code: currency.code || '',
        symbol: currency.symbol || '',
      };
    } else {
      resetForm();
    }
  },
  { immediate: true }
);

const handleSubmit = () => {
  emit("submit", form.value);
  document.querySelector<HTMLButtonElement>("#createModal .btn-close")?.click();
};
</script>