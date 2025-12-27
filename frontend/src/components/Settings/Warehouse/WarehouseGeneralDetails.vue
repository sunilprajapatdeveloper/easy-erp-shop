<template>
    <div class="card border-0 shadow-none rounded-1 mb-40">
        <div class="card-body p-xl-40">
            <h6 class="fs-18 mb-35 text-title fw-semibold">
                Warehouse General Details
            </h6>

            <form @submit.prevent="handleSubmit">
                <div class="row">
                    <!-- Warehouse Name -->
                    <div class="col-lg-4">
                        <div class="form-group mb-30">
                            <label class="d-block fs-14 text-black mb-2">
                                Warehouse Name <span class="text-danger">*</span>
                            </label>
                            <input v-model="form.name" type="text"
                                class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-title"
                                placeholder="Main Warehouse" required />
                        </div>
                    </div>

                    <!-- Phone -->
                    <div class="col-lg-4">
                        <div class="form-group mb-30">
                            <label class="d-block fs-14 text-black mb-2">Phone</label>
                            <input v-model="form.phone" type="text"
                                class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-title"
                                placeholder="123-456-7890" />
                        </div>
                    </div>

                    <!-- Email -->
                    <div class="col-lg-4">
                        <div class="form-group mb-30">
                            <label class="d-block fs-14 text-black mb-2">Email</label>
                            <input v-model="form.email" type="email"
                                class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-title"
                                placeholder="warehouse@example.com" />
                        </div>
                    </div>

                    <!-- City -->
                    <div class="col-lg-4">
                        <div class="form-group mb-30">
                            <label class="d-block fs-14 text-black mb-2">
                                City <span class="text-danger">*</span>
                            </label>
                            <input v-model="form.city" type="text"
                                class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-title"
                                placeholder="Los Angeles" required />
                        </div>
                    </div>

                    <!-- State -->
                    <div class="col-lg-4">
                        <div class="form-group mb-30">
                            <label class="d-block fs-14 text-black mb-2">State</label>
                            <input v-model="form.state" type="text"
                                class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-title"
                                placeholder="California" />
                        </div>
                    </div>

                    <!-- Country -->
                    <div class="col-lg-4">
                        <div class="form-group mb-30">
                            <label class="d-block fs-14 text-black mb-2">
                                Country <span class="text-danger">*</span>
                            </label>
                            <input v-model="form.country" type="text"
                                class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-title"
                                placeholder="United States" required />
                        </div>
                    </div>

                    <!-- Zip Code -->
                    <div class="col-lg-4">
                        <div class="form-group mb-30">
                            <label class="d-block fs-14 text-black mb-2">Zip Code</label>
                            <input v-model="form.zipCode" type="text"
                                class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-title"
                                placeholder="90001" />
                        </div>
                    </div>

                    <!-- Address Line 1 -->
                    <div class="col-lg-12">
                        <div class="form-group mb-30">
                            <label class="d-block fs-14 text-black mb-2">Address Line 1</label>
                            <input v-model="form.addressLine1" type="text"
                                class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-title"
                                placeholder="123 Industrial Road" />
                        </div>
                    </div>

                    <!-- Address Line 2 -->
                    <div class="col-lg-12">
                        <div class="form-group mb-30">
                            <label class="d-block fs-14 text-black mb-2">Address Line 2</label>
                            <input v-model="form.addressLine2" type="text"
                                class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-title"
                                placeholder="Suite 100" />
                        </div>
                    </div>

                    <!-- Timezone -->
                    <div class="col-lg-4">
                        <div class="form-group mb-30">
                            <label class="d-block fs-14 text-black mb-2">
                                Timezone <span class="text-danger">*</span>
                            </label>
                            <input v-model="form.timezone" type="text"
                                class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-title"
                                placeholder="America/Los_Angeles" required />
                        </div>
                    </div>

                    <!-- Invoice Prefix -->
                    <div class="col-lg-4">
                        <div class="form-group mb-30">
                            <label class="d-block fs-14 text-black mb-2">Invoice Prefix</label>
                            <input v-model="form.invoicePrefix" type="text"
                                class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-title" placeholder="WH-" />
                        </div>
                    </div>

                    <!-- Currency Selection -->
                    <!-- <div class="col-lg-4">
                        <div class="form-group mb-30">
                            <label class="d-block fs-14 text-black mb-2">
                                Currency <span class="text-danger">*</span>
                            </label>
                            <select v-model="form.currencyId"
                                class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-title" required>
                                <option v-for="c in currencies" :key="c.currencyId" :value="c.currencyId">
                                    {{ c.currencyCode }} - {{ c.currencyName }}
                                </option>
                            </select>
                        </div>
                    </div> -->

                    <!-- Checkboxes -->
                    <div class="row mt-25 mb-30">
                        <div class="col-lg-3">
                            <div class="checkbox style-four mb-30">
                                <input type="checkbox" id="headquarter" v-model="form.headquarter"
                                    class="form-check-input" />
                                <label class="form-check-label" for="headquarter">Headquarter</label>
                            </div>
                        </div>

                        <div class="col-lg-3">
                            <div class="checkbox style-four mb-30">
                                <input type="checkbox" id="applyTax" v-model="form.applyTax" class="form-check-input" />
                                <label class="form-check-label" for="applyTax">Apply Tax</label>
                            </div>
                        </div>

                        <div class="col-lg-3">
                            <div class="checkbox style-four mb-30">
                                <input type="checkbox" id="applyTds" v-model="form.applyTds" class="form-check-input" />
                                <label class="form-check-label" for="applyTds">Apply TDS</label>
                            </div>
                        </div>

                        <div class="col-lg-3">
                            <div class="checkbox style-four mb-30">
                                <input type="checkbox" id="trackInventory" v-model="form.trackInventory"
                                    class="form-check-input" />
                                <label class="form-check-label" for="trackInventory">Track Inventory</label>
                            </div>
                        </div>

                        <div class="col-lg-3">
                            <div class="checkbox style-four mb-30">
                                <input type="checkbox" id="isDefault" v-model="form.isDefault"
                                    class="form-check-input" />
                                <label class="form-check-label" for="isDefault">Default</label>
                            </div>
                        </div>

                        <div class="col-lg-3">
                            <div class="checkbox style-four mb-30">
                                <input type="checkbox" id="active" v-model="form.active" class="form-check-input" />
                                <label class="form-check-label" for="active">Active</label>
                            </div>
                        </div>
                    </div>

                    <!-- Submit -->
                    <div class="col-lg-12">
                        <button type="submit" class="btn style-five" :disabled="loading">
                            <span v-if="loading">
                                {{ props.warehouseId ? "Updating..." : "Creating..." }}
                            </span>
                            <span v-else>
                                {{ props.warehouseId ? "Save Settings" : "Create Warehouse" }}
                            </span>
                        </button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</template>

