<template>
    <form @submit.prevent="handleSearch">
        <div class="row mb-40">
            <!-- Date -->
            <div class="col-lg-4">
                <div class="form-group mb-25">
                    <label class="d-block fs-14 text-black mb-2">Date</label>
                    <input v-model="modelDate" type="date" required
                        class="w-100 h-55 bg_ash border-0 rounded-1 fs-14 text-black bg-white" />
                </div>
            </div>

            <!-- Supplier -->
            <div class="col-lg-4">
                <div class="form-group mb-25">
                    <label class="d-block fs-14 text-black mb-2">Supplier</label>
                    <select v-model="modelSupplierId" required class="bg-white border-0 rounded-1 fs-14 text-optional">
                        <option disabled :value="null">Select Supplier</option>
                        <option v-for="supplier in suppliers" :key="supplier.id" :value="supplier.id">
                            {{ supplier.name }}
                        </option>
                    </select>
                </div>
            </div>

            <!-- Warehouse -->
            <div class="col-lg-4">
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

            <!-- Product Search -->
            <div class="col-12">
                <div class="form-group position-relative">
                    <label class="d-block fs-14 text-black mb-2">Search Product</label>
                    <div class="search-area position-relative w-100">
                        <input v-model="searchQuery" @keyup.enter.prevent="handleSearch" @input="updateFilteredProducts"
                            type="text" placeholder="Scan / Search product by code"
                            class="w-100 h-55 bg_ash border-0 rounded-1 fs-14 text-black bg-white" />
                        <button type="button" @click="handleSearch"
                            class="bg-transparent border-0 position-absolute top-0 end-0 h-100 pt-0 py-0 px-2">
                            <img src="../../../assets/img/icons/search.svg" alt="Search" />
                        </button>
                    </div>

                    <!-- Dropdown -->
                    <div v-if="filteredProducts.length || searchQuery.trim()"
                        class="position-absolute w-100 mt-1 bg-white border rounded shadow"
                        style="z-index: 1050; max-height: 180px; overflow-y: auto;">
                        <ul v-if="filteredProducts.length" class="list-group list-group-flush m-0">
                            <li v-for="product in filteredProducts" :key="product.id"
                                @mousedown.prevent="selectProduct(product)"
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
import { ref, computed, onMounted } from "vue";
import type { Warehouse } from "@/types/Warehouse";
import type { Supplier } from "@/types/Supplier";
import type { Product } from "@/types/Product";
import type { SelectedPurchaseProduct } from "@/types/Purchase";
import { useProductStore } from "@/stores/productStore";
import { TaxInclusionType } from "@/enums/TaxInclusionType";

const props = defineProps<{
    warehouses: Warehouse[];
    suppliers: Supplier[];
    date: string;
    warehouseId: number | null;
    supplierId: number | null;
}>();

const emit = defineEmits<{
    (e: "update:date", value: string): void;
    (e: "update:warehouseId", value: number | null): void;
    (e: "update:supplierId", value: number | null): void;
    (e: "add-product", value: SelectedPurchaseProduct): void;
}>();

const searchQuery = ref("");
const allProducts = ref<Product[]>([]);
const filteredProducts = ref<Product[]>([]);

const productStore = useProductStore();

onMounted(async () => {
    if (!productStore.products.length) {
        await productStore.fetchProducts();
    }
    allProducts.value = productStore.products;
});

const modelDate = computed({
    get: () => props.date,
    set: (val) => emit("update:date", val),
});

const modelWarehouseId = computed({
    get: () => props.warehouseId,
    set: (val) => emit("update:warehouseId", val),
});

const modelSupplierId = computed({
    get: () => props.supplierId,
    set: (val) => emit("update:supplierId", val),
});

const updateFilteredProducts = () => {
    const query = searchQuery.value.trim().toLowerCase();
    if (!query) {
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
    if (typeof product.id !== "number") return;

    const costStr = product.price?.cost?.toString() ?? "0"; // use cost instead of price

    const selectedProduct: SelectedPurchaseProduct = {
        productId: product.id,
        productName: product.name,
        code: product.code,
        stock: product.stock?.quantity ?? 0,
        cost: costStr,
        discount: "0",
        tax: "0",
        inclusionType: product.tax?.isInclusive ? TaxInclusionType.INCLUSIVE : TaxInclusionType.EXCLUSIVE,
        subTotal: costStr,
        purchaseQty: 1,
    };

    emit("add-product", selectedProduct);

    searchQuery.value = "";
    filteredProducts.value = [];
};

const handleSearch = () => {
    if (filteredProducts.value.length > 0) {
        selectProduct(filteredProducts.value[0]);
    }
};
</script>
