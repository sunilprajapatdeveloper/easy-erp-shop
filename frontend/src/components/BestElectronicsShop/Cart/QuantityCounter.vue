<template>
  <div class="v-counter style-three bg-white">
    <button class="plusBtn" @click="increment"></button>
    <input type="text" size="25" :value="quantity" class="count" />
    <button class="minusBtn" @click="decrement"></button>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from "vue";

export default defineComponent({
  name: "QuantityCounter",
  setup() {
    const quantity = ref(1);

    const increment = () => {
      quantity.value++;
    };

    const decrement = () => {
      if (quantity.value > 1) {
        quantity.value--;
      }
    };

    return {
      quantity,
      increment,
      decrement,
    };
  },
});
</script>