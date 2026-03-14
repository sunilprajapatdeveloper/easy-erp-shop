<template>
    <div class="email-verification-step">
        <!-- Email Input Step -->
        <div v-if="!verificationSent" class="email-input-step">
            <div class="email-card">
                <div class="email-form">
                    <div class="form-group">
                        <input type="email" id="email" v-model="email" placeholder="name@company.com" required
                            :class="{ 'error-input': errors.email }" @keyup.enter="sendVerification"
                            @input="clearError" />
                        <div v-if="errors.email" class="error-message">
                            <i class="ri-error-warning-line"></i> {{ errors.email }}
                        </div>
                        <div class="input-hint">
                            We'll send a verification code to this address
                        </div>
                    </div>

                    <button class="btn btn-primary btn-large" @click="sendVerification"
                        :disabled="isSending || !email.trim()">
                        <span v-if="isSending" class="btn-loading">
                            <i class="ri-loader-4-line spin"></i> Sending...
                        </span>
                        <span v-else>Continue with Email</span>
                    </button>
                </div>

                <div class="alternative-options">
                    <div class="divider">
                        <span>or continue with</span>
                    </div>
                    <div class="social-buttons">
                        <button class="btn btn-outline social-btn" @click="signupWithGoogle">
                            <i class="ri-google-fill"></i> Google
                        </button>
                        <button class="btn btn-outline social-btn" @click="signupWithMicrosoft">
                            <i class="ri-microsoft-fill"></i> Microsoft
                        </button>
                    </div>
                </div>

                <div class="login-link">
                    <p>Already have an account? <router-link to="/login">Sign in</router-link></p>
                </div>
            </div>
        </div>

        <!-- Verification Code Step -->
        <div v-else-if="!verificationComplete" class="verification-step">
            <div class="step-header">
                <p class="subtitle">
                    We sent a 6-digit code to <strong>{{ email }}</strong>
                </p>
            </div>

            <div class="verification-card">
                <div class="verification-form">
                    <div class="code-input-container">
                        <div class="code-inputs">
                            <input v-for="i in 6" :key="i" type="text" maxlength="1" v-model="code[i - 1]"
                                :ref="el => inputRefs[i - 1] = el as HTMLInputElement"
                                @input="e => handleCodeInput(e, i - 1)"
                                @keydown.delete="e => handleCodeDelete(e, i - 1)" @paste="handlePaste"
                                :class="{ filled: code[i - 1] }" />
                        </div>
                        <div v-if="errors.code" class="error-message">
                            <i class="ri-error-warning-line"></i> {{ errors.code }}
                        </div>
                    </div>

                    <div class="timer-section">
                        <p v-if="timeLeft > 0" class="timer">
                            Code expires in <span class="timer-countdown">{{ formatTime(timeLeft) }}</span>
                        </p>
                        <p v-else class="timer expired">Code has expired</p>
                    </div>

                    <div class="verification-actions">
                        <button class="btn btn-primary btn-large" @click="verifyCode"
                            :disabled="isVerifying || !isCodeComplete">
                            <span v-if="isVerifying" class="btn-loading">
                                <i class="ri-loader-4-line spin"></i> Verifying...
                            </span>
                            <span v-else>Verify Email</span>
                        </button>

                        <button class="btn btn-text" @click="resendCode" :disabled="isResending || timeLeft > 0">
                            <span v-if="isResending" class="btn-loading">
                                <i class="ri-loader-4-line spin"></i>
                            </span>
                            <span v-else><i class="ri-refresh-line"></i> Resend Code</span>
                        </button>
                    </div>

                    <div class="email-tips">
                        <h4><i class="ri-lightbulb-line"></i> Didn't receive the email?</h4>
                        <ul>
                            <li>Check your spam or junk folder</li>
                            <li>Make sure you entered the correct email</li>
                            <li>Wait a few minutes and try again</li>
                        </ul>
                    </div>

                    <button class="btn btn-text" @click="changeEmail">
                        <i class="ri-arrow-left-line"></i> Change Email Address
                    </button>
                </div>
            </div>
        </div>

        <!-- Success Step -->
        <div v-else class="success-step">
            <div class="step-header">
                <h1>
                    Email Verified!
                    <div class="success-icon">
                        <i class="ri-checkbox-circle-line"></i>
                    </div>
                </h1>
                <p class="subtitle">Your email has been successfully verified</p>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed, onMounted, onUnmounted } from 'vue'
import { useOnboardingStore } from '@/stores/onboardingStore'
import { useVerificationStore } from '@/stores/verificationStore'
import { VerificationType } from '@/enums/VerificationType'

