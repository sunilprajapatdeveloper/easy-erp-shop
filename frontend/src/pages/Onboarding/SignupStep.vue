<template>
    <div class="signup-step">
        <div v-if="errors.general" class="error-message general-error">{{ errors.general }}</div>

        <div class="form-row">
            <div class="form-group">
                <label for="firstName">First Name *</label>
                <input type="text" id="firstName" v-model="form.firstName" placeholder="John" @input="validateForm"
                    :class="{ 'error-input': errors.firstName }" />
                <div v-if="errors.firstName" class="error-message">{{ errors.firstName }}</div>
            </div>

            <div class="form-group">
                <label for="lastName">Last Name *</label>
                <input type="text" id="lastName" v-model="form.lastName" placeholder="Doe" @input="validateForm"
                    :class="{ 'error-input': errors.lastName }" />
                <div v-if="errors.lastName" class="error-message">{{ errors.lastName }}</div>
            </div>
        </div>

        <div class="form-group">
            <label for="email">Work Email</label>
            <input type="email" id="email" :value="verifiedEmail" disabled class="disabled-input" />
            <p class="input-hint">Email verified in previous step</p>
        </div>

        <div class="form-group">
            <label for="phone">Phone Number *</label>
            <div class="phone-input">
                <select v-model="form.countryCode" class="country-code">
                    <option value="+1">+1 (US)</option>
                    <option value="+44">+44 (UK)</option>
                    <option value="+91">+91 (IN)</option>
                    <option value="+971">+971 (UAE)</option>
                </select>
                <input type="tel" id="phone" v-model="form.phone" placeholder="123 456 7890" @input="validateForm"
                    :class="{ 'error-input': errors.phone }" />
            </div>
            <div v-if="errors.phone" class="error-message">{{ errors.phone }}</div>
        </div>

        <div class="form-group terms-group">
            <label class="checkbox-label">
                <input type="checkbox" v-model="form.acceptTerms" />
                <span class="checkmark"></span>
                I agree to the <a href="/terms" target="_blank">Terms of Service</a> and <a href="/privacy"
                    target="_blank">Privacy Policy</a>
            </label>
            <div v-if="errors.acceptTerms" class="error-message">
                {{ errors.acceptTerms }}
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed, watch } from 'vue'
import { useOnboardingStore } from '@/stores/onboardingStore'
import { useUserStore } from '@/stores/userStore'

export default defineComponent({
    name: 'SignupStep',
    emits: ['validated', 'created'],
    setup(_, { emit }) {
        const onboardingStore = useOnboardingStore()
        const userStore = useUserStore()

        const verifiedEmail = computed(() => onboardingStore.getEmail)

        const form = ref({
            firstName: '',
            lastName: '',
            phone: '',
            countryCode: '+1',
            acceptTerms: false
        })

        const errors = ref<Record<string, string>>({})
        const isSubmitting = ref(false)

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

        const validatePhone = (phone: string): boolean => {
            const cleaned = phone.replace(/[^\d+]/g, '');
            return /^\+?\d{7,15}$/.test(cleaned);
        };

        const validateForm = () => {
            const newErrors: Record<string, string> = {}

            if (!form.value.firstName.trim()) {
                newErrors.firstName = 'First name is required'
            }

            if (!form.value.lastName.trim()) {
                newErrors.lastName = 'Last name is required'
            }

            if (!form.value.phone.trim()) {
                newErrors.phone = 'Phone number is required'
            } else if (!validatePhone(form.value.phone)) {
                newErrors.phone = 'Please enter a valid phone number (e.g., +1234567890)'
            }

            if (!form.value.acceptTerms) {
                newErrors.acceptTerms = 'You must accept the terms and conditions'
            }

            errors.value = newErrors

            const isValid = Object.keys(newErrors).length === 0
            emit('validated', isValid)

            return isValid
        }

        const isFormValid = computed(() => {
            return Object.keys(errors.value).length === 0 &&
                form.value.firstName &&
                form.value.lastName &&
                form.value.phone &&
                form.value.acceptTerms
        })

        const saveUser = async (): Promise<void> => {
            if (!validateForm()) {
                throw new Error('Form validation failed')
            }

            const companyId = onboardingStore.companyId
            if (!companyId) {
                errors.value.general = 'Company information missing. Please go back.'
                throw new Error('Company information missing')
            }

            isSubmitting.value = true
            try {
                const fullPhone = form.value.countryCode + form.value.phone
                const user = await userStore.register({
                    firstname: form.value.firstName,
                    lastname: form.value.lastName,
                    email: verifiedEmail.value,
                    phone: fullPhone
                }, companyId)

                onboardingStore.setUserData({
                    name: `${form.value.firstName} ${form.value.lastName}`,
                    email: verifiedEmail.value,
                    phone: fullPhone,
                    password: ''
                })

                emit('created', user.id)
            } catch (error: any) {
                console.error('User creation failed:', error)
                errors.value.general = getErrorMessage(error)
                throw error
            } finally {
                isSubmitting.value = false
            }
        }

        watch(form, validateForm, { deep: true, immediate: true })

        return {
            form,
            verifiedEmail,
            errors,
            isSubmitting,
            isFormValid,
            validateForm,
            saveUser
        }
    }
})
</script>

<style lang="scss" scoped>
.signup-step {
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

    .form-row {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 1rem;
        margin-bottom: 0; // handled by form-group
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

        .disabled-input {
            background-color: var(--color-background, #f8fafc);
            color: var(--color-text-muted, #64748b);
            cursor: not-allowed;
            opacity: 0.8;
        }

        .phone-input {
            display: flex;
            gap: 0.5rem;

            .country-code {
                flex: 0 0 120px;
                cursor: pointer;
            }

            input {
                flex: 1;
            }
        }

        .input-hint {
            margin-top: 0.25rem;
            font-size: 0.875rem;
            color: var(--color-text-muted, #64748b);
        }

        &.terms-group {
            margin-top: 1.5rem;
            padding-top: 1.5rem;
            border-top: 1px solid var(--color-border, #e2e8f0);
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

            a {
                color: var(--color-primary, #4f46e5);
                text-decoration: none;
                font-weight: 500;

                &:hover {
                    text-decoration: underline;
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

@media (max-width: 640px) {
    .signup-step .form-row {
        grid-template-columns: 1fr;
    }
}
</style>