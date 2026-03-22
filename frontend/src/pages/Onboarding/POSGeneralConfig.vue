<template>
    <div class="pos-general-config">
        <!-- General error display -->
        <div v-if="errors.general" class="error-message general-error">
            <i class="ri-error-warning-line"></i> {{ errors.general }}
        </div>

        <!-- Warehouse selection -->
        <div class="form-group">
            <label for="warehouseSelect">Warehouse *</label>
            <div class="select-wrapper">
                <select id="warehouseSelect" v-model="selectedWarehouseId" @change="onWarehouseChange"
                    :disabled="loadingWarehouses" :class="{ 'error-input': errors.warehouse }">
                    <option value="">Select a warehouse</option>
                    <option v-for="wh in warehouses" :key="wh.id" :value="wh.id">{{ wh.name }}</option>
                </select>
                <i class="ri-arrow-down-s-line select-icon"></i>
            </div>
            <div v-if="errors.warehouse" class="error-message">
                <i class="ri-error-warning-line"></i> {{ errors.warehouse }}
            </div>
            <div v-if="loadingWarehouses" class="loading-state">
                <i class="ri-loader-4-line spin"></i> Loading warehouses...
            </div>
        </div>

        <!-- Currency selection (only when warehouse selected) -->
        <div v-if="selectedWarehouseId" class="form-group">
            <label>Default Currency *</label>
            <CurrencySelector v-model="selectedCurrencyCode" :currencies="currencyOptions" :single="true"
                @change="validate" />
            <div v-if="errors.currency" class="error-message">
                <i class="ri-error-warning-line"></i> {{ errors.currency }}
            </div>
            <div v-if="loadingCurrencies" class="loading-state">
                <i class="ri-loader-4-line spin"></i> Loading currencies...
            </div>
        </div>

        <!-- Default customer -->
        <div class="form-group">
            <label for="customerSelect">Default Customer</label>
            <div class="select-wrapper">
                <select id="customerSelect" v-model="selectedCustomerId">
                    <option value="">None</option>
                    <option value="1">Walk-in Customer</option>
                    <option value="2">Regular Customer</option>
                </select>
                <i class="ri-arrow-down-s-line select-icon"></i>
            </div>
        </div>

        <!-- Default payment method -->
        <div class="form-group">
            <label for="paymentSelect">Default Payment Method</label>
            <div class="select-wrapper">
                <select id="paymentSelect" v-model="selectedPaymentMethod">
                    <option value="">None</option>
                    <option value="CASH">Cash</option>
                    <option value="CARD">Card</option>
                    <option value="UPI">UPI</option>
                    <option value="PAYPAL">PayPal</option>
                    <option value="CHEQUE">Cheque</option>
                    <option value="GIFT_CARD">Gift Card</option>
                    <option value="WALLET">Wallet</option>
                    <option value="BANK_TRANSFER">Bank Transfer</option>
                    <option value="VOUCHER">Voucher</option>
                    <option value="MULTIPLE">Multiple</option>
                </select>
                <i class="ri-arrow-down-s-line select-icon"></i>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted, watch, computed } from 'vue'
import { useWarehouseStore } from '@/stores/warehouseStore'
import { useWarehouseCurrencyStore } from '@/stores/warehouseCurrencyStore'
import { usePOSSettingsStore } from '@/stores/posSettingsStore'
import CurrencySelector, { type CurrencyOption } from '@/components/Onboarding/CurrencySelector.vue'

