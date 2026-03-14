<template>
    <div class="progress-stepper">
        <div class="stepper-container">
            <div class="stepper-line" :style="lineStyle"></div>

            <div v-for="(step, index) in steps" :key="index" class="stepper-step" :class="getStepClass(index + 1)">
                <div class="step-circle">
                    <span v-if="currentStep > index + 1" class="step-icon">
                        <i class="ri-check-line"></i>
                    </span>
                    <span v-else class="step-number">
                        {{ index + 1 }}
                    </span>
                </div>

                <div class="step-label">
                    <span class="step-title">{{ step.title }}</span>
                    <span class="step-description">{{ step.description }}</span>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, computed } from 'vue'

export default defineComponent({
    name: 'ProgressStepper',
    props: {
        steps: {
            type: Array as () => Array<{ title: string, description: string }>,
            required: true
        },
        currentStep: {
            type: Number,
            required: true
        }
    },
    setup(props) {
        const lineStyle = computed(() => {
            const progress = ((props.currentStep - 1) / (props.steps.length - 1)) * 100
            return { width: `${progress}%` }
        })

        const getStepClass = (stepNumber: number) => {
            if (stepNumber < props.currentStep) {
                return 'completed'
            } else if (stepNumber === props.currentStep) {
                return 'active'
            } else {
                return 'upcoming'
            }
        }

        return {
            lineStyle,
            getStepClass
        }
    }
})
</script>

<style lang="scss" scoped>
.progress-stepper {
    width: 100%;

    .stepper-container {
        display: flex;
        justify-content: space-between;
        position: relative;
        padding: 0 40px;

        .stepper-line {
            position: absolute;
            top: 20px;
            left: 40px;
            right: 40px;
            height: 2px;
            background: linear-gradient(90deg, var(--primaryColor) 0%, var(--primaryColor) 50%, rgba(0, 0, 0, 0.1) 50%, rgba(0, 0, 0, 0.1) 100%);
            z-index: 1;
            transition: width 0.3s ease;

            &::before {
                content: '';
                position: absolute;
                top: -1px;
                left: 0;
                width: 100%;
                height: 4px;
                background: linear-gradient(90deg, var(--primaryColor) 0%, rgba(102, 126, 234, 0.2) 100%);
                border-radius: 2px;
            }
        }

        .stepper-step {
            position: relative;
            z-index: 2;
            flex: 1;
            display: flex;
            flex-direction: column;
            align-items: center;
            text-align: center;

            &:not(:last-child) {
                margin-right: 20px;
            }

            .step-circle {
                width: 40px;
                height: 40px;
                border-radius: 50%;
                background: white;
                border: 2px solid rgba(0, 0, 0, 0.1);
                display: flex;
                align-items: center;
                justify-content: center;
                margin-bottom: 12px;
                transition: all 0.3s ease;

                .step-number {
                    font-size: 16px;
                    font-weight: 600;
                    color: var(--textColor);
                }

                .step-icon {
                    font-size: 20px;
                    color: white;

                    i {
                        display: flex;
                        align-items: center;
                        justify-content: center;
                    }
                }
            }

            .step-label {
                max-width: 120px;

                .step-title {
                    display: block;
                    font-size: 14px;
                    font-weight: 600;
                    color: var(--titleColor);
                    margin-bottom: 4px;
                    line-height: 1.2;
                }

                .step-description {
                    display: block;
                    font-size: 12px;
                    color: var(--textColor);
                    opacity: 0.7;
                    line-height: 1.3;
                }
            }

            &.completed {
                .step-circle {
                    background: var(--primaryColor);
                    border-color: var(--primaryColor);

                    .step-number {
                        color: white;
                    }
                }

                .step-label {
                    .step-title {
                        color: var(--primaryColor);
                    }
                }
            }

            &.active {
                .step-circle {
                    background: white;
                    border-color: var(--primaryColor);
                    box-shadow: 0 0 0 4px rgba(102, 126, 234, 0.2);

                    .step-number {
                        color: var(--primaryColor);
                        font-weight: 700;
                    }
                }

                .step-label {
                    .step-title {
                        color: var(--primaryColor);
                        font-weight: 700;
                    }
                }
            }

            &.upcoming {
                .step-circle {
                    background: white;
                    border-color: rgba(0, 0, 0, 0.1);
                }

                .step-label {

                    .step-title,
                    .step-description {
                        opacity: 0.5;
                    }
                }
            }
        }
    }
}

@media (max-width: 768px) {
    .progress-stepper {
        .stepper-container {
            padding: 0 20px;

            .stepper-line {
                left: 20px;
                right: 20px;
            }

            .stepper-step {
                .step-label {
                    max-width: 80px;

                    .step-title {
                        font-size: 12px;
                    }

                    .step-description {
                        display: none;
                    }
                }
            }
        }
    }
}
</style>