<template>
    <div class="product-warehouse-configurator pb-60">
        <div class="row">
            <div class="col-xxl-9 col-xl-8 col-lg-8 pe-xxl-6 mb-md-25">
                <div class="row gx-xxl-6 gy-3 align-items-end">
                    <div class="col-lg-6">
                        <ProductSearch v-model="selectedProduct" @update:model-value="onProductChange"
                            :disabled="isLoading" />
                    </div>
                    <div class="col-lg-4">
                        <WarehouseSelect v-model="selectedWarehouseId" @update:model-value="onWarehouseChange"
                            :disabled="isLoading" />
                    </div>
                    <div class="col-lg-2 text-sm-start text-lg-end">
                        <button type="button" class="btn style-one" @click="goToCreateProduct"
                            style="white-space: nowrap;" :disabled="isLoading">
                            <i class="ri-add-line"></i> Add New Product
                        </button>
                    </div>
                </div>

                <!-- Selected product info row (only if product selected) -->
                <div v-if="selectedProduct" class="row mt-2 small">
                    <div class="col-12">
                        <span class="me-1">Selected:</span>
                        <span class="text-success">{{ selectedProduct.name }} ({{ selectedProduct.code }})</span>
                    </div>
                </div>

                <!-- Configuration area -->
                <div v-if="selectedProduct && selectedWarehouseId" class="config-tabs mt-4">
                    <ul class="nav table-tablist" role="tablist">
                        <li class="nav-item" role="presentation">
                            <button class="nav-link" :class="{ active: activeTab === 'price' }"
                                @click="activeTab = 'price'" type="button" role="tab">
                                Price
                            </button>
                        </li>
                        <li class="nav-item" role="presentation">
                            <button class="nav-link" :class="{ active: activeTab === 'stock' }"
                                @click="activeTab = 'stock'" type="button" role="tab">
                                Stock
                            </button>
                        </li>
                        <li class="nav-item" role="presentation">
                            <button class="nav-link" :class="{ active: activeTab === 'tax' }" @click="activeTab = 'tax'"
                                type="button" role="tab">
                                Tax
                            </button>
                        </li>
                    </ul>

                    <div class="tab-content mt-3 pt-4 pb-4">
                        <div v-show="activeTab === 'price'" class="tab-pane fade show active">
                            <ProductPriceForm :product-id="selectedProduct.id" :warehouse-id="selectedWarehouseId"
                                :existing-data="priceData" :loading="loading.price" @save="handlePriceSave" />
                        </div>
                        <div v-show="activeTab === 'stock'" class="tab-pane fade show active">
                            <ProductStockForm :product-id="selectedProduct.id" :warehouse-id="selectedWarehouseId"
                                :existing-data="stockData" :loading="loading.stock" @save="handleStockSave" />
                        </div>
                        <div v-show="activeTab === 'tax'" class="tab-pane fade show active">
                            <ProductTaxForm :product-id="selectedProduct.id" :warehouse-id="selectedWarehouseId"
                                :existing-data="taxData" :loading="loading.tax" @save="handleTaxSave" />
                        </div>
                    </div>
                </div>

                <div v-else-if="selectedProduct && !selectedWarehouseId" class="alert alert-info text-center mt-4"
                    role="alert">
                    Please select a warehouse to configure the product.
                </div>
                <div v-else-if="!selectedProduct && selectedWarehouseId" class="alert alert-info text-center mt-4"
                    role="alert">
                    Please select a product to configure.
                </div>
                <div v-else class="alert alert-info text-center mt-4" role="alert">
                    Please select a product and a warehouse to configure.
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts" setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useProductPriceStore } from '@/stores/productPriceStore';
import { useProductStockStore } from '@/stores/productStockStore';
import { useProductTaxStore } from '@/stores/productTaxStore';
import { useProductStore } from '@/stores/productStore';
import ProductSearch from '@/components/Common/ProductSearch.vue';
import WarehouseSelect from '@/components/Common/WarehouseSelect.vue';
import ProductPriceForm from './ProductPriceForm.vue';
import ProductStockForm from './ProductStockForm.vue';
import ProductTaxForm from './ProductTaxForm.vue';
import type { ProductResponse } from '@/types/Product';
import type { ProductPrice } from '@/types/ProductPrice';
import type { ProductStock } from '@/types/ProductStock';
import type { ProductTax } from '@/types/ProductTax';
import type {
    CreateProductPriceRequest,
    UpdateProductPriceRequest,
} from '@/types/ProductPrice';
import type {
    CreateProductStockRequest,
    UpdateProductStockRequest,
} from '@/types/ProductStock';
import type {
    CreateProductTaxRequest,
    UpdateProductTaxRequest,
} from '@/types/ProductTax';

const router = useRouter();
const route = useRoute();
const productStore = useProductStore();

const selectedProduct = ref<ProductResponse | null>(null);
const selectedWarehouseId = ref<number | null>(null);
const activeTab = ref<'price' | 'stock' | 'tax'>('price');

