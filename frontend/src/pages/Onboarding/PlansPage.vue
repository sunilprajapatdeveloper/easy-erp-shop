<template>
    <div class="plans-step">
        <!-- Header with billing toggle -->
        <div class="plans-header">
            <div class="billing-toggle">
                <div class="toggle-switch">
                    <button class="toggle-option" :class="{ active: billingPeriod === 'monthly' }"
                        @click="billingPeriod = 'monthly'">
                        Monthly
                    </button>
                    <button class="toggle-option" :class="{ active: billingPeriod === 'annual' }"
                        @click="billingPeriod = 'annual'">
                        Annual
                        <span class="discount-badge">Save 20%</span>
                    </button>
                </div>
            </div>
        </div>

        <!-- Loading / Error States -->
        <div v-if="planStore.loading" class="loading-state">
            <i class="ri-loader-4-line spin"></i> Loading plans...
        </div>
        <div v-else-if="error" class="error-state">
            <i class="ri-error-warning-line"></i> {{ error }}
        </div>

        <!-- Plans Grid -->
        <div v-else class="plans-grid">
            <div v-for="plan in planStore.plans" :key="plan.id" class="plan-card" :class="{
                popular: plan.name === 'Professional',
                selected: selectedPlanId === plan.id,
            }" @click="selectPlan(plan, false)">
                <!-- Popular badge (example logic) -->
                <div v-if="plan.name === 'Professional'" class="popular-badge">
                    Most Popular
                </div>

                <div class="plan-header">
                    <h3>{{ plan.name }}</h3>
                    <p class="plan-description">
                        {{ plan.description || 'Perfect for your business' }}
                    </p>
                </div>

                <div class="plan-price">
                    <div class="price">
                        <span class="currency">{{ plan.currency }}</span>
                        <span class="amount">{{ displayPrice(plan) }}</span>
                        <span class="period">/{{ billingPeriod === 'monthly' ? 'mo' : 'yr' }}</span>
                    </div>
                    <p class="billing-note">
                        {{ billingPeriod === 'annual' ? 'Billed annually' : 'Billed monthly' }}
                    </p>
                </div>

                <div class="plan-features">
                    <ul>
                        <li v-for="(value, key) in plan.features" :key="key">
                            <i class="ri-check-line"></i> {{ key }}: {{ value }}
                        </li>
                        <li v-if="plan.maxUsers">
                            <i class="ri-check-line"></i> Up to {{ plan.maxUsers }} users
                        </li>
                        <li v-if="plan.maxBranches">
                            <i class="ri-check-line"></i> {{ plan.maxBranches }} branches
                        </li>
                    </ul>
                </div>

                <!-- Action buttons -->
                <div class="plan-actions" @click.stop>
                    <template v-if="plan.trialAvailable">
                        <button class="btn btn-primary" @click="selectPlan(plan, false)">Buy Now</button>
                        <button class="btn btn-outline" @click="selectPlan(plan, true)">
                            Start {{ plan.trialDays }}-day trial
                        </button>
                    </template>
                    <button v-else class="btn btn-primary" @click="selectPlan(plan, false)">
                        Choose {{ plan.name }}
                    </button>
                </div>
            </div>
        </div>

        <!-- Feature Comparison -->
        <div class="comparison-section" v-if="planStore.plans.length">
            <h2>Compare Plans</h2>
            <p class="section-subtitle">Detailed feature breakdown across all plans</p>
            <FeatureComparison :selected-plan="selectedPlanIdForComparison" />
        </div>

        <!-- FAQ Section -->
        <div class="faq-section">
            <h2>Frequently Asked Questions</h2>
            <div class="faq-grid">
                <div class="faq-item">
                    <h4>Can I change my plan later?</h4>
                    <p>Yes, you can upgrade or downgrade at any time. You'll only pay the difference.</p>
                </div>
                <div class="faq-item">
                    <h4>What happens after the free trial?</h4>
                    <p>After 14 days, you'll be prompted to choose a paid plan. Your account will pause if you don't.
                    </p>
                </div>
                <div class="faq-item">
                    <h4>Do you offer custom plans?</h4>
                    <p>Yes, for Enterprise customers. Contact our sales team for custom pricing.</p>
                </div>
                <div class="faq-item">
                    <h4>Is there a setup fee?</h4>
                    <p>No, there are no setup fees for any plan.</p>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted, computed, watch } from 'vue'
