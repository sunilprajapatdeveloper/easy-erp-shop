<template>
    <form @submit.prevent="handleSearch">
        <div class="row mb-40">
            <!-- Original Purchase Search -->
            <div class="col-12">
                <div v-if="isCreateMode" class="form-group mb-25 position-relative">
                    <label class="d-block fs-14 text-black mb-2">Search Original Purchase</label>
                    <div class="search-area position-relative w-100">
                        <input v-model="purchaseSearchQuery" @keyup.enter.prevent="handlePurchaseSearch"
                            @input="updateFilteredPurchases" type="text"
                            placeholder="Search by purchase reference or supplier"
                            class="w-100 h-55 bg_ash border-0 rounded-1 fs-14 text-black bg-white" />
                        <button type="button" @click="handlePurchaseSearch"
                            class="bg-transparent border-0 position-absolute top-0 end-0 h-100 pt-0 py-0 px-2">
                            <img src="../../../assets/img/icons/search.svg" alt="Search" />
                        </button>
                    </div>

                    <!-- Dropdown -->
                    <div v-if="filteredPurchases.length || purchaseSearchQuery.trim()"
                        class="position-absolute w-100 mt-1 bg-white border rounded shadow"
                        style="z-index: 1050; max-height: 180px; overflow-y: auto;">
                        <ul v-if="filteredPurchases.length" class="list-group list-group-flush m-0">
                            <li v-for="purchase in filteredPurchases" :key="purchase.id"
                                @mousedown.prevent="selectPurchase(purchase)"
                                class="list-group-item list-group-item-action px-3 py-2" style="cursor: pointer;">
                                <div class="fw-semibold small text-dark">
                                    Purchase #{{ purchase.referenceNumber }}
                                    <span class="text-muted ms-1">
                                        ({{ supplierNameById(purchase.supplierId) }})
                                    </span>
                                </div>
                            </li>
                        </ul>
                        <div v-else class="text-center text-muted small py-3">
                            No purchases found.
                        </div>
                    </div>
                </div>
            </div>

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
import { ref, computed, defineProps, defineEmits, onMounted } from "vue";
import type { Warehouse } from "@/types/Warehouse";
import type { Supplier } from "@/types/Supplier";
import type { Product } from "@/types/Product";
import type { SelectedPurchaseReturnProduct } from "@/types/PurchaseReturn";
import type { Purchase } from "@/types/Purchase";
import { useProductStore } from "@/stores/productStore";
import { usePurchaseStore } from "@/stores/purchaseStore";

const props = defineProps<{
    warehouses: Warehouse[];
    suppliers: Supplier[];
    date: string;
    warehouseId: number | null;
    supplierId: number | null;
    isCreateMode: boolean;
}>();

const emit = defineEmits<{
    (e: "update:date", value: string): void;
    (e: "update:warehouseId", value: number | null): void;
    (e: "update:supplierId", value: number | null): void;
    (e: "add-product", value: SelectedPurchaseReturnProduct): void;
    (e: "load-purchase", value: Purchase): void;
}>();

// PRODUCT SEARCH
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
    const costStr = product.cost?.toString() ?? "0";
    const selectedProduct: SelectedPurchaseReturnProduct = {
        productId: product.id,
        productName: product.name,
        code: product.code,
        stock: product.quantity ?? 0,
        unitCost: costStr,
        discount: "0",
        tax: "0",
        subTotal: costStr,
        returnQty: 1,
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

// ORIGINAL PURCHASE SEARCH
const purchaseSearchQuery = ref("");
const allPurchases = ref<Purchase[]>([]);
const filteredPurchases = ref<Purchase[]>([]);
const purchaseStore = usePurchaseStore();

onMounted(async () => {
    if (!purchaseStore.purchases.length) {
        await purchaseStore.fetchPurchases();
    }
    allPurchases.value = purchaseStore.purchases;
});

const supplierNameById = (id: number) => {
    const supplier = props.suppliers.find((s) => s.id === id);
    return supplier ? supplier.name : "Unknown";
};

const updateFilteredPurchases = () => {
    const query = purchaseSearchQuery.value.trim().toLowerCase();
    if (!query) {
        filteredPurchases.value = [];
        return;
    }
    filteredPurchases.value = allPurchases.value.filter(
        (p) =>
            p.referenceNumber.toLowerCase().includes(query) ||
            supplierNameById(p.supplierId).toLowerCase().includes(query)
    );
};

const selectPurchase = (purchase: Purchase) => {
    emit("load-purchase", purchase);
    purchaseSearchQuery.value = "";
    filteredPurchases.value = [];
};

const handlePurchaseSearch = () => {
    if (filteredPurchases.value.length > 0) {
        selectPurchase(filteredPurchases.value[0]);
    }
};

// BINDINGS
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
</script>
