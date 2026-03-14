<template>
    <div class="feature-comparison">
        <div class="comparison-table">
            <div class="table-header">
                <div class="feature-column">
                    <h3>Features</h3>
                </div>
                <div class="plan-columns">
                    <div v-for="plan in plans" :key="plan.id" class="plan-column"
                        :class="{ popular: plan.popular, selected: selectedPlan === plan.id }">
                        <div class="plan-name">{{ plan.name }}</div>
                        <div class="plan-price">{{ plan.price }}</div>
                        <div class="plan-period">{{ plan.period }}</div>
                        <button v-if="selectedPlan === plan.id" class="selected-badge">
                            <i class="ri-check-line"></i> Selected
                        </button>
                    </div>
                </div>
            </div>

            <div class="table-body">
                <div v-for="category in features" :key="category.name" class="feature-category">
                    <div class="category-header">
                        <h4>{{ category.name }}</h4>
                    </div>

                    <div v-for="feature in category.items" :key="feature.name" class="feature-row">
                        <div class="feature-info">
                            <span class="feature-name">{{ feature.name }}</span>
                            <span v-if="feature.description" class="feature-description">
                                {{ feature.description }}
                            </span>
                        </div>

                        <div class="feature-values">
                            <div v-for="plan in plans" :key="plan.id" class="feature-value"
                                :class="{ popular: plan.popular, selected: selectedPlan === plan.id }">
                                <div v-if="feature[plan.id] === true" class="feature-included">
                                    <i class="ri-check-line"></i>
                                </div>
                                <div v-else-if="typeof feature[plan.id] === 'string'" class="feature-limited">
                                    {{ feature[plan.id] }}
                                </div>
                                <div v-else-if="feature[plan.id] === false" class="feature-excluded">
                                    <i class="ri-close-line"></i>
                                </div>
                                <div v-else class="feature-info-icon">
                                    <i class="ri-information-line"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="feature-legend">
            <div class="legend-item">
                <i class="ri-check-line"></i>
                <span>Included</span>
            </div>
            <div class="legend-item">
                <i class="ri-close-line"></i>
                <span>Not Included</span>
            </div>
            <div class="legend-item">
                <i class="ri-information-line"></i>
                <span>More Info</span>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

type PlanId = 'starter' | 'professional' | 'enterprise'
type FeatureValue = boolean | string

interface Plan {
    id: PlanId
    name: string
    price: string
    period: string
    popular: boolean
}

interface FeatureItem {
    name: string
    description?: string
    starter: FeatureValue
    professional: FeatureValue
    enterprise: FeatureValue
}

interface FeatureCategory {
    name: string
    items: FeatureItem[]
}

export default defineComponent({
    name: 'FeatureComparison',
    props: {
        selectedPlan: {
            type: String as () => PlanId,
            default: 'professional'
        }
    },
    setup() {
        const plans: Plan[] = [
            {
                id: 'starter',
                name: 'Starter',
                price: '$29',
                period: '/month',
                popular: false
            },
            {
                id: 'professional',
                name: 'Professional',
                price: '$79',
                period: '/month',
                popular: true
            },
            {
                id: 'enterprise',
                name: 'Enterprise',
                price: '$199',
                period: '/month',
                popular: false
            }
        ]

        const features: FeatureCategory[] = [
            {
                name: 'Users & Access',
                items: [
                    {
                        name: 'Number of Users',
                        starter: '3 users',
                        professional: '15 users',
                        enterprise: 'Unlimited'
                    },
                    {
                        name: 'User Roles & Permissions',
                        starter: false,
                        professional: true,
                        enterprise: true
                    }
                ]
            },
            {
                name: 'Sales & Orders',
                items: [
                    {
                        name: 'Orders per Month',
                        starter: '1,000',
                        professional: '10,000',
                        enterprise: 'Unlimited'
                    },
                    {
                        name: 'POS Sales',
                        starter: false,
                        professional: true,
                        enterprise: true
                    }
                ]
            },
            {
                name: 'Reports',
                items: [
                    {
                        name: 'Basic Reports',
                        starter: true,
                        professional: true,
                        enterprise: true
                    },
                    {
                        name: 'Advanced Analytics',
                        starter: false,
                        professional: true,
                        enterprise: true
                    }
                ]
            }
        ]

        return {
            plans,
            features
        }
    }
})
</script>

