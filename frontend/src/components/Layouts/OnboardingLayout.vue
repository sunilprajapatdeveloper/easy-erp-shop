<template>
    <div class="onboarding-layout">
        <main class="onboarding-content">
            <div class="container">
                <div class="content-wrapper">
                    <!-- Left side - Preview/Info -->
                    <div class="preview-container">
                        <div class="preview-card">
                            <h3>What you're setting up:</h3>
                            <div class="preview-content">
                                <!-- Dynamic preview based on current step -->
                                <div class="step-preview">
                                    <div class="preview-item">
                                        <i :class="currentStepIcon"></i>
                                        <div>
                                            <h4>{{ currentStepTitle }}</h4>
                                            <p>{{ currentStepDescription }}</p>
                                        </div>
                                    </div>
                                </div>

                                <!-- Setup checklist -->
                                <div class="setup-checklist">
                                    <h4>Setup Progress</h4>
                                    <ul>
                                        <li v-for="(step, index) in steps" :key="index" :class="{
                                            completed: currentStep > index + 1,
                                            current: currentStep === index + 1
                                        }">
                                            <i :class="getStepIcon(index + 1)"></i>
                                            {{ step.title }}
                                        </li>
                                    </ul>
                                </div>
                            </div>

                            <!-- Help section -->
                            <div class="help-section">
                                <h4><i class="ri-question-line"></i> Need help?</h4>
                                <p>Our team is ready to assist you with setup.</p>
                                <button class="btn btn-text">
                                    <i class="ri-chat-3-line"></i> Chat with support
                                </button>
                            </div>
                        </div>
                    </div>

                    <!-- Right side - Form content -->
                    <div class="form-container">
                        <div class="form-header">
                            <h1>{{ currentStepTitle }}</h1>
                            <p class="subtitle">{{ currentStepDescription }}</p>
                        </div>

                        <div class="form-content">
                            <slot></slot>
                        </div>

                        <!-- Navigation buttons -->
                        <div class="form-navigation">
                            <button v-if="showBackButton" class="btn btn-outline" @click="goBack">
                                <i class="ri-arrow-left-line"></i> Back
                            </button>

                            <div class="navigation-right">
                                <button class="btn btn-text skip-btn" v-if="canSkip" @click="skipStep">
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
        </main>
    </div>
</template>

<script lang="ts">
import { defineComponent, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useOnboardingStore } from '@/stores/onboardingStore'
import { useUserStore } from '@/stores/userStore'
import ProgressStepper from '@/components/Onboarding/ProgressStepper.vue'

export default defineComponent({
    name: 'OnboardingLayout',
    components: {
        ProgressStepper
    },
    props: {
        currentStep: {
            type: Number,
            required: true
        },
        steps: {
            type: Array as () => Array<{ title: string, description: string }>,
            required: true
        },
        canContinue: {
            type: Boolean,
            default: true
        },
        canSkip: {
            type: Boolean,
            default: false
        },
        showBackButton: {
            type: Boolean,
            default: true
        }
    },
    emits: ['continue', 'back', 'skip'],
    setup(props, { emit }) {
        const router = useRouter()
        const onboardingStore = useOnboardingStore()
        const userStore = useUserStore()

        const currentStepTitle = computed(() => {
            return props.steps[props.currentStep - 1]?.title || ''
        })

        const currentStepDescription = computed(() => {
            return props.steps[props.currentStep - 1]?.description || ''
        })

        const currentStepIcon = computed(() => {
            const icons = [
                'ri-mail-check-line',
                'ri-user-3-line',
                'ri-building-line',
                'ri-store-line',
                'ri-bank-card-line',
                'ri-warehouse-line'
            ]
            return icons[props.currentStep - 1] || 'ri-question-line'
        })

        const getStepIcon = (step: number) => {
            if (step < props.currentStep) return 'ri-checkbox-circle-fill'
            if (step === props.currentStep) return 'ri-record-circle-line'
            return 'ri-checkbox-blank-circle-line'
        }

        const isLastStep = computed(() => {
            return props.currentStep === props.steps.length
        })

        const continueToNext = () => {
            emit('continue')
        }

        const goBack = () => {
            emit('back')
        }

        const skipStep = () => {
            emit('skip')
        }

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
            userStore
        }
    }
})
</script>

