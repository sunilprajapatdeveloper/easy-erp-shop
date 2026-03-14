<template>
    <div class="onboarding-layout">
        <main class="onboarding-content">
            <div class="container">
                <div class="content-wrapper">
                    <!-- Left side - Preview/Info -->
                    <div class="preview-panel">
                        <div class="preview-card">
                            <h3 class="preview-title">Setup Progress</h3>

                            <!-- Current step highlight -->
                            <div class="current-step-card">
                                <div class="step-icon" :class="`step-${currentStep}`">
                                    <i :class="currentStepIcon"></i>
                                </div>
                                <div class="step-details">
                                    <span class="step-label">Current step</span>
                                    <h4>{{ currentStepTitle }}</h4>
                                    <p>{{ currentStepDescription }}</p>
                                </div>
                            </div>

                            <!-- Progress checklist -->
                            <div class="progress-list">
                                <h4>Your journey</h4>
                                <ul>
                                    <li v-for="(step, index) in steps" :key="index" :class="{
                                        completed: currentStep > index + 1,
                                        active: currentStep === index + 1,
                                    }">
                                        <div class="step-marker">
                                            <i :class="getStepIcon(index + 1)"></i>
                                        </div>
                                        <div class="step-info">
                                            <span class="step-name">{{ step.title }}</span>
                                            <span class="step-desc">{{ step.description }}</span>
                                        </div>
                                    </li>
                                </ul>
                            </div>

                            <!-- Support section -->
                            <div class="support-card">
                                <div class="support-icon">
                                    <i class="ri-question-line"></i>
                                </div>
                                <div class="support-text">
                                    <h4>Need assistance?</h4>
                                    <p>Our support team is ready to help.</p>
                                </div>
                                <button class="btn btn-outline btn-sm">
                                    <i class="ri-chat-3-line"></i> Chat now
                                </button>
                            </div>
                        </div>
                    </div>

                    <!-- Right side - Form content -->
                    <div class="form-panel">
                        <div class="form-card">
                            <div class="form-header">
                                <div class="step-indicator">
                                    Step {{ currentStep }} of {{ steps.length }}
                                </div>
                                <h1>{{ currentStepTitle }}</h1>
                                <p class="form-description">{{ currentStepDescription }}</p>
                            </div>

                            <div class="form-body">
                                <slot></slot>
                            </div>

                            <div class="form-actions">
                                <button v-if="showBackButton" class="btn btn-outline" @click="goBack">
                                    <i class="ri-arrow-left-line"></i> Back
                                </button>
                                <div class="action-group">
                                    <button v-if="canSkip" class="btn btn-text skip-btn" @click="skipStep">
                                        Skip for now
                                    </button>
                                    <button class="btn btn-primary continue-btn" :disabled="!canContinue"
                                        @click="continueToNext">
                                        {{ isLastStep ? 'Launch Dashboard' : 'Continue' }}
                                        <i class="ri-arrow-right-line"></i>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </div>
</template>

<script lang="ts">
import { defineComponent, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useOnboardingStore } from '@/stores/onboardingStore'
import { useUserStore } from '@/stores/userStore'

export default defineComponent({
    name: 'OnboardingLayout',
    props: {
        currentStep: { type: Number, required: true },
        steps: {
            type: Array as () => Array<{ title: string; description: string }>,
            required: true,
        },
        canContinue: { type: Boolean, default: true },
        canSkip: { type: Boolean, default: false },
        showBackButton: { type: Boolean, default: true },
    },
    emits: ['continue', 'back', 'skip'],
    setup(props, { emit }) {
        const router = useRouter()
        const onboardingStore = useOnboardingStore()
        const userStore = useUserStore()

        const currentStepTitle = computed(
            () => props.steps[props.currentStep - 1]?.title || ''
        )
        const currentStepDescription = computed(
            () => props.steps[props.currentStep - 1]?.description || ''
        )

        const currentStepIcon = computed(() => {
            const icons = [
                'ri-mail-check-line',
                'ri-user-3-line',
                'ri-building-line',
                'ri-store-line',
                'ri-bank-card-line',
                'ri-warehouse-line',
            ]
            return icons[props.currentStep - 1] || 'ri-question-line'
        })

        const getStepIcon = (step: number) => {
            if (step < props.currentStep) return 'ri-checkbox-circle-fill'
            if (step === props.currentStep) return 'ri-record-circle-line'
            return 'ri-checkbox-blank-circle-line'
        }

        const isLastStep = computed(() => props.currentStep === props.steps.length)

        const continueToNext = () => emit('continue')
        const goBack = () => emit('back')
        const skipStep = () => emit('skip')

        const saveAndExit = async () => {
            await onboardingStore.saveProgress()
            router.push('/')
        }

        return {
            currentStepTitle,
            currentStepDescription,
            currentStepIcon,
            getStepIcon,
            isLastStep,
            continueToNext,
            goBack,
            skipStep,
            saveAndExit,
            userStore,
        }
    },
})
</script>