export default defineComponent({
    name: 'EmailVerification',
    emits: ['validated'],
    setup(_, { emit }) {
        const onboardingStore = useOnboardingStore()
        const verificationStore = useVerificationStore()

        // State
        const email = ref('')
        const acceptTerms = ref(true)
        const verificationSent = ref(false)
        const verificationComplete = ref(false)
        const code = ref(['', '', '', '', '', ''])
        const inputRefs = ref<(HTMLInputElement | null)[]>([])

        const expiresAt = ref<string | null>(null)

        // Loading states
        const isSending = ref(false)
        const isVerifying = ref(false)
        const isResending = ref(false)

        // Timer
        const timeLeft = ref(300) // 5 minutes in seconds
        let timerInterval: number | null = null

        // Errors
        const errors = ref<Record<string, string>>({})

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

        // Computed
        const isCodeComplete = computed(() => {
            return code.value.every(digit => digit !== '')
        })

        // Methods
        const validateEmail = (email: string): boolean => {
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
            return emailRegex.test(email)
        }

        const validateWorkEmail = (email: string): boolean => {
            // Additional validation for work emails – can be enabled as needed
            // const personalDomains = ['gmail.com', 'yahoo.com', 'outlook.com', 'hotmail.com', 'icloud.com']
            // const domain = email.split('@')[1]?.toLowerCase() || ''
            // if (personalDomains.includes(domain)) {
            //     errors.value.email = 'Please use your work email address'
            //     return false
            // }
            return true
        }

        const clearError = () => {
            errors.value = {}
        }

        const startTimer = (expiry?: string) => {
            if (expiry) {
                const expiryTime = new Date(expiry).getTime()
                const now = Date.now()
                const diff = Math.floor((expiryTime - now) / 1000)
                timeLeft.value = diff > 0 ? diff : 0
            } else {
                timeLeft.value = 300
            }

            if (timerInterval) {
                clearInterval(timerInterval)
            }

            timerInterval = setInterval(() => {
                if (timeLeft.value > 0) {
                    timeLeft.value--
                } else {
                    if (timerInterval) {
                        clearInterval(timerInterval)
                    }
                }
            }, 1000)
        }

        const formatTime = (seconds: number): string => {
            const minutes = Math.floor(seconds / 60)
            const secs = seconds % 60
            return `${minutes}:${secs.toString().padStart(2, '0')}`
        }

        const sendVerification = async () => {
            errors.value = {}

            if (!email.value.trim()) {
                errors.value.email = 'Email is required'
                return
            }

            if (!validateEmail(email.value)) {
                errors.value.email = 'Please enter a valid email address'
                return
            }

            if (!validateWorkEmail(email.value)) {
                return
            }

            if (!acceptTerms.value) {
                errors.value.acceptTerms = 'You must accept the terms and conditions'
                return
            }

            try {
                isSending.value = true

                const response = await verificationStore.request({
                    email: email.value,
                    verificationType: VerificationType.USER_REGISTRATION,
                })

                onboardingStore.setEmail(email.value)

                expiresAt.value = response.expiresAt
                startTimer(response.expiresAt)

                verificationSent.value = true

                setTimeout(() => {
                    inputRefs.value[0]?.focus()
                }, 100)
            } catch (error: any) {
                console.error('Failed to send verification:', error)
                errors.value.email = getErrorMessage(error)
            } finally {
                isSending.value = false
            }
        }

        const handleCodeInput = (event: Event, index: number) => {
            const target = event.target as HTMLInputElement
            const value = target.value

            if (!/^\d*$/.test(value)) {
                code.value[index] = ''
                return
            }

            if (value) {
                code.value[index] = value

                if (index < 5 && inputRefs.value[index + 1]) {
                    inputRefs.value[index + 1]?.focus()
                }
            }
        }

        const handleCodeDelete = (event: KeyboardEvent, index: number) => {
            if (event.key === 'Backspace' && !code.value[index]) {
                if (index > 0) {
                    code.value[index - 1] = ''
                    inputRefs.value[index - 1]?.focus()
                }
            }
        }

        const handlePaste = (event: ClipboardEvent) => {
            event.preventDefault()
            const pastedData = event.clipboardData?.getData('text') || ''
            const digits = pastedData.replace(/\D/g, '').split('').slice(0, 6)

            digits.forEach((digit, index) => {
                code.value[index] = digit
            })

            const lastFilledIndex = digits.length - 1
            if (lastFilledIndex >= 0 && lastFilledIndex < 6) {
                inputRefs.value[lastFilledIndex]?.focus()
            }
        }

        const verifyCode = async () => {
            if (!isCodeComplete.value) {
                errors.value.code = 'Please enter the complete 6-digit code'
                return
            }

            const verificationCode = code.value.join('')

            try {
                isVerifying.value = true
                errors.value.code = ''

                const result = await verificationStore.validate({
                    email: email.value,
                    token: verificationCode,
                    verificationType: VerificationType.USER_REGISTRATION,
                })

                if (result.success) {
                    onboardingStore.markEmailVerified()
                    verificationComplete.value = true
                    emit('validated', true)
                } else {
                    errors.value.code = result.message || 'Invalid verification code'
                    code.value = ['', '', '', '', '', '']
                    inputRefs.value[0]?.focus()
                }
            } catch (error: any) {
                console.error('Verification failed:', error)
                errors.value.code = getErrorMessage(error)
                code.value = ['', '', '', '', '', '']
                inputRefs.value[0]?.focus()
            } finally {
                isVerifying.value = false
            }
        }

        const resendCode = async () => {
            try {
                isResending.value = true
                errors.value.code = ''

                await verificationStore.resend(email.value, VerificationType.USER_REGISTRATION)

                if (expiresAt.value) {
                    startTimer(expiresAt.value)
                } else {
                    startTimer()
                }

                code.value = ['', '', '', '', '', '']
                inputRefs.value[0]?.focus()
            } catch (error: any) {
                console.error('Failed to resend code:', error)
                errors.value.code = getErrorMessage(error)
            } finally {
                isResending.value = false
            }
        }

        const changeEmail = () => {
            verificationSent.value = false
            verificationComplete.value = false
            code.value = ['', '', '', '', '', '']
            errors.value = {}

            if (timerInterval) {
                clearInterval(timerInterval)
                timerInterval = null
            }
        }

        const signupWithGoogle = () => {
            // Implement Google OAuth
            console.log('Google signup')
        }

        const signupWithMicrosoft = () => {
            // Implement Microsoft OAuth
            console.log('Microsoft signup')
        }

        // Lifecycle
        onMounted(() => {
            if (onboardingStore.isEmailVerified && onboardingStore.getEmail) {
                email.value = onboardingStore.getEmail
                verificationComplete.value = true
                emit('validated', true)
            }
        })

        onUnmounted(() => {
            if (timerInterval) {
                clearInterval(timerInterval)
            }
        })

        return {
            email,
            acceptTerms,
            verificationSent,
            verificationComplete,
            code,
            inputRefs,
            isSending,
            isVerifying,
            isResending,
            timeLeft,
            errors,
            isCodeComplete,
            sendVerification,
            handleCodeInput,
            handleCodeDelete,
            handlePaste,
            verifyCode,
            resendCode,
            changeEmail,
            signupWithGoogle,
            signupWithMicrosoft,
            formatTime,
            clearError
        }
    }
})
</script>

