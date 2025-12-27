<template>
    <div class="card border-0 shadow-none rounded-1 mb-40">
        <div class="card-body p-xl-40">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h6 class="fs-18 text-title fw-semibold">Exchange Rates</h6>
                <div>
                    <button class="btn style-five me-2" @click="openCreate">Add Rate</button>
                    <button class="btn style-six" :disabled="loading" @click="reload">
                        Refresh
                    </button>
                </div>
            </div>

            <div v-if="loading" class="text-center py-4">
                <span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                <span class="ms-2">Loading exchange rates...</span>
            </div>

            <div v-else>
                <div class="table-responsive style-three mb-3">
                    <table class="table text-nowrap align-middle mb-0">
                        <thead>
                            <tr>
                                <th>Base → Target</th>
                                <th>Rate</th>
                                <th>Level</th>
                                <th>Scope</th>
                                <th>Valid From</th>
                                <th>Valid To</th>
                                <th>Manual</th>
                                <th class="text-end">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="r in paged" :key="r.id">
                                <td class="fs-14 fw-semibold">
                                    {{ r.baseCurrency.code }} → {{ r.targetCurrency.code }}
                                    <div class="fs-12 text-optional">{{ r.baseCurrency.name }} → {{
                                        r.targetCurrency.name }}</div>
                                </td>
                                <td class="fs-14">{{ formatNumber(r.rate) }}</td>
                                <td class="fs-14">{{ ExchangeRateLevelLabels[r.level] }}</td>
                                <td class="fs-14">
                                    <div v-if="r.level === 'GLOBAL'">Global</div>
                                    <div v-else-if="r.level === 'COMPANY'">{{ r.company?.companyName ?? ('#' +
                                        (r.company?.id ?? '')) }}</div>
                                    <div v-else-if="r.level === 'WAREHOUSE'">{{ r.warehouse?.name ?? ('#' +
                                        (r.warehouse?.id ?? '')) }}</div>
                                </td>
                                <td class="fs-14">{{ formatDate(r.validFrom) }}</td>
                                <td class="fs-14">{{ r.validTo ? formatDate(r.validTo) : "-" }}</td>
                                <td class="fs-14">
                                    <span v-if="r.isManualOverride" class="badge badge-outline-red">Manual</span>
                                    <span v-else class="badge badge-outline-green">Auto</span>
                                </td>
                                <td class="text-end">
                                    <div class="button-group d-flex align-items-center justify-content-end">
                                        <a title="Edit" href="javascript:void(0)" @click="openEdit(r.id)">
                                            <img src="../../../assets/img/icons/edit.svg" alt="Edit" />
                                        </a>
                                        <a title="Delete" href="javascript:void(0)" class="ms-3"
                                            @click="confirmDelete(r.id)">
                                            <img src="../../../assets/img/icons/close.svg" alt="Delete" />
                                        </a>
                                    </div>
                                </td>
                            </tr>

                            <tr v-if="exchangeRates.length === 0">
                                <td colspan="8" class="text-center text-muted py-3">No exchange rates found.</td>
                            </tr>
                        </tbody>
                    </table>
                </div>

                <!-- Pagination simple -->
                <div class="d-flex justify-content-between align-items-center">
                    <div class="fs-13">Showing {{ paged.length }} of {{ exchangeRates.length }}</div>
                    <div>
                        <button class="btn btn-sm me-1" :disabled="page === 1" @click="page--">&lt;</button>
                        <span class="mx-2">{{ page }}</span>
                        <button class="btn btn-sm" :disabled="page >= pageCount" @click="page++">&gt;</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Create / Edit Modal -->
        <div class="modal fade" id="exchangeRateModal" tabindex="-1" aria-hidden="true" ref="modalRef">
            <div class="modal-dialog modal-lg modal-dialog-centered modal-dialog-scrollable">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">{{ isEditing ? "Edit Exchange Rate" : "Create Exchange Rate" }}</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" @click="closeModal"></button>
                    </div>
                    <div class="modal-body">
                        <form @submit.prevent="submitForm">
                            <div class="row">
                                <!-- Level -->
                                <div class="col-lg-4 mb-3">
                                    <label class="fs-14 text-black mb-2">Level</label>
                                    <select v-model="form.level" class="w-100 bg_ash border-0 rounded-1 fs-14">
                                        <option v-for="(label, key) in ExchangeRateLevelLabels" :key="key" :value="key">
                                            {{ label }}</option>
                                    </select>
                                </div>

                                <!-- Company (when level != GLOBAL) -->
                                <div class="col-lg-4 mb-3">
                                    <label class="fs-14 text-black mb-2">Company</label>
                                    <select v-model="form.companyId" :disabled="form.level === 'GLOBAL'"
                                        class="w-100 bg_ash border-0 rounded-1 fs-14">
                                        <option :value="undefined">-- Select Company --</option>
                                        <option v-for="c in companies" :key="c.id" :value="c.id">{{ c.companyName }}
                                        </option>
                                    </select>
                                </div>

                                <!-- Warehouse (when level === WAREHOUSE) -->
                                <div class="col-lg-4 mb-3">
                                    <label class="fs-14 text-black mb-2">Warehouse</label>
                                    <select v-model="form.warehouseId" :disabled="form.level !== 'WAREHOUSE'"
                                        class="w-100 bg_ash border-0 rounded-1 fs-14">
                                        <option :value="undefined">-- Select Warehouse --</option>
                                        <option v-for="w in warehouses" :key="w.id" :value="w.id">{{ w.name }} ({{
                                            w.city }})</option>
                                    </select>
                                </div>

                                <!-- Base Currency -->
                                <div class="col-lg-4 mb-3">
                                    <label class="fs-14 text-black mb-2">Base Currency</label>
                                    <select v-model="form.baseCurrencyId" class="w-100 bg_ash border-0 rounded-1 fs-14"
                                        required>
                                        <option :value="undefined">-- Select --</option>
                                        <option v-for="c in currencies" :key="c.id" :value="c.id">{{ c.code }} - {{
                                            c.name }}</option>
                                    </select>
                                </div>

                                <!-- Target Currency -->
                                <div class="col-lg-4 mb-3">
                                    <label class="fs-14 text-black mb-2">Target Currency</label>
                                    <select v-model="form.targetCurrencyId"
                                        class="w-100 bg_ash border-0 rounded-1 fs-14" required>
                                        <option :value="undefined">-- Select --</option>
                                        <option v-for="c in currencies" :key="c.id" :value="c.id">{{ c.code }} - {{
                                            c.name }}</option>
                                    </select>
                                </div>

                                <!-- Rate -->
                                <div class="col-lg-4 mb-3">
                                    <label class="fs-14 text-black mb-2">Rate</label>
                                    <input v-model="form.rate" type="number" step="any" min="0"
                                        class="w-100 bg_ash rounded-1 fs-14" required />
                                </div>

                                <!-- Bid / Ask -->
                                <div class="col-lg-4 mb-3">
                                    <label class="fs-14 text-black mb-2">Bid Rate</label>
                                    <input v-model="form.bidRate" type="number" step="any" min="0"
                                        class="w-100 bg_ash rounded-1 fs-14" />
                                </div>
                                <div class="col-lg-4 mb-3">
                                    <label class="fs-14 text-black mb-2">Ask Rate</label>
                                    <input v-model="form.askRate" type="number" step="any" min="0"
                                        class="w-100 bg_ash rounded-1 fs-14" />
                                </div>

                                <!-- Spread -->
                                <div class="col-lg-4 mb-3">
                                    <label class="fs-14 text-black mb-2">Spread %</label>
                                    <input v-model="form.spreadPercentage" type="number" step="any" min="0"
                                        class="w-100 bg_ash rounded-1 fs-14" />
                                </div>

                                <!-- Rate Source -->
                                <div class="col-lg-4 mb-3">
                                    <label class="fs-14 text-black mb-2">Rate Source</label>
                                    <input v-model="form.rateSource" type="text" class="w-100 bg_ash rounded-1 fs-14"
                                        placeholder="ECB / Manual / ProviderName" />
                                </div>

                                <!-- Provider Name / Reference -->
                                <div class="col-lg-4 mb-3">
                                    <label class="fs-14 text-black mb-2">Provider Name</label>
                                    <input v-model="form.providerName" type="text"
                                        class="w-100 bg_ash rounded-1 fs-14" />
                                </div>
                                <div class="col-lg-4 mb-3">
                                    <label class="fs-14 text-black mb-2">Provider Ref.</label>
                                    <input v-model="form.providerReferenceId" type="text"
                                        class="w-100 bg_ash rounded-1 fs-14" />
                                </div>

                                <!-- Manual Override -->
                                <div class="col-lg-4 mb-3 d-flex align-items-center">
                                    <input id="manual" type="checkbox" v-model="form.isManualOverride"
                                        class="form-check-input me-2" />
                                    <label for="manual" class="mb-0">Manual Override</label>
                                </div>

                                <div class="col-lg-8 mb-3">
                                    <label class="fs-14 text-black mb-2">Override Reason</label>
                                    <input v-model="form.overrideReason" type="text"
                                        class="w-100 bg_ash rounded-1 fs-14" :disabled="!form.isManualOverride" />
                                </div>

                                <!-- Valid From / To -->
                                <div class="col-lg-4 mb-3">
                                    <label class="fs-14 text-black mb-2">Valid From</label>
                                    <input v-model="form.validFrom" type="datetime-local"
                                        class="w-100 bg_ash rounded-1 fs-14" required />
                                </div>
                                <div class="col-lg-4 mb-3">
                                    <label class="fs-14 text-black mb-2">Valid To (optional)</label>
                                    <input v-model="form.validTo" type="datetime-local"
                                        class="w-100 bg_ash rounded-1 fs-14" />
                                </div>

                                <!-- Buttons -->
                                <div class="col-12 mt-2 d-flex justify-content-end">
                                    <button type="button" class="btn style-six me-2" @click="closeModal">Cancel</button>
                                    <button type="submit" class="btn style-five" :disabled="saving">
                                        {{ saving ? 'Saving...' : (isEditing ? 'Update' : 'Create') }}
                                    </button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Delete confirmation (simple) -->
        <div class="modal fade" id="deleteConfirmModal" tabindex="-1" aria-hidden="true" ref="deleteModalRef">
            <div class="modal-dialog modal-sm modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-body text-center p-4">
                        <p class="mb-3">Are you sure you want to delete this exchange rate?</p>
                        <div class="d-flex justify-content-center">
                            <button class="btn style-six me-2" @click="closeDelete">Cancel</button>
                            <button class="btn style-seven" @click="doDelete" :disabled="deleting">{{ deleting ?
                                'Deleting...' : 'Delete' }}</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</template>

