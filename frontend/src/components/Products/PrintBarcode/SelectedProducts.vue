<template>
  <div class="card border-0 shadow-none rounded-1 mb-30">
    <div class="card-body p-xl-40">
      <h6 class="fs-18 mb-40 text-title">
        Selected Products For Print Barcode
      </h6>

      <div v-if="localProducts.length" class="table-responsive">
        <table class="table text-nowrap align-middle mb-0">
          <thead>
            <tr>
              <th class="text-title fw-normal fs-14 pt-0 ps-0 ls-1">PRODUCT</th>
              <th class="text-title fw-normal fs-14 pt-0 ls-1">PRODUCT CODE</th>
              <th class="text-title fw-normal fs-14 pt-0 ls-1">QUANTITY</th>
              <th class="text-title fw-normal fs-14 pt-0 ls-1 text-end pe-0">DELETE</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(product, index) in localProducts" :key="product.id">
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph ps-0">
                {{ product.name }}
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                {{ product.code }}
              </td>
              <td class="shadow-none lh-1 fs-14 fw-normal text-paragraph">
                <QuantityCounter :modelValue="product.quantity" @update:modelValue="updateQuantity(index, $event)" />
              </td>
              <td class="shadow-none lh-1 text-end pe-0">
                <div class="button-group style-two ms-auto d-flex flex-wrap align-items-center">
                  <button type="button" class="delete-btn bg-transparent border-0" @click="removeProduct(product.id)">
                    <img src="@/assets/img/icons/close.svg" alt="Delete" />
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <div v-else class="text-center py-4 text-muted">
        No products selected yet.
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, computed, defineProps, defineEmits } from "vue";
import QuantityCounter from "./QuantityCounter.vue";
import type { Product } from "@/types/Product";

const props = defineProps<{
  products: (Product & { quantity?: number })[];
}>();

const emit = defineEmits(["update:products"]);

const localProducts = ref<(Product & { quantity: number })[]>([]);

// Initialize local copy on mount
localProducts.value = props.products.map((p) => ({
  ...p,
  quantity: p.quantity ?? 1,
}));

// Watch for prop changes (only update if truly different)
watch(
  () => props.products,
  (newVal) => {
    const newJSON = JSON.stringify(newVal);
    const oldJSON = JSON.stringify(localProducts.value);
    if (newJSON !== oldJSON) {
      localProducts.value = newVal.map((p) => ({
        ...p,
        quantity: p.quantity ?? 1,
      }));
    }
  },
  { deep: true }
);

const removeProduct = (id: number) => {
  localProducts.value = localProducts.value.filter((p) => p.id !== id);
  emit("update:products", localProducts.value);
};

const updateQuantity = (index: number, quantity: number) => {
  localProducts.value[index].quantity = quantity;
  emit("update:products", localProducts.value);
};

// Compute grouped list for barcode printing
const groupedProducts = computed(() => {
  const grouped: Product[] = [];
  localProducts.value.forEach((p) => {
    const qty = p.quantity ?? 1;
    for (let i = 0; i < qty; i++) grouped.push(p);
  });
  return grouped;
});

// Debug log
watch(groupedProducts, (val) => {
  console.log("🧾 Grouped Products for Barcode Print:", val);
});
</script>
