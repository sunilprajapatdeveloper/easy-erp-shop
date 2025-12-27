<template>
    <form @submit.prevent="handleSearch">
        <div class="row mb-40">
            <!-- Original Sale Search -->
            <div class="col-12">
                <div v-if="isCreateMode" class="form-group mb-25 position-relative">
                    <label class="d-block fs-14 text-black mb-2">Search Original Sale</label>
                    <div class="search-area position-relative w-100">
                        <input v-model="saleSearchQuery" @keyup.enter.prevent="handleSaleSearch"
                            @input="updateFilteredSales" type="text" placeholder="Search by sale reference or customer"
                            class="w-100 h-55 bg_ash border-0 rounded-1 fs-14 text-black bg-white" />
                        <button type="button" @click="handleSaleSearch"
                            class="bg-transparent border-0 position-absolute top-0 end-0 h-100 pt-0 py-0 px-2">
                            <img src="../../../assets/img/icons/search.svg" alt="Search" />
                        </button>
                    </div>

                    <!-- Dropdown -->
                    <div v-if="filteredSales.length || saleSearchQuery.trim()"
                        class="position-absolute w-100 mt-1 bg-white border rounded shadow"
                        style="z-index: 1050; max-height: 180px; overflow-y: auto;">
                        <ul v-if="filteredSales.length" class="list-group list-group-flush m-0">
                            <li v-for="sale in filteredSales" :key="sale.id" @mousedown.prevent="selectSale(sale)"
                                class="list-group-item list-group-item-action px-3 py-2" style="cursor: pointer;">
                                <div class="fw-semibold small text-dark">
                                    Sale #{{ sale.referenceNumber }}
                                    <span class="text-muted ms-1">
                                        ({{ customerNameById(sale.customerId) }})
                                    </span>
                                </div>
                            </li>
                        </ul>
                        <div v-else class="text-center text-muted small py-3">
                            No sales found.
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

            <!-- Customer -->
            <div class="col-lg-4">
                <div class="form-group mb-25">
                    <label class="d-block fs-14 text-black mb-2">Customer</label>
                    <select v-model="modelCustomerId" required class="bg-white border-0 rounded-1 fs-14 text-optional">
                        <option disabled :value="null">Select Customer</option>
                        <option v-for="customer in customers" :key="customer.id" :value="customer.id">
                            {{ customer.name }}
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
import type { Customer } from "@/types/Customer";
import type { Product } from "@/types/Product";
import type { SelectedSaleReturnProduct } from "@/types/saleReturn";
import type { Sale } from "@/types/Sale";
import { useProductStore } from "@/stores/productStore";
import { useSaleStore } from "@/stores/saleStore";

const props = defineProps<{
    warehouses: Warehouse[];
    customers: Customer[];
    date: string;
    warehouseId: number | null;
    customerId: number | null;
    isCreateMode: boolean;
}>();

const emit = defineEmits<{
    (e: "update:date", value: string): void;
    (e: "update:warehouseId", value: number | null): void;
    (e: "update:customerId", value: number | null): void;
    (e: "add-product", value: SelectedSaleReturnProduct): void;
    (e: "load-sale", value: Sale): void;
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
        (p) => p.code.toLowerCase().includes(query) || p.name.toLowerCase().includes(query)
    );
};

const selectProduct = (product: Product) => {
    if (typeof product.id !== "number") return;
    const priceStr = product.price?.toString() ?? "0";
    const selectedProduct: SelectedSaleReturnProduct = {
        productId: product.id,
        productName: product.name,
        code: product.code,
        stock: product.quantity ?? 0,
        price: priceStr,
        discount: "0",
        tax: "0",
        subTotal: priceStr,
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

// ORIGINAL SALE SEARCH
const saleSearchQuery = ref("");
const allSales = ref<Sale[]>([]);
const filteredSales = ref<Sale[]>([]);

const saleStore = useSaleStore();

onMounted(async () => {
    if (!saleStore.sales.length) {
        await saleStore.fetchSales();
    }
    allSales.value = saleStore.sales;
});

const customerNameById = (id: number) => {
    const customer = props.customers.find((c) => c.id === id);
    return customer ? customer.name : "Unknown";
};

const updateFilteredSales = () => {
    const query = saleSearchQuery.value.trim().toLowerCase();
    if (!query) {
        filteredSales.value = [];
        return;
    }
    filteredSales.value = allSales.value.filter(
        (s) =>
            s.referenceNumber.toLowerCase().includes(query) ||
            customerNameById(s.customerId).toLowerCase().includes(query)
    );
};

const selectSale = (sale: Sale) => {
    emit("load-sale", sale);
    saleSearchQuery.value = "";
    filteredSales.value = [];
};

const handleSaleSearch = () => {
    if (filteredSales.value.length > 0) {
        selectSale(filteredSales.value[0]);
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

const modelCustomerId = computed({
    get: () => props.customerId,
    set: (val) => emit("update:customerId", val),
});
</script>
