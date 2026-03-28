<template>
    <div class="product-search position-relative">
        <label class="d-block fs-14 text-black mb-2">Search Product</label>
        <div class="search-area position-relative w-100">
            <input v-model="searchQuery" @keyup.enter="handleSearch" @input="updateFilteredProducts" type="text"
                placeholder="Scan / Search product by code or name"
                class="w-100 h-55 bg_ash border-0 rounded-1 fs-14 text-black bg-white" :disabled="disabled" />
            <button type="button" @click="handleSearch"
                class="bg-transparent border-0 position-absolute top-0 end-0 h-100 pt-0 py-0 px-2">
                <img src="@/assets/img/icons/search.svg" alt="Search" />
            </button>
        </div>

        <div v-if="filteredProducts.length || searchQuery.trim()"
            class="position-absolute w-100 mt-1 bg-white border rounded shadow"
            style="z-index: 1050; max-height: 180px; overflow-y: auto">
            <ul v-if="filteredProducts.length" class="list-group list-group-flush m-0">
                <li v-for="product in filteredProducts" :key="product.id" @mousedown.prevent="selectProduct(product)"
                    class="list-group-item list-group-item-action px-3 py-2" style="cursor: pointer">
                    <div class="fw-semibold small text-dark">
                        {{ product.name }}
                        <span class="text-muted ms-1">({{ product.code }})</span>
                    </div>
                </li>
            </ul>
            <div v-else class="text-center text-muted small py-3">No products found.
            </div>
        </div>
    </div>
</template>

<script lang="ts" setup>
import { ref, watch, onMounted } from 'vue';
import { useProductStore } from '@/stores/productStore';
import type { ProductResponse } from '@/types/Product';

const props = defineProps<{
    modelValue: ProductResponse | null;
    disabled?: boolean;
}>();

const emit = defineEmits<{
    (e: 'update:modelValue', value: ProductResponse | null): void;
}>();

const productStore = useProductStore();

const searchQuery = ref('');
const filteredProducts = ref<ProductResponse[]>([]);
const allProducts = ref<ProductResponse[]>([]);

// No internal selectedProduct – we rely on the parent's modelValue.

watch(
    () => props.modelValue,
    (newVal) => {
        if (newVal) {
            searchQuery.value = '';
            filteredProducts.value = [];
        }
    }
);

onMounted(async () => {
    await productStore.fetchProducts();
    allProducts.value = productStore.products;
});

function updateFilteredProducts() {
    const q = searchQuery.value.trim().toLowerCase();
    if (!q) {
        filteredProducts.value = [];
        return;
    }
    filteredProducts.value = allProducts.value.filter(
        (p) =>
            (p.code || '').toLowerCase().includes(q) || (p.name || '').toLowerCase().includes(q)
    );
}

function handleSearch() {
    if (filteredProducts.value.length > 0) {
        selectProduct(filteredProducts.value[0]);
    }
}

function selectProduct(product: ProductResponse) {
    emit('update:modelValue', product);
    searchQuery.value = '';
    filteredProducts.value = [];
}
</script>