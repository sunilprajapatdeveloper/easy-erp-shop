<template>
    <div class="company-setup-step">
        <div class="setup-section">
            <h2>Tell us about your business</h2>
            <p class="section-subtitle">This information will be used for invoices and communication.</p>

            <div v-if="errors.general" class="error-message general-error">{{ errors.general }}</div>

            <div class="form-group">
                <label for="companyName">Business Name *</label>
                <input type="text" id="companyName" v-model="form.companyName" @input="validateForm" />
                <div v-if="errors.companyName" class="error-message">{{ errors.companyName }}</div>
            </div>

            <div class="form-group">
                <label for="companyEmail">Business Email *</label>
                <input type="email" id="companyEmail" v-model="form.email" @input="validateForm" />
                <div v-if="errors.email" class="error-message">{{ errors.email }}</div>
            </div>

            <div class="form-group">
                <label for="companyPhone">Business Phone *</label>
                <input type="tel" id="companyPhone" v-model="form.phone" @input="validateForm" />
                <div v-if="errors.phone" class="error-message">{{ errors.phone }}</div>
            </div>

            <div class="form-group">
                <label>Company Currency *</label>
                <CurrencySelector v-model="primaryCurrencyCode" :currencies="currencyOptions" :single="true"
                    @change="validateForm" />
                <p class="field-hint">This is your base currency for reporting</p>
                <div v-if="errors.primaryCurrency" class="error-message">{{ errors.primaryCurrency }}</div>
            </div>

            <div class="form-group">
                <label>Additional Currencies (Optional)</label>
                <CurrencySelector v-model="additionalCurrencyCodes" :currencies="currencyOptions" multiple
                    @change="validateForm" />
                <p class="field-hint">Add currencies you frequently transact in</p>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, ref, watch, onMounted, computed } from 'vue'
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

        onMounted(async () => {
            if (currencyStore.currencies.length === 0) {
                await currencyStore.fetchCurrencies()
            }
        })

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

        const validateForm = () => {
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
            if (!validateForm()) {
                throw new Error('Form validation failed');
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

                await companyCurrencyStore.create(companyId, {
                    currencyId: primaryCurrencyObj.value.id,
                    defaultCurrency: true,
                    status: CurrencyStatus.ACTIVE
                })

                for (const curr of additionalCurrencyObjs.value) {
                    await companyCurrencyStore.create(companyId, {
                        currencyId: curr.id,
                        defaultCurrency: false,
                        status: CurrencyStatus.ACTIVE
                    })
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
                errors.value.general = error.response?.data?.message || error.message || 'Failed to create company. Please try again.'
                throw error;
            } finally {
                isSubmitting.value = false
            }
        }

        // Validate when form fields change
        watch(form, validateForm, { deep: true })

        // Add a watcher to ensure validation runs when primary currency changes
        watch(primaryCurrencyCode, () => {
            validateForm()
        })

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
    max-width: 600px;
    margin: 0 auto;

    .setup-section {
        background: white;
        border-radius: 16px;
        padding: 2rem;
        box-shadow: 0 8px 32px rgba(0, 0, 0, 0.05);
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

    .general-error {
        background: rgba(255, 68, 68, 0.1);
        padding: 1rem;
        border-radius: 8px;
        margin-bottom: 1.5rem;
        text-align: center;
    }

    .form-group {
        margin-bottom: 1.5rem;

        label {
            display: block;
            margin-bottom: 0.5rem;
            font-weight: 500;
            color: var(--titleColor);
        }

        input {
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

        .field-hint {
            margin-top: 0.25rem;
            font-size: 0.875rem;
            color: var(--textColor);
            opacity: 0.7;
        }
    }

    .error-message {
        color: #ff4444;
        font-size: 0.875rem;
        margin-top: 0.25rem;
    }
}

@media (max-width: 768px) {
    .company-setup-step {
        .setup-section {
            padding: 1.5rem;
        }

        h2 {
            font-size: 1.5rem;
        }
    }
}
</style>