<script setup lang="ts">
/**
 * ExchangeRates.vue
 * Full management UI for Exchange Rates (list, create, edit, delete)
 */

import { ref, reactive, computed, onMounted } from "vue";
import { useExchangeRateStore } from "@/stores/exchangeRateStore";
import { useCurrencyStore } from "@/stores/currencyStore";
import { useCompanyStore } from "@/stores/companyStore";
import { useWarehouseStore } from "@/stores/warehouseStore";
import { useUserStore } from "@/stores/userStore";
import { ExchangeRateLevel, ExchangeRateLevelLabels } from "@/types/ExchangeRate";
import type { CreateExchangeRateRequest, UpdateExchangeRateRequest } from "@/types/ExchangeRate";
import type { ExchangeRateListItem } from "@/types/ExchangeRate";
import type { Currency } from "@/types/Currency";
import type { WarehouseListItem } from "@/types/Warehouse";
import type { CompanyDetail } from "@/types/Company";

// stores
const exchangeRateStore = useExchangeRateStore();
const currencyStore = useCurrencyStore();
const companyStore = useCompanyStore();
const warehouseStore = useWarehouseStore();
const userStore = useUserStore();

const loading = ref(false);
const saving = ref(false);
const deleting = ref(false);

// data lists
const exchangeRates = ref<ExchangeRateListItem[]>([]);
const currencies = ref<Currency[]>([]);
const companies = ref<CompanyDetail[]>([]);
const warehouses = ref<WarehouseListItem[]>([]);

