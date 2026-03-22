<template>
    <div class="warehouse-setup">
        <!-- General error display -->
        <div v-if="errors.general" class="error-message general-error">
            <i class="ri-error-warning-line"></i> {{ errors.general }}
        </div>

        <!-- Main Warehouse Form -->
        <div class="warehouse-form">
            <div class="form-row">
                <div class="form-group">
                    <label for="warehouseName">Warehouse Name *</label>
                    <input type="text" id="warehouseName" v-model="warehouseData.name"
                        placeholder="e.g., Main Warehouse, Downtown Store" required @input="validateForm"
                        :class="{ 'error-input': errors.name }" />
                    <div v-if="errors.name" class="error-message">
                        <i class="ri-error-warning-line"></i> {{ errors.name }}
                    </div>
                </div>
            </div>

            <div class="form-group">
                <label>Warehouse Address *</label>
                <div class="address-fields">
                    <input type="text" v-model="warehouseData.address.street" placeholder="Street Address" required
                        @input="validateForm"
                        :class="{ 'error-input': errors.address && !warehouseData.address.street }" />
                    <div class="address-row">
                        <input type="text" v-model="warehouseData.address.city" placeholder="City" required
                            @input="validateForm"
                            :class="{ 'error-input': errors.address && !warehouseData.address.city }" />
                        <input type="text" v-model="warehouseData.address.state" placeholder="State/Province" required
                            @input="validateForm"
                            :class="{ 'error-input': errors.address && !warehouseData.address.state }" />
                    </div>
                    <div class="address-row">
                        <input type="text" v-model="warehouseData.address.postalCode" placeholder="Postal Code" required
                            @input="validateForm"
                            :class="{ 'error-input': errors.address && !warehouseData.address.postalCode }" />
                        <select v-model="warehouseData.address.country" required @change="validateForm"
                            :class="{ 'error-input': errors.address && !warehouseData.address.country }">
                            <option value="">Select Country</option>
                            <option value="US">United States</option>
                            <option value="GB">United Kingdom</option>
                            <option value="IN">India</option>
                            <option value="AE">UAE</option>
                            <option value="CA">Canada</option>
                            <option value="AU">Australia</option>
                        </select>
                    </div>
                </div>
                <div v-if="errors.address" class="error-message">
                    <i class="ri-error-warning-line"></i> {{ errors.address }}
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="warehousePhone">Phone Number *</label>
                    <input type="tel" id="warehousePhone" v-model="warehouseData.phone" placeholder="+1 (555) 123-4567"
                        required @input="validateForm" :class="{ 'error-input': errors.phone }" />
                    <div v-if="errors.phone" class="error-message">
                        <i class="ri-error-warning-line"></i> {{ errors.phone }}
                    </div>
                </div>

                <div class="form-group">
                    <label for="warehouseCurrency">Default Currency *</label>
                    <CurrencySelector v-model="warehouseData.currency" :currencies="currencyOptions"
                        @change="validateForm" />
                    <div v-if="errors.currency" class="error-message">
                        <i class="ri-error-warning-line"></i> {{ errors.currency }}
                    </div>
                </div>
            </div>

            <!-- Additional Currencies (Optional) -->
            <div class="form-group">
                <label>Additional Currencies (Optional)</label>
                <CurrencySelector v-model="additionalCurrencyCodes" :currencies="currencyOptions" multiple
                    @change="validateForm" />
                <p class="input-hint">Add currencies for this warehouse (optional)</p>
            </div>

            <div class="form-group">
                <label class="checkbox-label">
                    <input type="checkbox" v-model="warehouseData.isDefault" />
                    <span class="checkmark"></span>
                    Set as default warehouse for all operations
                </label>
            </div>
        </div>

        <!-- Warehouse Preview -->
        <div class="preview-section">
            <h3>Warehouse Preview</h3>
            <div class="preview-card">
                <div class="preview-header">
                    <div class="preview-icon">
                        <i class="ri-store-line"></i>
                    </div>
                    <div class="preview-info">
                        <h4>{{ warehouseData.name || 'Warehouse Name' }}</h4>
                        <p>Code: {{ warehouseData.code || 'WH-001' }}</p>
                    </div>
                    <div v-if="warehouseData.isDefault" class="preview-badge">
                        Default
                    </div>
                </div>

                <div class="preview-details">
                    <div class="detail-item">
                        <i class="ri-map-pin-line"></i>
                        <div>
                            <span class="detail-label">Address</span>
                            <p>{{ getFullAddress() }}</p>
                        </div>
                    </div>

                    <div class="detail-item">
                        <i class="ri-user-line"></i>
                        <div>
                            <span class="detail-label">Manager</span>
                            <p>{{ warehouseData.managerName || 'Manager Name' }}</p>
                        </div>
                    </div>

                    <div class="detail-item">
                        <i class="ri-phone-line"></i>
                        <div>
                            <span class="detail-label">Contact</span>
                            <p>{{ warehouseData.phone || '+1 (555) 123-4567' }}</p>
                        </div>
                    </div>

                    <div class="detail-item">
                        <i class="ri-money-dollar-circle-line"></i>
                        <div>
                            <span class="detail-label">Currency</span>
                            <p>{{ warehouseData.currency || 'USD' }}</p>
                        </div>
                    </div>
                </div>

                <div class="preview-features">
                    <span class="feature-label">Enabled Features:</span>
                    <div class="feature-tags">
                        <span v-if="warehouseData.settings.enableInventoryTracking" class="feature-tag">
                            <i class="ri-check-line"></i> Inventory Tracking
                        </span>
                        <span v-if="warehouseData.settings.enableBarcode" class="feature-tag">
                            <i class="ri-check-line"></i> Barcode Scanning
                        </span>
                        <span v-if="warehouseData.settings.enablePos" class="feature-tag">
                            <i class="ri-check-line"></i> POS System
                        </span>
                        <span v-if="warehouseData.settings.enableReceiving" class="feature-tag">
                            <i class="ri-check-line"></i> GRN Management
                        </span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, ref, watch, onMounted, onUnmounted, computed } from 'vue'
