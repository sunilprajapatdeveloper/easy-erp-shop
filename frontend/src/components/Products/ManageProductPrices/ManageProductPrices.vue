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

                            <div v-if="selectedProduct" class="mt-2 small">
                                <span class="me-1">Selected:</span>
                                <span class="text-success">{{ selectedProduct.name }} ({{ selectedProduct.code
                                }})</span>
                                <div v-if="isFetching" class="small text-muted mt-1">Checking existing price...</div>
                                <div v-if="existingPrice" class="small text-muted mt-1">
                                    Loaded existing price for this combination (id: {{ existingPrice.id }}).
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Warehouse -->
                    <div class="col-lg-6">
                        <div class="form-group mb-25">
                            <label class="d-block fs-14 text-black mb-2">Warehouse</label>
                            <select v-model.number="price.warehouseId"
                                class="bg-white border-0 rounded-1 fs-14 text-optional" required>
                                <option disabled :value="null">Choose Warehouse</option>
                                <option v-for="w in warehouses" :key="w.id" :value="w.id">
                                    {{ w.name }}
                                </option>
                            </select>
                        </div>
                    </div>

                    <!-- Price List -->
                    <div class="col-lg-6">
                        <div class="form-group mb-25">
                            <label class="d-block fs-14 text-black mb-2">Price List</label>
                            <select v-model="price.priceList" class="bg-white border-0 rounded-1 fs-14 text-optional">
                                <option disabled value="">Choose Price List</option>
                                <option v-for="pl in Object.values(PriceListType)" :key="pl" :value="pl">
                                    {{ PriceListTypeLabels[pl] }}
                                </option>
                            </select>
                        </div>
                    </div>

                    <!-- Channel -->
                    <div class="col-lg-6">
                        <div class="form-group mb-25">
                            <label class="d-block fs-14 text-black mb-2">Channel</label>
                            <select v-model="price.channel" class="bg-white border-0 rounded-1 fs-14 text-optional">
                                <option disabled value="">Choose Channel</option>
                                <option v-for="ch in Object.values(SalesChannel)" :key="ch" :value="ch">
                                    {{ SalesChannelLabels[ch] }}
                                </option>
                            </select>
                        </div>
                    </div>

                    <!-- Customer Group -->
                    <div class="col-lg-6">
                        <div class="form-group mb-25">
                            <label class="d-block fs-14 text-black mb-2">Customer Group</label>
                            <select v-model="price.customerGroup"
                                class="bg-white border-0 rounded-1 fs-14 text-optional">
                                <option disabled value="">Choose Customer Group</option>
                                <option v-for="cg in Object.values(CustomerGroup)" :key="cg" :value="cg">
                                    {{ CustomerGroupLabels[cg] }}
                                </option>
                            </select>
                        </div>
                    </div>

                    <!-- Price -->
                    <div class="col-lg-6">
                        <div class="form-group mb-25">
                            <label class="d-block fs-14 text-black mb-2">Price</label>
                            <input v-model.number="price.price" @input="markDirty" type="number" min="0" step="0.01"
                                class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
                                placeholder="Enter Price" required />
                        </div>
                    </div>

                    <!-- Cost -->
                    <div class="col-lg-6">
                        <div class="form-group mb-25">
                            <label class="d-block fs-14 text-black mb-2">Cost</label>
                            <input v-model.number="price.cost" @input="markDirty" type="number" min="0" step="0.01"
                                class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
                                placeholder="Enter Cost" />
                        </div>
                    </div>

                    <!-- Min Price -->
                    <div class="col-lg-6">
                        <div class="form-group mb-25">
                            <label class="d-block fs-14 text-black mb-2">Min Price</label>
                            <input v-model.number="price.minPrice" @input="markDirty" type="number" min="0" step="0.01"
                                class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
                                placeholder="Enter Min Price" />
                        </div>
                    </div>

                    <!-- Max Price -->
                    <div class="col-lg-6">
                        <div class="form-group mb-25">
                            <label class="d-block fs-14 text-black mb-2">Max Price</label>
                            <input v-model.number="price.maxPrice" @input="markDirty" type="number" min="0" step="0.01"
                                class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
                                placeholder="Enter Max Price" />
                        </div>
                    </div>

                    <!-- Currency -->
                    <div class="col-lg-6">
                        <div class="form-group mb-25">
                            <label class="d-block fs-14 text-black mb-2">Currency</label>
                            <select v-model.number="price.currencyId"
                                class="bg-white border-0 rounded-1 fs-14 text-optional" required>
                                <option disabled :value="null">Choose Currency</option>
                                <option v-for="c in currencies" :key="c.id" :value="c.id">
                                    {{ c.code }} - {{ c.name }}
                                </option>
                            </select>
                        </div>
                    </div>

                    <!-- Min Quantity -->
                    <div class="col-lg-6">
                        <div class="form-group mb-25">
                            <label class="d-block fs-14 text-black mb-2">Min Quantity</label>
                            <input v-model.number="price.minQuantity" @input="markDirty" type="number" min="0"
                                class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
                                placeholder="Enter Min Quantity" />
                        </div>
                    </div>

                    <!-- Max Quantity -->
                    <div class="col-lg-6">
                        <div class="form-group mb-25">
                            <label class="d-block fs-14 text-black mb-2">Max Quantity</label>
                            <input v-model.number="price.maxQuantity" @input="markDirty" type="number" min="0"
                                class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
                                placeholder="Enter Max Quantity" />
                        </div>
                    </div>

                    <!-- Valid From -->
                    <div class="col-lg-6">
                        <div class="form-group mb-25">
                            <label class="d-block fs-14 text-black mb-2">Valid From</label>
                            <input v-model="price.validFrom" type="date"
                                class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title" />
                        </div>
                    </div>

                    <!-- Valid To -->
                    <div class="col-lg-6">
                        <div class="form-group mb-25">
                            <label class="d-block fs-14 text-black mb-2">Valid To</label>
                            <input v-model="price.validTo" type="date"
                                class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title" />
                        </div>
                    </div>

                    <!-- Submit -->
                    <div class="col-12">
                        <button
                            class="btn style-one transition border-0 fw-medium text-white rounded-1 fs-md-15 fs-lg-16"
                            type="submit">
                            {{ isExistingPrice ? 'Update Price' : 'Submit Price' }}
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
import { useProductPriceStore } from "@/stores/productPriceStore";
import { useProductStore } from "@/stores/productStore";
import { useWarehouseStore } from "@/stores/warehouseStore";
import { useCurrencyStore } from "@/stores/currencyStore";
import type {
    CreateProductPriceRequest,
    UpdateProductPriceRequest,
    ProductPriceResponse
} from "@/types/ProductPrice";
import type { Product } from "@/types/Product";
import { PriceListType } from "@/enums/PriceListType";
import { PriceListTypeLabels } from "@/enums/PriceListType";
import { SalesChannel } from "@/enums/SalesChannel";
import { SalesChannelLabels } from "@/enums/SalesChannel";
import { CustomerGroup } from "@/enums/CustomerGroup";
import { CustomerGroupLabels } from "@/enums/CustomerGroup";

