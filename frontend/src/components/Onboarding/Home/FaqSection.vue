<template>
    <section id="faq" class="faq-section">
        <div class="container">
            <!-- Section Header -->
            <div class="section-header text-center animate-on-scroll">
                <div class="section-badge">FAQ</div>
                <h2 class="section-title">Frequently asked questions</h2>
                <p class="section-subtitle">
                    Everything you need to know about our platform. Can't find an answer?
                    <a href="mailto:support@yourerp.com">Contact our support team</a>.
                </p>
            </div>

            <!-- FAQ Categories -->
            <div class="faq-categories animate-on-scroll delay-1">
                <div class="categories-tabs">
                    <button v-for="category in categories" :key="category.id" class="category-tab"
                        :class="{ active: activeCategory === category.id }" @click="activeCategory = category.id">
                        <i :class="category.icon"></i>
                        {{ category.name }}
                    </button>
                </div>
            </div>

            <!-- FAQ Accordion -->
            <div class="faq-accordion">
                <div v-for="faq in filteredFaqs" :key="faq.id" class="accordion-item animate-on-scroll"
                    :class="{ active: activeFaq === faq.id }">
                    <button class="accordion-header" @click="toggleFaq(faq.id)">
                        <div class="accordion-title">
                            <span class="faq-question">{{ faq.question }}</span>
                        </div>
                        <div class="accordion-icon">
                            <i :class="activeFaq === faq.id ? 'ri-subtract-line' : 'ri-add-line'"></i>
                        </div>
                    </button>
                    <div class="accordion-content">
                        <div class="faq-answer" v-html="faq.answer"></div>
                        <div class="faq-links" v-if="faq.links">
                            <a v-for="(link, index) in faq.links" :key="index" :href="link.url" class="faq-link">
                                <i class="ri-external-link-line"></i>
                                {{ link.text }}
                            </a>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Still Have Questions -->
            <div class="faq-cta animate-on-scroll">
                <div class="cta-card">
                    <div class="cta-icon">
                        <i class="ri-question-answer-line"></i>
                    </div>
                    <div class="cta-content">
                        <h3>Still have questions?</h3>
                        <p>Our support team is available 24/7 to help you get started.</p>
                        <div class="cta-buttons">
                            <a href="mailto:support@yourerp.com" class="btn btn-primary">
                                <i class="ri-mail-line"></i>
                                Email Support
                            </a>
                            <a href="#" class="btn btn-outline">
                                <i class="ri-file-text-line"></i>
                                Documentation
                            </a>
                            <a href="#" class="btn btn-outline">
                                <i class="ri-chat-3-line"></i>
                                Live Chat
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</template>

<script lang="ts">
import { defineComponent, ref, computed } from 'vue'

interface FaqItem {
    id: number
    category: string
    question: string
    answer: string
    links?: { text: string; url: string }[]
}