// paging
const page = ref(1);
const pageSize = ref(10);
const pageCount = computed(() => Math.ceil(exchangeRates.value.length / pageSize.value));
const paged = computed(() => {
    const start = (page.value - 1) * pageSize.value;
    return exchangeRates.value.slice(start, start + pageSize.value);
});

// modal control
const modalRef = ref<HTMLElement | null>(null);
const deleteModalRef = ref<HTMLElement | null>(null);
const isEditing = ref(false);
const currentEditId = ref<number | null>(null);

// form model
const emptyForm = (): Partial<CreateExchangeRateRequest & { companyId?: number; warehouseId?: number }> => ({
    baseCurrencyId: undefined,
    targetCurrencyId: undefined,
    rate: "",
    bidRate: undefined,
    askRate: undefined,
    level: ExchangeRateLevel.GLOBAL,
    companyId: userStore.currentUser?.companyId ?? undefined,
    warehouseId: undefined,
    rateSource: "MANUAL",
    providerName: undefined,
    providerReferenceId: undefined,
    spreadPercentage: undefined,
    isManualOverride: false,
    overrideReason: undefined,
    validFrom: new Date().toISOString().slice(0, 16), // datetime-local format
    validTo: undefined,
});

const form = reactive<any>(emptyForm());

const formatNumber = (v: any) => {
    try {
        const n = typeof v === "string" ? parseFloat(v) : v;
        if (isNaN(n)) return "-";
        return n.toLocaleString(undefined, { maximumFractionDigits: 8 });
    } catch {
        return String(v);
    }
};

