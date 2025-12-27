<template>
    <div class="card border-0 shadow-none rounded-1 mb-40">
        <div class="card-body p-xl-40">
            <h6 class="fs-18 mb-35 text-title fw-semibold">
                {{ levelLabel }} Currency Settings
            </h6>

            <div v-if="loading" class="text-center py-4">Loading...</div>

            <div v-else class="row">
                <!-- Currency dropdown -->
                <div class="col-xxl-3 col-xl-4 col-lg-4 col-md-6">
                    <div class="form-group mb-30">
                        <label class="d-block fs-14 text-black mb-2">Currency</label>
                        <select v-model="form.currencyId" class="bg_ash border-0 rounded-1 fs-14 text-optional w-100">
                            <option v-for="currency in currencies" :key="currency.id" :value="currency.id">
                                {{ currency.code }} - {{ currency.name }}
                            </option>
                        </select>
                    </div>
                </div>

                <!-- Decimal places -->
                <div class="col-xxl-3 col-xl-4 col-lg-4 col-md-6">
                    <div class="form-group mb-30">
                        <label class="d-block fs-14 text-black mb-2">Decimal Places</label>
                        <input type="number" v-model.number="form.decimalPlaces" min="0" max="6"
                            class="w-100 d-block shadow-none fs-14 bg_ash rounded-1 text-title" placeholder="2" />
                    </div>
                </div>

                <!-- Status -->
                <div class="col-xxl-3 col-xl-4 col-lg-4 col-md-6">
                    <div class="form-group mb-30">
                        <label class="d-block fs-14 text-black mb-2">Status</label>
                        <select v-model="form.status" class="bg_ash border-0 rounded-1 fs-14 text-optional w-100">
                            <option v-for="s in statusOptions" :key="s.value" :value="s.value">
                                {{ s.label }}
                            </option>
                        </select>
                    </div>
                </div>

                <!-- Default currency toggle (company only) -->
                <div class="col-xxl-6 col-xl-4 col-lg-4 col-md-6" v-if="level === 'company'">
                    <div class="checkbox style-four mb-40">
                        <input class="form-check-input" type="checkbox" id="defaultCurrency"
                            v-model="form.defaultCurrency" />
                        <label class="form-check-label" for="defaultCurrency">
                            Default Currency
                        </label>
                    </div>
                </div>

                <!-- Save Button -->
                <div class="col-lg-12">
                    <button type="button" class="btn style-five" @click="saveSettings" :disabled="saving">
                        {{ saving ? "Saving..." : "Save Settings" }}
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted, computed, defineProps } from "vue";
import { useUserStore } from "@/stores/userStore";
import { useCurrencyStore } from "@/stores/currencyStore";
import { useCompanyCurrencyStore } from "@/stores/companyCurrencyStore";
import { useWarehouseCurrencyStore } from "@/stores/warehouseCurrencyStore";
import { useWarehouseStore } from "@/stores/warehouseStore";
import type {
    CompanyCurrency,
    CreateCompanyCurrencyRequest,
    UpdateCompanyCurrencyRequest,
} from "@/types/CompanyCurrency";
import type {
    WarehouseCurrency,
    CreateWarehouseCurrencyRequest,
    UpdateWarehouseCurrencyRequest,
} from "@/types/WarehouseCurrency";
import { CurrencyStatus } from "@/enums/CurrencyStatus";
import type { Currency } from "@/types/Currency";

const props = defineProps<{
    level: "company" | "warehouse";
    warehouseId?: number;
}>();

// stores
const userStore = useUserStore();
const currencyStore = useCurrencyStore();
const companyCurrencyStore = useCompanyCurrencyStore();
const warehouseCurrencyStore = useWarehouseCurrencyStore();
const warehouseStore = useWarehouseStore();

const companyId = userStore.currentUser?.companyId;
const loading = ref(true);
const saving = ref(false);

const currencies = ref<Currency[]>([]);
const currentSetting = ref<CompanyCurrency | WarehouseCurrency | null>(null);

// form state
const form = reactive<any>({
    currencyId: undefined,
    decimalPlaces: 2,
    defaultCurrency: props.level === "company",
    status: CurrencyStatus.ACTIVE,
    warehouseId: props.level === "warehouse" ? props.warehouseId : undefined,
});

// status options
const statusOptions = [
    { value: CurrencyStatus.ACTIVE, label: "Active" },
    { value: CurrencyStatus.INACTIVE, label: "Inactive" },
];

const levelLabel = computed(() =>
    props.level === "company" ? "Company" : "Warehouse"
);

onMounted(async () => {
    if (!companyId) return;

    loading.value = true;

    // fetch global currencies
    await currencyStore.fetchCurrencies();
    currencies.value = currencyStore.currencies;

    // fetch warehouses if warehouse-level
    if (props.level === "warehouse") {
        await warehouseStore.fetchWarehouses();
    }

    // fetch settings
    if (props.level === "company") {
        await companyCurrencyStore.fetchAll(companyId);
        currentSetting.value = companyCurrencyStore.list.find(() => true) || null;
    } else if (props.level === "warehouse" && props.warehouseId) {
        await warehouseCurrencyStore.fetchAll(companyId, props.warehouseId);
        currentSetting.value = warehouseCurrencyStore.list.find(
            (cs) => cs.warehouseId === props.warehouseId
        ) || null;
    }

    // map to form
    if (currentSetting.value) {
        form.currencyId = currentSetting.value.currencyId;
        form.decimalPlaces = currentSetting.value.decimalPlaces;
        form.defaultCurrency = currentSetting.value.defaultCurrency;
        form.status = currentSetting.value.status;

        // only set warehouseId if this is a warehouse currency
        if ("warehouseId" in currentSetting.value) {
            form.warehouseId = currentSetting.value.warehouseId ?? undefined;
        }
    }

    loading.value = false;
});

const saveSettings = async () => {
    if (!companyId || !form.currencyId) return;

    saving.value = true;

    try {
        if (props.level === "company") {
            if (currentSetting.value) {
                const payload: UpdateCompanyCurrencyRequest = { ...form };
                await companyCurrencyStore.update(currentSetting.value.id, companyId, payload);
                alert("Company currency updated successfully");
            } else {
                const payload: CreateCompanyCurrencyRequest = { ...form };
                await companyCurrencyStore.create(companyId, payload);
                alert("Company currency created successfully");
            }
        } else if (props.level === "warehouse" && props.warehouseId) {
            if (currentSetting.value) {
                const payload: UpdateWarehouseCurrencyRequest = { ...form };
                await warehouseCurrencyStore.update(
                    currentSetting.value.id,
                    companyId,
                    props.warehouseId,
                    payload
                );
                alert("Warehouse currency updated successfully");
            } else {
                const payload: CreateWarehouseCurrencyRequest = { ...form };
                await warehouseCurrencyStore.create(companyId, props.warehouseId, payload);
                alert("Warehouse currency created successfully");
            }
        }
    } finally {
        saving.value = false;
    }
};
</script>
