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
                                <div v-if="isFetching" class="small text-muted mt-1">Checking existing tax...</div>
                                <div v-if="existingTax" class="small text-muted mt-1">
                                    Loaded existing tax (id: {{ existingTax.id }}).
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Warehouse -->
                    <div class="col-lg-6">
                        <div class="form-group mb-25">
                            <label class="d-block fs-14 text-black mb-2">Warehouse</label>
                            <select v-model.number="tax.warehouseId"
                                class="bg-white border-0 rounded-1 fs-14 text-optional">
                                <option :value="null">Global (all warehouses)</option>
                                <option v-for="w in warehouses" :key="w.id" :value="w.id">
                                    {{ w.name }}
                                </option>
                            </select>
                        </div>
                    </div>

                    <!-- Tax Code -->
                    <div class="col-lg-6">
                        <div class="form-group mb-25">
                            <label class="d-block fs-14 text-black mb-2">Tax Code</label>
                            <input v-model="tax.taxCode" @input="onTaxCodeInput" type="text"
                                class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
                                placeholder="e.g., VAT, GST" required maxlength="20" />
                        </div>
                    </div>

                    <!-- Tax Name -->
                    <div class="col-lg-6">
                        <div class="form-group mb-25">
                            <label class="d-block fs-14 text-black mb-2">Tax Name</label>
                            <input v-model="tax.taxName" type="text"
                                class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
                                placeholder="e.g., Value Added Tax" required maxlength="100" />
                        </div>
                    </div>

                    <!-- Tax Type -->
                    <div class="col-lg-6">
                        <div class="form-group mb-25">
                            <label class="d-block fs-14 text-black mb-2">Tax Type</label>
                            <select v-model="tax.taxType" class="bg-white border-0 rounded-1 fs-14 text-optional"
                                required>
                                <option disabled value="">Select Tax Type</option>
                                <option v-for="type in Object.values(TaxType)" :key="type" :value="type">
                                    {{ TaxTypeLabels[type] }}
                                </option>
                            </select>
                        </div>
                    </div>

                    <!-- Tax Rate -->
                    <div class="col-lg-6">
                        <div class="form-group mb-25">
                            <label class="d-block fs-14 text-black mb-2">Tax Rate</label>
                            <input v-model.number="tax.taxRate" @input="markDirty" type="number" min="0" step="0.001"
                                class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
                                placeholder="Enter Tax Rate" required />
                            <small class="text-muted">{{ tax.taxType === TaxType.VAT ? 'Percentage (%)' : tax.taxType
                                === TaxType.GST ? 'Percentage (%)' : tax.taxType === TaxType.TDS ? 'Percentage (%)' :
                                'Fixed Amount (if applicable)' }}</small>
                        </div>
                    </div>

                    <!-- Is Inclusive -->
                    <div class="col-lg-6">
                        <div class="form-check mb-25">
                            <input v-model="tax.isInclusive" type="checkbox" class="form-check-input"
                                id="isInclusive" />
                            <label class="form-check-label fs-14 text-black" for="isInclusive">
                                Inclusive Tax (already included in price)
                            </label>
                        </div>
                    </div>

                    <!-- Is Compound -->
                    <div class="col-lg-6">
                        <div class="form-check mb-25">
                            <input v-model="tax.isCompound" type="checkbox" class="form-check-input" id="isCompound" />
                            <label class="form-check-label fs-14 text-black" for="isCompound">
                                Compound Tax (applied on top of other taxes)
                            </label>
                        </div>
                    </div>

                    <!-- Is Active -->
                    <div class="col-lg-6">
                        <div class="form-check mb-25">
                            <input v-model="tax.isActive" type="checkbox" class="form-check-input" id="isActive" />
                            <label class="form-check-label fs-14 text-black" for="isActive">
                                Active
                            </label>
                        </div>
                    </div>

                    <!-- Submit -->
                    <div class="col-12">
                        <button
                            class="btn style-one transition border-0 fw-medium text-white rounded-1 fs-md-15 fs-lg-16"
                            type="submit">
                            {{ existingTax ? 'Update Tax' : 'Submit Tax' }}
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
import { useProductTaxStore } from "@/stores/productTaxStore";
import { useProductStore } from "@/stores/productStore";
import { useWarehouseStore } from "@/stores/warehouseStore";
import type {
    CreateProductTaxRequest,
    UpdateProductTaxRequest,
    ProductTaxResponse
} from "@/types/ProductTax";
import type { Product } from "@/types/Product";
import { TaxType, TaxTypeLabels } from "@/enums/TaxType";

