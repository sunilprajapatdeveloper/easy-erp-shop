<template>
    <OnboardingLayout :current-step="currentStep" :steps="steps" :can-continue="canContinue"
        :can-skip="canSkipCurrentStep" :show-back-button="showBackButton" @continue="handleContinue" @back="handleBack"
        @skip="handleSkip">
        <!-- Step 1: Email Verification -->
        <div v-if="currentStep === 1">
            <EmailVerification ref="emailVerificationRef" @validated="handleEmailVerified" />
        </div>

        <!-- Step 2: Company Setup -->
        <div v-else-if="currentStep === 2">
            <CompanySetupStep ref="companyRef" @validated="handleCompanyValidated" @created="handleCompanyCreated" />
        </div>

        <!-- Step 3: Signup (user details) -->
        <div v-else-if="currentStep === 3">
            <SignupStep ref="signupRef" @validated="handleSignupValidated" @created="handleUserCreated" />
        </div>

        <!-- Step 4: Plans Selection -->
        <div v-else-if="currentStep === 4">
            <PlansStep ref="plansRef" @selected="handlePlanSelected" />
        </div>

        <!-- Step 5: Payment (only if payment required) -->
        <div v-else-if="currentStep === 5 && paymentRequired">
            <CheckoutStep ref="checkoutRef" @validated="handlePaymentValidated" />
        </div>

        <!-- Step 5 or 6: Warehouse Setup -->
        <div v-else-if="currentStep === (paymentRequired ? 6 : 5)">
            <WarehouseSetup ref="warehouseRef" @validated="handleWarehouseValidated" />
        </div>
    </OnboardingLayout>
</template>

<script lang="ts">
import { defineComponent, ref, computed, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useOnboardingStore } from '@/stores/onboardingStore'
import { useCompanySubscriptionStore } from '@/stores/companySubscriptionStore'
import { usePaymentStore } from '@/stores/paymentStore'
import OnboardingLayout from '@/components/Layouts/OnboardingLayout.vue'
import EmailVerification from './EmailVerification.vue'
import SignupStep from './SignupStep.vue'
import CompanySetupStep from './CompanySetup.vue'
import PlansStep from './PlansPage.vue'
import CheckoutStep from './CheckoutStep.vue'
import WarehouseSetup from '@/components/Onboarding/WarehouseSetup.vue'
import { PaymentSourceType } from '@/enums/paymentSourceType'
import { PaymentType } from '@/enums/paymentType'
import { PaymentStatus } from '@/enums/paymentStatus'
import { PaymentMethod } from '@/enums/paymentMethods'
import { PaymentGatewayProvider } from '@/enums/PaymentGatewayProvider'
import { BillingCycle } from '@/enums/BillingCycle'

