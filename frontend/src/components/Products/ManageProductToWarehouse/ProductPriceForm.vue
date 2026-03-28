<template>
    <form @submit.prevent="submit" class="price-form">
        <div class="row g-3">
            <!-- Price List -->
            <div class="col-md-6">
                <div class="form-group">
                    <label class="d-block fs-14 text-black mb-2">Price List</label>
                    <select v-model="form.priceList" class="bg-white border-0 rounded-1 fs-14 text-optional w-100">
                        <option disabled value="">Choose Price List</option>
                        <option v-for="pl in Object.values(PriceListType)" :key="pl" :value="pl">
                            {{ PriceListTypeLabels[pl] }}
                        </option>
                    </select>
                </div>
            </div>

            <!-- Channel -->
            <div class="col-md-6">
                <div class="form-group">
                    <label class="d-block fs-14 text-black mb-2">Channel</label>
                    <select v-model="form.channel" class="bg-white border-0 rounded-1 fs-14 text-optional w-100">
                        <option disabled value="">Choose Channel</option>
                        <option v-for="ch in Object.values(SalesChannel)" :key="ch" :value="ch">
                            {{ SalesChannelLabels[ch] }}
                        </option>
                    </select>
                </div>
            </div>

            <!-- Customer Group -->
            <div class="col-md-6">
                <div class="form-group">
                    <label class="d-block fs-14 text-black mb-2">Customer Group</label>
                    <select v-model="form.customerGroup" class="bg-white border-0 rounded-1 fs-14 text-optional w-100">
                        <option disabled value="">Choose Customer Group</option>
                        <option v-for="cg in Object.values(CustomerGroup)" :key="cg" :value="cg">
                            {{ CustomerGroupLabels[cg] }}
                        </option>
                    </select>
                </div>
            </div>

            <!-- Price -->
            <div class="col-md-6">
                <div class="form-group">
                    <label class="d-block fs-14 text-black mb-2">Price</label>
                    <input v-model.number="form.price" type="number" min="0" step="0.01"
                        class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title" placeholder="Enter Price"
                        required />
                </div>
            </div>

            <!-- Cost -->
            <div class="col-md-6">
                <div class="form-group">
                    <label class="d-block fs-14 text-black mb-2">Cost</label>
                    <input v-model.number="form.cost" type="number" min="0" step="0.01"
                        class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
                        placeholder="Enter Cost" />
                </div>
            </div>

            <!-- Min Price -->
            <div class="col-md-6">
                <div class="form-group">
                    <label class="d-block fs-14 text-black mb-2">Min Price</label>
                    <input v-model.number="form.minPrice" type="number" min="0" step="0.01"
                        class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
                        placeholder="Enter Min Price" />
                </div>
            </div>

            <!-- Max Price -->
            <div class="col-md-6">
                <div class="form-group">
                    <label class="d-block fs-14 text-black mb-2">Max Price</label>
                    <input v-model.number="form.maxPrice" type="number" min="0" step="0.01"
                        class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
                        placeholder="Enter Max Price" />
                </div>
            </div>

            <!-- Currency -->
            <div class="col-md-6">
                <div class="form-group">
                    <label class="d-block fs-14 text-black mb-2">Currency</label>
                    <select v-model.number="form.currencyId"
                        class="bg-white border-0 rounded-1 fs-14 text-optional w-100" required>
                        <option disabled :value="null">Choose Currency</option>
                        <option v-for="c in currencies" :key="c.id" :value="c.id">
                            {{ c.code }} - {{ c.name }}
                        </option>
                    </select>
                </div>
            </div>

            <!-- Min Quantity -->
            <div class="col-md-6">
                <div class="form-group">
                    <label class="d-block fs-14 text-black mb-2">Min Quantity</label>
                    <input v-model.number="form.minQuantity" type="number" min="0"
                        class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
                        placeholder="Enter Min Quantity" />
                </div>
            </div>

            <!-- Max Quantity -->
            <div class="col-md-6">
                <div class="form-group">
                    <label class="d-block fs-14 text-black mb-2">Max Quantity</label>
                    <input v-model.number="form.maxQuantity" type="number" min="0"
                        class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
                        placeholder="Enter Max Quantity" />
                </div>
            </div>

            <!-- Valid From -->
            <div class="col-md-6">
                <div class="form-group">
                    <label class="d-block fs-14 text-black mb-2">Valid From</label>
                    <input v-model="form.validFrom" type="date"
                        class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title" />
                </div>
            </div>

            <!-- Valid To -->
            <div class="col-md-6">
                <div class="form-group">
                    <label class="d-block fs-14 text-black mb-2">Valid To</label>
                    <input v-model="form.validTo" type="date"
                        class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title" />
                </div>
            </div>

            <!-- Is Active with BaseCheckbox -->
            <div class="col-md-12">
                <BaseCheckbox v-model="form.isActive" label="Active" />
            </div>

            <div class="col-12">
                <button class="btn style-one transition border-0 fw-medium text-white rounded-1 fs-md-15 fs-lg-16"
                    type="submit" :disabled="loading">
                    {{ existingData ? 'Update Price' : 'Create Price' }}
                </button>
            </div>
        </div>
    </form>
