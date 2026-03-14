<template>
    <section class="hero-section">
        <!-- Background Elements -->
        <div class="hero-bg-elements">
            <div class="bg-gradient"></div>
            <div class="bg-dots"></div>
            <div class="bg-blob-1"></div>
            <div class="bg-blob-2"></div>
        </div>

        <div class="container">
            <div class="hero-content">
                <!-- Badge -->
                <div class="hero-badge animate-on-scroll">
                    <span class="badge-text">✨ Trusted by 10,000+ businesses</span>
                </div>

                <!-- Main Headline -->
                <h1 class="hero-headline animate-on-scroll delay-1">
                    Complete <span class="gradient-text">POS & ERP</span> Solution
                    <br>for Multi-Branch Businesses
                </h1>

                <!-- Subheadline -->
                <p class="hero-subheadline animate-on-scroll delay-2">
                    Manage inventory, sales, purchases, and accounting across multiple warehouses
                    in one powerful platform. Everything you need to run and grow your business.
                </p>

                <!-- CTA Buttons -->
                <div class="hero-cta animate-on-scroll delay-3">
                    <router-link to="/signup" class="btn btn-primary btn-xl">
                        <span>Start 14-Day Free Trial</span>
                        <i class="ri-arrow-right-line"></i>
                    </router-link>
                    <a href="#demo" class="btn btn-outline btn-xl">
                        <i class="ri-play-circle-line"></i>
                        Watch Demo
                    </a>
                </div>

                <!-- Trial Note -->
                <div class="trial-note animate-on-scroll delay-4">
                    <i class="ri-checkbox-circle-line"></i>
                    <span>No credit card required • Cancel anytime</span>
                </div>

                <!-- Hero Image/Preview -->
                <div class="hero-preview animate-on-scroll delay-4">
                    <div class="preview-wrapper">
                        <div class="preview-frame">
                            <!-- Mock Dashboard -->
                            <div class="dashboard-mock">
                                <div class="mock-header">
                                    <div class="mock-nav">
                                        <div class="mock-nav-item active">Dashboard</div>
                                        <div class="mock-nav-item">POS</div>
                                        <div class="mock-nav-item">Inventory</div>
                                        <div class="mock-nav-item">Reports</div>
                                    </div>
                                </div>
                                <div class="mock-content">
                                    <div class="mock-stats">
                                        <div class="mock-stat-card">
                                            <div class="stat-label">Today's Sales</div>
                                            <div class="stat-value">$12,458</div>
                                        </div>
                                        <div class="mock-stat-card">
                                            <div class="stat-label">Orders</div>
                                            <div class="stat-value">84</div>
                                        </div>
                                        <div class="mock-stat-card">
                                            <div class="stat-label">Inventory Value</div>
                                            <div class="stat-value">$245,785</div>
                                        </div>
                                    </div>
                                    <div class="mock-chart">
                                        <div class="chart-bar" style="height: 60%"></div>
                                        <div class="chart-bar" style="height: 80%"></div>
                                        <div class="chart-bar" style="height: 45%"></div>
                                        <div class="chart-bar" style="height: 90%"></div>
                                        <div class="chart-bar" style="height: 70%"></div>
                                    </div>
                                </div>
                            </div>

                            <!-- POS Mock Overlay -->
                            <div class="pos-mock-overlay">
                                <div class="pos-screen">
                                    <div class="pos-header">
                                        <div class="pos-store">Main Store</div>
                                        <div class="pos-time">2:30 PM</div>
                                    </div>
                                    <div class="pos-cart">
                                        <div class="cart-item">
                                            <div class="item-name">iPhone 14 Pro</div>
                                            <div class="item-price">$999.00</div>
                                        </div>
                                        <div class="cart-item">
                                            <div class="item-name">AirPods Pro</div>
                                            <div class="item-price">$249.00</div>
                                        </div>
                                        <div class="cart-total">
                                            <span>Total</span>
                                            <span>$1,248.00</span>
                                        </div>
                                    </div>
                                    <div class="pos-keypad">
                                        <button class="pos-key">1</button>
                                        <button class="pos-key">2</button>
                                        <button class="pos-key">3</button>
                                        <button class="pos-key">4</button>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Floating Elements -->
                        <div class="floating-card card-1">
                            <div class="card-icon">📱</div>
                            <div class="card-text">Mobile POS App</div>
                        </div>
                        <div class="floating-card card-2">
                            <div class="card-icon">📊</div>
                            <div class="card-text">Real-time Analytics</div>
                        </div>
                        <div class="floating-card card-3">
                            <div class="card-icon">🔄</div>
                            <div class="card-text">Auto Sync</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({
    name: 'HeroSection'
})
</script>

