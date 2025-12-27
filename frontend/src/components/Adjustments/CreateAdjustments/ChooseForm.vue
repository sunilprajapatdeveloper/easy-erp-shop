<template>
  <form class="mb-40" @submit.prevent="handleSearch">
    <div class="row gx-xxl-6">
      <div class="col-lg-6">
        <div class="form-group mb-25">
          <label class="d-block fs-14 text-black mb-2">Warehouse</label>
          <select v-model="modelWarehouseId" required class="bg-white border-0 rounded-1 fs-14 text-optional">
            <option disabled :value="null">Select Warehouse</option>
            <option v-for="warehouse in warehouses" :key="warehouse.id" :value="warehouse.id">
              {{ warehouse.name }}
            </option>
          </select>
        </div>
      </div>
      <div class="col-lg-6">
        <div class="form-group">
          <label class="d-block fs-14 text-black mb-2">Adjustment Date</label>
          <input v-model="modelDate" type="date" required
            class="w-100 h-55 bg_ash border-0 rounded-1 fs-14 text-black bg-white" />
        </div>
      </div>

      <div class="col-12">
        <div class="form-group position-relative">
          <label class="d-block fs-14 text-black mb-2">Search Product</label>
          <div class="search-area position-relative w-100">
            <input v-model="searchQuery" @keyup.enter.prevent="handleSearch" @input="updateFilteredProducts" type="text"
              placeholder="Scan / Search product by code"
              class="w-100 h-55 bg_ash border-0 rounded-1 fs-14 text-black bg-white" />
            <button type="button" class="bg-transparent border-0 position-absolute top-0 end-0 h-100 pt-0 py-0 px-2"
              @click="handleSearch">
              <img src="../../../assets/img/icons/search.svg" alt="Image" />
            </button>
          </div>

          <!-- Product Dropdown List -->
          <div v-if="filteredProducts.length || searchQuery.trim()"
            class="position-absolute w-100 mt-1 bg-white border rounded shadow"
            style="z-index: 1050; max-height: 180px; overflow-y: auto;">
            <!-- Products Found -->
            <ul v-if="filteredProducts.length" class="list-group list-group-flush m-0">
              <li v-for="product in filteredProducts" :key="product.id" @mousedown.prevent="selectProduct(product)"
                class="list-group-item list-group-item-action px-3 py-2" style="cursor: pointer;">
                <div class="fw-semibold small text-dark">
                  {{ product.name }}
                  <span class="text-muted ms-1">({{ product.code }})</span>
                </div>
              </li>
            </ul>

            <!-- No Results -->
            <div v-else class="text-center text-muted small py-3">
              No products found.
            </div>
          </div>
        </div>
      </div>
    </div>
  </form>
</template>

<script setup lang="ts">
import { ref, computed, defineProps, defineEmits, onMounted } from "vue";
import type { Warehouse } from "@/types/Warehouse";
import { useProductStore } from "@/stores/productStore";
import { AdjustmentType } from "@/types/Adjustment";
import type { Product } from "@/types/Product";
import type { SelectedProduct } from "@/types/Adjustment";

const props = defineProps<{
  warehouses: Warehouse[];
  warehouseId: number | null;
  date: string;
}>();

const emit = defineEmits<{
  (e: "update:warehouseId", value: number | null): void;
  (e: "update:date", value: string): void;
  (e: "add-product", value: SelectedProduct): void;
}>();

const productStore = useProductStore();
const searchQuery = ref("");
const allProducts = ref<Product[]>([]);
const filteredProducts = ref<Product[]>([]);
const adjustedQty = ref(1);

onMounted(async () => {
  if (!productStore.products.length) {
    await productStore.fetchProducts();
  }
  allProducts.value = productStore.products;
});

const modelWarehouseId = computed({
  get: () => props.warehouseId,
  set: (value) => emit("update:warehouseId", value),
});

const modelDate = computed({
  get: () => props.date,
  set: (value) => emit("update:date", value),
});

const updateFilteredProducts = () => {
  const query = searchQuery.value.trim().toLowerCase();
  if (query === "") {
    filteredProducts.value = [];
    return;
  }
  filteredProducts.value = allProducts.value.filter(
    (p) =>
      p.code.toLowerCase().includes(query) ||
      p.name.toLowerCase().includes(query)
  );
};

const selectProduct = (product: Product) => {
  if (product.id === undefined) return;
  emit("add-product", {
    productId: product.id,
    productName: product.name,
    code: product.code,
    stock: product.quantity ?? 0,
    adjustedQty: adjustedQty.value,
    stockEffect: AdjustmentType.ADD
  });
  searchQuery.value = "";
  filteredProducts.value = [];
};

const handleSearch = () => {
  if (filteredProducts.value.length > 0) {
    selectProduct(filteredProducts.value[0]);
  }
};
</script>