<style lang="scss" scoped>
.feature-comparison {
    .comparison-table {
        background: white;
        border-radius: 12px;
        overflow: hidden;
        box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);

        .table-header {
            display: flex;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;

            .feature-column {
                flex: 2;
                padding: 24px;
                display: flex;
                align-items: center;

                h3 {
                    margin: 0;
                    font-size: 20px;
                    font-weight: 600;
                }
            }

            .plan-columns {
                flex: 3;
                display: flex;

                .plan-column {
                    flex: 1;
                    padding: 24px 16px;
                    text-align: center;
                    position: relative;
                    transition: all 0.3s ease;

                    &:not(:last-child) {
                        border-right: 1px solid rgba(255, 255, 255, 0.1);
                    }

                    &.popular {
                        background: rgba(255, 255, 255, 0.1);
                        transform: scale(1.05);
                        z-index: 1;

                        &::before {
                            content: 'Most Popular';
                            position: absolute;
                            top: -10px;
                            left: 50%;
                            transform: translateX(-50%);
                            background: #ffbb33;
                            color: #333;
                            padding: 4px 12px;
                            border-radius: 12px;
                            font-size: 12px;
                            font-weight: 600;
                            white-space: nowrap;
                        }
                    }

                    &.selected {
                        background: rgba(255, 255, 255, 0.15);

                        .selected-badge {
                            background: rgba(255, 255, 255, 0.2);
                        }
                    }

                    .plan-name {
                        font-size: 18px;
                        font-weight: 700;
                        margin-bottom: 8px;
                    }

                    .plan-price {
                        font-size: 32px;
                        font-weight: 800;
                        margin-bottom: 4px;
                    }

                    .plan-period {
                        font-size: 14px;
                        opacity: 0.9;
                        margin-bottom: 12px;
                    }

                    .selected-badge {
                        background: rgba(255, 255, 255, 0.1);
                        border: none;
                        color: white;
                        padding: 6px 12px;
                        border-radius: 20px;
                        font-size: 12px;
                        font-weight: 600;
                        display: inline-flex;
                        align-items: center;
                        gap: 4px;
                        cursor: default;

                        i {
                            font-size: 14px;
                        }
                    }
                }
            }
        }

        .table-body {
            .feature-category {
                &:not(:last-child) {
                    border-bottom: 2px solid rgba(0, 0, 0, 0.05);
                }

                .category-header {
                    padding: 16px 24px;
                    background: rgba(0, 0, 0, 0.02);
                    border-bottom: 1px solid rgba(0, 0, 0, 0.05);

                    h4 {
                        margin: 0;
                        font-size: 16px;
                        font-weight: 600;
                        color: var(--titleColor);
                    }
                }

                .feature-row {
                    display: flex;
                    border-bottom: 1px solid rgba(0, 0, 0, 0.05);

                    &:last-child {
                        border-bottom: none;
                    }

                    .feature-info {
                        flex: 2;
                        padding: 16px 24px;
                        display: flex;
                        flex-direction: column;
                        justify-content: center;

                        .feature-name {
                            font-size: 14px;
                            font-weight: 500;
                            color: var(--titleColor);
                            margin-bottom: 4px;
                        }

                        .feature-description {
                            font-size: 12px;
                            color: var(--textColor);
                            opacity: 0.7;
                        }
                    }

                    .feature-values {
                        flex: 3;
                        display: flex;

                        .feature-value {
                            flex: 1;
                            padding: 16px;
                            display: flex;
                            align-items: center;
                            justify-content: center;
                            border-left: 1px solid rgba(0, 0, 0, 0.05);
                            transition: background-color 0.3s ease;

                            &.popular {
                                background: rgba(102, 126, 234, 0.02);
                            }

                            &.selected {
                                background: linear-gradient(135deg, #667eea08 0%, #764ba208 100%);
                            }

                            .feature-included {
                                color: var(--successColor);
                                font-size: 20px;
                            }

                            .feature-limited {
                                font-size: 14px;
                                font-weight: 500;
                                color: var(--titleColor);
                                text-align: center;
                            }

                            .feature-excluded {
                                color: #ff4444;
                                opacity: 0.5;
                                font-size: 20px;
                            }

                            .feature-info-icon {
                                color: var(--primaryColor);
                                opacity: 0.5;
                                font-size: 18px;
                            }
                        }
                    }
                }
            }
        }
    }

    .feature-legend {
        display: flex;
        justify-content: center;
        gap: 24px;
        margin-top: 24px;
        padding: 16px;
        background: rgba(0, 0, 0, 0.02);
        border-radius: 8px;

        .legend-item {
            display: flex;
            align-items: center;
            gap: 8px;
            font-size: 14px;
            color: var(--textColor);

            i {
                font-size: 18px;

                &.ri-check-line {
                    color: var(--successColor);
                }

                &.ri-close-line {
                    color: #ff4444;
                }

                &.ri-information-line {
                    color: var(--primaryColor);
                }
            }
        }
    }
}

@media (max-width: 992px) {
    .feature-comparison {
        .comparison-table {
            .table-header {
                flex-direction: column;

                .plan-columns {
                    .plan-column {
                        padding: 16px 8px;

                        .plan-name {
                            font-size: 16px;
                        }

                        .plan-price {
                            font-size: 24px;
                        }
                    }
                }
            }

            .table-body {
                .feature-category {
                    .feature-row {
                        .feature-info {
                            padding: 12px 16px;
                        }

                        .feature-values {
                            .feature-value {
                                padding: 12px 8px;
                            }
                        }
                    }
                }
            }
        }
    }
}

@media (max-width: 768px) {
    .feature-comparison {
        .comparison-table {
            .table-header {
                .plan-columns {
                    flex-direction: column;

                    .plan-column {
                        border-right: none;
                        border-bottom: 1px solid rgba(255, 255, 255, 0.1);

                        &.popular {
                            transform: none;

                            &::before {
                                top: 8px;
                                left: auto;
                                right: 8px;
                                transform: none;
                            }
                        }
                    }
                }
            }

            .table-body {
                .feature-category {
                    .feature-row {
                        flex-direction: column;

                        .feature-values {
                            flex-direction: column;

                            .feature-value {
                                border-left: none;
                                border-top: 1px solid rgba(0, 0, 0, 0.05);
                                justify-content: flex-start;
                                padding: 12px 16px;

                                &::before {
                                    content: attr(data-plan);
                                    font-weight: 600;
                                    color: var(--titleColor);
                                    min-width: 100px;
                                    margin-right: 16px;
                                }
                            }
                        }
                    }
                }
            }
        }

        .feature-legend {
            flex-direction: column;
            gap: 12px;
            align-items: flex-start;
        }
    }
}
</style>