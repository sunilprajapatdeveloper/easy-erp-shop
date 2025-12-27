<template>
    <form @submit.prevent="submit" class="pb-60">
        <div class="row">
            <div class="col-xxl-9 col-xl-8 col-lg-8 pe-xxl-6 mb-md-25">
                <div class="row gx-xxl-6">

                    <!-- Product Search -->
                    <div class="col-lg-6">
                        <div class="form-group mb-25 position-relative">
                            <label class="d-block fs-14 text-black mb-2">Search Product</label>
                            <div class="search-area position-relative w-100">
                                <input v-model="searchQuery" @keyup.enter.prevent="handleSearch"
                                    @input="updateFilteredProducts" type="text"
                                    placeholder="Scan / Search product by code or name"
                                    class="w-100 h-55 bg_ash border-0 rounded-1 fs-14 text-black bg-white" />
                                <button type="button" @click="handleSearch"
                                    class="bg-transparent border-0 position-absolute top-0 end-0 h-100 pt-0 py-0 px-2">
                                    <img src="@/assets/img/icons/search.svg" alt="Search" />
                                </button>
                            </div>

                            <!-- Dropdown -->
                            <div v-if="filteredProducts.length || searchQuery.trim()"
                                class="position-absolute w-100 mt-1 bg-white border rounded shadow"
                                style="z-index: 1050; max-height: 180px; overflow-y: auto;">
                                <ul v-if="filteredProducts.length" class="list-group list-group-flush m-0">
                                    <li v-for="product in filteredProducts" :key="product.id"
                                        @mousedown.prevent="selectProduct(product)"
                                        class="list-group-item list-group-item-action px-3 py-2"
                                        style="cursor: pointer;">
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

                            <!-- Selected Product Display -->
                            <div v-if="selectedProduct" class="mt-2 small">
                                <span class="me-1">Selected:</span>
                                <span class="text-success">{{ selectedProduct.name }} ({{ selectedProduct.code
                                    }})</span>
                                <div v-if="isFetching" class="small text-muted mt-1">Checking existing stock...</div>
                                <div v-if="existingStock" class="small text-muted mt-1">
                                    Loaded existing stock for this warehouse (id: {{ existingStock.id }}).
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Warehouse -->
                    <div class="col-lg-6">
                        <div class="form-group mb-25">
                            <label class="d-block fs-14 text-black mb-2">Warehouse</label>
                            <select v-model.number="stock.warehouseId"
                                class="bg-white border-0 rounded-1 fs-14 text-optional" required>
                                <option disabled :value="null">Choose Warehouse</option>
                                <option v-for="w in warehouses" :key="w.id" :value="w.id">
                                    {{ w.name }}
                                </option>
                            </select>
                        </div>
                    </div>

                    <!-- Quantity -->
                    <div class="col-lg-6">
                        <div class="form-group mb-25">
                            <label class="d-block fs-14 text-black mb-2">Quantity</label>
                            <input v-model.number="stock.quantity" @input="markDirty" type="number" min="0"
                                class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
                                placeholder="Enter Quantity" />
                        </div>
                    </div>

                    <!-- Reserved Quantity -->
                    <div class="col-lg-6">
                        <div class="form-group mb-25">
                            <label class="d-block fs-14 text-black mb-2">Reserved Quantity</label>
                            <input v-model.number="stock.reservedQuantity" @input="markDirty" type="number" min="0"
                                class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
                                placeholder="Enter Reserved Qty" />
                        </div>
                    </div>

                    <!-- In Transit -->
                    <div class="col-lg-6">
                        <div class="form-group mb-25">
                            <label class="d-block fs-14 text-black mb-2">In Transit Quantity</label>
                            <input v-model.number="stock.inTransitQuantity" @input="markDirty" type="number" min="0"
                                class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
                                placeholder="Enter In-Transit Qty" />
                        </div>
                    </div>

                    <!-- Committed -->
                    <div class="col-lg-6">
                        <div class="form-group mb-25">
                            <label class="d-block fs-14 text-black mb-2">Committed Quantity</label>
                            <input v-model.number="stock.committedQuantity" @input="markDirty" type="number" min="0"
                                class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
                                placeholder="Enter Committed Qty" />
                        </div>
                    </div>

                    <!-- Min Stock Level -->
                    <div class="col-lg-6">
                        <div class="form-group mb-25">
                            <label class="d-block fs-14 text-black mb-2">Min Stock Level</label>
                            <input v-model.number="stock.minStockLevel" @input="markDirty" type="number" min="0"
                                class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
                                placeholder="Enter Min Stock Level" />
                        </div>
                    </div>

                    <!-- Max Stock Level -->
                    <div class="col-lg-6">
                        <div class="form-group mb-25">
                            <label class="d-block fs-14 text-black mb-2">Max Stock Level</label>
                            <input v-model.number="stock.maxStockLevel" @input="markDirty" type="number" min="0"
                                class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
                                placeholder="Enter Max Stock Level" />
                        </div>
                    </div>

                    <!-- Reorder Level -->
                    <div class="col-lg-6">
                        <div class="form-group mb-25">
                            <label class="d-block fs-14 text-black mb-2">Reorder Level</label>
                            <input v-model.number="stock.reorderLevel" @input="markDirty" type="number" min="0"
                                class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
                                placeholder="Enter Reorder Level" />
                        </div>
                    </div>

                    <!-- Average Cost -->
                    <div class="col-lg-6">
                        <div class="form-group mb-25">
                            <label class="d-block fs-14 text-black mb-2">Average Cost</label>
                            <input v-model="stock.averageCost" @input="markDirty" type="text"
                                class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
                                placeholder="Enter Average Cost" />
                        </div>
                    </div>

                    <!-- Last Count Date -->
                    <div class="col-lg-6">
                        <div class="form-group mb-25">
                            <label class="d-block fs-14 text-black mb-2">Last Count Date</label>
                            <input v-model="stock.lastCountDate" @input="markDirty" type="date"
                                class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title" />
                        </div>
                    </div>

                    <!-- Next Count Date -->
                    <div class="col-lg-6">
                        <div class="form-group mb-25">
                            <label class="d-block fs-14 text-black mb-2">Next Count Date</label>
                            <input v-model="stock.nextCountDate" @input="markDirty" type="date"
                                class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title" />
                        </div>
                    </div>

                    <!-- Submit -->
                    <div class="col-12">
                        <button
                            class="btn style-one transition border-0 fw-medium text-white rounded-1 fs-md-15 fs-lg-16"
                            type="submit">
                            {{ isExistingStock ? 'Update Stock' : 'Submit Stock' }}
                        </button>
                    </div>

                </div>
            </div>
        </div>
    </form>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted, computed, watch } from "vue";