const formatDate = (iso?: string) => {
    if (!iso) return "-";
    const d = new Date(iso);
    if (isNaN(d.getTime())) return iso;
    return d.toLocaleString();
};

// load supporting data + list
const loadAll = async () => {
    loading.value = true;
    try {
        // load currencies (seeded)
        await currencyStore.fetchCurrencies();
        currencies.value = currencyStore.currencies;

        // load companies & warehouses (for selection)
        // if companyStore does not have listing endpoint, we can derive from current user/company only
        // Attempt to load company detail if companyId exists
        const companyId = userStore.currentUser?.companyId;
        if (companyId) {
            const c = await companyStore.fetchCompanyDetail(companyId);
            if (c) companies.value = [c];
        } else {
            // fallback: use store's companies list if available
            companies.value = companyStore.companies ?? [];
        }

        // warehouses
        await warehouseStore.fetchWarehouses();
        warehouses.value = warehouseStore.warehouses;

        // exchange rates
        await exchangeRateStore.fetchAll();
        exchangeRates.value = exchangeRateStore.exchangeRates;
    } catch (err: any) {
        console.error("Failed to load exchange rate page data:", err);
        // You can surface error to UI via toasts
    } finally {
        loading.value = false;
    }
};

onMounted(() => {
    loadAll();
});

// open create modal
const openCreate = () => {
    isEditing.value = false;
    currentEditId.value = null;
    Object.assign(form, emptyForm());
    // show bootstrap modal
    const modalEl = document.getElementById("exchangeRateModal");
    if (modalEl) {
        const m = new (window as any).bootstrap.Modal(modalEl);
        m.show();
        modalRef.value = modalEl;
    }
};

// open edit modal
const openEdit = async (id: number) => {
    isEditing.value = true;
    currentEditId.value = id;
    saving.value = true;
    try {
        await exchangeRateStore.fetchById(id);
        const current = exchangeRateStore.currentExchangeRate;
        if (!current) throw new Error("Rate not found");
        // map into form (convert ISO to datetime-local)
        Object.assign(form, {
            baseCurrencyId: current.baseCurrency.id,
            targetCurrencyId: current.targetCurrency.id,
            rate: current.rate,
            bidRate: current.bidRate,
            askRate: current.askRate,
            spreadPercentage: current.spreadPercentage,
            level: current.level,
            companyId: current.company?.id,
            warehouseId: current.warehouse?.id,
            isManualOverride: current.isManualOverride,
            overrideReason: current.overrideReason,
            rateSource: current.rateSource,
            providerName: current.providerName,
            providerReferenceId: current.providerReferenceId,
            validFrom: current.validFrom ? current.validFrom.slice(0, 16) : new Date().toISOString().slice(0, 16),
            validTo: current.validTo ? current.validTo.slice(0, 16) : undefined,
        });

        // show modal
        const modalEl = document.getElementById("exchangeRateModal");
        if (modalEl) {
            const m = new (window as any).bootstrap.Modal(modalEl);
            m.show();
            modalRef.value = modalEl;
        }
    } catch (err) {
        console.error("Failed load exchange rate for edit:", err);
    } finally {
        saving.value = false;
    }
};