<script setup lang="ts">
import { reactive, ref, defineProps, watch } from "vue";
import { useWarehouseStore } from "@/stores/warehouseStore";
import { useUserStore } from "@/stores/userStore";
import { useCompanyCurrencyStore } from "@/stores/companyCurrencyStore";
import { useWarehouseCurrencyStore } from "@/stores/warehouseCurrencyStore";
import type {
    WarehouseDetail,
    CreateWarehouseRequest,
    UpdateWarehouseRequest,
} from "@/types/Warehouse";
import type { CompanyCurrency } from "@/types/CompanyCurrency";
import { CurrencyStatus } from "@/enums/CurrencyStatus";
import type { CreateWarehouseCurrencyRequest } from "@/types/WarehouseCurrency";

const props = defineProps<{ warehouseId?: number }>();

const warehouseStore = useWarehouseStore();
const userStore = useUserStore();
const companyCurrencyStore = useCompanyCurrencyStore();
const warehouseCurrencyStore = useWarehouseCurrencyStore();

const loading = ref(false);
const currencies = ref<CompanyCurrency[]>([]);

// form state
const form = reactive<CreateWarehouseRequest & Partial<UpdateWarehouseRequest>>({
    name: "",
    phone: "",
    email: "",
    addressLine1: "",
    addressLine2: "",
    city: "",
    state: "",
    country: "",
    zipCode: "",
    headquarter: false,
    isDefault: false,
    currencyId: 0,
    timezone: Intl.DateTimeFormat().resolvedOptions().timeZone,
    active: true,
    applyTax: false,
    applyTds: false,
    trackInventory: false,
    invoicePrefix: "",
    companyId: userStore.currentUser?.companyId ?? 0,
    createdBy: userStore.currentUser?.id ?? 0,
    updatedBy: userStore.currentUser?.id ?? 0,
});

