<template>
  <div class="v-counter style-two">
    <button class="plusBtn" @click="decrement" type="button">-</button>
    <input
      type="number"
      class="count"
      :value="modelValue"
      @input="onInput"
      :min="min"
      :max="max"
    />
    <button class="minusBtn" @click="increment" type="button">+</button>
  </div>
</template>

<script lang="ts" setup>
import { defineProps, defineEmits } from "vue";

const props = defineProps<{
  modelValue: number;
  min?: number;
  max?: number;
}>();

const emit = defineEmits<{
  (e: "update:modelValue", value: number): void;
}>();

const min = props.min ?? 1;
const max = props.max ?? Infinity;

function increment() {
  if (props.modelValue < max) {
    emit("update:modelValue", props.modelValue + 1);
  }
}

function decrement() {
  if (props.modelValue > min) {
    emit("update:modelValue", props.modelValue - 1);
  }
}

function onInput(event: Event) {
  const input = event.target as HTMLInputElement;
  const value = parseInt(input.value, 10);

  if (!isNaN(value)) {
    const clamped = Math.max(min, Math.min(max, value));
    emit("update:modelValue", clamped);
  }
}
</script>
