<template>
    <div class="signup-step">
        <div class="setup-section">
            <h2>Complete your account</h2>
            <p class="section-subtitle">We already verified your email, now tell us a bit about yourself.</p>

            <div v-if="errors.general" class="error-message general-error">{{ errors.general }}</div>

            <div class="form-row">
                <div class="form-group">
                    <label for="firstName">First Name *</label>
                    <input type="text" id="firstName" v-model="form.firstName" placeholder="John"
                        @input="validateForm" />
                    <div v-if="errors.firstName" class="error-message">{{ errors.firstName }}</div>
                </div>

                <div class="form-group">
                    <label for="lastName">Last Name *</label>
                    <input type="text" id="lastName" v-model="form.lastName" placeholder="Doe" @input="validateForm" />
                    <div v-if="errors.lastName" class="error-message">{{ errors.lastName }}</div>
                </div>
            </div>

            <div class="form-group">
                <label for="email">Work Email</label>
                <input type="email" id="email" :value="verifiedEmail" disabled class="disabled-input" />
                <p class="field-hint">Email verified in previous step</p>
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
                    <input type="tel" id="phone" v-model="form.phone" placeholder="123 456 7890"
                        @input="validateForm" />
                </div>
                <div v-if="errors.phone" class="error-message">{{ errors.phone }}</div>
            </div>

            <div class="form-group terms-group">
                <label class="checkbox-label">
                    <input type="checkbox" v-model="form.acceptTerms" required>
                    <span class="checkmark"></span>
                    I agree to the <a href="/terms" target="_blank">Terms of Service</a> and <a href="/privacy"
                        target="_blank">Privacy Policy</a>
                </label>
                <div v-if="errors.acceptTerms" class="error-message">
                    {{ errors.acceptTerms }}
                </div>
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
                throw new Error('Form validation failed');
            }

            const companyId = onboardingStore.companyId
            if (!companyId) {
                errors.value.general = 'Company information missing. Please go back.'
                throw new Error('Company information missing');
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

                // Store minimal user data for progress
                onboardingStore.setUserData({
                    name: `${form.value.firstName} ${form.value.lastName}`,
                    email: verifiedEmail.value,
                    phone: fullPhone,
                    password: ''
                })

                emit('created', user.id)
            } catch (error: any) {
                console.error('User creation failed:', error)
                errors.value.general = error.response?.data?.message || error.message || 'Failed to create user'
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

    .form-row {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 1rem;
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

            &::placeholder {
                color: rgba(0, 0, 0, 0.4);
            }
        }

        .disabled-input {
            background-color: #f5f5f5;
            cursor: not-allowed;
            opacity: 0.7;
        }

        .phone-input {
            display: flex;
            gap: 0.5rem;

            .country-code {
                flex: 0 0 120px;
                padding: 0.875rem 1rem;
                border: 2px solid rgba(0, 0, 0, 0.1);
                border-radius: 8px;
                background: white;
                cursor: pointer;
            }

            input {
                flex: 1;
            }
        }

        .field-hint {
            margin-top: 0.25rem;
            font-size: 0.875rem;
            color: var(--textColor);
            opacity: 0.7;
        }

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
                width: 20px;
                height: 20px;
                border: 2px solid rgba(0, 0, 0, 0.2);
                border-radius: 4px;
                position: relative;
                margin-top: 2px;

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

            a {
                color: var(--primaryColor);
                text-decoration: none;

                &:hover {
                    text-decoration: underline;
                }
            }
        }

        &.terms-group {
            margin-top: 2rem;
            padding-top: 1rem;
            border-top: 1px solid rgba(0, 0, 0, 0.06);
        }
    }

    .error-message {
        color: #ff4444;
        font-size: 0.875rem;
        margin-top: 0.25rem;
    }
}

@media (max-width: 768px) {
    .signup-step {
        .form-row {
            grid-template-columns: 1fr;
        }

        .setup-section {
            padding: 1.5rem;
        }

        h2 {
            font-size: 1.5rem;
        }
    }
}
</style>