import { useSubscriptionPlanStore } from '@/stores/subscriptionPlanStore'
import { useOnboardingStore } from '@/stores/onboardingStore'
import FeatureComparison from '@/components/Onboarding/FeatureComparison.vue'
import type { SubscriptionPlan } from '@/types/SubscriptionPlan'

type PlanId = 'starter' | 'professional' | 'enterprise'

export default defineComponent({
    name: 'PlansPage',
    components: { FeatureComparison },
    emits: ['selected'],
    setup(_, { emit }) {
        const planStore = useSubscriptionPlanStore()
        const onboardingStore = useOnboardingStore()

        const billingPeriod = ref<'monthly' | 'annual'>('annual')
        const selectedPlanId = ref<number | null>(null)
        const selectedPlanName = ref<string>('')
        const error = ref<string | null>(null)

        const selectedPlanIdForComparison = computed<PlanId>(() => {
            const name = selectedPlanName.value.toLowerCase()
            if (name.includes('starter')) return 'starter'
            if (name.includes('professional')) return 'professional'
            if (name.includes('enterprise')) return 'enterprise'
            return 'professional'
        })

        const displayPrice = (plan: SubscriptionPlan): string => {
            if (billingPeriod.value === 'annual') {
                return (plan.price * 12 * 0.8).toFixed(2)
            }
            return plan.price.toFixed(2)
        }

        const selectPlan = (plan: SubscriptionPlan, isTrial: boolean) => {
            selectedPlanId.value = plan.id
            selectedPlanName.value = plan.name

            const planData = {
                subscriptionPlanId: plan.id,
                billingCycle: billingPeriod.value,
                price: isTrial ? 0 : parseFloat(displayPrice(plan)),
                isTrial,
                trialEndDate: isTrial && plan.trialAvailable
                    ? new Date(Date.now() + plan.trialDays * 24 * 60 * 60 * 1000).toISOString()
                    : undefined,
            }

            onboardingStore.setPlanData(planData)
            emit('selected', true)
        }

        onMounted(async () => {
            try {
                await planStore.fetchPlans()
            } catch (err) {
                error.value = 'Failed to load plans. Please try again.'
                console.error(err)
            }
        })

        // When billing period changes, update the stored plan price if a plan is already selected
        watch(billingPeriod, () => {
            if (selectedPlanId.value) {
                const plan = planStore.plans.find(p => p.id === selectedPlanId.value)
                if (plan) {
                    const currentPlanData = onboardingStore.planData
                    if (currentPlanData) {
                        const newPlanData = {
                            ...currentPlanData,
                            billingCycle: billingPeriod.value,
                            price: currentPlanData.isTrial ? 0 : parseFloat(displayPrice(plan)),
                        }
                        onboardingStore.setPlanData(newPlanData)
                        emit('selected', true)
                    }
                }
            }
        })

        return {
            planStore,
            billingPeriod,
            selectedPlanId,
            selectedPlanName,
            selectedPlanIdForComparison,
            error,
            displayPrice,
            selectPlan,
        }
    },
})
</script>

