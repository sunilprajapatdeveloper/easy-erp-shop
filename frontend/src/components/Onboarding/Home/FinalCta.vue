<template>
    <section class="final-cta-section">
        <div class="container">
            <div class="cta-content text-center animate-on-scroll">
                <h2 class="cta-title">Ready to transform your business?</h2>
                <p class="cta-description">
                    Join 10,000+ businesses that trust YourERP to manage their operations.
                </p>
                <div class="cta-buttons">
                    <router-link to="/signup" class="btn btn-primary btn-xl">
                        Start Free Trial
                    </router-link>
                    <a href="#" class="btn btn-outline btn-xl">
                        <i class="ri-calendar-line"></i>
                        Book a Demo
                    </a>
                </div>
                <div class="cta-features">
                    <div class="feature">
                        <i class="ri-checkbox-circle-line"></i>
                        <span>14-day free trial</span>
                    </div>
                    <div class="feature">
                        <i class="ri-checkbox-circle-line"></i>
                        <span>No credit card required</span>
                    </div>
                    <div class="feature">
                        <i class="ri-checkbox-circle-line"></i>
                        <span>Cancel anytime</span>
                    </div>
                </div>
            </div>
        </div>
    </section>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({
    name: 'FinalCta'
})
</script>

<style lang="scss" scoped>
.final-cta-section {
    padding: 100px 0;
    background: linear-gradient(135deg, #1a1a2e 0%, #16213e 100%);
    color: white;

    .cta-content {
        max-width: 800px;
        margin: 0 auto;
    }

    .cta-title {
        font-size: 3rem;
        font-weight: 700;
        margin-bottom: 1.5rem;
        line-height: 1.2;

        @media (max-width: 768px) {
            font-size: 2.5rem;
        }
    }

    .cta-description {
        font-size: 1.25rem;
        opacity: 0.9;
        margin-bottom: 2.5rem;
        line-height: 1.6;
    }

    .cta-buttons {
        display: flex;
        gap: 1rem;
        justify-content: center;
        margin-bottom: 2.5rem;
        flex-wrap: wrap;

        .btn {
            padding: 1rem 2rem;
            font-size: 1.125rem;
            font-weight: 600;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 0.5rem;

            &.btn-primary {
                background: white;
                color: var(--primaryColor);

                &:hover {
                    background: rgba(255, 255, 255, 0.9);
                    transform: translateY(-2px);
                }
            }

            &.btn-outline {
                border: 2px solid white;
                color: white;

                &:hover {
                    background: white;
                    color: var(--primaryColor);
                }
            }
        }
    }

    .cta-features {
        display: flex;
        gap: 2rem;
        justify-content: center;
        flex-wrap: wrap;

        .feature {
            display: flex;
            align-items: center;
            gap: 0.5rem;
            font-size: 0.875rem;
            opacity: 0.8;

            i {
                color: #10b981;
                font-size: 1rem;
            }
        }
    }
}
</style>