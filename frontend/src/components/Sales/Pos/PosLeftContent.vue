<template>
  <div class="pos-left bg-white rounded-1">
    <!-- Only show selected products -->
    <div class="card border-0 shadow-none rounded-1 mb-30">
      <SelectedProducts :products="posStore.selectedProducts" @update:products="posStore.selectedProducts = $event" />
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { usePosStore } from "@/stores/posStore";
import SelectedProducts from "./SelectedProducts.vue";

export default defineComponent({
  components: { SelectedProducts },
  setup() {
    const posStore = usePosStore();
    return { posStore };
  },
});
</script>

<style lang="scss" scoped>
.pos-left {
  width: calc(50% - 10px);
  height: 100%;
  // padding: 15px 15px 50px;
}

@media only screen and (max-width: 991px) {
  .pos-left {
    width: 100%;
    margin-bottom: 30px;
  }
}

@media only screen and (min-width: 1600px) {
  .pos-left {
    width: calc(50% - 10px);
  }
}
</style>