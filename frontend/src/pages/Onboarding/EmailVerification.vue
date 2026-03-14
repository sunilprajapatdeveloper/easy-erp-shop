<template>
    <div class="email-verification-step">
        <!-- Email Input Step -->
        <div v-if="!verificationSent" class="email-input-step">
            <div class="email-card">
                <div class="email-form">
                    <div class="form-group">
                        <input type="email" id="email" v-model="email" placeholder="name@company.com" required
                            :class="{ 'error-input': errors.email }" @keyup.enter="sendVerification"
                            @input="clearError">
                        <div v-if="errors.email" class="error-message">
                            <i class="ri-error-warning-line"></i> {{ errors.email }}
                        </div>
                        <div class="email-hint">
                            We'll send a verification code to this address
                        </div>
                    </div>

                    <button class="btn btn-primary btn-large" @click="sendVerification"
                        :disabled="isSending || !email.trim()">
                        <span v-if="isSending" class="btn-loading">
                            <i class="ri-loader-4-line spin"></i> Sending...
                        </span>
                        <span v-else>
                            Continue with Email
                        </span>
                    </button>
                </div>

                <div class="alternative-options">
                    <div class="divider">
                        <span>or continue with</span>
                    </div>
                    <div class="social-buttons">
                        <button class="btn btn-outline social-btn" @click="signupWithGoogle">
                            <i class="ri-google-fill"></i>
                            Google
                        </button>
                        <button class="btn btn-outline social-btn" @click="signupWithMicrosoft">
                            <i class="ri-microsoft-fill"></i>
                            Microsoft
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
                <p class="subtitle">We sent a 6-digit code to <strong>{{ email }}</strong></p>
            </div>

            <div class="verification-card">
                <div class="verification-form">
                    <div class="code-input-container">
                        <div class="code-inputs">
                            <input v-for="i in 6" :key="i" type="text" maxlength="1" v-model="code[i - 1]"
                                :ref="el => inputRefs[i - 1] = el as HTMLInputElement"
                                @input="e => handleCodeInput(e, i - 1)"
                                @keydown.delete="e => handleCodeDelete(e, i - 1)" @paste="handlePaste"
                                :class="{ 'filled': code[i - 1] }" />
                        </div>
                        <div v-if="errors.code" class="error-message">
                            <i class="ri-error-warning-line"></i> {{ errors.code }}
                        </div>
                    </div>

                    <div class="timer-section">
                        <p v-if="timeLeft > 0" class="timer">
                            Code expires in <span class="timer-countdown">{{ formatTime(timeLeft) }}</span>
                        </p>
                        <p v-else class="timer expired">
                            Code has expired
                        </p>
                    </div>

                    <div class="verification-actions">
                        <button class="btn btn-primary btn-large" @click="verifyCode"
                            :disabled="isVerifying || !isCodeComplete">
                            <span v-if="isVerifying" class="btn-loading">
                                <i class="ri-loader-4-line spin"></i> Verifying...
                            </span>
                            <span v-else>
                                Verify Email
                            </span>
                        </button>

                        <button class="btn btn-text" @click="resendCode" :disabled="isResending || timeLeft > 0">
                            <span v-if="isResending" class="btn-loading">
                                <i class="ri-loader-4-line spin"></i>
                            </span>
                            <span v-else>
                                <i class="ri-refresh-line"></i> Resend Code
                            </span>
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
                errors.value.email = error.response?.data?.message || error.message || 'Failed to send verification code'
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
                errors.value.code = error.response?.data?.message || error.message || 'Verification failed'
                code.value = ['', '', '', '', '', '']
                inputRefs.value[0]?.focus()
            } finally {
                isVerifying.value = false
            }
        }

        const resendCode = async () => {
            try {
                isResending.value = true

                await verificationStore.resend(email.value, VerificationType.USER_REGISTRATION)

                if (expiresAt.value) {
                    startTimer(expiresAt.value)
                } else {
                    startTimer()
                }

                code.value = ['', '', '', '', '', '']
                inputRefs.value[0]?.focus()

                // Optionally show a success toast/message
                alert('Verification code has been resent to your email.')
            } catch (error: any) {
                console.error('Failed to resend code:', error)
                alert('Failed to resend code. Please try again.')
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
.email-verification-step {
    // margin: 0 auto;
    // padding: 2rem 1rem;

    .step-header {
        text-align: center;
        margin-bottom: 2.5rem;

        h1 {
            font-size: 2rem;
            font-weight: 700;
            color: var(--titleColor);
            margin-bottom: 0.5rem;
        }

        .subtitle {
            font-size: 1.125rem;
            color: var(--textColor);
            opacity: 0.8;

            strong {
                color: var(--titleColor);
                font-weight: 600;
            }
        }
    }

    .email-card,
    .verification-card,
    .success-card {
        background: white;
        border-radius: 16px;
        // padding: 2.5rem;
        // box-shadow: 0 10px 40px rgba(0, 0, 0, 0.08);
        // border: 1px solid rgba(0, 0, 0, 0.06);
    }

    .email-icon,
    .verification-icon,
    .success-icon {
        // text-align: center;
        margin-bottom: 1.5rem;

        i {
            font-size: 3.5rem;
            color: var(--primaryColor);
        }
    }

    .email-form,
    .verification-form,
    .success-content {
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
                transition: all 0.3s ease;

                &:focus {
                    outline: none;
                    border-color: var(--primaryColor);
                    box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
                }

                &.error-input {
                    border-color: #ff4444;
                    background-color: rgba(255, 68, 68, 0.02);

                    &:focus {
                        box-shadow: 0 0 0 3px rgba(255, 68, 68, 0.1);
                    }
                }
            }

            .email-hint {
                font-size: 0.875rem;
                color: var(--textColor);
                opacity: 0.7;
                margin-top: 0.5rem;
            }
        }

        .terms-group {
            margin-top: 1.5rem;
            padding-top: 1.5rem;
            border-top: 1px solid rgba(0, 0, 0, 0.06);

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
                    font-weight: 500;

                    &:hover {
                        text-decoration: underline;
                    }
                }
            }
        }
    }

    .btn {
        width: 100%;
        margin-bottom: 1rem;

        &.btn-primary {
            padding: 1rem;
            font-size: 1.125rem;
            font-weight: 600;

            &:disabled {
                opacity: 0.6;
                cursor: not-allowed;
            }
        }

        &.btn-text {
            background: none;
            border: none;
            color: var(--primaryColor);
            padding: 0.5rem;

            &:hover {
                background: rgba(102, 126, 234, 0.05);
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

    .alternative-options {
        margin-top: 2rem;
        padding-top: 2rem;
        border-top: 1px solid rgba(0, 0, 0, 0.06);

        .divider {
            text-align: center;
            margin-bottom: 1.5rem;
            position: relative;

            span {
                background: white;
                padding: 0 1rem;
                color: var(--textColor);
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
                background: rgba(0, 0, 0, 0.1);
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
                padding: 0.875rem;
                border: 2px solid rgba(0, 0, 0, 0.1);
                background: white;
                font-weight: 500;

                i {
                    font-size: 1.25rem;
                }

                &:hover {
                    border-color: var(--primaryColor);
                    background: rgba(102, 126, 234, 0.05);
                }
            }
        }
    }

    .login-link {
        text-align: center;
        margin-top: 2rem;
        padding-top: 1.5rem;
        border-top: 1px solid rgba(0, 0, 0, 0.06);

        p {
            color: var(--textColor);
            font-size: 0.9375rem;

            a {
                color: var(--primaryColor);
                text-decoration: none;
                font-weight: 500;

                &:hover {
                    text-decoration: underline;
                }
            }
        }
    }

    .code-input-container {
        margin-bottom: 2rem;

        .code-inputs {
            display: flex;
            gap: 0.75rem;
            justify-content: center;
            margin-bottom: 1rem;

            input {
                width: 3.5rem;
                height: 3.5rem;
                border: 2px solid rgba(0, 0, 0, 0.1);
                border-radius: 8px;
                font-size: 1.5rem;
                font-weight: 600;
                text-align: center;
                background: white;
                transition: all 0.3s ease;

                &:focus {
                    outline: none;
                    border-color: var(--primaryColor);
                    box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
                }

                &.filled {
                    border-color: var(--primaryColor);
                    background-color: rgba(102, 126, 234, 0.05);
                }
            }
        }
    }

    .timer-section {
        text-align: center;
        margin-bottom: 2rem;

        .timer {
            color: var(--textColor);
            font-size: 0.9375rem;

            .timer-countdown {
                font-weight: 600;
                color: var(--primaryColor);
            }

            &.expired {
                color: #ff4444;
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

    .email-tips {
        background: rgba(102, 126, 234, 0.05);
        border-radius: 8px;
        padding: 1.5rem;
        margin-bottom: 2rem;

        h4 {
            display: flex;
            align-items: center;
            gap: 0.5rem;
            margin-bottom: 1rem;
            color: var(--titleColor);
            font-size: 1rem;

            i {
                color: #ffbb33;
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
                color: var(--textColor);
                font-size: 0.9375rem;

                &::before {
                    content: '•';
                    color: var(--primaryColor);
                    font-weight: bold;
                }
            }
        }
    }

    .success-content {
        text-align: center;

        h3 {
            font-size: 1.5rem;
            font-weight: 600;
            color: var(--titleColor);
            margin-bottom: 1rem;
        }

        p {
            color: var(--textColor);
            margin-bottom: 2rem;
            line-height: 1.6;

            strong {
                color: var(--titleColor);
            }
        }

        .next-steps {
            text-align: left;
            background: rgba(102, 126, 234, 0.05);
            border-radius: 8px;
            padding: 1.5rem;
            margin-bottom: 2rem;

            h4 {
                color: var(--titleColor);
                margin-bottom: 1rem;
                font-size: 1rem;
            }

            ul {
                list-style: none;
                padding: 0;
                margin: 0;

                li {
                    display: flex;
                    align-items: center;
                    gap: 0.75rem;
                    margin-bottom: 0.75rem;
                    color: var(--textColor);

                    i {
                        color: var(--primaryColor);
                        font-size: 1.25rem;
                    }
                }
            }
        }

        .btn {
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 0.5rem;

            i {
                transition: transform 0.3s ease;
            }

            &:hover i {
                transform: translateX(4px);
            }
        }
    }
}

.error-message {
    display: flex;
    align-items: center;
    gap: 0.5rem;
    color: #ff4444;
    font-size: 0.875rem;
    margin-top: 0.5rem;

    i {
        font-size: 1rem;
    }
}

@media (max-width: 480px) {
    .email-verification-step {
        // padding: 1rem;

        .email-card,
        .verification-card,
        .success-card {
            padding: 1.5rem;
        }

        .code-inputs {
            input {
                width: 2.75rem !important;
                height: 2.75rem !important;
                font-size: 1.25rem !important;
            }
        }

        .social-buttons {
            grid-template-columns: 1fr !important;
        }
    }
}
</style>