const resetForm = () => {
    Object.assign(form, {
        name: "",
        phone: "",
        email: "",
        addressLine1: "",
        addressLine2: "",
        city: "",
        state: "",
        country: "",
        zipCode: "",
        headquarter: false,
        isDefault: false,
        currencyId: currencies.value.find(c => c.defaultCurrency)?.currencyId ?? 0,
        timezone: Intl.DateTimeFormat().resolvedOptions().timeZone,
        active: true,
        applyTax: false,
        applyTds: false,
        trackInventory: false,
        invoicePrefix: "",
        companyId: userStore.currentUser?.companyId ?? 0,
        createdBy: userStore.currentUser?.id ?? 0,
        updatedBy: userStore.currentUser?.id ?? 0,
    });
};

// Load company currencies
const loadCompanyCurrencies = async () => {
    loading.value = true;
    try {
        await companyCurrencyStore.fetchAll(form.companyId);
        currencies.value = companyCurrencyStore.list;

        // set default currency for new warehouse
        const defaultCurrency = currencies.value.find(c => c.defaultCurrency);
        if (defaultCurrency) form.currencyId = defaultCurrency.currencyId;
    } catch (err) {
        console.error("Failed to load company currencies:", err);
    } finally {
        loading.value = false;
    }
};

// Load warehouse details (for edit)
const loadWarehouseDetails = async (id: number) => {
    loading.value = true;
    try {
        const warehouse: WarehouseDetail = await warehouseStore.fetchWarehouseDetail(id);
        Object.assign(form, { ...warehouse, updatedBy: userStore.currentUser?.id });

        // fetch company currencies for selection
        await companyCurrencyStore.fetchAll(form.companyId);
        currencies.value = companyCurrencyStore.list;

        // pick warehouse-specific currency if exists
        await warehouseCurrencyStore.fetchAll(form.companyId, id);
        const warehouseCurrency = warehouseCurrencyStore.list.find(wc => wc.warehouseId === id);
        if (warehouseCurrency) form.currencyId = warehouseCurrency.currencyId;
    } catch (err) {
        console.error("Failed to load warehouse details:", err);
    } finally {
        loading.value = false;
    }
};

// Watch for warehouseId to decide between edit and create
watch(
    () => props.warehouseId,
    async (id) => {
        if (id) {
            await loadWarehouseDetails(id);
        } else {
            await loadCompanyCurrencies();
        }
    },
    { immediate: true }
);

// Handle form submit
const handleSubmit = async () => {
    loading.value = true;
    try {
        if (props.warehouseId) {
            // Update existing warehouse
            await warehouseStore.updateWarehouse(props.warehouseId, form as UpdateWarehouseRequest);
            alert("Warehouse updated successfully!");
        } else {
            // Create new warehouse
            const created = await warehouseStore.addWarehouse(form as CreateWarehouseRequest);
            alert("Warehouse created successfully!");

            // create warehouse currency based on selected company currency
            const selectedCurrency = currencies.value.find(c => c.currencyId === form.currencyId);
            if (selectedCurrency) {
                const payload: CreateWarehouseCurrencyRequest = {
                    currencyId: selectedCurrency.currencyId,
                    decimalPlaces: selectedCurrency.decimalPlaces ?? 2,
                    defaultCurrency: true,
                    status: CurrencyStatus.ACTIVE,
                };
                await warehouseCurrencyStore.create(form.companyId, created.id, payload);
                console.log(`Default currency ${selectedCurrency.currencyCode} applied to warehouse ${created.id}`);
            }

            resetForm();
        }
    } catch (err: any) {
        console.error(err);
        alert(err.message ?? "Failed to save warehouse");
    } finally {
        loading.value = false;
    }
};
</script>
