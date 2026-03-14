<template>
    <section id="pricing" class="pricing-preview-section">
        <div class="container">
            <!-- Section Header -->
            <div class="section-header text-center animate-on-scroll">
                <div class="section-badge">Pricing</div>
                <h2 class="section-title">Simple, transparent pricing</h2>
                <p class="section-subtitle">
                    Start with a 14-day free trial. No credit card required. Cancel anytime.
                </p>
            </div>

            <!-- Billing Toggle -->
            <div class="billing-toggle animate-on-scroll delay-1">
                <span>Monthly</span>
                <label class="toggle-switch">
                    <input type="checkbox" v-model="isYearly">
                    <span class="slider"></span>
                </label>
                <span>Yearly <span class="discount-badge">Save 20%</span></span>
            </div>

            <!-- Pricing Cards -->
            <div class="pricing-cards">
                <!-- Starter Plan -->
                <div class="pricing-card animate-on-scroll delay-2">
                    <div class="card-header">
                        <h3 class="plan-name">Starter</h3>
                        <p class="plan-description">Perfect for small businesses</p>
                        <div class="plan-price">
                            <span class="price-amount">{{ formatPrice(starterPrice) }}</span>
                            <span class="price-period">/{{ isYearly ? 'year' : 'month' }}</span>
                        </div>
                        <router-link to="/signup?plan=starter" class="btn btn-outline btn-block">
                            Start Free Trial
                        </router-link>
                    </div>

                    <div class="card-features">
                        <div class="feature-header">What's included:</div>
                        <ul class="feature-list">
                            <li><i class="ri-check-line"></i> Up to 10 users</li>
                            <li><i class="ri-check-line"></i> 3 warehouses</li>
                            <li><i class="ri-check-line"></i> 5,000 products</li>
                            <li><i class="ri-check-line"></i> Basic POS features</li>
                            <li><i class="ri-check-line"></i> Multi-currency support</li>
                            <li><i class="ri-check-line"></i> Email support</li>
                            <li class="disabled"><i class="ri-close-line"></i> Advanced analytics</li>
                            <li class="disabled"><i class="ri-close-line"></i> Custom reports</li>
                            <li class="disabled"><i class="ri-close-line"></i> API access</li>
                        </ul>
                    </div>

                    <div class="card-footer">
                        <div class="popular-tag" v-if="!isYearly">Most popular for startups</div>
                        <div class="trial-info">
                            <i class="ri-checkbox-circle-line"></i>
                            <span>14-day free trial included</span>
                        </div>
                    </div>
                </div>

                <!-- Professional Plan -->
                <div class="pricing-card recommended animate-on-scroll delay-3">
                    <div class="recommended-badge">Most Popular</div>
                    <div class="card-header">
                        <h3 class="plan-name">Professional</h3>
                        <p class="plan-description">Best for growing businesses</p>
                        <div class="plan-price">
                            <span class="price-amount">{{ formatPrice(professionalPrice) }}</span>
                            <span class="price-period">/{{ isYearly ? 'year' : 'month' }}</span>
                        </div>
                        <router-link to="/signup?plan=professional" class="btn btn-primary btn-block">
                            Get Started
                        </router-link>
                    </div>

                    <div class="card-features">
                        <div class="feature-header">Everything in Starter, plus:</div>
                        <ul class="feature-list">
                            <li><i class="ri-check-line"></i> Up to 50 users</li>
                            <li><i class="ri-check-line"></i> 10 warehouses</li>
                            <li><i class="ri-check-line"></i> Unlimited products</li>
                            <li><i class="ri-check-line"></i> Advanced analytics</li>
                            <li><i class="ri-check-line"></i> Custom reports</li>
                            <li><i class="ri-check-line"></i> API access</li>
                            <li><i class="ri-check-line"></i> Priority support</li>
                            <li><i class="ri-check-line"></i> Inventory forecasting</li>
                            <li><i class="ri-check-line"></i> Mobile app access</li>
                        </ul>
                    </div>

                    <div class="card-footer">
                        <div class="value-proposition">
                            <i class="ri-star-line"></i>
                            <span>Best value for growing businesses</span>
                        </div>
                    </div>
                </div>

                <!-- Enterprise Plan -->
                <div class="pricing-card animate-on-scroll delay-4">
                    <div class="card-header">
                        <h3 class="plan-name">Enterprise</h3>
                        <p class="plan-description">For large organizations</p>
                        <div class="plan-price">
                            <span class="price-amount">Custom</span>
                        </div>
                        <a href="mailto:sales@yourerp.com" class="btn btn-outline btn-block">
                            Contact Sales
                        </a>
                    </div>

                    <div class="card-features">
                        <div class="feature-header">Everything in Professional, plus:</div>
                        <ul class="feature-list">
                            <li><i class="ri-check-line"></i> Unlimited users</li>
                            <li><i class="ri-check-line"></i> Unlimited warehouses</li>
                            <li><i class="ri-check-line"></i> Dedicated account manager</li>
                            <li><i class="ri-check-line"></i> Custom integrations</li>
                            <li><i class="ri-check-line"></i> SLA 99.9% uptime</li>
                            <li><i class="ri-check-line"></i> White-label option</li>
                            <li><i class="ri-check-line"></i> On-premise deployment</li>
                            <li><i class="ri-check-line"></i> Advanced security</li>
                            <li><i class="ri-check-line"></i> Training & onboarding</li>
                        </ul>
                    </div>

                    <div class="card-footer">
                        <div class="enterprise-info">
                            <i class="ri-building-line"></i>
                            <span>Custom solutions for enterprises</span>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Feature Comparison -->
            <div class="feature-comparison animate-on-scroll">
                <div class="comparison-header">
                    <h3>Compare all features</h3>
                    <button class="btn btn-text" @click="toggleComparison">
                        {{ showComparison ? 'Hide details' : 'Show all features' }}
                        <i :class="showComparison ? 'ri-arrow-up-s-line' : 'ri-arrow-down-s-line'"></i>
                    </button>
                </div>

                <div class="comparison-table" :class="{ 'expanded': showComparison }">
                    <table>
                        <thead>
                            <tr>
                                <th>Feature</th>
                                <th>Starter</th>
                                <th>Professional</th>
                                <th>Enterprise</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="feature in comparisonFeatures" :key="feature.name">
                                <td>{{ feature.name }}</td>
                                <td>
                                    <span v-if="feature.starter === true" class="feature-yes">
                                        <i class="ri-check-line"></i>
                                    </span>
                                    <span v-else-if="feature.starter === false" class="feature-no">
                                        <i class="ri-close-line"></i>
                                    </span>
                                    <span v-else>{{ feature.starter }}</span>
                                </td>
                                <td>
                                    <span v-if="feature.professional === true" class="feature-yes">
                                        <i class="ri-check-line"></i>
                                    </span>
                                    <span v-else-if="feature.professional === false" class="feature-no">
                                        <i class="ri-close-line"></i>
                                    </span>
                                    <span v-else>{{ feature.professional }}</span>
                                </td>
                                <td>
                                    <span v-if="feature.enterprise === true" class="feature-yes">
                                        <i class="ri-check-line"></i>
                                    </span>
                                    <span v-else-if="feature.enterprise === false" class="feature-no">
                                        <i class="ri-close-line"></i>
                                    </span>
                                    <span v-else>{{ feature.enterprise }}</span>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <!-- Pricing FAQ -->
            <div class="pricing-faq animate-on-scroll">
                <h3>Frequently asked questions</h3>
                <div class="faq-grid">
                    <div class="faq-item">
                        <h4>Is there a free trial?</h4>
                        <p>Yes! All plans include a 14-day free trial. No credit card required.</p>
                    </div>
                    <div class="faq-item">
                        <h4>Can I change plans later?</h4>
                        <p>Yes, you can upgrade or downgrade your plan at any time.</p>
                    </div>
                    <div class="faq-item">
                        <h4>What payment methods do you accept?</h4>
                        <p>We accept all major credit cards, PayPal, and bank transfers for annual plans.</p>
                    </div>
                    <div class="faq-item">
                        <h4>Is there a setup fee?</h4>
                        <p>No setup fees for any of our plans. What you see is what you pay.</p>
                    </div>
                </div>
            </div>

            <!-- Bottom CTA -->
            <div class="pricing-cta animate-on-scroll">
                <div class="cta-content">
                    <h3>Still have questions?</h3>
                    <p>Our team is here to help you choose the right plan for your business.</p>
                    <div class="cta-buttons">
                        <a href="mailto:sales@yourerp.com" class="btn btn-outline btn-xl">
                            <i class="ri-mail-line"></i>
                            Contact Sales
                        </a>
                        <a href="#" class="btn btn-text">
                            <i class="ri-question-line"></i>
                            View all FAQs
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </section>
</template>