<style lang="scss" scoped>
/* --------------------------------------------
   Email Verification – Styled for OnboardingLayout
   Uses global design variables
-------------------------------------------- */
.email-verification-step {
    // No extra padding – layout provides spacing

    .step-header {
        margin-bottom: 2rem;
        text-align: center;

        h1 {
            font-size: 1.875rem;
            font-weight: 700;
            color: var(--color-text, #1e293b);
            margin-bottom: 0.5rem;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 0.75rem;

            .success-icon {
                color: var(--color-success, #10b981);
                font-size: 2rem;
            }
        }

        .subtitle {
            font-size: 1rem;
            color: var(--color-text-light, #475569);
            line-height: 1.5;

            strong {
                color: var(--color-text, #1e293b);
                font-weight: 600;
            }
        }
    }

    .email-card,
    .verification-card {
        background: var(--color-surface, #ffffff);
        border-radius: var(--radius-lg, 0.75rem);
        padding: 1.5rem;
        border: 1px solid var(--color-border, #e2e8f0);
    }

    .form-group {
        margin-bottom: 1.5rem;

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
            font-size: 0.875rem;
            color: var(--color-text-muted, #64748b);
            margin-top: 0.5rem;
        }
    }

    .error-message {
        display: flex;
        align-items: center;
        gap: 0.5rem;
        color: var(--color-danger, #ef4444);
        font-size: 0.875rem;
        margin-top: 0.5rem;

        i {
            font-size: 1rem;
        }
    }

    // Buttons
    .btn {
        &.btn-primary {
            background: linear-gradient(135deg, var(--color-primary, #4f46e5) 0%, var(--color-primary-dark, #3730a3) 100%);
            color: white;
            border: none;
            padding: 0.75rem 1.5rem;
            font-weight: 600;
            transition: var(--transition, all 0.2s ease);

            &:hover:not(:disabled) {
                transform: translateY(-2px);
                box-shadow: 0 10px 20px -5px var(--color-primary, #4f46e5);
            }

            &:disabled {
                opacity: 0.5;
                cursor: not-allowed;
            }

            &.btn-large {
                width: 100%;
                padding: 0.875rem 1.5rem;
                font-size: 1rem;
            }
        }

        &.btn-outline {
            background: transparent;
            border: 1px solid var(--color-border, #e2e8f0);
            color: var(--color-text, #1e293b);
            padding: 0.625rem 1.25rem;

            &:hover {
                background: var(--color-background, #f8fafc);
                border-color: var(--color-primary, #4f46e5);
            }
        }

        &.btn-text {
            background: transparent;
            border: none;
            color: var(--color-primary, #4f46e5);
            padding: 0.5rem;
            font-size: 0.875rem;

            &:hover:not(:disabled) {
                background: rgba(79, 70, 229, 0.05);
            }

            &:disabled {
                opacity: 0.5;
                cursor: not-allowed;
            }
        }
    }

    .btn-loading {
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 0.5rem;

        .spin {
            animation: spin 1s linear infinite;
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

    // Alternative options (Google/Microsoft)
    .alternative-options {
        margin-top: 2rem;
        padding-top: 1.5rem;
        border-top: 1px solid var(--color-border, #e2e8f0);

        .divider {
            text-align: center;
            margin-bottom: 1.5rem;
            position: relative;

            span {
                background: var(--color-surface, #ffffff);
                padding: 0 1rem;
                color: var(--color-text-muted, #64748b);
                font-size: 0.875rem;
                position: relative;
                z-index: 1;
            }

            &::before {
                content: '';
                position: absolute;
                top: 50%;
                left: 0;
                right: 0;
                height: 1px;
                background: var(--color-border, #e2e8f0);
            }
        }

        .social-buttons {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 1rem;

            .social-btn {
                display: flex;
                align-items: center;
                justify-content: center;
                gap: 0.75rem;
                padding: 0.75rem;
                font-weight: 500;
                background: var(--color-surface, #ffffff);

                i {
                    font-size: 1.25rem;
                }
            }
        }
    }

    // Login link
    .login-link {
        text-align: center;
        margin-top: 1.5rem;
        padding-top: 1.5rem;
        border-top: 1px solid var(--color-border, #e2e8f0);

        p {
            color: var(--color-text-muted, #64748b);
            font-size: 0.875rem;

            a {
                color: var(--color-primary, #4f46e5);
                font-weight: 500;
                text-decoration: none;

                &:hover {
                    text-decoration: underline;
                }
            }
        }
    }

    // Code input boxes
    .code-input-container {
        margin-bottom: 2rem;

        .code-inputs {
            display: flex;
            gap: 0.75rem;
            justify-content: center;
            margin-bottom: 1rem;

            input {
                width: 3rem;
                height: 3rem;
                border: 2px solid var(--color-border, #e2e8f0);
                border-radius: var(--radius-md, 0.5rem);
                font-size: 1.5rem;
                font-weight: 600;
                text-align: center;
                background: var(--color-surface, #ffffff);
                transition: var(--transition, all 0.2s ease);

                &:focus {
                    outline: none;
                    border-color: var(--color-primary, #4f46e5);
                    box-shadow: 0 0 0 3px rgba(79, 70, 229, 0.1);
                }

                &.filled {
                    border-color: var(--color-primary, #4f46e5);
                    background-color: rgba(79, 70, 229, 0.05);
                }
            }
        }
    }

    // Timer
    .timer-section {
        text-align: center;
        margin-bottom: 2rem;

        .timer {
            color: var(--color-text-muted, #64748b);
            font-size: 0.875rem;

            .timer-countdown {
                font-weight: 600;
                color: var(--color-primary, #4f46e5);
            }

            &.expired {
                color: var(--color-danger, #ef4444);
                font-weight: 500;
            }
        }
    }

    .verification-actions {
        display: flex;
        flex-direction: column;
        gap: 1rem;
        margin-bottom: 2rem;
    }

    // Email tips box
    .email-tips {
        background: var(--color-background, #f8fafc);
        border-radius: var(--radius-md, 0.5rem);
        padding: 1.25rem;
        margin-bottom: 2rem;

        h4 {
            display: flex;
            align-items: center;
            gap: 0.5rem;
            margin-bottom: 0.75rem;
            color: var(--color-text, #1e293b);
            font-size: 0.875rem;
            font-weight: 600;

            i {
                color: var(--color-warning, #f59e0b);
            }
        }

        ul {
            list-style: none;
            padding: 0;
            margin: 0;

            li {
                display: flex;
                align-items: center;
                gap: 0.5rem;
                margin-bottom: 0.5rem;
                color: var(--color-text-light, #475569);
                font-size: 0.875rem;

                &::before {
                    content: '•';
                    color: var(--color-primary, #4f46e5);
                    font-weight: bold;
                }
            }
        }
    }

    // Responsive adjustments
    @media (max-width: 480px) {
        .code-inputs input {
            width: 2.5rem;
            height: 2.5rem;
            font-size: 1.25rem;
        }

        .social-buttons {
            grid-template-columns: 1fr;
        }

        .step-header h1 {
            font-size: 1.5rem;
            flex-direction: column;
            gap: 0.5rem;
        }
    }
}
</style>