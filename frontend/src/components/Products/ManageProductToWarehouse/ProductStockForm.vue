<template>
    <form @submit.prevent="submit" class="stock-form">
        <div class="row g-3">
            <!-- Quantity -->
            <div class="col-md-6">
                <div class="form-group">
                    <label class="d-block fs-14 text-black mb-2">Quantity</label>
                    <input v-model.number="form.quantity" type="number" min="0" step="1"
                        class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
                        placeholder="Enter Quantity" required />
                </div>
            </div>

            <!-- Reserved Quantity -->
            <div class="col-md-6">
                <div class="form-group">
                    <label class="d-block fs-14 text-black mb-2">Reserved Quantity</label>
                    <input v-model.number="form.reservedQuantity" type="number" min="0" step="1"
                        class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
                        placeholder="Enter Reserved Quantity" />
                </div>
            </div>

            <!-- In Transit Quantity -->
            <div class="col-md-6">
                <div class="form-group">
                    <label class="d-block fs-14 text-black mb-2">In Transit Quantity</label>
                    <input v-model.number="form.inTransitQuantity" type="number" min="0" step="1"
                        class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
                        placeholder="Enter In Transit Quantity" />
                </div>
            </div>

            <!-- Committed Quantity -->
            <div class="col-md-6">
                <div class="form-group">
                    <label class="d-block fs-14 text-black mb-2">Committed Quantity</label>
                    <input v-model.number="form.committedQuantity" type="number" min="0" step="1"
                        class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
                        placeholder="Enter Committed Quantity" />
                </div>
            </div>

            <!-- Min Stock Level -->
            <div class="col-md-6">
                <div class="form-group">
                    <label class="d-block fs-14 text-black mb-2">Min Stock Level</label>
                    <input v-model.number="form.minStockLevel" type="number" min="0" step="1"
                        class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
                        placeholder="Enter Min Stock Level" />
                </div>
            </div>

            <!-- Max Stock Level -->
            <div class="col-md-6">
                <div class="form-group">
                    <label class="d-block fs-14 text-black mb-2">Max Stock Level</label>
                    <input v-model.number="form.maxStockLevel" type="number" min="0" step="1"
                        class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
                        placeholder="Enter Max Stock Level" />
                </div>
            </div>

            <!-- Reorder Level -->
            <div class="col-md-6">
                <div class="form-group">
                    <label class="d-block fs-14 text-black mb-2">Reorder Level</label>
                    <input v-model.number="form.reorderLevel" type="number" min="0" step="1"
                        class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
                        placeholder="Enter Reorder Level" />
                </div>
            </div>

            <!-- Average Cost -->
            <div class="col-md-6">
                <div class="form-group">
                    <label class="d-block fs-14 text-black mb-2">Average Cost</label>
                    <input v-model="form.averageCost" type="text"
                        class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
                        placeholder="Enter Average Cost" />
                </div>
            </div>

            <!-- Last Count Date -->
            <div class="col-md-6">
                <div class="form-group">
                    <label class="d-block fs-14 text-black mb-2">Last Count Date</label>
                    <input v-model="form.lastCountDate" type="date"
                        class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title" />
                </div>
            </div>

            <!-- Next Count Date -->
            <div class="col-md-6">
                <div class="form-group">
                    <label class="d-block fs-14 text-black mb-2">Next Count Date</label>
                    <input v-model="form.nextCountDate" type="date"
                        class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title" />
                </div>
            </div>

            <div class="col-12">
                <button class="btn style-one transition border-0 fw-medium text-white rounded-1 fs-md-15 fs-lg-16"
                    type="submit" :disabled="loading">
                    {{ existingData ? 'Update Stock' : 'Create Stock' }}
                </button>
            </div>
        </div>
    </form>
</template>

<script lang="ts" setup>
import { ref, watch } from 'vue';
import type { ProductStock, CreateProductStockRequest, UpdateProductStockRequest } from '@/types/ProductStock';

const props = defineProps<{
    productId: number;
    warehouseId: number;
    existingData: ProductStock | null;
    loading: boolean;
}>();

const emit = defineEmits<{
    (e: 'save', payload: CreateProductStockRequest | UpdateProductStockRequest): void;
}>();

type FormData = {
    productId: number;
    warehouseId: number;
    quantity: number;
    reservedQuantity: number;
    inTransitQuantity: number;
    committedQuantity: number;
    minStockLevel: number | undefined;
    maxStockLevel: number | undefined;
    reorderLevel: number | undefined;
    averageCost: string;
    lastCountDate: string;
    nextCountDate: string;
};

const defaultForm = (): FormData => ({
    productId: props.productId,
    warehouseId: props.warehouseId,
    quantity: 0,
    reservedQuantity: 0,
    inTransitQuantity: 0,
    committedQuantity: 0,
    minStockLevel: undefined,
    maxStockLevel: undefined,
    reorderLevel: undefined,
    averageCost: '',
    lastCountDate: '',
    nextCountDate: '',
});

const form = ref<FormData>(defaultForm());

watch(
    () => props.existingData,
    (data) => {
        if (data) {
            form.value = {
                productId: data.productId,
                warehouseId: data.warehouseId,
                quantity: data.quantity,
                reservedQuantity: data.reservedQuantity,
                inTransitQuantity: data.inTransitQuantity,
                committedQuantity: data.committedQuantity,
                minStockLevel: data.minStockLevel,
                maxStockLevel: data.maxStockLevel,
                reorderLevel: data.reorderLevel,
                averageCost: data.averageCost ?? '',
                lastCountDate: data.lastCountDate ? data.lastCountDate.split('T')[0] : '',
                nextCountDate: data.nextCountDate ? data.nextCountDate.split('T')[0] : '',
            };
        } else {
            form.value = {
                ...defaultForm(),
                productId: props.productId,
                warehouseId: props.warehouseId,
            };
        }
    },
    { immediate: true }
);

watch([() => props.productId, () => props.warehouseId], ([prodId, wareId]) => {
    form.value.productId = prodId;
    form.value.warehouseId = wareId;
});

function submit() {
    const payload: CreateProductStockRequest | UpdateProductStockRequest = {
        productId: form.value.productId,
        warehouseId: form.value.warehouseId,
        quantity: form.value.quantity,
        reservedQuantity: form.value.reservedQuantity,
        inTransitQuantity: form.value.inTransitQuantity,
        committedQuantity: form.value.committedQuantity,
        minStockLevel: form.value.minStockLevel,
        maxStockLevel: form.value.maxStockLevel,
        reorderLevel: form.value.reorderLevel,
        averageCost: form.value.averageCost,
        lastCountDate: form.value.lastCountDate ? `${form.value.lastCountDate}T00:00:00` : undefined,
        nextCountDate: form.value.nextCountDate ? `${form.value.nextCountDate}T00:00:00` : undefined,
    };

    if (props.existingData) {
        (payload as UpdateProductStockRequest).id = props.existingData.id;
    }

    emit('save', payload);
}
</script>