export default defineComponent({
    name: 'FaqSection',
    setup() {
        const activeCategory = ref('general')
        const activeFaq = ref<number | null>(1)

        const categories = [
            { id: 'general', name: 'General', icon: 'ri-question-line' },
            { id: 'pricing', name: 'Pricing & Plans', icon: 'ri-money-dollar-circle-line' },
            { id: 'setup', name: 'Setup & Migration', icon: 'ri-settings-5-line' },
            { id: 'features', name: 'Features', icon: 'ri-apps-line' },
            { id: 'security', name: 'Security & Compliance', icon: 'ri-shield-check-line' }
        ]

        const faqs: FaqItem[] = [
            {
                id: 1,
                category: 'general',
                question: 'What is YourERP and who is it for?',
                answer: 'YourERP is a comprehensive POS and ERP solution designed for multi-branch retail, wholesale, and distribution businesses. It\'s ideal for businesses that need to manage inventory, sales, purchases, and accounting across multiple locations.',
                links: [
                    { text: 'View all features', url: '#features' },
                    { text: 'See use cases', url: '#' }
                ]
            },
            {
                id: 2,
                category: 'general',
                question: 'How does the free trial work?',
                answer: 'All plans include a 14-day free trial with full access to all features. No credit card is required to start. After 14 days, you can choose to continue with a paid plan or cancel anytime.',
                links: [
                    { text: 'Start free trial', url: '/signup' },
                    { text: 'View pricing', url: '#pricing' }
                ]
            },
            {
                id: 3,
                category: 'pricing',
                question: 'What payment methods do you accept?',
                answer: 'We accept all major credit cards (Visa, MasterCard, American Express), PayPal, and bank transfers for annual plans. All payments are processed securely through our PCI-compliant payment processor.',
                links: [
                    { text: 'Security overview', url: '#' }
                ]
            },
            {
                id: 4,
                category: 'pricing',
                question: 'Can I change my plan later?',
                answer: 'Yes, you can upgrade or downgrade your plan at any time. When you upgrade, the new plan takes effect immediately and you\'ll be charged the prorated difference. Downgrades take effect at the start of your next billing cycle.',
                links: [
                    { text: 'View plan comparison', url: '#pricing' }
                ]
            },
            {
                id: 5,
                category: 'setup',
                question: 'How long does it take to set up?',
                answer: 'Most businesses can get started in under 30 minutes. The setup wizard guides you through creating your company, adding warehouses, and configuring basic settings. Importing existing data may take additional time depending on the volume.',
                links: [
                    { text: 'Setup guide', url: '#' },
                    { text: 'Import documentation', url: '#' }
                ]
            },
            {
                id: 6,
                category: 'setup',
                question: 'Can I import my existing data?',
                answer: 'Yes, we support CSV and Excel imports for products, customers, suppliers, and inventory. We also provide templates and validation tools to ensure smooth data migration. For larger datasets, our support team can assist with the import process.',
                links: [
                    { text: 'Download templates', url: '#' },
                    { text: 'Import guide', url: '#' }
                ]
            },
            {
                id: 7,
                category: 'features',
                question: 'Does it work offline?',
                answer: 'Yes, our POS system includes offline mode. Sales can be processed without internet connection and will sync automatically when connection is restored. Inventory updates and other data are cached locally during offline periods.',
                links: [
                    { text: 'POS features', url: '#pos' }
                ]
            },
            {
                id: 8,
                category: 'features',
                question: 'How many warehouses can I manage?',
                answer: 'The number of warehouses depends on your plan. Starter includes 3 warehouses, Professional includes 10, and Enterprise includes unlimited warehouses. Each warehouse can have its own currency, pricing, and inventory settings.',
                links: [
                    { text: 'View plan limits', url: '#pricing' }
                ]
            },
            {
                id: 9,
                category: 'security',
                question: 'Is my data secure?',
                answer: 'Yes, we use enterprise-grade security measures including 256-bit SSL encryption, regular security audits, SOC 2 compliance, and data backup to multiple geographic locations. We never share your data with third parties.',
                links: [
                    { text: 'Security whitepaper', url: '#' },
                    { text: 'Privacy policy', url: '#' }
                ]
            },
            {
                id: 10,
                category: 'security',
                question: 'Where is my data stored?',
                answer: 'Data is stored in secure AWS data centers with 99.9% uptime SLA. You can choose between US, EU, or Asia-Pacific regions during setup. All data centers comply with GDPR and other regional data protection regulations.',
                links: [
                    { text: 'Compliance overview', url: '#' }
                ]
            }
        ]

        const filteredFaqs = computed(() => {
            return faqs.filter(faq => faq.category === activeCategory.value)
        })

        const toggleFaq = (id: number) => {
            activeFaq.value = activeFaq.value === id ? null : id
        }

        return {
            categories,
            faqs,
            activeCategory,
            activeFaq,
            filteredFaqs,
            toggleFaq
        }
    }
})
</script>

