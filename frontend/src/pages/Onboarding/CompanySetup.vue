<template>
    <div class="company-setup-step">
        <div v-if="errors.general" class="error-message general-error">{{ errors.general }}</div>

        <div class="form-group">
            <label for="companyName">Business Name *</label>
            <input type="text" id="companyName" v-model="form.companyName" @input="validateForm"
                :class="{ 'error-input': errors.companyName }" placeholder="e.g., Acme Inc." />
            <div v-if="errors.companyName" class="error-message">{{ errors.companyName }}</div>
        </div>

        <div class="form-group">
            <label for="companyEmail">Business Email *</label>
            <input type="email" id="companyEmail" v-model="form.email" @input="validateForm"
                :class="{ 'error-input': errors.email }" placeholder="contact@acme.com" />
            <div v-if="errors.email" class="error-message">{{ errors.email }}</div>
        </div>

        <div class="form-group">
            <label for="companyPhone">Business Phone *</label>
            <input type="tel" id="companyPhone" v-model="form.phone" @input="validateForm"
                :class="{ 'error-input': errors.phone }" placeholder="+1 (555) 123-4567" />
            <div v-if="errors.phone" class="error-message">{{ errors.phone }}</div>
        </div>

        <div class="form-group">
            <label>Company Currency *</label>
            <CurrencySelector v-model="primaryCurrencyCode" :currencies="currencyOptions" :single="true"
                @change="validateForm" />
            <p class="input-hint">This is your base currency for reporting</p>
            <div v-if="errors.primaryCurrency" class="error-message">{{ errors.primaryCurrency }}</div>
        </div>

        <div class="form-group">
            <label>Additional Currencies (Optional)</label>
            <CurrencySelector v-model="additionalCurrencyCodes" :currencies="currencyOptions" multiple
                @change="validateForm" />
            <p class="input-hint">Add currencies you frequently transact in</p>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, ref, watch, onMounted, onUnmounted, computed } from 'vue'
import { useOnboardingStore } from '@/stores/onboardingStore'
import { useCompanyStore } from '@/stores/companyStore'
import { useCompanyCurrencyStore } from '@/stores/companyCurrencyStore'
import { useCurrencyStore } from '@/stores/currencyStore'
import CurrencySelector, { type CurrencyOption } from '@/components/Onboarding/CurrencySelector.vue'
import { CurrencyStatus } from '@/enums/CurrencyStatus'