<style lang="scss" scoped>
.plans-step {
    width: 100%;
    max-width: 100%;
    margin: 0 auto;
    padding: 0 1rem;

    .plans-header {
        text-align: center;
        margin-bottom: 2rem;

        .billing-toggle {
            display: inline-block;
            background: #f1f5f9;
            border-radius: 40px;
            padding: 0.25rem;

            .toggle-switch {
                display: flex;
                gap: 0.25rem;

                .toggle-option {
                    padding: 0.5rem 1.5rem;
                    border: none;
                    background: transparent;
                    border-radius: 30px;
                    font-size: 1rem;
                    font-weight: 600;
                    color: #64748b;
                    cursor: pointer;
                    transition: all 0.2s ease;
                    position: relative;

                    &.active {
                        background: white;
                        color: #3b82f6;
                        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
                    }

                    .discount-badge {
                        position: absolute;
                        top: -8px;
                        right: -8px;
                        background: #10b981;
                        color: white;
                        font-size: 0.7rem;
                        padding: 0.2rem 0.5rem;
                        border-radius: 20px;
                        font-weight: 600;
                    }
                }
            }
        }
    }

    .loading-state,
    .error-state {
        text-align: center;
        padding: 3rem;
        color: #64748b;

        i {
            font-size: 2rem;
            margin-bottom: 1rem;
            display: block;
        }

        .spin {
            animation: spin 1s linear infinite;
        }
    }

    .plans-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
        gap: 1.5rem;
        margin-bottom: 3rem;

        .plan-card {
            background: white;
            border-radius: 24px;
            padding: 2rem 1.5rem;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.04);
            transition: all 0.3s ease;
            border: 2px solid transparent;
            cursor: pointer;
            display: flex;
            flex-direction: column;
            position: relative;
            overflow: hidden;

            &:hover {
                transform: translateY(-4px);
                box-shadow: 0 20px 30px rgba(0, 0, 0, 0.08);
                border-color: #e2e8f0;
            }

            &.popular {
                border-color: #3b82f6;
                background: linear-gradient(to bottom, #ffffff, #f8fafc);

                .popular-badge {
                    position: absolute;
                    top: 12px;
                    right: 12px;
                    background: #3b82f6;
                    color: white;
                    font-size: 0.75rem;
                    font-weight: 600;
                    padding: 0.25rem 1rem;
                    border-radius: 20px;
                    letter-spacing: 0.5px;
                }
            }

            &.selected {
                border-color: #3b82f6;
                box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.2);
            }

            .plan-header {
                margin-bottom: 1.5rem;

                h3 {
                    font-size: 1.5rem;
                    font-weight: 700;
                    color: #0f172a;
                    margin: 0 0 0.25rem;
                }

                .plan-description {
                    font-size: 0.875rem;
                    color: #64748b;
                    line-height: 1.4;
                    margin: 0;
                }
            }

            .plan-price {
                margin-bottom: 1.5rem;

                .price {
                    display: flex;
                    align-items: baseline;
                    gap: 0.25rem;

                    .currency {
                        font-size: 1.25rem;
                        font-weight: 600;
                        color: #0f172a;
                    }

                    .amount {
                        font-size: 2.5rem;
                        font-weight: 800;
                        color: #0f172a;
                        line-height: 1;
                    }

                    .period {
                        font-size: 1rem;
                        color: #64748b;
                    }
                }

                .billing-note {
                    font-size: 0.8rem;
                    color: #94a3b8;
                    margin: 0.25rem 0 0;
                }
            }

            .plan-features {
                flex: 1;
                margin-bottom: 2rem;

                ul {
                    list-style: none;
                    padding: 0;
                    margin: 0;

                    li {
                        display: flex;
                        align-items: flex-start;
                        gap: 0.5rem;
                        font-size: 0.875rem;
                        color: #334155;
                        margin-bottom: 0.75rem;

                        i {
                            color: #10b981;
                            font-size: 1.1rem;
                            flex-shrink: 0;
                            margin-top: 0.1rem;
                        }
                    }
                }
            }

            .plan-actions {
                display: flex;
                flex-direction: column;
                gap: 0.5rem;

                .btn {
                    width: 100%;
                    padding: 0.75rem;
                    font-weight: 600;
                    border-radius: 12px;
                    transition: all 0.2s ease;

                    &.btn-primary {
                        background: #3b82f6;
                        color: white;
                        border: none;

                        &:hover {
                            background: #2563eb;
                        }
                    }

                    &.btn-outline {
                        background: transparent;
                        border: 2px solid #e2e8f0;
                        color: #334155;

                        &:hover {
                            border-color: #3b82f6;
                            color: #3b82f6;
                        }
                    }
                }
            }
        }
    }

    .comparison-section {
        margin-bottom: 3rem;
        text-align: center;

        h2 {
            font-size: 2rem;
            font-weight: 700;
            color: #0f172a;
            margin-bottom: 0.5rem;
        }

        .section-subtitle {
            font-size: 1.125rem;
            color: #64748b;
            margin-bottom: 2rem;
        }
    }

    .faq-section {
        h2 {
            font-size: 2rem;
            font-weight: 700;
            color: #0f172a;
            text-align: center;
            margin-bottom: 2rem;
        }

        .faq-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
            gap: 1.5rem;
            max-width: 900px;
            margin: 0 auto;

            .faq-item {
                background: white;
                border-radius: 16px;
                padding: 1.5rem;
                box-shadow: 0 2px 12px rgba(0, 0, 0, 0.02);

                h4 {
                    font-size: 1.125rem;
                    font-weight: 600;
                    color: #0f172a;
                    margin: 0 0 0.5rem;
                }

                p {
                    font-size: 0.9375rem;
                    color: #475569;
                    line-height: 1.5;
                    margin: 0;
                }
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

@media (max-width: 768px) {
    .plans-step {
        padding: 0 0.5rem;

        .plans-grid {
            grid-template-columns: 1fr;
        }

        .faq-grid {
            grid-template-columns: 1fr;
        }
    }
}
</style>