export default defineComponent({
    name: "ManageProductTaxes",
    setup() {
        const route = useRoute();
        const productTaxStore = useProductTaxStore();
        const productStore = useProductStore();
        const warehouseStore = useWarehouseStore();

        const taxIdFromRoute = route.params.id ? Number(route.params.id) : null;

        type EditableTax = {
            id?: number;
            productId: number | null;
            warehouseId: number | null;
            taxCode: string;
            taxName: string;
            taxType: TaxType | "";
            taxRate: number;
            isInclusive: boolean;
            isCompound: boolean;
            isActive: boolean;
        };

        function getDefaultTax(): EditableTax {
            return {
                productId: null,
                warehouseId: null,
                taxCode: "",
                taxName: "",
                taxType: "",
                taxRate: 0,
                isInclusive: false,
                isCompound: false,
                isActive: true,
            };
        }

        const tax = ref<EditableTax>(getDefaultTax());
        const selectedProduct = ref<Product | null>(null);
        const existingTax = ref<ProductTaxResponse | null>(null);
        const isExistingTax = computed(() => !!existingTax.value);

        const isFetching = ref(false);
        const suppressDirty = ref(false);
        const dirty = ref(false);
        const lastFetchedKey = ref<{ productId: number; warehouseId: number | null; taxCode: string } | null>(null);

        const searchQuery = ref("");
        const allProducts = ref<Product[]>([]);
        const filteredProducts = ref<Product[]>([]);

        const warehouses = computed(() => warehouseStore.warehouses);

        function markDirty() {
            if (!suppressDirty.value) dirty.value = true;
        }

        onMounted(async () => {
            await Promise.all([
                productStore.fetchProducts(),
                warehouseStore.fetchWarehouses(),
            ]);
            allProducts.value = productStore.products;

            if (taxIdFromRoute) {
                const fetched = await productTaxStore.fetchTaxById(taxIdFromRoute);
                if (fetched) {
                    existingTax.value = fetched;
                    suppressDirty.value = true;
                    tax.value = {
                        ...fetched,
                        productId: fetched.productId,
                        warehouseId: fetched.warehouseId ?? null,
                        taxType: fetched.taxType,
                        taxRate: fetched.taxRate,
                        isInclusive: fetched.isInclusive,
                        isCompound: fetched.isCompound,
                        isActive: fetched.isActive,
                    };
                    selectedProduct.value = productStore.products.find(p => p.id === fetched.productId) ?? null;
                    suppressDirty.value = false;
                    dirty.value = false;
                    lastFetchedKey.value = {
                        productId: fetched.productId,
                        warehouseId: fetched.warehouseId ?? null,
                        taxCode: fetched.taxCode,
                    };
                }
            }
        });

        // Watch for changes that might trigger a fetch for existing tax
        watch(
            () => [tax.value.productId, tax.value.warehouseId, tax.value.taxCode],
            async ([productId, warehouseId, taxCode]) => {
                if (!productId || !taxCode) {
                    existingTax.value = null;
                    lastFetchedKey.value = null;
                    return;
                }

                // Cast to correct types after null check
                const prodId = productId as number;
                const wareId = warehouseId as number | null;
                const taxCd = taxCode as string;

                const currentKey = { productId: prodId, warehouseId: wareId, taxCode: taxCd };
                if (lastFetchedKey.value &&
                    lastFetchedKey.value.productId === currentKey.productId &&
                    lastFetchedKey.value.warehouseId === currentKey.warehouseId &&
                    lastFetchedKey.value.taxCode === currentKey.taxCode) {
                    return;
                }

                if (dirty.value) {
                    existingTax.value = null;
                    lastFetchedKey.value = currentKey;
                    return;
                }

                isFetching.value = true;
                try {
                    const found = await productTaxStore.fetchEffectiveTax(prodId, taxCd, wareId ?? undefined);
                    if (found) {
                        existingTax.value = found;
                        suppressDirty.value = true;
                        tax.value = {
                            ...found,
                            productId: found.productId,
                            warehouseId: found.warehouseId ?? null,
                            taxType: found.taxType,
                            taxRate: found.taxRate,
                            isInclusive: found.isInclusive,
                            isCompound: found.isCompound,
                            isActive: found.isActive,
                        };
                        selectedProduct.value = productStore.products.find(p => p.id === found.productId) ?? null;
                        suppressDirty.value = false;
                        dirty.value = false;
                        lastFetchedKey.value = currentKey;
                    } else {
                        existingTax.value = null;
                        lastFetchedKey.value = currentKey;
                    }
                } catch (err) {
                    console.error("Failed to fetch tax:", err);
                    alert("Failed to fetch existing tax.");
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

        function selectProduct(p: Product) {
            selectedProduct.value = p;
            suppressDirty.value = true;
            tax.value.productId = p.id;
            suppressDirty.value = false;
            dirty.value = false;
            searchQuery.value = "";
            filteredProducts.value = [];
        }

        function handleSearch() {
            if (filteredProducts.value.length > 0) selectProduct(filteredProducts.value[0]);
        }

        function onTaxCodeInput() {
            // When tax code changes, we need to mark dirty and potentially trigger fetch
            // But watch already handles it. We just need to mark dirty if user typed.
            markDirty();
        }

        async function submit() {
            try {
                if (!tax.value.productId || !tax.value.taxCode || !tax.value.taxName || !tax.value.taxType || tax.value.taxRate <= 0) {
                    alert("Please fill all required fields (product, tax code, tax name, tax type, tax rate > 0).");
                    return;
                }

                const payload: CreateProductTaxRequest | UpdateProductTaxRequest = {
                    productId: tax.value.productId,
                    warehouseId: tax.value.warehouseId ?? undefined,
                    taxCode: tax.value.taxCode,
                    taxName: tax.value.taxName,
                    taxType: tax.value.taxType as TaxType,
                    taxRate: tax.value.taxRate,
                    isInclusive: tax.value.isInclusive,
                    isCompound: tax.value.isCompound,
                    isActive: tax.value.isActive,
                };

                if (existingTax.value) {
                    await productTaxStore.modifyTax(existingTax.value.id, payload);
                    alert("Tax updated successfully!");
                } else {
                    await productTaxStore.addTax(payload as CreateProductTaxRequest);
                    alert("Tax created successfully!");
                }

                resetForm();
            } catch (err: any) {
                console.error("Tax save failed:", err);
                alert(err?.response?.data?.message || "Failed to save tax.");
            }
        }

        function resetForm() {
            tax.value = getDefaultTax();
            selectedProduct.value = null;
            existingTax.value = null;
            searchQuery.value = "";
            filteredProducts.value = [];
            dirty.value = false;
            lastFetchedKey.value = null;
        }

        return {
            tax,
            warehouses,
            searchQuery,
            filteredProducts,
            selectedProduct,
            existingTax,
            isExistingTax,
            isFetching,
            updateFilteredProducts,
            selectProduct,
            handleSearch,
            onTaxCodeInput,
            submit,
            markDirty,
            TaxType,
            TaxTypeLabels,
        };
    }
});
</script>