export default defineComponent({
    name: 'SetupWizard',
    components: {
        OnboardingLayout,
        EmailVerification,
        SignupStep,
        CompanySetupStep,
        PlansStep,
        CheckoutStep,
        WarehouseSetup,
    },
    setup() {
        const router = useRouter()
        const onboardingStore = useOnboardingStore()
        const companySubscriptionStore = useCompanySubscriptionStore()
        const paymentStore = usePaymentStore()

        // Refs to child components
        const emailVerificationRef = ref<InstanceType<typeof EmailVerification>>()
        const signupRef = ref<InstanceType<typeof SignupStep>>()
        const companyRef = ref<InstanceType<typeof CompanySetupStep>>()
        const plansRef = ref<InstanceType<typeof PlansStep>>()
        const checkoutRef = ref<InstanceType<typeof CheckoutStep>>()
        const warehouseRef = ref<InstanceType<typeof WarehouseSetup> & { saveWarehouses: () => Promise<void> }>()

        // Current step and validation flags
        const currentStep = ref(1)
        const emailVerified = ref(false)
        const companyValidated = ref(false)
        const signupValidated = ref(false)
        const planSelected = ref(false)
        const paymentValidated = ref(false)
        const warehouseValidated = ref(false)

        const paymentRequired = computed(() => onboardingStore.isPaymentRequired)

        // Dynamic steps (new order)
        const steps = computed(() => {
            const baseSteps = [
                { title: 'Email Verification', description: 'Verify your work email' },
                { title: 'Company Details', description: 'Tell us about your business' },
                { title: 'Create Account', description: 'Setup your personal credentials' },
                { title: 'Choose Plan', description: 'Select your subscription plan' },
            ]
            if (paymentRequired.value) {
                baseSteps.push({ title: 'Payment', description: 'Enter your payment details' })
            }
            baseSteps.push({ title: 'Warehouse Setup', description: 'Configure your first location' })
            return baseSteps
        })

        const totalSteps = computed(() => steps.value.length)

        const canContinue = computed(() => {
            const step = currentStep.value
            if (step === 1) return emailVerified.value
            if (step === 2) return companyValidated.value
            if (step === 3) return signupValidated.value
            if (step === 4) return planSelected.value
            if (step === 5 && paymentRequired.value) return paymentValidated.value
            if (step === (paymentRequired.value ? 6 : 5)) return warehouseValidated.value
            return false
        })

        const canSkipCurrentStep = computed(() => {
            const step = currentStep.value
            return step === 4 || step === (paymentRequired.value ? 6 : 5)
        })

        const showBackButton = computed(() => {
            return currentStep.value > 1 && currentStep.value <= totalSteps.value
        })

        // Validation handlers
        const handleEmailVerified = (isValid: boolean) => {
            emailVerified.value = isValid
        }

        const handleCompanyValidated = (isValid: boolean) => {
            companyValidated.value = isValid
        }

        const handleCompanyCreated = (id: number) => {
            onboardingStore.setCompanyId(id)
        }

        const handleSignupValidated = (isValid: boolean) => {
            signupValidated.value = isValid
        }

        const handleUserCreated = (id: number) => {
            onboardingStore.setUserId(id)
        }

        const handlePlanSelected = (isValid: boolean) => {
            planSelected.value = isValid
            paymentValidated.value = false
        }

        const handlePaymentValidated = (paymentData: any | null) => {
            if (paymentData) {
                onboardingStore.setPaymentInfo(paymentData)
                paymentValidated.value = true
            } else {
                paymentValidated.value = false
            }
        }

        const handleWarehouseValidated = (isValid: boolean) => {
            warehouseValidated.value = isValid
        }

        // Navigation
        const handleContinue = async () => {
            // Step 2: save company
            if (currentStep.value === 2 && companyValidated.value) {
                try {
                    await companyRef.value?.saveCompany()
                } catch (error) {
                    console.error('Company creation failed:', error)
                    return
                }
            }

            // Step 3: save user
            if (currentStep.value === 3 && signupValidated.value) {
                try {
                    await signupRef.value?.saveUser()
                } catch (error) {
                    console.error('User creation failed:', error)
                    return
                }
            }

            // Final step: save warehouses and complete onboarding
            if (currentStep.value === totalSteps.value) {
                // Save warehouses before final submission
                try {
                    await warehouseRef.value?.saveWarehouses()
                } catch (error) {
                    console.error('Warehouse creation failed:', error)
                    return
                }
                await submitOnboarding()
            } else {
                currentStep.value++
            }
        }

        const handleBack = () => {
            if (currentStep.value > 1) currentStep.value--
        }

        const handleSkip = () => {
            const step = currentStep.value
            if (step === 4) {
                onboardingStore.setDefaultPlan()
                planSelected.value = true
            } else if (step === (paymentRequired.value ? 6 : 5)) {
                onboardingStore.setDefaultWarehouse()
                warehouseValidated.value = true
            }
        }

        // Final submission (unchanged)
        const submitOnboarding = async () => {
            const plan = onboardingStore.planData
            const companyId = onboardingStore.companyId
            const userId = onboardingStore.userId

            if (!companyId || !userId || !plan) {
                console.error('Missing required data')
                return
            }

            try {
                const billingCycleEnum = plan.billingCycle === 'monthly' ? BillingCycle.MONTHLY : BillingCycle.YEARLY

                const subscriptionPayload = {
                    companyId,
                    subscriptionPlanId: plan.subscriptionPlanId,
                    startDate: new Date(Date.now() + 30 * 60 * 1000).toISOString(),
                    trialActive: plan.isTrial,
                    trialEndDate: plan.trialEndDate,
                    autoRenew: true,
                    createdBy: userId,
                }

                const subscription = await companySubscriptionStore.create(subscriptionPayload, userId)

                if (paymentRequired.value) {
                    const paymentInfo = onboardingStore.paymentInfo
                    if (!paymentInfo) throw new Error('Payment info missing')

                    const paymentPayload = {
                        referenceType: PaymentSourceType.SUBSCRIPTION,
                        referenceId: subscription.id,
                        paymentType: PaymentType.INCOMING,
                        amount: plan.price,
                        paymentMethod: PaymentMethod.CARD,
                        gatewayProvider: PaymentGatewayProvider.STRIPE,
                        status: PaymentStatus.PAID,
                        paymentDate: new Date().toISOString(),
                        currencyCode: 'USD',
                        exchangeRate: 1,
                        createdBy: userId,
                        transactionReference: 'onboarding_' + Date.now(),
                    }

                    await paymentStore.addPayment(paymentPayload)
                }

                await onboardingStore.completeOnboarding()
                router.push('/dashboard')
            } catch (error) {
                console.error('Onboarding submission failed:', error)
            }
        }

        watch(paymentRequired, (newVal, oldVal) => {
            if (newVal !== oldVal && currentStep.value > 4) {
                currentStep.value = 4
            }
        })

        onMounted(() => {
            onboardingStore.loadProgress()
            if (onboardingStore.currentStep) {
                currentStep.value = onboardingStore.currentStep
            }
            if (onboardingStore.isEmailVerified) {
                emailVerified.value = true
            }
            if (onboardingStore.companyData) {
                companyValidated.value = true
            }
            if (onboardingStore.userData) {
                signupValidated.value = true
            }
            if (onboardingStore.planData) {
                planSelected.value = true
            }
            if (onboardingStore.warehouseData) {
                warehouseValidated.value = true
            }
        })

        watch(currentStep, (step) => {
            onboardingStore.setCurrentStep(step)
            onboardingStore.saveProgress()
        })

        return {
            currentStep,
            steps,
            canContinue,
            canSkipCurrentStep,
            showBackButton,
            paymentRequired,
            emailVerificationRef,
            signupRef,
            companyRef,
            plansRef,
            checkoutRef,
            warehouseRef,
            handleEmailVerified,
            handleCompanyValidated,
            handleCompanyCreated,
            handleSignupValidated,
            handleUserCreated,
            handlePlanSelected,
            handlePaymentValidated,
            handleWarehouseValidated,
            handleContinue,
            handleBack,
            handleSkip,
        }
    },
})
</script>