import { useOnboardingStore } from '@/stores/onboardingStore'
import { useCompanyCurrencyStore } from '@/stores/companyCurrencyStore'
import { useWarehouseStore } from '@/stores/warehouseStore'
import { useWarehouseCurrencyStore } from '@/stores/warehouseCurrencyStore'
import CurrencySelector, { type CurrencyOption } from '@/components/Onboarding/CurrencySelector.vue'
import { CurrencyStatus } from '@/enums/CurrencyStatus'
import type { WarehouseData, WarehouseType } from '@/types/onboarding'

interface AdditionalWarehouse {
    name: string
    code: string
}

export default defineComponent({
    name: 'WarehouseSetupStep',
    components: { CurrencySelector },
    emits: ['validated'],
    setup(_, { emit }) {
        const onboardingStore = useOnboardingStore()
        const companyCurrencyStore = useCompanyCurrencyStore()
        const warehouseStore = useWarehouseStore()
        const warehouseCurrencyStore = useWarehouseCurrencyStore()

        const warehouseData = ref<WarehouseData>({
            name: '',
            code: '',
            type: 'retail' as WarehouseType,
            address: {
                street: '',
                city: '',
                state: '',
                postalCode: '',
                country: ''
            },
            managerName: '',
            managerEmail: '',
            phone: '',
            currency: 'USD',
            isDefault: true,
            settings: {
                enableInventoryTracking: true,
                enableBarcode: true,
                lowStockAlert: 10,
                requireApproval: false,
                enablePos: true,
                defaultTaxRate: 0,
                enableReceiving: true,
                enableQualityCheck: false,
                defaultSupplier: ''
            }
        })

        const additionalWarehouses = ref<AdditionalWarehouse[]>([])
        const additionalCurrencyCodes = ref<string[]>([])
        const additionalCurrencyObjs = ref<{ id: number; code: string }[]>([])

        const errors = ref<Record<string, string>>({})
        const loadingCurrencies = ref(false)
        const isValidationActive = ref(false)

        const currencyOptions = computed<CurrencyOption[]>(() =>
            companyCurrencyStore.list.map(c => ({
                id: c.currencyId,
                code: c.currencyCode,
                name: c.currencyName,
                symbol: c.symbol
            }))
        )

        const getCurrencyByCode = (code: string) => {
            return companyCurrencyStore.list.find(c => c.currencyCode === code) || null
        }

        const getCurrencyIdByCode = (code: string): number | undefined => {
            const currency = companyCurrencyStore.list.find(c => c.currencyCode === code)
            return currency?.currencyId
        }

        const getErrorMessage = (error: any): string => {
            if (error.response?.data) {
                const data = error.response.data
                if (typeof data === 'object') {
                    if (data.error) return data.error
                    if (data.message) return data.message
                }
                if (typeof data === 'string') return data
            }
            return error.message || 'An unexpected error occurred'
        }

        const syncAdditionalCurrencies = () => {
            if (additionalCurrencyCodes.value.length > 0) {
                additionalCurrencyObjs.value = additionalCurrencyCodes.value
                    .map(code => {
                        const currency = getCurrencyByCode(code);
                        if (currency) {
                            return { id: currency.currencyId, code: currency.currencyCode };
                        }
                        return null;
                    })
                    .filter((item): item is { id: number; code: string } => item !== null);
            } else {
                additionalCurrencyObjs.value = [];
            }
        }

        onMounted(async () => {
            const companyId = onboardingStore.companyId
            if (!companyId) {
                errors.value.general = 'Company not found. Please go back.'
                return
            }
            loadingCurrencies.value = true
            try {
                await companyCurrencyStore.fetchAll(companyId)
                if (companyCurrencyStore.list.length === 0) {
                    errors.value.general = 'No currencies found for this company.'
                }
            } catch (err: any) {
                errors.value.general = getErrorMessage(err)
            } finally {
                loadingCurrencies.value = false
            }

            // Activate validation on first user interaction
            const rootElement = document.querySelector('.warehouse-setup')
            if (rootElement) {
                const activateValidation = () => {
                    if (!isValidationActive.value) {
                        isValidationActive.value = true
                        validateForm()
                    }
                }
                rootElement.addEventListener('input', activateValidation)
                rootElement.addEventListener('change', activateValidation)
                onUnmounted(() => {
                    rootElement.removeEventListener('input', activateValidation)
                    rootElement.removeEventListener('change', activateValidation)
                })
            }
        })

        watch(() => companyCurrencyStore.list, () => {
            syncAdditionalCurrencies()
        }, { deep: true })

        watch(additionalCurrencyCodes, (newCodes) => {
            syncAdditionalCurrencies()
            validateForm()
        })

        const validatePhone = (phone: string): boolean => {
            const cleaned = phone.replace(/[^\d+]/g, '');
            return /^\+?\d{7,15}$/.test(cleaned);
        };

        const validateForm = () => {
            if (!isValidationActive.value) {
                errors.value = {}
                emit('validated', false)
                return false
            }

            const newErrors: Record<string, string> = {}

            if (!warehouseData.value.name.trim()) {
                newErrors.name = 'Warehouse name is required'
            }

            if (!warehouseData.value.phone.trim()) {
                newErrors.phone = 'Phone number is required'
            } else if (!validatePhone(warehouseData.value.phone)) {
                newErrors.phone = 'Please enter a valid phone number (e.g., +1234567890)'
            }

            if (!warehouseData.value.currency) {
                newErrors.currency = 'Currency is required'
            }

            const address = warehouseData.value.address
            if (!address.street || !address.city || !address.state || !address.postalCode || !address.country) {
                newErrors.address = 'Complete warehouse address is required'
            }

            errors.value = { ...errors.value, ...newErrors, general: errors.value.general }

            const isValid = Object.keys(newErrors).length === 0
            emit('validated', isValid)
            return isValid
        }

        const getFullAddress = () => {
            const address = warehouseData.value.address
            if (!address.street) return 'Address not specified'
            const parts = [
                address.street,
                address.city,
                address.state,
                address.postalCode,
                address.country
            ].filter(part => part.trim())
            return parts.join(', ')
        }

        const addAdditionalWarehouse = () => {
            additionalWarehouses.value.push({
                name: '',
                code: `WH-${(additionalWarehouses.value.length + 2).toString().padStart(3, '0')}`
            })
        }

        const removeAdditionalWarehouse = (index: number) => {
            additionalWarehouses.value.splice(index, 1)
        }

        const saveWarehouses = async (): Promise<void> => {
            if (!isValidationActive.value) {
                isValidationActive.value = true
            }

            if (!validateForm()) return

            const companyId = onboardingStore.companyId
            if (!companyId) throw new Error('Company ID missing')

            const currencyId = getCurrencyIdByCode(warehouseData.value.currency)
            if (!currencyId) {
                errors.value.currency = 'Selected currency not found in company currencies'
                throw new Error('Selected currency not found in company currencies')
            }

            try {
                const created = await warehouseStore.addWarehouse({
                    name: warehouseData.value.name,
                    city: warehouseData.value.address.city,
                    country: warehouseData.value.address.country,
                    zipCode: warehouseData.value.address.postalCode,
                    currencyId: currencyId,
                    isDefault: warehouseData.value.isDefault,
                    phone: warehouseData.value.phone,
                    addressLine1: warehouseData.value.address.street,
                    addressLine2: '',
                    state: warehouseData.value.address.state,
                })

                await warehouseCurrencyStore.create(
                    created.id,
                    {
                        currencyId: currencyId,
                        defaultCurrency: true,
                        status: CurrencyStatus.ACTIVE
                    },
                    companyId
                )

                for (const curr of additionalCurrencyObjs.value) {
                    await warehouseCurrencyStore.create(
                        created.id,
                        {
                            currencyId: curr.id,
                            defaultCurrency: false,
                            status: CurrencyStatus.ACTIVE
                        },
                        companyId
                    )
                }
            } catch (error: any) {
                console.error('Warehouse creation failed:', error)
                errors.value.general = getErrorMessage(error)
                throw error
            }
        }

        watch(warehouseData, validateForm, { deep: true })

        return {
            warehouseData,
            additionalWarehouses,
            additionalCurrencyCodes,
            currencyOptions,
            loadingCurrencies,
            errors,
            validateForm,
            getFullAddress,
            addAdditionalWarehouse,
            removeAdditionalWarehouse,
            saveWarehouses
        }
    }
})
</script>

