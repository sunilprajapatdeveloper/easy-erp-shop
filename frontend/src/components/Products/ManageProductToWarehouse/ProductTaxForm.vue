<template>
    <form @submit.prevent="submit" class="tax-form">
        <div class="row g-3">
            <!-- Tax Code -->
            <div class="col-md-6">
                <div class="form-group">
                    <label class="d-block fs-14 text-black mb-2">Tax Code</label>
                    <input v-model="form.taxCode" type="text"
                        class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
                        placeholder="e.g., VAT, GST" required maxlength="20" />
                </div>
            </div>

            <!-- Tax Name -->
            <div class="col-md-6">
                <div class="form-group">
                    <label class="d-block fs-14 text-black mb-2">Tax Name</label>
                    <input v-model="form.taxName" type="text"
                        class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
                        placeholder="e.g., Value Added Tax" required maxlength="100" />
                </div>
            </div>

            <!-- Tax Type -->
            <div class="col-md-6">
                <div class="form-group">
                    <label class="d-block fs-14 text-black mb-2">Tax Type</label>
                    <select v-model="form.taxCategory" class="bg-white border-0 rounded-1 fs-14 text-optional w-100"
                        required>
                        <option disabled value="">Select Tax Type</option>
                        <option v-for="type in Object.values(TaxCategory)" :key="type" :value="type">
                            {{ TaxCategoryLabels[type] }}
                        </option>
                    </select>
                </div>
            </div>

            <!-- Tax Rate -->
            <div class="col-md-6">
                <div class="form-group">
                    <label class="d-block fs-14 text-black mb-2">Tax Rate</label>
                    <input v-model.number="form.taxRate" type="number" min="0" step="0.001"
                        class="w-100 d-block shadow-none fs-14 bg-white rounded-1 text-title"
                        placeholder="Enter Tax Rate" required />
                    <small class="text-muted">
                        {{
                            form.taxCategory === TaxCategory.VAT
                                ? "Percentage (%)"
                                : form.taxCategory === TaxCategory.GST
                                    ? "Percentage (%)"
                                    : form.taxCategory === TaxCategory.TDS
                                        ? "Percentage (%)"
                                        : "Fixed Amount (if applicable)"
                        }}
                    </small>
                </div>
            </div>

            <!-- Is Inclusive -->
            <div class="col-md-6">
                <BaseCheckbox v-model="form.isInclusive" label="Inclusive Tax (already included in price)" />
            </div>

            <!-- Is Compound -->
            <div class="col-md-6">
                <BaseCheckbox v-model="form.isCompound" label="Compound Tax (applied on top of other taxes)" />
            </div>

            <!-- Is Active -->
            <div class="col-md-12">
                <BaseCheckbox v-model="form.isActive" label="Active" />
            </div>

            <div class="col-12">
                <button class="btn style-one transition border-0 fw-medium text-white rounded-1 fs-md-15 fs-lg-16"
                    type="submit" :disabled="loading">
                    {{ existingData ? "Update Tax" : "Create Tax" }}
                </button>
            </div>
        </div>
    </form>
</template>

<script lang="ts" setup>
import { ref, watch } from "vue";
import type {
    ProductTax,
    CreateProductTaxRequest,
    UpdateProductTaxRequest,
} from "@/types/ProductTax";
import { TaxCategory, TaxCategoryLabels } from "@/enums/TaxCategory";
import BaseCheckbox from "@/components/ui/BaseCheckbox.vue";

const props = defineProps<{
    productId: number;
    warehouseId: number;
    existingData: ProductTax | null;
    loading: boolean;
}>();

const emit = defineEmits<{
    (e: "save", payload: CreateProductTaxRequest | UpdateProductTaxRequest): void;
}>();

type FormData = {
    productId: number;
    warehouseId: number | null;
    taxCode: string;
    taxName: string;
    taxCategory: TaxCategory | "";
    taxRate: number;
    isInclusive: boolean;
    isCompound: boolean;
    isActive: boolean;
};

const defaultForm = (): FormData => ({
    productId: props.productId,
    warehouseId: props.warehouseId,
    taxCode: "",
    taxName: "",
    taxCategory: "",
    taxRate: 0,
    isInclusive: false,
    isCompound: false,
    isActive: true,
});

const form = ref<FormData>(defaultForm());

watch(
    () => props.existingData,
    (data) => {
        if (data) {
            form.value = {
                productId: data.productId,
                warehouseId: data.warehouseId ?? null,
                taxCode: data.taxCode,
                taxName: data.taxName,
                taxCategory: data.taxCategory,
                taxRate: data.taxRate,
                isInclusive: data.isInclusive,
                isCompound: data.isCompound,
                isActive: data.isActive,
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

watch(
    [() => props.productId, () => props.warehouseId],
    ([prodId, wareId]) => {
        form.value.productId = prodId;
        form.value.warehouseId = wareId;
    }
);

function submit() {
    const payload: CreateProductTaxRequest | UpdateProductTaxRequest = {
        productId: form.value.productId,
        warehouseId: form.value.warehouseId ?? undefined,
        taxCode: form.value.taxCode,
        taxName: form.value.taxName,
        taxCategory: form.value.taxCategory as TaxCategory,
        taxRate: form.value.taxRate,
        isInclusive: form.value.isInclusive,
        isCompound: form.value.isCompound,
        isActive: form.value.isActive,
    };

    if (props.existingData) {
        emit("save", payload);
    } else {
        emit("save", payload);
    }
}
</script>