<style lang="scss" scoped>
.hero-section {
    position: relative;
    padding: 180px 0 100px;
    overflow: hidden;
    background: linear-gradient(135deg, #f5f7ff 0%, #f0f2ff 100%);

    @media (max-width: 768px) {
        padding: 140px 0 60px;
    }

    .hero-bg-elements {
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        overflow: hidden;
        z-index: 0;

        .bg-gradient {
            position: absolute;
            top: -200px;
            right: -200px;
            width: 800px;
            height: 800px;
            background: radial-gradient(circle, rgba(102, 126, 234, 0.1) 0%, rgba(102, 126, 234, 0) 70%);
        }

        .bg-dots {
            position: absolute;
            top: 50%;
            left: 10%;
            width: 300px;
            height: 300px;
            background-image: radial-gradient(rgba(102, 126, 234, 0.1) 2px, transparent 2px);
            background-size: 20px 20px;
        }

        .bg-blob-1 {
            position: absolute;
            bottom: -100px;
            left: -100px;
            width: 400px;
            height: 400px;
            background: linear-gradient(135deg, rgba(118, 75, 162, 0.1) 0%, rgba(118, 75, 162, 0) 100%);
            border-radius: 50%;
            filter: blur(40px);
        }

        .bg-blob-2 {
            position: absolute;
            top: 100px;
            right: -200px;
            width: 500px;
            height: 500px;
            background: linear-gradient(135deg, rgba(245, 158, 11, 0.1) 0%, rgba(245, 158, 11, 0) 100%);
            border-radius: 50%;
            filter: blur(40px);
        }
    }

    .hero-content {
        position: relative;
        z-index: 1;
        text-align: center;
        max-width: 900px;
        margin: 0 auto;
    }

    .hero-badge {
        display: inline-flex;
        align-items: center;
        background: rgba(255, 255, 255, 0.9);
        backdrop-filter: blur(10px);
        border: 1px solid rgba(102, 126, 234, 0.2);
        border-radius: 50px;
        padding: 8px 20px;
        margin-bottom: 2rem;

        .badge-text {
            font-size: 0.875rem;
            font-weight: 500;
            color: var(--primaryColor);
        }
    }

    .hero-headline {
        font-size: 3.5rem;
        font-weight: 800;
        line-height: 1.2;
        margin-bottom: 1.5rem;
        color: var(--titleColor);

        @media (max-width: 768px) {
            font-size: 2.5rem;
        }

        @media (max-width: 576px) {
            font-size: 2rem;
        }

        .gradient-text {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
        }
    }

    .hero-subheadline {
        font-size: 1.25rem;
        line-height: 1.6;
        color: var(--textColor);
        max-width: 700px;
        margin: 0 auto 2.5rem;

        @media (max-width: 768px) {
            font-size: 1.125rem;
        }
    }

    .hero-cta {
        display: flex;
        gap: 1rem;
        justify-content: center;
        margin-bottom: 1.5rem;
        flex-wrap: wrap;

        .btn {
            padding: 1rem 2rem;
            font-size: 1.125rem;
            font-weight: 600;
            display: flex;
            align-items: center;
            gap: 0.5rem;

            &.btn-primary {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                border: none;
                color: white;
                box-shadow: 0 10px 30px rgba(102, 126, 234, 0.3);
                transition: transform 0.3s ease, box-shadow 0.3s ease;

                &:hover {
                    transform: translateY(-2px);
                    box-shadow: 0 15px 40px rgba(102, 126, 234, 0.4);
                }
            }

            &.btn-outline {
                border: 2px solid var(--primaryColor);
                color: var(--primaryColor);

                &:hover {
                    background: var(--primaryColor);
                    color: white;
                }
            }
        }
    }

    .trial-note {
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 0.5rem;
        color: var(--textColor);
        font-size: 0.875rem;

        i {
            color: #10b981;
        }
    }

    .hero-preview {
        margin-top: 4rem;
        position: relative;

        .preview-wrapper {
            position: relative;
            max-width: 900px;
            margin: 0 auto;
        }

        .preview-frame {
            position: relative;
            background: white;
            border-radius: 20px;
            padding: 20px;
            box-shadow: 0 40px 80px rgba(0, 0, 0, 0.1);
            overflow: hidden;
            border: 1px solid rgba(0, 0, 0, 0.05);
        }

        .dashboard-mock {
            background: #f8fafc;
            border-radius: 12px;
            overflow: hidden;

            .mock-header {
                padding: 1rem 1.5rem;
                background: white;
                border-bottom: 1px solid #e5e7eb;

                .mock-nav {
                    display: flex;
                    gap: 2rem;

                    .mock-nav-item {
                        padding: 0.5rem 0;
                        color: #6b7280;
                        font-weight: 500;
                        cursor: pointer;
                        position: relative;

                        &.active {
                            color: var(--primaryColor);

                            &::after {
                                content: '';
                                position: absolute;
                                bottom: 0;
                                left: 0;
                                right: 0;
                                height: 2px;
                                background: var(--primaryColor);
                            }
                        }
                    }
                }
            }

            .mock-content {
                padding: 1.5rem;

                .mock-stats {
                    display: grid;
                    grid-template-columns: repeat(3, 1fr);
                    gap: 1rem;
                    margin-bottom: 2rem;

                    .mock-stat-card {
                        background: white;
                        padding: 1rem;
                        border-radius: 8px;
                        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);

                        .stat-label {
                            font-size: 0.875rem;
                            color: #6b7280;
                            margin-bottom: 0.5rem;
                        }

                        .stat-value {
                            font-size: 1.5rem;
                            font-weight: 700;
                            color: var(--titleColor);
                        }
                    }
                }

                .mock-chart {
                    display: flex;
                    align-items: flex-end;
                    gap: 1rem;
                    height: 100px;
                    padding: 1rem;
                    background: white;
                    border-radius: 8px;

                    .chart-bar {
                        flex: 1;
                        background: linear-gradient(to top, #667eea, #764ba2);
                        border-radius: 4px 4px 0 0;
                        min-height: 10px;
                    }
                }
            }
        }

        .pos-mock-overlay {
            position: absolute;
            bottom: -20px;
            right: -20px;
            width: 300px;
            background: white;
            border-radius: 16px;
            padding: 1rem;
            box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);
            border: 1px solid rgba(0, 0, 0, 0.05);
            transform: rotate(3deg);

            .pos-screen {
                .pos-header {
                    display: flex;
                    justify-content: space-between;
                    padding-bottom: 0.5rem;
                    border-bottom: 1px solid #e5e7eb;
                    margin-bottom: 1rem;

                    .pos-store {
                        font-weight: 600;
                        color: var(--titleColor);
                    }

                    .pos-time {
                        color: #6b7280;
                        font-size: 0.875rem;
                    }
                }

                .pos-cart {
                    margin-bottom: 1rem;

                    .cart-item {
                        display: flex;
                        justify-content: space-between;
                        padding: 0.5rem 0;
                        border-bottom: 1px solid #f3f4f6;

                        .item-name {
                            color: #4b5563;
                        }

                        .item-price {
                            font-weight: 600;
                            color: var(--titleColor);
                        }
                    }

                    .cart-total {
                        display: flex;
                        justify-content: space-between;
                        padding-top: 0.5rem;
                        font-weight: 700;
                        color: var(--titleColor);
                    }
                }

                .pos-keypad {
                    display: grid;
                    grid-template-columns: repeat(4, 1fr);
                    gap: 0.5rem;

                    .pos-key {
                        aspect-ratio: 1;
                        border: 1px solid #e5e7eb;
                        background: #f9fafb;
                        border-radius: 8px;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        font-weight: 600;
                        color: var(--titleColor);
                        cursor: pointer;
                        transition: background 0.3s ease;

                        &:hover {
                            background: #f3f4f6;
                        }
                    }
                }
            }
        }

        .floating-card {
            position: absolute;
            background: white;
            padding: 0.75rem 1rem;
            border-radius: 12px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            display: flex;
            align-items: center;
            gap: 0.5rem;
            animation: float 3s ease-in-out infinite;
            z-index: 2;

            &.card-1 {
                top: -20px;
                left: -20px;
                animation-delay: 0s;
            }

            &.card-2 {
                top: 50%;
                right: -40px;
                transform: translateY(-50%);
                animation-delay: 0.5s;
            }

            &.card-3 {
                bottom: 40px;
                left: 10%;
                animation-delay: 1s;
            }

            .card-icon {
                font-size: 1.25rem;
            }

            .card-text {
                font-size: 0.875rem;
                font-weight: 500;
                white-space: nowrap;
            }
        }
    }
}

@keyframes float {

    0%,
    100% {
        transform: translateY(0);
    }

    50% {
        transform: translateY(-10px);
    }
}
</style>