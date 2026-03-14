<template>
    <div class="warehouse-setup">
        <!-- Main Warehouse Setup -->
        <div class="setup-section">
            <div class="warehouse-form">
                <div class="form-row">
                    <div class="form-group">
                        <label for="warehouseName">Warehouse Name *</label>
                        <input type="text" id="warehouseName" v-model="warehouseData.name"
                            placeholder="e.g., Main Warehouse, Downtown Store" required @input="validateForm" />
                        <div v-if="errors.name" class="error-message">
                            {{ errors.name }}
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <label>Warehouse Address *</label>
                    <div class="address-fields">
                        <input type="text" v-model="warehouseData.address.street" placeholder="Street Address" required
                            @input="validateForm" />
                        <div class="address-row">
                            <input type="text" v-model="warehouseData.address.city" placeholder="City" required
                                @input="validateForm" />
                            <input type="text" v-model="warehouseData.address.state" placeholder="State/Province"
                                required @input="validateForm" />
                        </div>
                        <div class="address-row">
                            <input type="text" v-model="warehouseData.address.postalCode" placeholder="Postal Code"
                                required @input="validateForm" />
                            <select v-model="warehouseData.address.country" required @change="validateForm">
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
                        {{ errors.address }}
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="warehousePhone">Phone Number *</label>
                        <input type="tel" id="warehousePhone" v-model="warehouseData.phone"
                            placeholder="+1 (555) 123-4567" required @input="validateForm" />
                        <div v-if="errors.phone" class="error-message">
                            {{ errors.phone }}
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="warehouseCurrency">Default Currency *</label>
                        <CurrencySelector v-model="warehouseData.currency" :currencies="currencyOptions"
                            @change="validateForm" />
                        <div v-if="errors.currency" class="error-message">
                            {{ errors.currency }}
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <label class="checkbox-label">
                        <input type="checkbox" v-model="warehouseData.isDefault" />
                        <span class="checkmark"></span>
                        Set as default warehouse for all operations
                    </label>
                </div>
            </div>
        </div>

        <!-- Warehouse Preview -->
        <div class="setup-section">
            <h2>Warehouse Preview</h2>

            <div class="preview-card">
                <div class="preview-header">
                    <div class="preview-icon">
                        <i class="ri-store-line"></i>
                    </div>
                    <div class="preview-info">
                        <h3>{{ warehouseData.name || 'Warehouse Name' }}</h3>
                        <p>Code: {{ warehouseData.code || 'WH-001' }}</p>
                    </div>
                    <div class="preview-badge" v-if="warehouseData.isDefault">
                        Default Warehouse
                    </div>
                </div>

                <div class="preview-details">
                    <div class="detail-item">
                        <i class="ri-map-pin-line"></i>
                        <div>
                            <strong>Address</strong>
                            <p>{{ getFullAddress() }}</p>
                        </div>
                    </div>

                    <div class="detail-item">
                        <i class="ri-user-line"></i>
                        <div>
                            <strong>Manager</strong>
                            <p>{{ warehouseData.managerName || 'Manager Name' }}</p>
                        </div>
                    </div>

                    <div class="detail-item">
                        <i class="ri-phone-line"></i>
                        <div>
                            <strong>Contact</strong>
                            <p>{{ warehouseData.phone || '+1 (555) 123-4567' }}</p>
                        </div>
                    </div>

                    <div class="detail-item">
                        <i class="ri-money-dollar-circle-line"></i>
                        <div>
                            <strong>Currency</strong>
                            <p>{{ warehouseData.currency || 'USD' }}</p>
                        </div>
                    </div>
                </div>

                <div class="preview-features">
                    <h4>Enabled Features:</h4>
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
import { defineComponent, ref, watch, onMounted, computed } from 'vue'
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
    components: {
        CurrencySelector
    },
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
        const errors = ref<Record<string, string>>({})

        // Currencies from the company (fetched on mount)
        const companyCurrencies = ref<any[]>([])
        const loadingCurrencies = ref(false)
        const currencyError = ref('')

        // Transform to CurrencyOption format for the selector
        const currencyOptions = computed<CurrencyOption[]>(() =>
            companyCurrencies.value.map(c => ({
                id: c.currencyId,
                code: c.currencyCode,
                name: c.currencyName,
                symbol: c.symbol
            }))
        )

        // Helper to get currency ID by code
        const getCurrencyIdByCode = (code: string): number | undefined => {
            const currency = companyCurrencies.value.find(c => c.currencyCode === code)
            return currency?.currencyId
        }

        onMounted(async () => {
            const companyId = onboardingStore.companyId
            if (!companyId) {
                currencyError.value = 'Company not found. Please go back.'
                return
            }
            loadingCurrencies.value = true
            try {
                await companyCurrencyStore.fetchAll(companyId)
                companyCurrencies.value = companyCurrencyStore.list
                if (companyCurrencies.value.length === 0) {
                    currencyError.value = 'No currencies found for this company.'
                }
            } catch (err: any) {
                currencyError.value = err.message || 'Failed to load currencies'
            } finally {
                loadingCurrencies.value = false
            }
        })

        const validateForm = () => {
            const newErrors: Record<string, string> = {}

            // Required fields validation
            if (!warehouseData.value.name.trim()) {
                newErrors.name = 'Warehouse name is required'
            }

            if (!warehouseData.value.phone.trim()) {
                newErrors.phone = 'Phone number is required'
            }

            if (!warehouseData.value.currency) {
                newErrors.currency = 'Currency is required'
            }

            // Address validation
            const address = warehouseData.value.address
            if (!address.street || !address.city || !address.state || !address.postalCode || !address.country) {
                newErrors.address = 'Complete warehouse address is required'
            }

            errors.value = newErrors

            // Emit validation status
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

        // Save to store when valid (original behavior)
        watch(() => validateForm(), (isValid) => {
            if (isValid) {
                // Create warehouse data matching the store type
                const mainWarehouse: WarehouseData = {
                    ...warehouseData.value,
                    // Only include required settings plus any optional ones
                    settings: {
                        enableInventoryTracking: warehouseData.value.settings.enableInventoryTracking,
                        enableBarcode: warehouseData.value.settings.enableBarcode,
                        lowStockAlert: warehouseData.value.settings.lowStockAlert,
                        requireApproval: warehouseData.value.settings.requireApproval,
                        // Optional settings
                        enablePos: warehouseData.value.settings.enablePos,
                        defaultTaxRate: warehouseData.value.settings.defaultTaxRate,
                        enableReceiving: warehouseData.value.settings.enableReceiving,
                        enableQualityCheck: warehouseData.value.settings.enableQualityCheck,
                        defaultSupplier: warehouseData.value.settings.defaultSupplier
                    }
                }

                onboardingStore.setWarehouseData(mainWarehouse)
            }
        })

        // Called by parent wizard to actually create warehouse and currency
        const saveWarehouses = async (): Promise<void> => {
            if (!validateForm()) return

            const companyId = onboardingStore.companyId
            if (!companyId) throw new Error('Company ID missing')

            const currencyId = getCurrencyIdByCode(warehouseData.value.currency)
            if (!currencyId) {
                throw new Error('Selected currency not found in company currencies')
            }

            // Create main warehouse
            const created = await warehouseStore.addWarehouse({
                name: warehouseData.value.name,
                city: warehouseData.value.address.city,
                country: warehouseData.value.address.country,
                zipCode: warehouseData.value.address.postalCode,
                currencyId: currencyId,
                isDefault: warehouseData.value.isDefault,
                // additional fields from original data
                phone: warehouseData.value.phone,
                addressLine1: warehouseData.value.address.street,
                addressLine2: '',
                state: warehouseData.value.address.state,
            })

            // Create warehouse-currency association
            await warehouseCurrencyStore.create(
                created.id,
                {
                    currencyId: currencyId,
                    defaultCurrency: true,
                    status: CurrencyStatus.ACTIVE
                },
                companyId
            )
        }

        // Watch for validation changes
        watch(warehouseData, validateForm, { deep: true, immediate: true })

        return {
            warehouseData,
            additionalWarehouses,
            currencyOptions,
            loadingCurrencies,
            currencyError,
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
    .setup-section {
        margin-bottom: 3rem;

        &:last-child {
            margin-bottom: 0;
        }

        h2 {
            font-size: 1.75rem;
            margin-bottom: 0.5rem;
            color: var(--titleColor);
        }

        .section-subtitle {
            color: var(--textColor);
            margin-bottom: 2rem;
            font-size: 1.1rem;
        }
    }

    .warehouse-form {
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
                color: var(--titleColor);
            }

            input,
            select {
                width: 100%;
                padding: 0.875rem 1rem;
                border: 2px solid rgba(0, 0, 0, 0.1);
                border-radius: 8px;
                font-size: 1rem;
                transition: border-color 0.3s ease;

                &:focus {
                    outline: none;
                    border-color: var(--primaryColor);
                    box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
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
                align-items: center;
                gap: 0.75rem;
                cursor: pointer;
                font-size: 1rem;

                input {
                    display: none;
                }

                .checkmark {
                    flex-shrink: 0;
                    width: 20px;
                    height: 20px;
                    border: 2px solid rgba(0, 0, 0, 0.2);
                    border-radius: 4px;
                    position: relative;

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
                    background: var(--primaryColor);
                    border-color: var(--primaryColor);

                    &::after {
                        display: block;
                    }
                }
            }
        }
    }

    .settings-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
        gap: 1.5rem;

        .setting-card {
            background: white;
            border: 2px solid rgba(0, 0, 0, 0.08);
            border-radius: 12px;
            padding: 1.5rem;

            h4 {
                display: flex;
                align-items: center;
                gap: 0.5rem;
                font-size: 1.125rem;
                margin-bottom: 1rem;
                color: var(--titleColor);

                i {
                    color: var(--primaryColor);
                }
            }

            .setting-options {
                display: flex;
                flex-direction: column;
                gap: 1rem;

                .checkbox-label {
                    display: flex;
                    align-items: flex-start;
                    gap: 0.75rem;
                    cursor: pointer;
                    font-size: 0.9375rem;

                    input {
                        display: none;
                    }

                    .checkmark {
                        flex-shrink: 0;
                        width: 18px;
                        height: 18px;
                        border: 2px solid rgba(0, 0, 0, 0.2);
                        border-radius: 4px;
                        position: relative;
                        margin-top: 2px;

                        &::after {
                            content: '';
                            position: absolute;
                            display: none;
                            left: 4px;
                            top: 1px;
                            width: 5px;
                            height: 8px;
                            border: solid white;
                            border-width: 0 2px 2px 0;
                            transform: rotate(45deg);
                        }
                    }

                    input:checked+.checkmark {
                        background: var(--primaryColor);
                        border-color: var(--primaryColor);

                        &::after {
                            display: block;
                        }
                    }
                }

                .setting-input {
                    label {
                        display: block;
                        margin-bottom: 0.5rem;
                        font-weight: 500;
                        color: var(--titleColor);
                        font-size: 0.9375rem;
                    }

                    .input-with-unit {
                        display: flex;
                        align-items: center;

                        input {
                            flex: 1;
                            padding: 0.75rem 1rem;
                            border: 2px solid rgba(0, 0, 0, 0.1);
                            border-radius: 8px 0 0 8px;
                            font-size: 1rem;

                            &:focus {
                                outline: none;
                                border-color: var(--primaryColor);
                            }
                        }

                        .unit {
                            padding: 0.75rem 1rem;
                            background: rgba(0, 0, 0, 0.02);
                            border: 2px solid rgba(0, 0, 0, 0.1);
                            border-left: none;
                            border-radius: 0 8px 8px 0;
                            color: var(--textColor);
                            font-weight: 500;
                        }
                    }

                    select {
                        width: 100%;
                        padding: 0.75rem 1rem;
                        border: 2px solid rgba(0, 0, 0, 0.1);
                        border-radius: 8px;
                        font-size: 1rem;

                        &:focus {
                            outline: none;
                            border-color: var(--primaryColor);
                        }
                    }
                }
            }
        }
    }

    .additional-warehouses {
        .warehouse-card {
            background: white;
            border: 2px solid rgba(0, 0, 0, 0.08);
            border-radius: 12px;
            padding: 1.5rem;
            margin-bottom: 1rem;

            &-header {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 1rem;

                h4 {
                    margin: 0;
                    font-size: 1.125rem;
                    color: var(--titleColor);
                }

                .btn-text {
                    color: #ff4444;

                    &:hover {
                        background: rgba(255, 68, 68, 0.1);
                    }
                }
            }

            &-body {
                display: grid;
                grid-template-columns: 1fr 1fr;
                gap: 1rem;

                .form-input {
                    padding: 0.75rem 1rem;
                    border: 2px solid rgba(0, 0, 0, 0.1);
                    border-radius: 8px;
                    font-size: 1rem;

                    &:focus {
                        outline: none;
                        border-color: var(--primaryColor);
                    }
                }
            }
        }

        .add-warehouse-btn {
            width: 100%;
            padding: 1rem;
            border: 2px dashed rgba(0, 0, 0, 0.1);
            background: transparent;
            color: var(--textColor);
            font-size: 1rem;

            &:hover {
                border-color: var(--primaryColor);
                color: var(--primaryColor);
                background: rgba(102, 126, 234, 0.05);
            }
        }
    }

    .preview-card {
        background: white;
        border-radius: 16px;
        padding: 2rem;
        box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);

        .preview-header {
            display: flex;
            align-items: center;
            gap: 1.5rem;
            margin-bottom: 2rem;
            padding-bottom: 1.5rem;
            border-bottom: 1px solid rgba(0, 0, 0, 0.08);

            .preview-icon {
                width: 60px;
                height: 60px;
                border-radius: 50%;
                background: linear-gradient(135deg, #667eea15 0%, #764ba215 100%);
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 2rem;
                color: var(--primaryColor);
            }

            .preview-info {
                flex: 1;

                h3 {
                    margin: 0 0 0.5rem 0;
                    font-size: 1.5rem;
                    color: var(--titleColor);
                }

                p {
                    margin: 0;
                    color: var(--textColor);
                    opacity: 0.7;
                }
            }

            .preview-badge {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                padding: 0.5rem 1rem;
                border-radius: 20px;
                font-size: 0.875rem;
                font-weight: 600;
            }
        }

        .preview-details {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 1.5rem;
            margin-bottom: 2rem;

            .detail-item {
                display: flex;
                align-items: flex-start;
                gap: 1rem;

                i {
                    font-size: 1.5rem;
                    color: var(--primaryColor);
                    margin-top: 0.25rem;
                }

                div {
                    flex: 1;

                    strong {
                        display: block;
                        font-size: 0.875rem;
                        color: var(--textColor);
                        opacity: 0.7;
                        margin-bottom: 0.25rem;
                    }

                    p {
                        margin: 0;
                        font-size: 1rem;
                        color: var(--titleColor);
                        line-height: 1.4;
                    }
                }
            }
        }

        .preview-features {
            h4 {
                font-size: 1rem;
                margin-bottom: 1rem;
                color: var(--titleColor);
            }

            .feature-tags {
                display: flex;
                flex-wrap: wrap;
                gap: 0.5rem;

                .feature-tag {
                    display: inline-flex;
                    align-items: center;
                    gap: 0.5rem;
                    background: rgba(102, 126, 234, 0.1);
                    color: var(--primaryColor);
                    padding: 0.5rem 1rem;
                    border-radius: 20px;
                    font-size: 0.875rem;
                    font-weight: 500;

                    i {
                        font-size: 1rem;
                    }
                }
            }
        }
    }

    .error-message {
        color: #ff4444;
        font-size: 0.875rem;
        margin-top: 0.25rem;
    }
}

@media (max-width: 768px) {
    .warehouse-setup {

        .warehouse-form .form-row,
        .preview-details {
            grid-template-columns: 1fr;
        }

        .settings-grid {
            grid-template-columns: 1fr;
        }

        .additional-warehouses .warehouse-card-body {
            grid-template-columns: 1fr;
        }
    }
}
</style>