<style lang="scss" scoped>
.faq-section {
    padding: 100px 0;
    background: linear-gradient(to bottom, #ffffff, #f8fafc);

    .section-header {
        margin-bottom: 60px;

        .section-badge {
            display: inline-block;
            background: rgba(139, 92, 246, 0.1);
            color: #8b5cf6;
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

    .faq-categories {
        margin-bottom: 3rem;

        .categories-tabs {
            display: flex;
            flex-wrap: wrap;
            gap: 0.5rem;
            justify-content: center;
        }

        .category-tab {
            display: flex;
            align-items: center;
            gap: 0.5rem;
            padding: 0.75rem 1.5rem;
            background: white;
            border: 1px solid #e5e7eb;
            border-radius: 50px;
            color: var(--textColor);
            font-weight: 500;
            cursor: pointer;
            transition: all 0.3s ease;

            i {
                font-size: 1.125rem;
            }

            &:hover {
                border-color: var(--primaryColor);
                color: var(--primaryColor);
            }

            &.active {
                background: var(--primaryColor);
                border-color: var(--primaryColor);
                color: white;
            }
        }
    }

    .faq-accordion {
        max-width: 800px;
        margin: 0 auto 80px;

        .accordion-item {
            background: white;
            border-radius: 12px;
            margin-bottom: 1rem;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
            border: 1px solid rgba(0, 0, 0, 0.05);
            overflow: hidden;
            transition: all 0.3s ease;

            &:hover {
                box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            }

            &.active {
                border-color: var(--primaryColor);
                box-shadow: 0 10px 30px rgba(102, 126, 234, 0.15);
            }
        }

        .accordion-header {
            width: 100%;
            padding: 1.5rem;
            display: flex;
            justify-content: space-between;
            align-items: center;
            background: none;
            border: none;
            cursor: pointer;
            text-align: left;

            .accordion-title {
                flex: 1;

                .faq-question {
                    font-size: 1.125rem;
                    font-weight: 600;
                    color: var(--titleColor);
                    line-height: 1.4;
                }
            }

            .accordion-icon {
                width: 32px;
                height: 32px;
                background: #f3f4f6;
                border-radius: 50%;
                display: flex;
                align-items: center;
                justify-content: center;
                flex-shrink: 0;
                transition: all 0.3s ease;

                i {
                    font-size: 1.25rem;
                    color: var(--textColor);
                }
            }

            &:hover {
                .accordion-icon {
                    background: var(--primaryColor);

                    i {
                        color: white;
                    }
                }
            }
        }

        .accordion-content {
            max-height: 0;
            overflow: hidden;
            transition: max-height 0.3s ease;

            .faq-answer {
                padding: 0 1.5rem 1.5rem;
                color: var(--textColor);
                line-height: 1.6;

                p {
                    margin-bottom: 1rem;

                    &:last-child {
                        margin-bottom: 0;
                    }
                }
            }

            .faq-links {
                display: flex;
                gap: 1rem;
                flex-wrap: wrap;
                padding: 0 1.5rem 1.5rem;

                .faq-link {
                    display: flex;
                    align-items: center;
                    gap: 0.5rem;
                    color: var(--primaryColor);
                    text-decoration: none;
                    font-weight: 500;
                    font-size: 0.875rem;

                    &:hover {
                        text-decoration: underline;
                    }

                    i {
                        font-size: 1rem;
                    }
                }
            }
        }

        .accordion-item.active {
            .accordion-content {
                max-height: 1000px;
            }

            .accordion-icon {
                background: var(--primaryColor);

                i {
                    color: white;
                }
            }
        }
    }

    .faq-cta {
        .cta-card {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border-radius: 24px;
            padding: 4rem;
            display: grid;
            grid-template-columns: auto 1fr;
            gap: 3rem;
            align-items: center;
            color: white;

            @media (max-width: 768px) {
                grid-template-columns: 1fr;
                text-align: center;
                padding: 2rem;
                gap: 2rem;
            }

            .cta-icon {
                i {
                    font-size: 4rem;
                    opacity: 0.8;

                    @media (max-width: 768px) {
                        font-size: 3rem;
                    }
                }
            }

            .cta-content {
                h3 {
                    font-size: 2rem;
                    font-weight: 700;
                    margin-bottom: 1rem;
                }

                p {
                    font-size: 1.125rem;
                    opacity: 0.9;
                    margin-bottom: 2rem;
                    line-height: 1.6;
                }

                .cta-buttons {
                    display: flex;
                    gap: 1rem;
                    flex-wrap: wrap;

                    .btn {
                        padding: 0.75rem 1.5rem;

                        &.btn-primary {
                            background: white;
                            color: var(--primaryColor);

                            &:hover {
                                background: rgba(255, 255, 255, 0.9);
                            }
                        }

                        &.btn-outline {
                            background: transparent;
                            border: 2px solid white;
                            color: white;

                            &:hover {
                                background: white;
                                color: var(--primaryColor);
                            }
                        }

                        i {
                            margin-right: 0.5rem;
                        }
                    }
                }
            }
        }
    }
}
</style>