const priceStore = useProductPriceStore();
const stockStore = useProductStockStore();
const taxStore = useProductTaxStore();

const priceData = ref<ProductPrice | null>(null);
const stockData = ref<ProductStock | null>(null);
const taxData = ref<ProductTax | null>(null);

const loading = ref({
    price: false,
    stock: false,
    tax: false,
});

const isLoading = computed(() => loading.value.price || loading.value.stock || loading.value.tax);

function goToCreateProduct() {
    router.push('/create-product');
}

async function fetchAllConfigs(productId: number, warehouseId: number) {
    // Reset current data
    priceData.value = null;
    stockData.value = null;
    taxData.value = null;

    loading.value.price = true;
    loading.value.stock = true;
    loading.value.tax = true;

    try {
        const [price, stock] = await Promise.all([
            priceStore.fetchByProductAndWarehouse(productId, warehouseId),
            stockStore.fetchByProductAndWarehouse(productId, warehouseId),
        ]);
        priceData.value = price;
        stockData.value = stock;

        const taxes = await taxStore.fetchTaxesByProduct(productId);
        const tax = taxes.find(
            (t) => t.warehouseId === warehouseId || t.warehouseId === undefined || t.warehouseId === null
        );
        taxData.value = tax || null;
    } catch (error) {
        console.error('Failed to fetch configurations:', error);
        alert('Failed to load configuration data. Please try again.');
    } finally {
        loading.value.price = false;
        loading.value.stock = false;
        loading.value.tax = false;
    }
}

function onProductChange(product: ProductResponse | null) {
    selectedProduct.value = product;
    if (product && selectedWarehouseId.value) {
        fetchAllConfigs(product.id, selectedWarehouseId.value);
    }
}

function onWarehouseChange(warehouseId: number | null) {
    selectedWarehouseId.value = warehouseId;
    if (selectedProduct.value && warehouseId) {
        fetchAllConfigs(selectedProduct.value.id, warehouseId);
    }
}

async function loadProductFromQuery() {
    const productId = route.query.product_id ? Number(route.query.product_id) : null;
    if (productId && !isNaN(productId)) {
        try {
            const product = await productStore.fetchProductById(productId);
            if (product) {
                selectedProduct.value = product;
            } else {
                console.warn('Product not found for ID:', productId);
            }
        } catch (error) {
            console.error('Failed to load product from query:', error);
        }
    }
}

onMounted(async () => {
    await loadProductFromQuery();
});

async function handlePriceSave(payload: CreateProductPriceRequest | UpdateProductPriceRequest) {
    try {
        if (priceData.value) {
            await priceStore.updatePrice(priceData.value.id, payload as UpdateProductPriceRequest);
            alert('Price updated successfully!');
        } else {
            await priceStore.addPrice(payload as CreateProductPriceRequest);
            alert('Price created successfully!');
        }

        if (selectedProduct.value && selectedWarehouseId.value) {
            priceData.value = await priceStore.fetchByProductAndWarehouse(
                selectedProduct.value.id,
                selectedWarehouseId.value
            );
        }
    } catch (error: any) {
        console.error('Failed to save price:', error);
        alert(error?.response?.data?.message || 'Failed to save price.');
    }
}

async function handleStockSave(payload: CreateProductStockRequest | UpdateProductStockRequest) {
    try {
        if (stockData.value) {
            await stockStore.updateStock(stockData.value.id, payload as UpdateProductStockRequest);
            alert('Stock updated successfully!');
        } else {
            await stockStore.addStock(payload as CreateProductStockRequest);
            alert('Stock created successfully!');
        }

        if (selectedProduct.value && selectedWarehouseId.value) {
            stockData.value = await stockStore.fetchByProductAndWarehouse(
                selectedProduct.value.id,
                selectedWarehouseId.value
            );
        }
    } catch (error: any) {
        console.error('Failed to save stock:', error);
        alert(error?.response?.data?.message || 'Failed to save stock.');
    }
}

async function handleTaxSave(payload: CreateProductTaxRequest | UpdateProductTaxRequest) {
    try {
        if (taxData.value) {
            await taxStore.modifyTax(taxData.value.id, payload);
            alert('Tax updated successfully!');
        } else {
            await taxStore.addTax(payload as CreateProductTaxRequest);
            alert('Tax created successfully!');
        }

        if (selectedProduct.value && selectedWarehouseId.value) {
            const taxes = await taxStore.fetchTaxesByProduct(selectedProduct.value.id);
            const tax = taxes.find(
                (t) =>
                    t.warehouseId === selectedWarehouseId.value ||
                    t.warehouseId === undefined ||
                    t.warehouseId === null
            );
            taxData.value = tax || null;
        }
    } catch (error: any) {
        console.error('Failed to save tax:', error);
        alert(error?.response?.data?.message || 'Failed to save tax.');
    }
}
</script>