</template>

<script lang="ts" setup>
import { ref, watch, onMounted } from 'vue';
import { useCurrencyStore } from '@/stores/currencyStore';
import type { ProductPrice, CreateProductPriceRequest, UpdateProductPriceRequest } from '@/types/ProductPrice';
import { PriceListType, PriceListTypeLabels } from '@/enums/PriceListType';
import { SalesChannel, SalesChannelLabels } from '@/enums/SalesChannel';
import { CustomerGroup, CustomerGroupLabels } from '@/enums/CustomerGroup';
import BaseCheckbox from '@/components/ui/BaseCheckbox.vue';

const props = defineProps<{
    productId: number;
    warehouseId: number;
    existingData: ProductPrice | null;
    loading: boolean;
}>();

const emit = defineEmits<{
    (e: 'save', payload: CreateProductPriceRequest | UpdateProductPriceRequest): void;
}>();

const currencyStore = useCurrencyStore();
const currencies = ref<any[]>([]);

type FormData = {
    productId: number;
    warehouseId: number | null;
    priceList: string;
    channel: string;
    customerGroup: string;
    price: number;
    cost: number;
    minPrice: number;
    maxPrice: number;
    currencyId: number | null;
    isActive: boolean;
    minQuantity: number;
    maxQuantity: number;
    validFrom: string;
    validTo: string;
};

const defaultForm = (): FormData => ({
    productId: props.productId,
    warehouseId: props.warehouseId,
    priceList: '',
    channel: '',
    customerGroup: '',
    price: 0,
    cost: 0,
    minPrice: 0,
    maxPrice: 0,
    currencyId: null,
    isActive: true,
    minQuantity: 0,
    maxQuantity: 0,
    validFrom: '',
    validTo: '',
});

const form = ref<FormData>(defaultForm());

// Populate form when existingData changes
watch(
    () => props.existingData,
    (data) => {
        if (data) {
            form.value = {
                productId: data.productId,
                warehouseId: data.warehouseId ?? null,
                priceList: data.priceList || '',
                channel: data.channel || '',
                customerGroup: data.customerGroup || '',
                price: data.price,
                cost: data.cost ?? 0,
                minPrice: data.minPrice ?? 0,
                maxPrice: data.maxPrice ?? 0,
                currencyId: data.currencyId,
                isActive: data.isActive,
                minQuantity: data.minQuantity ?? 0,
                maxQuantity: data.maxQuantity ?? 0,
                validFrom: data.validFrom ? data.validFrom.split('T')[0] : '',
                validTo: data.validTo ? data.validTo.split('T')[0] : '',
            };
        } else {
            // Reset to defaults but keep product/warehouse
            form.value = {
                ...defaultForm(),
                productId: props.productId,
                warehouseId: props.warehouseId,
            };
        }
    },
    { immediate: true }
);

// Watch for prop changes to ensure productId/warehouseId are correct
watch([() => props.productId, () => props.warehouseId], ([prodId, wareId]) => {
    if (prodId && wareId) {
        form.value.productId = prodId;
        form.value.warehouseId = wareId;
    }
});

onMounted(async () => {
    await currencyStore.fetchCurrencies();
    currencies.value = currencyStore.currencies;
});

function submit() {
    // Prepare payload
    const payload: CreateProductPriceRequest | UpdateProductPriceRequest = {
        productId: form.value.productId,
        warehouseId: form.value.warehouseId ?? undefined,
        priceList: form.value.priceList || undefined,
        channel: form.value.channel || undefined,
        customerGroup: form.value.customerGroup || undefined,
        price: form.value.price,
        cost: form.value.cost,
        minPrice: form.value.minPrice,
        maxPrice: form.value.maxPrice,
        currencyId: form.value.currencyId!,
        isActive: form.value.isActive,
        minQuantity: form.value.minQuantity,
        maxQuantity: form.value.maxQuantity,
        validFrom: form.value.validFrom ? `${form.value.validFrom}T00:00:00` : undefined,
        validTo: form.value.validTo ? `${form.value.validTo}T00:00:00` : undefined,
    };

    if (props.existingData) {
        (payload as UpdateProductPriceRequest).id = props.existingData.id;
    }

    emit('save', payload);
}
</script>