import { useRoute } from "vue-router";
import { useProductStockStore } from "@/stores/productStockStore";
import { useProductStore } from "@/stores/productStore";
import { useWarehouseStore } from "@/stores/warehouseStore";
import type {
    CreateProductStockRequest,
    UpdateProductStockRequest,
    ProductStockResponse,
} from "@/types/ProductStock";
import type { Product } from "@/types/Product";

export default defineComponent({
    name: "ManageProductStocks",
    setup() {
        const route = useRoute();
        const productStockStore = useProductStockStore();
        const productStore = useProductStore();
        const warehouseStore = useWarehouseStore();

        const stockIdFromRoute = route.params.id ? Number(route.params.id) : null;

        type EditableStock = {
            id?: number;
            productId: number | null;
            warehouseId: number | null;
            quantity: number;
            reservedQuantity: number;
            inTransitQuantity: number;
            committedQuantity: number;
            minStockLevel: number | null;
            maxStockLevel: number | null;
            reorderLevel: number | null;
            averageCost: string;
            lastCountDate: string;
            nextCountDate: string;
        };

        function getDefaultStock(): EditableStock {
            return {
                productId: null,
                warehouseId: null,
                quantity: 0,
                reservedQuantity: 0,
                inTransitQuantity: 0,
                committedQuantity: 0,
                minStockLevel: null,
                maxStockLevel: null,
                reorderLevel: null,
                averageCost: "",
                lastCountDate: "",
                nextCountDate: "",
            };
        }

        const stock = ref<EditableStock>(getDefaultStock());
        const selectedProduct = ref<Product | null>(null);
        const existingStock = ref<ProductStockResponse | null>(null);
        const isExistingStock = computed(() => !!existingStock.value);

        const isFetching = ref(false);
        const suppressDirty = ref(false);
        const dirty = ref(false);
        const lastFetchedPair = ref<{ productId: number; warehouseId: number } | null>(null);

        // product search
        const searchQuery = ref("");
        const allProducts = ref<Product[]>([]);
        const filteredProducts = ref<Product[]>([]);

        const warehouses = computed(() => warehouseStore.warehouses);

        // mark field edits
        function markDirty() {
            if (!suppressDirty.value) dirty.value = true;
        }

        // fetch lists on mount and optionally load by route id
        onMounted(async () => {
            await Promise.all([productStore.fetchProducts(), warehouseStore.fetchWarehouses()]);
            allProducts.value = productStore.products;

            if (stockIdFromRoute) {
                // explicit edit-by-stock-id: load and populate
                const fetched = await productStockStore.fetchStockById(stockIdFromRoute);
                if (fetched) {
                    existingStock.value = fetched;
                    suppressDirty.value = true;
                    stock.value = {
                        id: fetched.id,
                        productId: fetched.productId ?? null,
                        warehouseId: fetched.warehouseId ?? null,
                        quantity: fetched.quantity ?? 0,
                        reservedQuantity: fetched.reservedQuantity ?? 0,
                        inTransitQuantity: fetched.inTransitQuantity ?? 0,
                        committedQuantity: fetched.committedQuantity ?? 0,
                        minStockLevel: fetched.minStockLevel ?? null,
                        maxStockLevel: fetched.maxStockLevel ?? null,
                        reorderLevel: fetched.reorderLevel ?? null,
                        averageCost: fetched.averageCost ?? "",
                        lastCountDate: fetched.lastCountDate ?? "",
                        nextCountDate: fetched.nextCountDate ?? "",
                    };
                    // set selected product from product store
                    selectedProduct.value = productStore.products.find((p) => p.id === fetched.productId) ?? null;
                    suppressDirty.value = false;
                    dirty.value = false;
                    lastFetchedPair.value = fetched.productId != null && fetched.warehouseId != null
                        ? { productId: fetched.productId, warehouseId: fetched.warehouseId }
                        : null;
                }
            }
        });

        // When productId or warehouseId changes we try to load existing stock for that pair.
        watch(
            () => [stock.value.productId, stock.value.warehouseId],
            async ([productId, warehouseId], [_oldP, _oldW]) => {
                // reset existingStock unless we fetch and find again for same pair
                if (productId == null || warehouseId == null) {
                    // if either missing, just clear existingStock but keep current inputs
                    existingStock.value = null;
                    lastFetchedPair.value = null;
                    return;
                }

                // don't re-fetch the same pair
                if (
                    lastFetchedPair.value &&
                    lastFetchedPair.value.productId === productId &&
                    lastFetchedPair.value.warehouseId === warehouseId
                ) {
                    return;
                }

                // If user modified fields (dirty) and you don't want to override them, skip fetch.
                // We only auto-fetch/populate when the form is not dirty (fresh selection).
                if (dirty.value) {
                    // clear existingStock so submit will create unless user chooses otherwise
                    existingStock.value = null;
                    lastFetchedPair.value = { productId, warehouseId };
                    return;
                }

                // fetch existing
                isFetching.value = true;
                try {
                    const found = await productStockStore.fetchByProductAndWarehouse(productId, warehouseId);
                    if (found) {
                        existingStock.value = found;
                        suppressDirty.value = true;
                        stock.value = {
                            id: found.id,
                            productId: found.productId ?? null,
                            warehouseId: found.warehouseId ?? null,
                            quantity: found.quantity ?? 0,
                            reservedQuantity: found.reservedQuantity ?? 0,
                            inTransitQuantity: found.inTransitQuantity ?? 0,
                            committedQuantity: found.committedQuantity ?? 0,
                            minStockLevel: found.minStockLevel ?? null,
                            maxStockLevel: found.maxStockLevel ?? null,
                            reorderLevel: found.reorderLevel ?? null,
                            averageCost: found.averageCost ?? "",
                            lastCountDate: found.lastCountDate ?? "",
                            nextCountDate: found.nextCountDate ?? "",
                        };
                        // set selected product reference if available
                        selectedProduct.value = productStore.products.find((p) => p.id === found.productId) ?? null;
                        suppressDirty.value = false;
                        dirty.value = false;
                        lastFetchedPair.value = { productId, warehouseId };
                    } else {
                        // No existing stock: clear existing and set default numeric fields but keep productId & warehouseId
                        existingStock.value = null;
                        const keepProduct = stock.value.productId;
                        const keepWarehouse = stock.value.warehouseId;
                        suppressDirty.value = true;
                        stock.value = getDefaultStock();
                        stock.value.productId = keepProduct;
                        stock.value.warehouseId = keepWarehouse;
                        suppressDirty.value = false;
                        dirty.value = false;
                        lastFetchedPair.value = { productId, warehouseId };
                    }
                } catch (err) {
                    console.error("Failed to check existing product stock:", err);
                    const message =
                        (err as any)?.response?.data?.error ||
                        (err as any)?.response?.data?.message ||
                        (err as any)?.message ||
                        "Failed to load existing stock.";
                    alert(message);
                } finally {
                    isFetching.value = false;
                }
            }
        );

        // product search helpers
        function updateFilteredProducts() {
            const q = searchQuery.value.trim().toLowerCase();
            if (!q) {
                filteredProducts.value = [];
                return;
            }
            filteredProducts.value = allProducts.value.filter((p) => {
                return (p.code || "").toLowerCase().includes(q) || (p.name || "").toLowerCase().includes(q);
            });
        }

        function selectProduct(p: Product) {
            if (typeof p.id !== "number") return;
            selectedProduct.value = p;
            // set productId; if warehouse is already selected the watch will attempt load
            suppressDirty.value = true;
            stock.value.productId = p.id;
            suppressDirty.value = false;
            dirty.value = false;
            searchQuery.value = "";
            filteredProducts.value = [];
        }

        function handleSearch() {
            if (filteredProducts.value.length > 0) {
                selectProduct(filteredProducts.value[0]);
            }
        }

        // create / update
        async function submit() {
            try {
                if (stock.value.productId == null || stock.value.warehouseId == null) {
                    alert("Please select a product and a warehouse before saving.");
                    return;
                }

                const createPayload: CreateProductStockRequest = {
                    productId: stock.value.productId,
                    warehouseId: stock.value.warehouseId,
                    quantity: stock.value.quantity ?? 0,
                    reservedQuantity: stock.value.reservedQuantity ?? 0,
                    inTransitQuantity: stock.value.inTransitQuantity ?? 0,
                    committedQuantity: stock.value.committedQuantity ?? 0,
                    minStockLevel: stock.value.minStockLevel ?? undefined,
                    maxStockLevel: stock.value.maxStockLevel ?? undefined,
                    reorderLevel: stock.value.reorderLevel ?? undefined,
                    averageCost: stock.value.averageCost || undefined,
                    lastCountDate: stock.value.lastCountDate || undefined,
                    nextCountDate: stock.value.nextCountDate || undefined,
                };

                if (existingStock.value) {
                    const updatePayload: UpdateProductStockRequest = {
                        id: existingStock.value.id,
                        productId: stock.value.productId!,
                        warehouseId: stock.value.warehouseId!,
                        quantity: stock.value.quantity ?? 0,
                        reservedQuantity: stock.value.reservedQuantity ?? 0,
                        inTransitQuantity: stock.value.inTransitQuantity ?? 0,
                        committedQuantity: stock.value.committedQuantity ?? 0,
                        minStockLevel: stock.value.minStockLevel ?? undefined,
                        maxStockLevel: stock.value.maxStockLevel ?? undefined,
                        reorderLevel: stock.value.reorderLevel ?? undefined,
                        stockAlert: existingStock.value.stockAlert ?? undefined,
                        averageCost: stock.value.averageCost || undefined,
                        lastCountDate: stock.value.lastCountDate || undefined,
                        nextCountDate: stock.value.nextCountDate || undefined,
                    };

                    await productStockStore.updateStock(existingStock.value.id, updatePayload);
                    alert("Stock updated successfully!");
                } else {
                    await productStockStore.addStock(createPayload);
                    alert("Stock created successfully!");
                }

                resetForm();
            } catch (err: any) {
                console.error("Stock save failed:", err);
                const message =
                    err?.response?.data?.error ||
                    err?.response?.data?.message ||
                    err?.message ||
                    "Failed to save stock. Please try again.";
                alert(message);
            }
        }

        function resetForm() {
            stock.value = getDefaultStock();
            selectedProduct.value = null;
            existingStock.value = null;
            searchQuery.value = "";
            filteredProducts.value = [];
            dirty.value = false;
            lastFetchedPair.value = null;
        }

        // keep a local copy of product list (updated on mount)
        onMounted(() => {
            allProducts.value = productStore.products;
        });

        return {
            stock,
            submit,
            warehouses,
            searchQuery,
            filteredProducts,
            selectedProduct,
            updateFilteredProducts,
            selectProduct,
            handleSearch,
            isExistingStock,
            existingStock,
            isFetching,
            markDirty,
        };
    },
});
</script>