const closeModal = () => {
    const modalEl = document.getElementById("exchangeRateModal");
    if (modalEl) {
        const m = (window as any).bootstrap.Modal.getInstance(modalEl);
        m?.hide();
    }
};

// basic validation
const validateForm = (): string | null => {
    if (!form.baseCurrencyId) return "Base currency is required";
    if (!form.targetCurrencyId) return "Target currency is required";
    if (!form.rate || Number(form.rate) <= 0) return "Rate must be > 0";
    if (!form.level) return "Level is required";
    if (form.level === ExchangeRateLevel.COMPANY && !form.companyId) return "Company is required for COMPANY level";
    if (form.level === ExchangeRateLevel.WAREHOUSE && (!form.companyId || !form.warehouseId)) return "Company & Warehouse are required for WAREHOUSE level";
    if (!form.validFrom) return "Valid From is required";
    return null;
};

const submitForm = async () => {
    const err = validateForm();
    if (err) {
        alert(err);
        return;
    }

    saving.value = true;
    try {
        // Prepare payload shape according to API
        const payload: any = {
            baseCurrencyId: Number(form.baseCurrencyId),
            targetCurrencyId: Number(form.targetCurrencyId),
            rate: String(form.rate),
            bidRate: form.bidRate ? String(form.bidRate) : undefined,
            askRate: form.askRate ? String(form.askRate) : undefined,
            spreadPercentage: form.spreadPercentage ? String(form.spreadPercentage) : undefined,
            level: form.level,
            companyId: form.companyId,
            warehouseId: form.warehouseId,
            rateSource: form.rateSource,
            providerName: form.providerName,
            providerReferenceId: form.providerReferenceId,
            isManualOverride: !!form.isManualOverride,
            overrideReason: form.overrideReason,
            validFrom: new Date(form.validFrom).toISOString(),
            validTo: form.validTo ? new Date(form.validTo).toISOString() : undefined,
        };

        if (isEditing.value && currentEditId.value) {
            await exchangeRateStore.update(currentEditId.value, payload as UpdateExchangeRateRequest);
            // update local list smartly: refresh list item
            await exchangeRateStore.fetchAll();
            exchangeRates.value = exchangeRateStore.exchangeRates;
        } else {
            await exchangeRateStore.create(payload as CreateExchangeRateRequest);
            // push to local
            await exchangeRateStore.fetchAll();
            exchangeRates.value = exchangeRateStore.exchangeRates;
        }

        closeModal();
    } catch (e: any) {
        console.error("Save failed:", e);
        alert(e?.message ?? "Save failed");
    } finally {
        saving.value = false;
    }
};

// delete flow
const deleteTarget = ref<number | null>(null);
const confirmDelete = (id: number) => {
    deleteTarget.value = id;
    const modalEl = document.getElementById("deleteConfirmModal");
    if (modalEl) {
        const m = new (window as any).bootstrap.Modal(modalEl);
        m.show();
        deleteModalRef.value = modalEl;
    }
};
const closeDelete = () => {
    const modalEl = document.getElementById("deleteConfirmModal");
    if (modalEl) {
        const m = (window as any).bootstrap.Modal.getInstance(modalEl);
        m?.hide();
    }
    deleteTarget.value = null;
};
const doDelete = async () => {
    if (!deleteTarget.value) return;
    deleting.value = true;
    try {
        await exchangeRateStore.delete(deleteTarget.value);
        // refresh list
        await exchangeRateStore.fetchAll();
        exchangeRates.value = exchangeRateStore.exchangeRates;
        closeDelete();
    } catch (err: any) {
        console.error("Delete failed:", err);
        alert(err?.message ?? "Delete failed");
    } finally {
        deleting.value = false;
    }
};

const reload = async () => {
    await loadAll();
};

</script>

<style scoped>
/* light styles to match your system; tweak to match exact design tokens */
.badge-outline-red {
    border: 1px solid #ff6b6b;
    color: #c92a2a;
    padding: 4px 8px;
    border-radius: 12px;
}

.badge-outline-green {
    border: 1px solid #2ecc71;
    color: #118a3d;
    padding: 4px 8px;
    border-radius: 12px;
}
</style>