export default defineComponent({
    name: 'POSGeneralConfig',
    components: { CurrencySelector },
    emits: ['validated', 'saved'],
    setup(_, { emit }) {
        const warehouseStore = useWarehouseStore()
        const warehouseCurrencyStore = useWarehouseCurrencyStore()
        const posSettingsStore = usePOSSettingsStore()

        const warehouses = ref<any[]>([])
        const loadingWarehouses = ref(false)
        const selectedWarehouseId = ref<number | null>(null)
        const currencies = ref<any[]>([])
        const loadingCurrencies = ref(false)
        const selectedWarehouseCurrencyId = ref<number | null>(null) // holds warehouse currency ID
        const selectedCustomerId = ref<number | null>(null)
        const selectedPaymentMethod = ref<string>('')
        const errors = ref<Record<string, string>>({})

        // Transform currencies for CurrencySelector
        const currencyOptions = computed<CurrencyOption[]>(() =>
            currencies.value.map(c => ({
                id: c.id,
                code: String(c.id), // Use ID as code for unique value
                name: `${c.currencyCode} - ${c.currencyName}`,
                symbol: c.symbol
            }))
        )

        // Selected currency code (string) for the selector
        const selectedCurrencyCode = computed({
            get: () => selectedWarehouseCurrencyId.value ? String(selectedWarehouseCurrencyId.value) : '',
            set: (val: string) => {
                selectedWarehouseCurrencyId.value = val ? parseInt(val, 10) : null
            }
        })

        const validate = () => {
            const newErrors: Record<string, string> = {}
            if (!selectedWarehouseId.value) newErrors.warehouse = 'Please select a warehouse'
            if (!selectedWarehouseCurrencyId.value) newErrors.currency = 'Please select a default currency'
            errors.value = newErrors
            const isValid = Object.keys(newErrors).length === 0
            emit('validated', isValid)
            return isValid
        }

        const onWarehouseChange = async () => {
            if (!selectedWarehouseId.value) {
                currencies.value = []
                selectedWarehouseCurrencyId.value = null
                return
            }
            loadingCurrencies.value = true
            try {
                await warehouseCurrencyStore.fetchAll(selectedWarehouseId.value)
                currencies.value = warehouseCurrencyStore.list
                if (currencies.value.length === 0) {
                    errors.value.currency = 'No currencies found for this warehouse'
                } else {
                    // Auto-select the first currency if none selected
                    if (!selectedWarehouseCurrencyId.value) {
                        selectedWarehouseCurrencyId.value = currencies.value[0].id
                    }
                }
            } catch (err) {
                errors.value.currency = 'Failed to load currencies'
            } finally {
                loadingCurrencies.value = false
                validate()
            }
        }

        const savePosSettings = async () => {
            if (!validate()) throw new Error('Form validation failed')
            const payload: any = {
                defaultCurrencyId: selectedWarehouseCurrencyId.value!,
            }
            if (selectedPaymentMethod.value) payload.defaultPaymentMethod = selectedPaymentMethod.value
            if (selectedCustomerId.value) payload.defaultCustomerId = selectedCustomerId.value

            await posSettingsStore.createPOSSettings(selectedWarehouseId.value!, payload)
        }

        onMounted(async () => {
            loadingWarehouses.value = true
            try {
                await warehouseStore.fetchWarehouses()
                warehouses.value = warehouseStore.warehouses
                if (warehouses.value.length === 0) {
                    errors.value.general = 'No warehouses found. Please go back and create at least one warehouse.'
                }
            } catch (err) {
                errors.value.general = 'Failed to load warehouses'
            } finally {
                loadingWarehouses.value = false
            }
        })

        watch([selectedWarehouseId, selectedWarehouseCurrencyId, selectedPaymentMethod, selectedCustomerId], validate, { immediate: true })

        return {
            warehouses,
            loadingWarehouses,
            selectedWarehouseId,
            currencies,
            loadingCurrencies,
            currencyOptions,
            selectedCurrencyCode,
            selectedWarehouseCurrencyId,
            selectedCustomerId,
            selectedPaymentMethod,
            errors,
            onWarehouseChange,
            validate,
            savePosSettings,
        }
    }
})
</script>

<style lang="scss" scoped>
.pos-general-config {
    // No outer container – fills the form panel naturally

    .general-error {
        background: rgba(239, 68, 68, 0.1);
        border: 1px solid var(--color-danger, #ef4444);
        color: var(--color-danger, #ef4444);
        padding: 0.75rem 1rem;
        border-radius: var(--radius-md, 0.5rem);
        margin-bottom: 1.5rem;
        font-size: 0.875rem;
        text-align: center;
    }

    .form-group {
        margin-bottom: 1.5rem;

        label {
            display: block;
            margin-bottom: 0.5rem;
            font-weight: 500;
            color: var(--color-text, #1e293b);
            font-size: 0.9375rem;
        }

        .select-wrapper {
            position: relative;

            select {
                width: 100%;
                padding: 0.75rem 2.5rem 0.75rem 1rem;
                border: 1px solid var(--color-border, #e2e8f0);
                border-radius: var(--radius-md, 0.5rem);
                font-size: 1rem;
                transition: var(--transition, all 0.2s ease);
                background: var(--color-surface, #ffffff);
                appearance: none;
                cursor: pointer;

                &:focus {
                    outline: none;
                    border-color: var(--color-primary, #4f46e5);
                    box-shadow: 0 0 0 3px rgba(79, 70, 229, 0.1);
                }

                &.error-input {
                    border-color: var(--color-danger, #ef4444);
                    background-color: rgba(239, 68, 68, 0.02);
                }

                &:disabled {
                    background-color: var(--color-background, #f8fafc);
                    cursor: not-allowed;
                    opacity: 0.8;
                }
            }

            .select-icon {
                position: absolute;
                right: 1rem;
                top: 50%;
                transform: translateY(-50%);
                pointer-events: none;
                color: var(--color-text-muted, #64748b);
                font-size: 1.25rem;
            }
        }

        .loading-state {
            margin-top: 0.5rem;
            font-size: 0.875rem;
            color: var(--color-text-muted, #64748b);
            display: flex;
            align-items: center;
            gap: 0.5rem;

            .spin {
                animation: spin 1s linear infinite;
            }
        }

        .error-message {
            display: flex;
            align-items: center;
            gap: 0.375rem;
            color: var(--color-danger, #ef4444);
            font-size: 0.875rem;
            margin-top: 0.375rem;

            i {
                font-size: 0.875rem;
            }
        }
    }
}

@keyframes spin {
    from {
        transform: rotate(0deg);
    }

    to {
        transform: rotate(360deg);
    }
}
</style>