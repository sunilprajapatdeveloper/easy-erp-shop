<template>
  <form class="mb-40" @submit.prevent="handleSearch">
    <div class="row">
      <!-- Transfer Date -->
      <div class="col-lg-4">
        <div class="form-group mb-25">
          <label class="d-block fs-14 text-black mb-2">Transfer Date</label>
          <input v-model="modelDate" type="date" required
            class="w-100 h-55 bg_ash border-0 rounded-1 fs-14 text-black bg-white" />
        </div>
      </div>

      <!-- From Warehouse -->
      <div class="col-lg-4">
        <div class="form-group mb-25">
          <label class="d-block fs-14 text-black mb-2">From Warehouse</label>
          <select v-model="modelFromWarehouseId" required class="bg-white border-0 rounded-1 fs-14 text-optional">
            <option disabled :value="null">Select Warehouse</option>
            <option v-for="warehouse in warehouses" :key="warehouse.id" :value="warehouse.id">
              {{ warehouse.name }}
            </option>
          </select>
        </div>
      </div>

      <!-- To Warehouse -->
      <div class="col-lg-4">
        <div class="form-group mb-25">
          <label class="d-block fs-14 text-black mb-2">To Warehouse</label>
          <select v-model="modelToWarehouseId" required class="bg-white border-0 rounded-1 fs-14 text-optional">
            <option disabled :value="null">Select Warehouse</option>
            <option v-for="warehouse in warehouses" :key="warehouse.id" :value="warehouse.id">
              {{ warehouse.name }}
            </option>
          </select>
        </div>
      </div>

      <!-- Search Product -->
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

          <!-- Product Dropdown -->
          <div v-if="filteredProducts.length || searchQuery.trim()"
            class="position-absolute w-100 mt-1 bg-white border rounded shadow"
            style="z-index: 1050; max-height: 180px; overflow-y: auto;">
            <ul v-if="filteredProducts.length" class="list-group list-group-flush m-0">
              <li v-for="product in filteredProducts" :key="product.id" @mousedown.prevent="selectProduct(product)"
                class="list-group-item list-group-item-action px-3 py-2" style="cursor: pointer;">
                <div class="fw-semibold small text-dark">
                  {{ product.name }}
                  <span class="text-muted ms-1">({{ product.code }})</span>
                </div>
              </li>
            </ul>
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
import type { Product } from "@/types/Product";
import type { SelectedTransferProduct } from "@/types/Transfer";
import { useProductStore } from "@/stores/productStore";
import { TaxType } from "@/types/TaxTypes";

const props = defineProps<{
  warehouses: Warehouse[];
  fromWarehouseId: number | null;
  toWarehouseId: number | null;
  date: string;
}>();

const emit = defineEmits<{
  (e: "update:fromWarehouseId", value: number | null): void;
  (e: "update:toWarehouseId", value: number | null): void;
  (e: "update:date", value: string): void;
  (e: "add-product", value: SelectedTransferProduct): void;
}>();

const productStore = useProductStore();
const allProducts = ref<Product[]>([]);
const filteredProducts = ref<Product[]>([]);
const searchQuery = ref("");

onMounted(async () => {
  if (!productStore.products.length) {
    await productStore.fetchProducts();
  }
  allProducts.value = productStore.products;
});

const modelFromWarehouseId = computed({
  get: () => props.fromWarehouseId,
  set: (value) => emit("update:fromWarehouseId", value),
});

const modelToWarehouseId = computed({
  get: () => props.toWarehouseId,
  set: (value) => emit("update:toWarehouseId", value),
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
  if (product.id == null || product.cost == null || product.quantity == null) {
    console.warn("Invalid product data:", product);
    return;
  }

  emit("add-product", {
    productId: product.id,
    productName: product.name,
    code: product.code,
    stock: product.quantity,
    cost: product.cost.toString(),
    discount: product.discount?.toString() ?? "0",
    tax: product.orderTax?.toString() ?? "0",
    taxType: (product.taxType ?? "Exclusive").toUpperCase() as TaxType,
    subTotal: "0",
    transferredQty: 1
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