<script lang="ts">
import { defineComponent, ref, computed } from 'vue'

export default defineComponent({
    name: 'PricingPreview',
    setup() {
        const isYearly = ref(false)
        const showComparison = ref(false)

        const starterPrice = computed(() => isYearly.value ? 470 : 49)
        const professionalPrice = computed(() => isYearly.value ? 1238 : 129)

        const formatPrice = (price: number) => {
            if (price >= 1000) {
                return `$${(price / 1000).toFixed(1)}K`
            }
            return `$${price}`
        }

        const toggleComparison = () => {
            showComparison.value = !showComparison.value
        }

        const comparisonFeatures = [
            { name: 'Users', starter: 'Up to 10', professional: 'Up to 50', enterprise: 'Unlimited' },
            { name: 'Warehouses', starter: 3, professional: 10, enterprise: 'Unlimited' },
            { name: 'Products', starter: '5,000', professional: 'Unlimited', enterprise: 'Unlimited' },
            { name: 'Storage', starter: '10GB', professional: '50GB', enterprise: '500GB' },
            { name: 'POS Terminals', starter: 3, professional: 10, enterprise: 'Unlimited' },
            { name: 'Multi-currency', starter: true, professional: true, enterprise: true },
            { name: 'Offline Mode', starter: true, professional: true, enterprise: true },
            { name: 'Mobile App', starter: false, professional: true, enterprise: true },
            { name: 'API Access', starter: false, professional: true, enterprise: true },
            { name: 'Custom Reports', starter: false, professional: true, enterprise: true },
            { name: 'Advanced Analytics', starter: false, professional: true, enterprise: true },
            { name: 'Priority Support', starter: false, professional: true, enterprise: true },
            { name: 'Dedicated Account Manager', starter: false, professional: false, enterprise: true },
            { name: 'SLA Guarantee', starter: false, professional: false, enterprise: true },
            { name: 'White-label Option', starter: false, professional: false, enterprise: true }
        ]

        return {
            isYearly,
            showComparison,
            starterPrice,
            professionalPrice,
            comparisonFeatures,
            formatPrice,
            toggleComparison
        }
    }
})
</script>