<style lang="scss" scoped>
.onboarding-layout {
    min-height: 100vh;
    background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);

    .onboarding-content {
        padding: 3rem 0;

        .content-wrapper {
            display: grid;
            grid-template-columns: 400px 1fr;
            gap: 3rem;
            min-height: calc(100vh - 140px);
        }

        .form-container {
            background: white;
            border-radius: 20px;
            padding: 3rem;
            box-shadow: 0 10px 40px rgba(0, 0, 0, 0.08);
            display: flex;
            flex-direction: column;

            .form-header {
                margin-bottom: 2.5rem;

                h1 {
                    font-size: 2.5rem;
                    font-weight: 700;
                    margin-bottom: 0.5rem;
                    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                    -webkit-background-clip: text;
                    -webkit-text-fill-color: transparent;
                }

                .subtitle {
                    color: var(--textColor);
                    font-size: 1.1rem;
                    line-height: 1.6;
                }
            }

            .form-content {
                flex: 1;
            }

            .form-navigation {
                display: flex;
                justify-content: space-between;
                align-items: center;
                padding-top: 2rem;
                border-top: 1px solid rgba(0, 0, 0, 0.06);
                margin-top: auto;

                .navigation-right {
                    display: flex;
                    align-items: center;
                    gap: 1rem;

                    .skip-btn {
                        color: var(--textColor);
                    }

                    .continue-btn {
                        min-width: 160px;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        gap: 0.5rem;
                        padding: 0.875rem 2rem;
                        font-weight: 600;

                        &:disabled {
                            opacity: 0.6;
                            cursor: not-allowed;
                        }
                    }
                }
            }
        }

        .preview-container {
            .preview-card {
                background: white;
                border-radius: 20px;
                padding: 2rem;
                box-shadow: 0 10px 40px rgba(0, 0, 0, 0.08);
                height: 100%;
                display: flex;
                flex-direction: column;

                h3 {
                    font-size: 1.25rem;
                    margin-bottom: 1.5rem;
                    color: var(--titleColor);
                }

                .preview-content {
                    flex: 1;

                    .step-preview {
                        .preview-item {
                            display: flex;
                            align-items: center;
                            gap: 1rem;
                            padding: 1.5rem;
                            background: linear-gradient(135deg, #667eea15 0%, #764ba215 100%);
                            border-radius: 12px;
                            margin-bottom: 1.5rem;

                            i {
                                font-size: 2rem;
                                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                                -webkit-background-clip: text;
                                -webkit-text-fill-color: transparent;
                            }

                            h4 {
                                font-size: 1rem;
                                margin-bottom: 0.25rem;
                            }

                            p {
                                color: var(--textColor);
                                font-size: 0.875rem;
                            }
                        }
                    }

                    .setup-checklist {
                        margin-top: 2rem;

                        h4 {
                            font-size: 1rem;
                            margin-bottom: 1rem;
                            color: var(--titleColor);
                        }

                        ul {
                            list-style: none;
                            padding: 0;

                            li {
                                display: flex;
                                align-items: center;
                                gap: 0.75rem;
                                padding: 0.875rem 0;
                                color: var(--textColor);
                                border-bottom: 1px solid rgba(0, 0, 0, 0.06);

                                i {
                                    font-size: 1.25rem;
                                }

                                &.completed {
                                    color: var(--successColor);

                                    i {
                                        color: var(--successColor);
                                    }
                                }

                                &.current {
                                    color: var(--primaryColor);
                                    font-weight: 500;

                                    i {
                                        color: var(--primaryColor);
                                    }
                                }

                                &:last-child {
                                    border-bottom: none;
                                }
                            }
                        }
                    }
                }

                .help-section {
                    margin-top: 2rem;
                    padding-top: 2rem;
                    border-top: 1px solid rgba(0, 0, 0, 0.06);

                    h4 {
                        display: flex;
                        align-items: center;
                        gap: 0.5rem;
                        margin-bottom: 0.5rem;
                        color: var(--titleColor);

                        i {
                            color: var(--primaryColor);
                        }
                    }

                    p {
                        color: var(--textColor);
                        font-size: 0.875rem;
                        margin-bottom: 1rem;
                    }

                    button {
                        width: 100%;
                        justify-content: center;
                    }
                }
            }
        }
    }
}

@media (max-width: 1200px) {
    .onboarding-content .content-wrapper {
        grid-template-columns: 1fr;
        gap: 2rem;
    }

    .preview-container {
        display: none;
    }
}

@media (max-width: 768px) {
    .onboarding-content {
        padding: 1rem 0;

        .form-container {
            padding: 2rem 1.5rem;
            border-radius: 12px;
            margin: 0 -1rem;

            .form-header h1 {
                font-size: 2rem;
            }
        }
    }
}
</style>