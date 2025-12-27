<template>
    <div class="card border-0 shadow-none rounded-1 mb-40">
        <div class="card-body p-xl-40">
            <h6 class="fs-18 mb-35 text-title fw-semibold">POS General Settings</h6>

            <form @submit.prevent="handleSubmit">
                <div class="row">

                    <!-- Warehouse Selection -->
                    <div class="col-lg-4">
                        <div class="form-group mb-30">
                            <label class="d-block fs-14 text-black mb-2">Warehouse</label>
                            <select v-model="selectedWarehouseId"
                                class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-title"
                                @change="onWarehouseChange">
                                <option :value="0">-- Select Warehouse --</option>
                                <option v-for="w in warehouses" :key="w.id" :value="w.id">{{ w.name }}</option>
                            </select>
                        </div>
                    </div>

                    <!-- Default Customer -->
                    <div class="col-lg-4">
                        <div class="form-group mb-30">
                            <label class="d-block fs-14 text-black mb-2">Default Customer</label>
                            <select v-model="form.defaultCustomerId"
                                class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-title">
                                <option :value="undefined">-- Select Customer --</option>
                                <option v-for="c in customers" :key="c.id" :value="c.id">{{ c.name }}</option>
                            </select>
                        </div>
                    </div>

                    <!-- Default Currency -->
                    <div class="col-lg-4">
                        <div class="form-group mb-30">
                            <label class="d-block fs-14 text-black mb-2">Default Currency</label>
                            <select v-model="form.defaultCurrencyId" required
                                class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-title">
                                <option :value="undefined">-- Select Currency --</option>
                                <option v-for="c in currencies" :key="c.id" :value="c.id">{{ c.currencyCode }} ({{
                                    c.symbol }})
                                </option>
                            </select>
                        </div>
                    </div>

                    <!-- Default Payment Method -->
                    <div class="col-lg-4">
                        <div class="form-group mb-30">
                            <label class="d-block fs-14 text-black mb-2">Default Payment Method</label>
                            <input v-model="form.defaultPaymentMethod" type="text"
                                class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-title"
                                placeholder="Cash / Card / UPI" />
                        </div>
                    </div>

                    <div class="row mt-20 mb-20">
                        <!-- Default Tax Inclusive -->
                        <div class="col-lg-3">
                            <div class="checkbox style-four mb-30">
                                <input type="checkbox" id="taxInclusive" v-model="form.defaultTaxInclusive"
                                    class="form-check-input" />
                                <label class="form-check-label" for="taxInclusive">Tax Inclusive</label>
                            </div>
                        </div>

                    </div>

                    <!-- Submit Button -->
                    <div class="col-lg-6">
                        <button type="submit" class="btn style-five" :disabled="loading || !selectedWarehouseId">
                            <span v-if="loading">Saving...</span>
                            <span v-else>Save Settings</span>
                        </button>
                    </div>

                </div>
            </form>
        </div>
    </div>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted, watch } from "vue";
import { useUserStore } from "@/stores/userStore";
import { usePOSSettingsStore } from "@/stores/posSettingsStore";
import { useWarehouseStore } from "@/stores/warehouseStore";
import { useCustomerStore } from "@/stores/customerStore";
import { useWarehouseCurrencyStore } from "@/stores/warehouseCurrencyStore";
import type {
    POSGeneralSettingsResponse,
    UpdatePOSGeneralSettingsRequest,
} from "@/types/POSGeneralSettings";

// Stores
const userStore = useUserStore();
const posSettingsStore = usePOSSettingsStore();
const warehouseStore = useWarehouseStore();
const customerStore = useCustomerStore();
const currencyStore = useWarehouseCurrencyStore();

// Reactive form
const form = reactive<UpdatePOSGeneralSettingsRequest>({
    defaultCustomerId: undefined,
    defaultCurrencyId: 0,
    defaultPaymentMethod: "",
    defaultTaxInclusive: false,
});

// Loading & selected warehouse
const loading = ref(false);
const selectedWarehouseId = ref<number>(0);

// Dropdown lists
const warehouses = ref(warehouseStore.warehouses);
const customers = ref(customerStore.customers);
const currencies = ref(currencyStore.list);

// Fetch warehouses, customers, currencies on mount
onMounted(async () => {
    try {
        loading.value = true;

        await warehouseStore.fetchWarehouses();
        warehouses.value = warehouseStore.warehouses;

        if (userStore.currentUser?.defaultWarehouseId) {
            selectedWarehouseId.value = userStore.currentUser.defaultWarehouseId;
        } else if (warehouses.value.length > 0) {
            selectedWarehouseId.value = warehouses.value[0].id;
        }

        await customerStore.fetchCustomers();
        customers.value = customerStore.customers;

        if (selectedWarehouseId.value) {
            await loadPOSSettings(selectedWarehouseId.value);
            await currencyStore.fetchAll(userStore.currentUser!.companyId, selectedWarehouseId.value);
            currencies.value = currencyStore.list;
        }
    } catch (err) {
        console.error(err);
    } finally {
        loading.value = false;
    }
});

// Watch warehouse selection to reload POS settings & currencies
const onWarehouseChange = async () => {
    if (!selectedWarehouseId.value) return;

    try {
        loading.value = true;
        await loadPOSSettings(selectedWarehouseId.value);
        await currencyStore.fetchAll(userStore.currentUser!.companyId, selectedWarehouseId.value);
        currencies.value = currencyStore.list;
    } catch (err) {
        console.error("Failed to load warehouse settings:", err);
    } finally {
        loading.value = false;
    }
};

// Load POS settings for selected warehouse
const loadPOSSettings = async (warehouseId: number) => {
    const data: POSGeneralSettingsResponse = await posSettingsStore.fetchPOSSettings(warehouseId);
    if (data) {
        form.defaultCustomerId = data.defaultCustomerId;
        form.defaultCurrencyId = data.defaultCurrencyId;
        form.defaultPaymentMethod = data.defaultPaymentMethod ?? "";
        form.defaultTaxInclusive = data.defaultTaxInclusive;
    }
};

// Submit handler
const handleSubmit = async () => {
    if (!selectedWarehouseId.value) return;

    try {
        loading.value = true;
        const updateData: UpdatePOSGeneralSettingsRequest = { ...form };
        await posSettingsStore.updatePOSSettings(selectedWarehouseId.value, posSettingsStore.posSettings!.id, updateData);
        alert("POS settings updated successfully!");
    } catch (err: any) {
        console.error("Failed to save POS settings:", err);
        alert(err.message ?? "Save failed");
    } finally {
        loading.value = false;
    }
};
</script>