<style lang="scss" scoped>
.pricing-preview-section {
    padding: 100px 0;
    background: linear-gradient(to bottom, #ffffff, #f8fafc);

    .section-header {
        margin-bottom: 60px;

        .section-badge {
            display: inline-block;
            background: rgba(16, 185, 129, 0.1);
            color: #10b981;
            padding: 8px 20px;
            border-radius: 50px;
            font-size: 0.875rem;
            font-weight: 500;
            margin-bottom: 1rem;
        }

        .section-title {
            font-size: 2.5rem;
            font-weight: 700;
            margin-bottom: 1rem;
            color: var(--titleColor);
        }

        .section-subtitle {
            font-size: 1.125rem;
            color: var(--textColor);
            max-width: 600px;
            margin: 0 auto;
            line-height: 1.6;
        }
    }

    .billing-toggle {
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 1rem;
        margin-bottom: 60px;

        span {
            font-weight: 500;
            color: var(--textColor);
        }

        .toggle-switch {
            position: relative;
            display: inline-block;
            width: 60px;
            height: 32px;

            input {
                opacity: 0;
                width: 0;
                height: 0;

                &:checked+.slider {
                    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);

                    &::before {
                        transform: translateX(28px);
                    }
                }
            }

            .slider {
                position: absolute;
                cursor: pointer;
                top: 0;
                left: 0;
                right: 0;
                bottom: 0;
                background: #d1d5db;
                transition: .4s;
                border-radius: 34px;

                &::before {
                    position: absolute;
                    content: "";
                    height: 24px;
                    width: 24px;
                    left: 4px;
                    bottom: 4px;
                    background: white;
                    transition: .4s;
                    border-radius: 50%;
                }
            }
        }

        .discount-badge {
            background: #10b981;
            color: white;
            padding: 2px 8px;
            border-radius: 12px;
            font-size: 0.75rem;
            font-weight: 600;
        }
    }

    .pricing-cards {
        display: grid;
        grid-template-columns: 1fr;
        gap: 2rem;
        margin-bottom: 80px;

        @media (min-width: 768px) {
            grid-template-columns: repeat(3, 1fr);
        }
    }

    .pricing-card {
        background: white;
        border-radius: 20px;
        overflow: hidden;
        box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
        border: 1px solid rgba(0, 0, 0, 0.05);
        position: relative;
        transition: transform 0.3s ease;

        &:hover {
            transform: translateY(-10px);
            box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
        }

        &.recommended {
            border: 2px solid var(--primaryColor);
            transform: scale(1.05);

            @media (max-width: 768px) {
                transform: none;
            }

            .recommended-badge {
                position: absolute;
                top: 0;
                right: 0;
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                padding: 8px 20px;
                font-size: 0.75rem;
                font-weight: 600;
                border-radius: 0 20px 0 20px;
            }
        }

        .card-header {
            padding: 2rem;
            text-align: center;
            border-bottom: 1px solid #e5e7eb;

            .plan-name {
                font-size: 1.5rem;
                font-weight: 700;
                color: var(--titleColor);
                margin-bottom: 0.5rem;
            }

            .plan-description {
                color: var(--textColor);
                margin-bottom: 1.5rem;
                font-size: 0.875rem;
            }

            .plan-price {
                margin-bottom: 1.5rem;

                .price-amount {
                    font-size: 3rem;
                    font-weight: 700;
                    color: var(--titleColor);
                }

                .price-period {
                    color: var(--textColor);
                    font-size: 1rem;
                }
            }

            .btn {
                padding: 0.75rem;
                font-weight: 600;
            }
        }

        .card-features {
            padding: 2rem;

            .feature-header {
                font-weight: 600;
                color: var(--titleColor);
                margin-bottom: 1.5rem;
                font-size: 0.875rem;
                text-transform: uppercase;
                letter-spacing: 0.5px;
            }

            .feature-list {
                list-style: none;
                padding: 0;
                margin: 0;

                li {
                    display: flex;
                    align-items: center;
                    gap: 0.75rem;
                    margin-bottom: 1rem;
                    color: var(--textColor);
                    font-size: 0.875rem;

                    i {
                        &.ri-check-line {
                            color: #10b981;
                        }

                        &.ri-close-line {
                            color: #ef4444;
                        }
                    }

                    &.disabled {
                        opacity: 0.5;
                    }
                }
            }
        }

        .card-footer {
            padding: 1rem 2rem;
            border-top: 1px solid #e5e7eb;
            background: #f8fafc;

            .popular-tag {
                background: rgba(245, 158, 11, 0.1);
                color: #f59e0b;
                padding: 4px 12px;
                border-radius: 12px;
                font-size: 0.75rem;
                font-weight: 500;
                text-align: center;
                margin-bottom: 0.5rem;
            }

            .trial-info {
                display: flex;
                align-items: center;
                justify-content: center;
                gap: 0.5rem;
                font-size: 0.75rem;
                color: var(--textColor);

                i {
                    color: #10b981;
                }
            }

            .value-proposition {
                display: flex;
                align-items: center;
                justify-content: center;
                gap: 0.5rem;
                font-size: 0.75rem;
                color: var(--primaryColor);
                font-weight: 500;

                i {
                    font-size: 1rem;
                }
            }

            .enterprise-info {
                display: flex;
                align-items: center;
                justify-content: center;
                gap: 0.5rem;
                font-size: 0.75rem;
                color: var(--textColor);
                font-weight: 500;

                i {
                    font-size: 1rem;
                    color: var(--titleColor);
                }
            }
        }
    }

    .feature-comparison {
        margin-bottom: 80px;

        .comparison-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 2rem;

            h3 {
                font-size: 1.5rem;
                font-weight: 600;
                color: var(--titleColor);
            }

            .btn-text {
                color: var(--primaryColor);
                font-weight: 500;

                i {
                    margin-left: 0.5rem;
                    transition: transform 0.3s ease;
                }
            }
        }

        .comparison-table {
            overflow: hidden;
            max-height: 600px;
            transition: max-height 0.3s ease;

            &.expanded {
                max-height: 2000px;
            }

            table {
                width: 100%;
                border-collapse: collapse;
                background: white;
                border-radius: 12px;
                overflow: hidden;
                box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);

                thead {
                    background: #f8fafc;

                    th {
                        padding: 1.5rem;
                        text-align: left;
                        font-weight: 600;
                        color: var(--titleColor);
                        border-bottom: 1px solid #e5e7eb;

                        &:first-child {
                            width: 40%;
                        }

                        &:nth-child(2) {
                            color: var(--textColor);
                        }

                        &:nth-child(3) {
                            color: var(--primaryColor);
                        }

                        &:nth-child(4) {
                            color: var(--titleColor);
                        }
                    }
                }

                tbody {
                    tr {
                        border-bottom: 1px solid #e5e7eb;

                        &:last-child {
                            border-bottom: none;
                        }

                        td {
                            padding: 1.5rem;
                            color: var(--textColor);

                            &:first-child {
                                font-weight: 500;
                                color: var(--titleColor);
                            }

                            .feature-yes {
                                color: #10b981;
                                font-weight: 600;
                            }

                            .feature-no {
                                color: #ef4444;
                            }
                        }
                    }
                }
            }
        }
    }

    .pricing-faq {
        margin-bottom: 60px;

        h3 {
            font-size: 1.5rem;
            font-weight: 600;
            color: var(--titleColor);
            margin-bottom: 2rem;
            text-align: center;
        }

        .faq-grid {
            display: grid;
            grid-template-columns: 1fr;
            gap: 2rem;

            @media (min-width: 768px) {
                grid-template-columns: repeat(2, 1fr);
            }
        }

        .faq-item {
            background: white;
            border-radius: 12px;
            padding: 1.5rem;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);

            h4 {
                font-size: 1.125rem;
                font-weight: 600;
                color: var(--titleColor);
                margin-bottom: 0.75rem;
            }

            p {
                color: var(--textColor);
                line-height: 1.6;
                font-size: 0.875rem;
            }
        }
    }

    .pricing-cta {
        .cta-content {
            text-align: center;
            max-width: 600px;
            margin: 0 auto;

            h3 {
                font-size: 2rem;
                font-weight: 700;
                color: var(--titleColor);
                margin-bottom: 1rem;
            }

            p {
                font-size: 1.125rem;
                color: var(--textColor);
                margin-bottom: 2rem;
                line-height: 1.6;
            }

            .cta-buttons {
                display: flex;
                gap: 1rem;
                justify-content: center;
                flex-wrap: wrap;

                .btn {
                    padding: 0.75rem 2rem;

                    i {
                        margin-right: 0.5rem;
                    }
                }
            }
        }
    }
}
</style>