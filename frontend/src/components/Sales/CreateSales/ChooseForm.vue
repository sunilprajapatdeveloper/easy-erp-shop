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

    <!-- Error Popup -->
    <div class="error-popup offcanvas offcanvas-end border-0" tabindex="-1" id="errorPopup">
        <div class="offcanvas-body p-0">
            <div class="create-error">
                <img src="../../../assets/img/icons/close-circle-2.svg" alt="Image"
                    style="filter: brightness(0) invert(1);" />
                <span class="text-white fw-medium">{{ errorMessage }}</span>
            </div>
        </div>
    </div>
    <a id="triggerErrorPopup" class="d-none" data-bs-toggle="offcanvas" href="#errorPopup" role="button"></a>
</template>

<script setup lang="ts">
import { ref, computed, watch, onMounted } from "vue";
import { Offcanvas } from "bootstrap";
import type { WarehouseListItem } from "@/types/Warehouse";
import type { Customer } from "@/types/Customer";
import type { Product } from "@/types/Product";
import type { SelectedSaleProduct } from "@/types/Sale";
import { TaxInclusionType } from "@/enums/TaxInclusionType";
import { TaxApplicationOrder } from "@/enums/TaxApplicationOrder";
import { useProductStore } from "@/stores/productStore";
import { useTaxSettingStore } from "@/stores/taxSettingStore";
import { TaxCategory } from "@/enums/TaxCategory";

const props = defineProps<{
    warehouses: WarehouseListItem[];
    customers: Customer[];
    date: string;
    warehouseId: number | null;
    customerId: number | null;
}>();

const emit = defineEmits<{
    (e: "update:date", value: string): void;
    (e: "update:warehouseId", value: number | null): void;
    (e: "update:customerId", value: number | null): void;
    (e: "add-product", value: SelectedSaleProduct): void;
}>();

const searchQuery = ref("");
const allProducts = ref<Product[]>([]);
const filteredProducts = ref<Product[]>([]);
const errorMessage = ref("");

const productStore = useProductStore();
const taxSettingStore = useTaxSettingStore();

watch(
    () => props.warehouseId,
    async (newId) => {
        if (!newId) return;
        await Promise.all([
            productStore.fetchProducts({
                warehouseId: newId,
                includePrice: true,
                includeStock: true,
                includeTax: true,
            }),
            taxSettingStore.fetchActive(newId),
        ]);
        allProducts.value = productStore.products;
        updateFilteredProducts();
    }
);

onMounted(async () => {
    await productStore.fetchProducts({
        warehouseId: props.warehouseId ?? undefined,
        includePrice: true,
        includeStock: true,
        includeTax: true,
    });
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
const modelCustomerId = computed({
    get: () => props.customerId,
    set: (val) => emit("update:customerId", val),
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
    if (!product.id || !props.warehouseId) return;

    const warehouse = props.warehouses.find((w) => w.id === props.warehouseId);
    const warehouseName = warehouse?.name ?? "selected warehouse";

    if (!product.price) {
        showError(
            `Price for product "${product.name}" (${product.code}) is not configured for warehouse "${warehouseName}".`
        );
        return;
    }
    if (!product.stock) {
        showError(
            `Stock for product "${product.name}" (${product.code}) is not configured for warehouse "${warehouseName}".`
        );
        return;
    }

    const productTax = product.tax;
    const activeTaxSetting = taxSettingStore.activeTaxSetting;

    const taxInclusionType: TaxInclusionType =
        productTax?.overrideInclusionType ?? activeTaxSetting?.inclusionType ?? TaxInclusionType.EXCLUSIVE;
    const taxApplicationOrder: TaxApplicationOrder =
        productTax?.overrideApplicationOrder ?? activeTaxSetting?.applicationOrder ?? TaxApplicationOrder.AFTER_DISCOUNT;

    // Emit raw product – NO calculated values
    const selected: SelectedSaleProduct = {
        productId: product.id,
        productName: product.name,
        code: product.code,
        productUnitPrice: Number(product.price.price),
        quantity: 1,
        stock: product.stock.availableQuantity ?? product.stock.quantity ?? 0,
        taxName: productTax?.taxName ?? "",
        taxCategory: productTax?.taxCategory ?? activeTaxSetting?.taxCategory ?? TaxCategory.CUSTOM,
        taxRate: productTax?.taxRate ?? 0,
        taxInclusionType,
        taxApplicationOrder,
        lineDiscountAmount: 0,
        lineNetAmount: 0,
        lineTaxAmount: 0,
        lineGrossAmount: 0,
    };

    emit("add-product", selected);
    searchQuery.value = "";
    filteredProducts.value = [];
};

const handleSearch = () => {
    if (filteredProducts.value.length > 0) {
        selectProduct(filteredProducts.value[0]);
    }
};

const showError = (message: string) => {
    errorMessage.value = message;
    document.getElementById("triggerErrorPopup")?.click();
    setTimeout(() => {
        Offcanvas.getOrCreateInstance(document.getElementById("errorPopup")!).hide();
    }, 3000);
};
</script>