<style lang="scss" scoped>
/* ============================================
   Design System Variables
   (Override these to match your brand)
============================================ */
.onboarding-layout {
    --color-primary: #4f46e5; // Indigo
    --color-primary-light: #818cf8;
    --color-primary-dark: #3730a3;
    --color-success: #10b981; // Emerald
    --color-warning: #f59e0b;
    --color-danger: #ef4444;
    --color-text: #1e293b; // Slate-800
    --color-text-light: #475569; // Slate-600
    --color-text-muted: #64748b; // Slate-500
    --color-background: #f8fafc; // Slate-50
    --color-surface: #ffffff;
    --color-border: #e2e8f0; // Slate-200
    --shadow-sm: 0 1px 3px rgba(0, 0, 0, 0.05), 0 1px 2px rgba(0, 0, 0, 0.1);
    --shadow-md: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
    --shadow-lg: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
    --shadow-xl: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
    --radius-sm: 0.375rem;
    --radius-md: 0.5rem;
    --radius-lg: 0.75rem;
    --radius-xl: 1rem;
    --font-sans: 'Inter', system-ui, -apple-system, sans-serif;
    --transition: all 0.2s ease;
}

/* ============================================
   Base Layout
============================================ */
.onboarding-layout {
    min-height: 100vh;
    background: linear-gradient(145deg, #f1f5f9 0%, #e2e8f0 100%);
    font-family: var(--font-sans);
    color: var(--color-text);

    .onboarding-content {
        padding: 2rem 1.5rem;

        .container {
            max-width: 1280px;
            margin: 0 auto;
        }

        .content-wrapper {
            display: grid;
            grid-template-columns: 320px 1fr;
            gap: 2rem;
            min-height: calc(100vh - 4rem);
            align-items: start;
        }
    }
}

/* ============================================
   Left Preview Panel
============================================ */
.preview-panel {
    position: sticky;
    top: 2rem;

    .preview-card {
        background: var(--color-surface);
        border-radius: var(--radius-xl);
        padding: 1.75rem;
        box-shadow: var(--shadow-lg);
        border: 1px solid var(--color-border);
        display: flex;
        flex-direction: column;
        gap: 2rem;

        .preview-title {
            font-size: 1.125rem;
            font-weight: 600;
            color: var(--color-text);
            margin: 0;
            letter-spacing: -0.01em;
        }

        // Current step highlight
        .current-step-card {
            background: linear-gradient(135deg, var(--color-primary-light) 0%, var(--color-primary) 100%);
            border-radius: var(--radius-lg);
            padding: 1.25rem;
            display: flex;
            align-items: flex-start;
            gap: 1rem;
            color: white;

            .step-icon {
                width: 48px;
                height: 48px;
                background: rgba(255, 255, 255, 0.2);
                border-radius: 50%;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 1.5rem;
                backdrop-filter: blur(2px);
            }

            .step-details {
                flex: 1;

                .step-label {
                    font-size: 0.75rem;
                    text-transform: uppercase;
                    letter-spacing: 0.05em;
                    opacity: 0.8;
                    font-weight: 500;
                }

                h4 {
                    font-size: 1.125rem;
                    font-weight: 600;
                    margin: 0.25rem 0 0.125rem;
                    line-height: 1.3;
                    color: white;
                }

                p {
                    font-size: 0.875rem;
                    opacity: 0.9;
                    margin: 0;
                    color: white;
                }
            }
        }

        // Progress list
        .progress-list {
            h4 {
                font-size: 0.875rem;
                font-weight: 600;
                color: var(--color-text);
                margin-bottom: 1rem;
                text-transform: uppercase;
                letter-spacing: 0.05em;
            }

            ul {
                list-style: none;
                padding: 0;
                margin: 0;

                li {
                    display: flex;
                    align-items: flex-start;
                    gap: 0.75rem;
                    padding: 0.75rem 0;
                    border-bottom: 1px solid var(--color-border);

                    &:last-child {
                        border-bottom: none;
                    }

                    .step-marker {
                        width: 24px;
                        height: 24px;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        color: var(--color-text-muted);
                        font-size: 1.25rem;
                    }

                    .step-info {
                        flex: 1;

                        .step-name {
                            display: block;
                            font-size: 0.875rem;
                            font-weight: 500;
                            color: var(--color-text);
                            line-height: 1.4;
                        }

                        .step-desc {
                            display: block;
                            font-size: 0.75rem;
                            color: var(--color-text-muted);
                        }
                    }

                    &.completed {
                        .step-marker {
                            color: var(--color-success);
                        }

                        .step-name {
                            color: var(--color-text);
                        }
                    }

                    &.active {
                        .step-marker {
                            color: var(--color-primary);
                        }

                        .step-name {
                            color: var(--color-primary);
                            font-weight: 600;
                        }
                    }
                }
            }
        }

        // Support card
        .support-card {
            background: var(--color-background);
            border-radius: var(--radius-lg);
            padding: 1rem;
            display: flex;
            align-items: center;
            gap: 0.75rem;
            border: 1px solid var(--color-border);

            .support-icon {
                width: 40px;
                height: 40px;
                background: var(--color-primary-light);
                border-radius: 50%;
                display: flex;
                align-items: center;
                justify-content: center;
                color: white;
                flex-shrink: 0;
            }

            .support-text {
                flex: 1;

                h4 {
                    font-size: 0.875rem;
                    font-weight: 600;
                    margin: 0 0 0.125rem;
                    color: var(--color-text);
                }

                p {
                    font-size: 0.75rem;
                    color: var(--color-text-muted);
                    margin: 0;
                }
            }

            .btn-sm {
                padding: 0.375rem 0.75rem;
                font-size: 0.75rem;
                white-space: nowrap;
            }
        }
    }
}

/* ============================================
   Right Form Panel
============================================ */
.form-panel {
    .form-card {
        background: var(--color-surface);
        border-radius: var(--radius-xl);
        padding: 2.5rem;
        box-shadow: var(--shadow-lg);
        border: 1px solid var(--color-border);
        display: flex;
        flex-direction: column;
        min-height: 100%;

        .form-header {
            margin-bottom: 2rem;

            .step-indicator {
                display: inline-block;
                background: var(--color-primary-light);
                color: white;
                font-size: 0.75rem;
                font-weight: 600;
                padding: 0.25rem 0.75rem;
                border-radius: 2rem;
                margin-bottom: 1rem;
                letter-spacing: 0.02em;
            }

            h1 {
                font-size: 2.25rem;
                font-weight: 700;
                color: var(--color-text);
                margin: 0 0 0.5rem;
                line-height: 1.2;
            }

            .form-description {
                font-size: 1rem;
                color: var(--color-text-light);
                margin: 0;
            }
        }

        .form-body {
            flex: 1;
            margin-bottom: 2rem;
        }

        .form-actions {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding-top: 1.5rem;
            border-top: 1px solid var(--color-border);

            .action-group {
                display: flex;
                align-items: center;
                gap: 1rem;
            }

            .skip-btn {
                background: transparent;
                border: none;
                color: var(--color-text-muted);
                font-size: 0.875rem;
                cursor: pointer;
                padding: 0.5rem 1rem;
                transition: color 0.2s;

                &:hover {
                    color: var(--color-primary);
                }

                &:focus-visible {
                    outline: 2px solid var(--color-primary);
                    outline-offset: 2px;
                }
            }

            .continue-btn {
                min-width: 160px;
                display: flex;
                align-items: center;
                justify-content: center;
                gap: 0.5rem;
                padding: 0.75rem 1.5rem;
                font-weight: 600;
                transition: all 0.2s;
                background: linear-gradient(135deg, var(--color-primary) 0%, var(--color-primary-dark) 100%);
                border: none;
                color: white;

                &:hover:not(:disabled) {
                    transform: translateY(-2px);
                    box-shadow: 0 10px 20px -5px var(--color-primary);
                }

                &:disabled {
                    opacity: 0.5;
                    cursor: not-allowed;
                }
            }
        }
    }
}

/* ============================================
   Shared Button Styles (fallback)
============================================ */
.btn {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    gap: 0.5rem;
    padding: 0.625rem 1.25rem;
    border-radius: var(--radius-md);
    font-weight: 500;
    font-size: 0.875rem;
    line-height: 1;
    cursor: pointer;
    transition: var(--transition);
    border: 1px solid transparent;

    &:focus-visible {
        outline: 2px solid var(--color-primary);
        outline-offset: 2px;
    }

    &.btn-outline {
        background: transparent;
        border: 1px solid var(--color-border);
        color: var(--color-text);

        &:hover {
            background: var(--color-background);
            border-color: var(--color-primary);
        }
    }

    &.btn-primary {
        background: linear-gradient(135deg, var(--color-primary) 0%, var(--color-primary-dark) 100%);
        color: white;
        border: none;

        &:hover:not(:disabled) {
            background: linear-gradient(135deg, var(--color-primary-dark) 0%, var(--color-primary) 100%);
        }
    }
}

/* ============================================
   Responsive Design
============================================ */
@media (max-width: 1024px) {
    .onboarding-layout .onboarding-content .content-wrapper {
        grid-template-columns: 1fr;
    }

    .preview-panel {
        display: none; // Hide on tablet/mobile for better focus on form
    }
}

@media (max-width: 768px) {
    .onboarding-layout .onboarding-content {
        padding: 1rem;

        .form-panel .form-card {
            padding: 1.5rem;

            .form-header h1 {
                font-size: 1.75rem;
            }

            .form-actions {
                flex-direction: column-reverse;
                gap: 1rem;

                .action-group {
                    flex-direction: column-reverse;
                    width: 100%;

                    .skip-btn,
                    .continue-btn {
                        width: 100%;
                    }
                }

                .btn-outline {
                    width: 100%;
                }
            }
        }
    }
}

@media (max-width: 480px) {
    .onboarding-layout .onboarding-content .form-panel .form-card {
        padding: 1.25rem;

        .form-header h1 {
            font-size: 1.5rem;
        }
    }
}
</style>