<template>
  <form class="mb-30" @submit.prevent>
    <!-- Warehouse Selection -->
    <div class="form-group mb-25">
      <label class="d-block fs-14 text-black mb-2">Choose Warehouse</label>
      <select v-model="selectedWarehouseId" class="bg-white border-0 rounded-1 fs-14 text-optional form-select"
        @change="loadProducts">
        <option value="">Choose Warehouse</option>
        <option v-for="warehouse in warehouseStore.warehouses" :key="warehouse.id" :value="warehouse.id">
          {{ warehouse.name }}
        </option>
      </select>
    </div>

    <!-- Product Search / Barcode Scan -->
    <div class="form-group position-relative">
      <label class="d-block fs-14 text-black mb-2">Choose / Scan Product</label>
      <div class="search-area style-two position-relative w-100">
        <input type="text" v-model="searchQuery" placeholder="Scan / Search product by code"
          class="w-100 h-55 bg_ash border-0 rounded-1 fs-14 text-black bg-white form-control"
          @input="updateFilteredProducts" @keydown.enter.prevent="handleSearch" />
        <button type="button" class="bg-transparent border-0 position-absolute top-0 end-0 h-100 pt-0 py-0 px-2"
          @click="handleSearch">
          <img src="@/assets/img/icons/search.svg" alt="Search" />
        </button>
      </div>

      <!-- Search Results Dropdown -->
      <ul v-if="filteredProducts.length && searchQuery" class="list-group position-absolute w-100 mt-1 shadow-sm"
        style="max-height: 200px; overflow-y: auto; z-index: 1050;">
        <li v-for="product in filteredProducts" :key="product.id" class="list-group-item list-group-item-action"
          @mousedown.prevent="selectProduct(product)">
          {{ product.name }} ({{ product.code }})
        </li>
      </ul>
    </div>
  </form>
</template>

<script setup lang="ts">
import { ref, onMounted, defineEmits } from "vue";
import { useWarehouseStore } from "@/stores/warehouseStore";
import { useProductStore } from "@/stores/productStore";
import { useUserStore } from "@/stores/userStore";
import type { Product } from "@/types/Product";

// Emit single event for product selection
const emit = defineEmits(["product-selected"]);

// Stores
const warehouseStore = useWarehouseStore();
const productStore = useProductStore();
const userStore = useUserStore();

// State
const selectedWarehouseId = ref<number | null>(null);
const allProducts = ref<Product[]>([]);
const filteredProducts = ref<Product[]>([]);
const searchQuery = ref("");

// Lifecycle
onMounted(async () => {
  if (!warehouseStore.warehouses.length) {
    await warehouseStore.fetchWarehouses();
  }

  // Default to user’s warehouse or first available
  selectedWarehouseId.value =
    userStore.currentUser?.defaultWarehouseId ??
    warehouseStore.warehouses[0]?.id ??
    null;

  if (selectedWarehouseId.value) await loadProducts();
});

// Methods
const loadProducts = async () => {
  if (!selectedWarehouseId.value) return;

  await productStore.fetchProducts({
    warehouseId: selectedWarehouseId.value,
    userId: userStore.currentUser?.id,
    includePrice: true,
    includeStock: true,
  });

  allProducts.value = productStore.products;
};

const updateFilteredProducts = () => {
  const q = searchQuery.value.toLowerCase().trim();
  filteredProducts.value = q
    ? allProducts.value.filter(
      (p) =>
        p.name.toLowerCase().includes(q) ||
        p.code.toLowerCase().includes(q) ||
        (p.barcode && p.barcode.toLowerCase().includes(q))
    )
    : [];
};

const handleSearch = () => {
  if (filteredProducts.value.length) {
    selectProduct(filteredProducts.value[0]);
  }
};

const selectProduct = (product: Product) => {
  emit("product-selected", product);
  searchQuery.value = "";
  filteredProducts.value = [];
};
</script>

<style scoped>
.form-select,
.form-control {
  height: 55px;
}
</style>
