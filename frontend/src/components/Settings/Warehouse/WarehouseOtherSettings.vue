<template>
    <div class="card border-0 shadow-none rounded-1 mb-40">
        <div class="card-body p-xl-40">
            <h6 class="fs-18 mb-35 text-title fw-semibold">Warehouse Other Settings</h6>

            <form @submit.prevent="handleSubmit">
                <div class="row">

                    <!-- Currency (read-only) -->
                    <div class="col-lg-4">
                        <div class="form-group mb-30">
                            <label class="d-block fs-14 text-black mb-2">Currency</label>
                            <input type="text" class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-title"
                                :value="currentCurrencyText" disabled />
                            <small class="text-muted">Currency is set in the Currency Settings page</small>
                        </div>
                    </div>

                    <!-- Headquarter -->
                    <div class="col-lg-4">
                        <div class="checkbox style-four mb-30">
                            <input type="checkbox" id="headquarter" v-model="form.headquarter"
                                class="form-check-input" />
                            <label class="form-check-label" for="headquarter">Headquarter</label>
                        </div>
                    </div>

                    <!-- Default Warehouse -->
                    <div class="col-lg-4">
                        <div class="checkbox style-four mb-30">
                            <input type="checkbox" id="defaultWarehouse" v-model="form.isDefault"
                                class="form-check-input" />
                            <label class="form-check-label" for="defaultWarehouse">Default Warehouse</label>
                        </div>
                    </div>

                    <!-- Active -->
                    <div class="col-lg-4">
                        <div class="checkbox style-four mb-30">
                            <input type="checkbox" id="active" v-model="form.active" class="form-check-input" />
                            <label class="form-check-label" for="active">Active</label>
                        </div>
                    </div>

                    <!-- Apply Tax -->
                    <div class="col-lg-4">
                        <div class="checkbox style-four mb-30">
                            <input type="checkbox" id="applyTax" v-model="form.applyTax" class="form-check-input" />
                            <label class="form-check-label" for="applyTax">Apply Tax</label>
                        </div>
                    </div>

                    <!-- Apply TDS -->
                    <div class="col-lg-4">
                        <div class="checkbox style-four mb-30">
                            <input type="checkbox" id="applyTds" v-model="form.applyTds" class="form-check-input" />
                            <label class="form-check-label" for="applyTds">Apply TDS</label>
                        </div>
                    </div>

                    <!-- Track Inventory -->
                    <div class="col-lg-4">
                        <div class="checkbox style-four mb-30">
                            <input type="checkbox" id="trackInventory" v-model="form.trackInventory"
                                class="form-check-input" />
                            <label class="form-check-label" for="trackInventory">Track Inventory</label>
                        </div>
                    </div>

                    <!-- Submit -->
                    <div class="col-lg-6">
                        <button type="submit" class="btn style-five" :disabled="loading">
                            <span v-if="loading">Updating...</span>
                            <span v-else>Save Other Settings</span>
                        </button>
                    </div>

                </div>
            </form>
        </div>
    </div>
</template>

<script setup lang="ts">
import { reactive, ref, computed, watch, defineProps } from "vue";
import { useWarehouseStore } from "@/stores/warehouseStore";
import { useCurrencySettingStore } from "@/stores/warehouseCurrencyStore";
import { useUserStore } from "@/stores/userStore";
import type { WarehouseDetail, UpdateWarehouseRequest } from "@/types/Warehouse";
import type { CurrencySetting } from "@/types/WarehouseCurrency";

// Props
const props = defineProps<{ warehouseId: number | undefined }>();

// Stores
const warehouseStore = useWarehouseStore();
const currencySettingStore = useCurrencySettingStore();
const userStore = useUserStore();

// Loading state
const loading = ref(false);

// Reactive form
const form = reactive<UpdateWarehouseRequest>({
    id: 0,
    headquarter: false,
    isDefault: false,
    active: true,
    applyTax: false,
    applyTds: false,
    trackInventory: true,
    updatedBy: userStore.currentUser?.id ?? 0,
    companyId: 0,
});

// Current warehouse currency
const currentCurrency = ref<CurrencySetting | null>(null);
const currentCurrencyText = computed(() =>
    currentCurrency.value
        ? `${currentCurrency.value.currencyCode} - ${currentCurrency.value.currencyName}`
        : ""
);

// Helper: Load warehouse data
const loadWarehouseData = async (warehouseId: number) => {
    try {
        const warehouse: WarehouseDetail = await warehouseStore.fetchWarehouseDetail(warehouseId);

        Object.assign(form, {
            id: warehouse.id,
            headquarter: warehouse.headquarter,
            isDefault: warehouse.isDefault,
            active: warehouse.active,
            applyTax: warehouse.applyTax,
            applyTds: warehouse.applyTds,
            trackInventory: warehouse.trackInventory,
            updatedBy: userStore.currentUser?.id ?? 0,
            companyId: warehouse.companyId,
        });

        await currencySettingStore.fetchAll(warehouse.companyId);

        currentCurrency.value =
            currencySettingStore.list.find(cs => cs.warehouseId === warehouseId) ||
            currencySettingStore.list.find(cs => cs.defaultCurrency) ||
            null;
    } catch (err) {
        console.error("Failed to load warehouse other settings:", err);
    }
};

// Watch warehouseId prop
watch(
    () => props.warehouseId,
    id => {
        if (id !== undefined) loadWarehouseData(id);
    },
    { immediate: true }
);

// Submit handler
const handleSubmit = async () => {
    if (!form.id) return;
    try {
        loading.value = true;
        await warehouseStore.updateWarehouse(form.id, form);
        alert("Warehouse other settings updated successfully!");
    } catch (err: any) {
        console.error("Failed to update warehouse other settings:", err);
        alert(err.message ?? "Update failed");
    } finally {
        loading.value = false;
    }
};
</script>