export default defineComponent({
    name: "ManageProductPrices",
    setup() {
        const route = useRoute();
        const productPriceStore = useProductPriceStore();
        const productStore = useProductStore();
        const warehouseStore = useWarehouseStore();
        const currencyStore = useCurrencyStore();

        const priceIdFromRoute = route.params.id ? Number(route.params.id) : null;

        type EditablePrice = {
            id?: number;
            productId: number | null;
            warehouseId: number | null;
            priceList?: string;
            channel?: string;
            customerGroup?: string;
            price: number;
            cost?: number;
            minPrice?: number;
            maxPrice?: number;
            currencyId: number | null;
            validFrom?: string;
            validTo?: string;
            minQuantity?: number;
            maxQuantity?: number;
        };

        function getDefaultPrice(): EditablePrice {
            return {
                productId: null,
                warehouseId: null,
                priceList: "",
                channel: "",
                customerGroup: "",
                price: 0,
                cost: 0,
                minPrice: 0,
                maxPrice: 0,
                currencyId: null,
                validFrom: "",
                validTo: "",
                minQuantity: 0,
                maxQuantity: 0
            };
        }

        const price = ref<EditablePrice>(getDefaultPrice());
        const selectedProduct = ref<Product | null>(null);
        const existingPrice = ref<ProductPriceResponse | null>(null);
        const isExistingPrice = computed(() => !!existingPrice.value);

        const isFetching = ref(false);
        const suppressDirty = ref(false);
        const dirty = ref(false);
        const lastFetchedPair = ref<{ productId: number; warehouseId: number } | null>(null);

        const searchQuery = ref("");
        const allProducts = ref<Product[]>([]);
        const filteredProducts = ref<Product[]>([]);

        const warehouses = computed(() => warehouseStore.warehouses);
        const currencies = computed(() => currencyStore.currencies);

        function markDirty() {
            if (!suppressDirty.value) dirty.value = true;
        }

        onMounted(async () => {
            await Promise.all([
                productStore.fetchProducts(),
                warehouseStore.fetchWarehouses(),
                currencyStore.fetchCurrencies()
            ]);
            allProducts.value = productStore.products;

            if (priceIdFromRoute) {
                const fetched = await productPriceStore.fetchPriceById(priceIdFromRoute);
                if (fetched) {
                    existingPrice.value = fetched;
                    suppressDirty.value = true;
                    price.value = {
                        ...fetched,
                        warehouseId: fetched.warehouseId ?? null,
                        validFrom: fetched.validFrom ? fetched.validFrom.split("T")[0] : "",
                        validTo: fetched.validTo ? fetched.validTo.split("T")[0] : "",
                    };
                    selectedProduct.value = productStore.products.find(p => p.id === fetched.productId) ?? null;
                    suppressDirty.value = false;
                    dirty.value = false;
                    lastFetchedPair.value = { productId: fetched.productId, warehouseId: fetched.warehouseId ?? 0 };
                }
            }
        });

        watch(
            () => [price.value.productId, price.value.warehouseId],
            async ([productId, warehouseId]) => {
                if (!productId || !warehouseId) {
                    existingPrice.value = null;
                    lastFetchedPair.value = null;
                    return;
                }

                if (lastFetchedPair.value?.productId === productId && lastFetchedPair.value?.warehouseId === warehouseId) return;

                if (dirty.value) {
                    existingPrice.value = null;
                    lastFetchedPair.value = { productId, warehouseId };
                    return;
                }

                isFetching.value = true;
                try {
                    const found = await productPriceStore.fetchByProductAndWarehouse(productId, warehouseId);
                    if (found) {
                        existingPrice.value = found;
                        suppressDirty.value = true;
                        price.value = {
                            ...found,
                            warehouseId: found.warehouseId ?? null,
                            validFrom: found.validFrom ? found.validFrom.split("T")[0] : "",
                            validTo: found.validTo ? found.validTo.split("T")[0] : "",
                        };
                        selectedProduct.value = productStore.products.find(p => p.id === found.productId) ?? null;
                        suppressDirty.value = false;
                        dirty.value = false;
                        lastFetchedPair.value = { productId, warehouseId };
                    } else {
                        existingPrice.value = null;
                        const keepProduct = price.value.productId;
                        const keepWarehouse = price.value.warehouseId;
                        suppressDirty.value = true;
                        price.value = getDefaultPrice();
                        price.value.productId = keepProduct;
                        price.value.warehouseId = keepWarehouse ?? null;
                        suppressDirty.value = false;
                        dirty.value = false;
                        lastFetchedPair.value = { productId, warehouseId };
                    }
                } catch (err) {
                    console.error("Failed to fetch price:", err);
                    alert("Failed to fetch existing price.");
                } finally {
                    isFetching.value = false;
                }
            }
        );

        function updateFilteredProducts() {
            const q = searchQuery.value.trim().toLowerCase();
            if (!q) {
                filteredProducts.value = [];
                return;
            }
            filteredProducts.value = allProducts.value.filter(p =>
                (p.code || "").toLowerCase().includes(q) || (p.name || "").toLowerCase().includes(q)
            );
        }

        function toDateTimeString(date?: string): string | undefined {
            if (!date) return undefined;
            return `${date}T00:00:00`;
        }

        function selectProduct(p: Product) {
            selectedProduct.value = p;
            suppressDirty.value = true;
            price.value.productId = p.id;
            suppressDirty.value = false;
            dirty.value = false;
            searchQuery.value = "";
            filteredProducts.value = [];
        }

        function handleSearch() {
            if (filteredProducts.value.length > 0) selectProduct(filteredProducts.value[0]);
        }

        async function submit() {
            try {
                if (!price.value.productId || !price.value.warehouseId || !price.value.currencyId) {
                    alert("Please select product, warehouse, and currency.");
                    return;
                }

                const payload: CreateProductPriceRequest | UpdateProductPriceRequest = {
                    ...price.value,
                    productId: price.value.productId,
                    warehouseId: price.value.warehouseId,
                    currencyId: price.value.currencyId,
                    validFrom: toDateTimeString(price.value.validFrom),
                    validTo: toDateTimeString(price.value.validTo),
                };

                if (existingPrice.value) {
                    (payload as UpdateProductPriceRequest).id = existingPrice.value.id;
                    await productPriceStore.updatePrice(existingPrice.value.id, payload as UpdateProductPriceRequest);
                    alert("Price updated successfully!");
                } else {
                    await productPriceStore.addPrice(payload as CreateProductPriceRequest);
                    alert("Price created successfully!");
                }

                resetForm();
            } catch (err: any) {
                console.error("Price save failed:", err);
                alert(err?.response?.data?.message || "Failed to save price.");
            }
        }

        function resetForm() {
            price.value = getDefaultPrice();
            selectedProduct.value = null;
            existingPrice.value = null;
            searchQuery.value = "";
            filteredProducts.value = [];
            dirty.value = false;
            lastFetchedPair.value = null;
        }

        return {
            price,
            warehouses,
            currencies,
            searchQuery,
            filteredProducts,
            selectedProduct,
            existingPrice,
            isExistingPrice,
            isFetching,
            updateFilteredProducts,
            selectProduct,
            handleSearch,
            submit,
            markDirty,
            PriceListType,
            PriceListTypeLabels,
            SalesChannel,
            SalesChannelLabels,
            CustomerGroup,
            CustomerGroupLabels,
        };
    }
});
</script>