<style lang="scss" scoped>
.warehouse-setup {
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

    .form-row {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 1.5rem;
        margin-bottom: 1.5rem;
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

        input,
        select {
            width: 100%;
            padding: 0.75rem 1rem;
            border: 1px solid var(--color-border, #e2e8f0);
            border-radius: var(--radius-md, 0.5rem);
            font-size: 1rem;
            transition: var(--transition, all 0.2s ease);
            background: var(--color-surface, #ffffff);

            &:focus {
                outline: none;
                border-color: var(--color-primary, #4f46e5);
                box-shadow: 0 0 0 3px rgba(79, 70, 229, 0.1);
            }

            &.error-input {
                border-color: var(--color-danger, #ef4444);
                background-color: rgba(239, 68, 68, 0.02);

                &:focus {
                    box-shadow: 0 0 0 3px rgba(239, 68, 68, 0.1);
                }
            }

            &::placeholder {
                color: var(--color-text-muted, #94a3b8);
            }
        }

        .address-fields {
            .address-row {
                display: grid;
                grid-template-columns: 1fr 1fr;
                gap: 1rem;
                margin-top: 1rem;
            }
        }

        .checkbox-label {
            display: flex;
            align-items: flex-start;
            gap: 0.75rem;
            cursor: pointer;
            font-size: 0.9375rem;
            color: var(--color-text, #1e293b);

            input {
                display: none;
            }

            .checkmark {
                flex-shrink: 0;
                width: 20px;
                height: 20px;
                border: 2px solid var(--color-border, #cbd5e1);
                border-radius: var(--radius-sm, 0.25rem);
                position: relative;
                margin-top: 2px;
                transition: var(--transition, all 0.2s ease);
                background: var(--color-surface, #ffffff);

                &::after {
                    content: '';
                    position: absolute;
                    display: none;
                    left: 5px;
                    top: 2px;
                    width: 6px;
                    height: 10px;
                    border: solid white;
                    border-width: 0 2px 2px 0;
                    transform: rotate(45deg);
                }
            }

            input:checked+.checkmark {
                background: var(--color-primary, #4f46e5);
                border-color: var(--color-primary, #4f46e5);

                &::after {
                    display: block;
                }
            }
        }
    }

    .preview-section {
        margin-top: 2rem;

        h3 {
            font-size: 1.25rem;
            font-weight: 600;
            color: var(--color-text, #1e293b);
            margin-bottom: 1rem;
        }

        .preview-card {
            background: var(--color-surface, #ffffff);
            border: 1px solid var(--color-border, #e2e8f0);
            border-radius: var(--radius-lg, 0.75rem);
            padding: 1.5rem;
            box-shadow: var(--shadow-sm, 0 1px 3px rgba(0, 0, 0, 0.05));

            .preview-header {
                display: flex;
                align-items: center;
                gap: 1rem;
                margin-bottom: 1.5rem;
                padding-bottom: 1rem;
                border-bottom: 1px solid var(--color-border, #e2e8f0);

                .preview-icon {
                    width: 48px;
                    height: 48px;
                    border-radius: 50%;
                    background: rgba(79, 70, 229, 0.1);
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    font-size: 1.5rem;
                    color: var(--color-primary, #4f46e5);
                }

                .preview-info {
                    flex: 1;

                    h4 {
                        font-size: 1.125rem;
                        font-weight: 600;
                        color: var(--color-text, #1e293b);
                        margin: 0 0 0.25rem;
                    }

                    p {
                        font-size: 0.875rem;
                        color: var(--color-text-muted, #64748b);
                        margin: 0;
                    }
                }

                .preview-badge {
                    background: linear-gradient(135deg, var(--color-primary, #4f46e5) 0%, var(--color-primary-dark, #3730a3) 100%);
                    color: white;
                    padding: 0.25rem 0.75rem;
                    border-radius: 2rem;
                    font-size: 0.75rem;
                    font-weight: 600;
                    letter-spacing: 0.02em;
                }
            }

            .preview-details {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
                gap: 1rem;
                margin-bottom: 1.5rem;

                .detail-item {
                    display: flex;
                    align-items: flex-start;
                    gap: 0.75rem;

                    i {
                        font-size: 1.25rem;
                        color: var(--color-primary, #4f46e5);
                        margin-top: 0.125rem;
                    }

                    div {
                        flex: 1;

                        .detail-label {
                            display: block;
                            font-size: 0.75rem;
                            text-transform: uppercase;
                            letter-spacing: 0.05em;
                            color: var(--color-text-muted, #64748b);
                            margin-bottom: 0.125rem;
                        }

                        p {
                            font-size: 0.875rem;
                            color: var(--color-text, #1e293b);
                            margin: 0;
                            line-height: 1.4;
                        }
                    }
                }
            }

            .preview-features {
                .feature-label {
                    font-size: 0.875rem;
                    font-weight: 600;
                    color: var(--color-text, #1e293b);
                    display: block;
                    margin-bottom: 0.75rem;
                }

                .feature-tags {
                    display: flex;
                    flex-wrap: wrap;
                    gap: 0.5rem;

                    .feature-tag {
                        display: inline-flex;
                        align-items: center;
                        gap: 0.375rem;
                        background: rgba(79, 70, 229, 0.1);
                        color: var(--color-primary, #4f46e5);
                        padding: 0.375rem 0.75rem;
                        border-radius: 2rem;
                        font-size: 0.75rem;
                        font-weight: 500;

                        i {
                            font-size: 0.875rem;
                        }
                    }
                }
            }
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

@media (max-width: 768px) {
    .warehouse-setup {
        .form-row {
            grid-template-columns: 1fr;
        }

        .address-fields .address-row {
            grid-template-columns: 1fr;
        }

        .preview-details {
            grid-template-columns: 1fr;
        }
    }
}
</style>