export default defineComponent({
    name: 'CompanySetupStep',
    components: { CurrencySelector },
    emits: ['validated', 'created'],
    setup(_, { emit }) {
        const onboardingStore = useOnboardingStore()
        const companyStore = useCompanyStore()
        const companyCurrencyStore = useCompanyCurrencyStore()
        const currencyStore = useCurrencyStore()

        const form = ref({
            companyName: '',
            email: '',
            phone: ''
        })

        const primaryCurrencyCode = ref<string>('')
        const additionalCurrencyCodes = ref<string[]>([])

        const primaryCurrencyObj = ref<{ id: number; code: string } | null>(null)
        const additionalCurrencyObjs = ref<{ id: number; code: string }[]>([])

        const errors = ref<Record<string, string>>({})
        const isSubmitting = ref(false)
        const isValidationActive = ref(false)

        // Helper to extract error message from API response
        const getErrorMessage = (error: any): string => {
            if (error.response?.data) {
                const data = error.response.data
                // Handle { status, error } format
                if (typeof data === 'object') {
                    if (data.error) return data.error
                    if (data.message) return data.message
                }
                // If data is a string, use it
                if (typeof data === 'string') return data
            }
            // Fallback to error.message or generic message
            return error.message || 'An unexpected error occurred'
        }

        const currencyOptions = computed<CurrencyOption[]>(() =>
            currencyStore.currencies.map(c => ({
                id: c.id,
                code: c.code,
                name: c.name,
                symbol: c.symbol
            }))
        )

        const getCurrencyByCode = (code: string) => {
            return currencyStore.currencies.find(c => c.code === code) || null
        }

        // Helper to sync currency objects based on current codes and store currencies
        const syncCurrencyObjects = () => {
            if (primaryCurrencyCode.value) {
                primaryCurrencyObj.value = getCurrencyByCode(primaryCurrencyCode.value) || null
            }
            if (additionalCurrencyCodes.value.length > 0) {
                additionalCurrencyObjs.value = additionalCurrencyCodes.value
                    .map(code => getCurrencyByCode(code))
                    .filter(Boolean) as { id: number; code: string }[]
            } else {
                additionalCurrencyObjs.value = []
            }
        }

        onMounted(async () => {
            if (currencyStore.currencies.length === 0) {
                try {
                    await currencyStore.fetchCurrencies()
                } catch (error: any) {
                    errors.value.general = getErrorMessage(error)
                }
            }
            // Sync after currencies are loaded (or if already loaded)
            syncCurrencyObjects()

            // Activate validation on first user interaction
            const rootElement = document.querySelector('.company-setup-step')
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

        // Watch the currency store to sync whenever currencies change (e.g., after async load)
        watch(() => currencyStore.currencies, () => {
            syncCurrencyObjects()
        }, { deep: true })

        // Watch primary currency code to update the corresponding currency object
        watch(primaryCurrencyCode, (newCode) => {
            primaryCurrencyObj.value = getCurrencyByCode(newCode) || null
        })

        // Watch additional currency codes to update the corresponding currency objects
        watch(additionalCurrencyCodes, (newCodes) => {
            additionalCurrencyObjs.value = newCodes
                .map(code => getCurrencyByCode(code))
                .filter(Boolean) as { id: number; code: string }[]
        })

        const validatePhone = (phone: string): boolean => {
            const cleaned = phone.replace(/[^\d+]/g, '');
            return /^\+?\d{7,15}$/.test(cleaned);
        };

        const validateForm = () => {
            // If validation is not yet active, clear errors and emit false (no error display)
            if (!isValidationActive.value) {
                errors.value = {}
                emit('validated', false)
                return false
            }

            const newErrors: Record<string, string> = {}

            if (!form.value.companyName.trim()) {
                newErrors.companyName = 'Business Name is required'
            }

            if (!form.value.email.trim()) {
                newErrors.email = 'Business Email is required'
            } else {
                const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
                if (!emailRegex.test(form.value.email)) {
                    newErrors.email = 'Please enter a valid email address'
                }
            }

            if (!form.value.phone.trim()) {
                newErrors.phone = 'Business Phone is required'
            } else if (!validatePhone(form.value.phone)) {
                newErrors.phone = 'Please enter a valid phone number (e.g., +1234567890)'
            }

            if (!primaryCurrencyCode.value) {
                newErrors.primaryCurrency = 'Company Currency is required'
            }

            errors.value = newErrors

            const isValid = Object.keys(newErrors).length === 0
            emit('validated', isValid)

            return isValid
        }

        const saveCompany = async (): Promise<void> => {
            // Force validation to be active so errors show on submit
            if (!isValidationActive.value) {
                isValidationActive.value = true
            }

            if (!validateForm()) {
                throw new Error('Form validation failed')
            }

            if (!primaryCurrencyObj.value) {
                errors.value.primaryCurrency = 'Selected currency not found in system'
                throw new Error('Selected currency not found in system')
            }

            isSubmitting.value = true
            try {
                const createdCompany = await companyStore.addCompany({
                    companyName: form.value.companyName,
                    email: form.value.email,
                    phone: form.value.phone
                })

                const companyId = createdCompany.id
                onboardingStore.setCompanyId(companyId)
                if (!createdCompany.onboardingToken) {
                    throw new Error('Secure onboarding context missing')
                }
                onboardingStore.setOnboardingToken(createdCompany.onboardingToken)

                await companyCurrencyStore.create(companyId, {
                    currencyId: primaryCurrencyObj.value.id,
                    defaultCurrency: true,
                    status: CurrencyStatus.ACTIVE
                }, createdCompany.onboardingToken)

                for (const curr of additionalCurrencyObjs.value) {
                    await companyCurrencyStore.create(companyId, {
                        currencyId: curr.id,
                        defaultCurrency: false,
                        status: CurrencyStatus.ACTIVE
                    }, createdCompany.onboardingToken)
                }

                onboardingStore.setCompanyData({
                    legalName: form.value.companyName,
                    tradingName: '',
                    businessType: '',
                    industry: '',
                    email: form.value.email,
                    phone: form.value.phone,
                    taxId: '',
                    registrationNumber: '',
                    address: {
                        street: '',
                        city: '',
                        state: '',
                        postalCode: '',
                        country: ''
                    },
                    timezone: '',
                    primaryCurrency: primaryCurrencyCode.value,
                    additionalCurrencies: additionalCurrencyCodes.value,
                    subdomain: '',
                    logo: null,
                    brandColor: '#667eea',
                    enableMultiWarehouse: false,
                    enableMultiCurrency: additionalCurrencyCodes.value.length > 0,
                    enableInventoryTracking: false,
                    enablePos: false,
                    enableEcommerce: false
                })

                emit('created', companyId)
            } catch (error: any) {
                console.error('Company creation failed:', error)
                errors.value.general = getErrorMessage(error)
                throw error
            } finally {
                isSubmitting.value = false
            }
        }

        // Validate when form fields change
        watch(form, validateForm, { deep: true })
        watch(primaryCurrencyCode, validateForm)

        return {
            form,
            primaryCurrencyCode,
            additionalCurrencyCodes,
            currencyOptions,
            errors,
            isSubmitting,
            validateForm,
            saveCompany
        }
    }
})
</script>

<style lang="scss" scoped>
.company-setup-step {
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

        input {
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
        }

        .input-hint {
            margin-top: 0.25rem;
            font-size: 0.875rem;
            color: var(--color-text-muted, #64748b);
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
</style>
