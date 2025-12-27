<template>
  <div class="v-counter style-two">
    <button class="plusBtn" @click="decrement"></button>
    <input type="text" size="25" :value="modelValue" class="count" @input="onInput" />
    <button class="minusBtn" @click="increment"></button>
  </div>
</template>

<script setup lang="ts">
import { defineProps, defineEmits } from "vue";

const props = defineProps<{
  modelValue: number;
}>();

const emit = defineEmits<{
  (e: "update:modelValue", value: number): void;
}>();

const increment = () => {
  emit("update:modelValue", props.modelValue + 1);
};

const decrement = () => {
  if (props.modelValue > 1) {
    emit("update:modelValue", props.modelValue - 1);
  }
};

const onInput = (event: Event) => {
  const input = event.target as HTMLInputElement;
  const parsed = parseInt(input.value);
  if (!isNaN(parsed) && parsed >= 1) {
    emit("update:modelValue", parsed);
  }
};
</script>