<template>
    <section id="pos" class="pos-preview-section">
        <div class="container">
            <div class="pos-content">
                <!-- Left: POS Preview -->
                <div class="pos-preview animate-on-scroll">
                    <div class="preview-container">
                        <!-- Main POS Screen -->
                        <div class="pos-screen">
                            <!-- Header -->
                            <div class="pos-header">
                                <div class="store-info">
                                    <div class="store-name">Main Store</div>
                                    <div class="store-status">
                                        <span class="status-dot"></span>
                                        Online
                                    </div>
                                </div>
                                <div class="pos-time">3:45 PM</div>
                            </div>

                            <!-- Cart Items -->
                            <div class="pos-cart">
                                <div class="cart-header">
                                    <h4>Current Sale</h4>
                                    <div class="cart-actions">
                                        <button class="cart-action-btn">
                                            <i class="ri-delete-bin-line"></i>
                                        </button>
                                        <button class="cart-action-btn">
                                            <i class="ri-add-circle-line"></i>
                                        </button>
                                    </div>
                                </div>

                                <div class="cart-items">
                                    <div class="cart-item" v-for="item in cartItems" :key="item.id">
                                        <div class="item-info">
                                            <div class="item-name">{{ item.name }}</div>
                                            <div class="item-sku">{{ item.sku }}</div>
                                        </div>
                                        <div class="item-quantity">
                                            <button class="qty-btn">−</button>
                                            <span>{{ item.quantity }}</span>
                                            <button class="qty-btn">+</button>
                                        </div>
                                        <div class="item-price">${{ item.price }}</div>
                                    </div>
                                </div>

                                <!-- Cart Summary -->
                                <div class="cart-summary">
                                    <div class="summary-row">
                                        <span>Subtotal</span>
                                        <span>$1,248.00</span>
                                    </div>
                                    <div class="summary-row">
                                        <span>Tax (8.5%)</span>
                                        <span>$106.08</span>
                                    </div>
                                    <div class="summary-row total">
                                        <span>Total</span>
                                        <span>$1,354.08</span>
                                    </div>
                                </div>
                            </div>

                            <!-- Product Grid -->
                            <div class="product-grid">
                                <div class="product-item" v-for="product in products" :key="product.id">
                                    <div class="product-image">
                                        <img :src="product.image" :alt="product.name">
                                    </div>
                                    <div class="product-info">
                                        <div class="product-name">{{ product.name }}</div>
                                        <div class="product-price">${{ product.price }}</div>
                                    </div>
                                </div>
                            </div>

                            <!-- Payment Methods -->
                            <div class="payment-methods">
                                <button class="payment-btn cash">
                                    <i class="ri-money-dollar-circle-line"></i>
                                    <span>Cash</span>
                                </button>
                                <button class="payment-btn card active">
                                    <i class="ri-bank-card-line"></i>
                                    <span>Card</span>
                                </button>
                                <button class="payment-btn mobile">
                                    <i class="ri-smartphone-line"></i>
                                    <span>Mobile</span>
                                </button>
                                <button class="payment-btn other">
                                    <i class="ri-more-line"></i>
                                    <span>Other</span>
                                </button>
                            </div>

                            <!-- Action Buttons -->
                            <div class="action-buttons">
                                <button class="action-btn void">
                                    <i class="ri-close-circle-line"></i>
                                    Void
                                </button>
                                <button class="action-btn hold">
                                    <i class="ri-time-line"></i>
                                    Hold
                                </button>
                                <button class="action-btn pay">
                                    <i class="ri-check-line"></i>
                                    Pay Now
                                </button>
                            </div>
                        </div>

                        <!-- Floating Elements -->
                        <div class="floating-barcode">
                            <div class="barcode-icon">
                                <i class="ri-barcode-line"></i>
                            </div>
                            <div class="barcode-text">Scan Item</div>
                        </div>

                        <div class="floating-receipt">
                            <div class="receipt-icon">
                                <i class="ri-printer-line"></i>
                            </div>
                            <div class="receipt-text">Print Receipt</div>
                        </div>
                    </div>
                </div>

                <!-- Right: Text Content -->
                <div class="pos-text animate-on-scroll delay-1">
                    <div class="section-badge">Point of Sale</div>
                    <h2 class="section-title">Fast, reliable POS for any retail environment</h2>
                    <p class="section-description">
                        Our advanced POS system is designed for speed and reliability.
                        With barcode scanning, multiple payment methods, and offline capability,
                        you can process sales anywhere, anytime.
                    </p>

                    <div class="pos-features">
                        <div class="feature-item">
                            <div class="feature-icon">
                                <i class="ri-wifi-off-line"></i>
                            </div>
                            <div class="feature-content">
                                <h4>Offline Mode</h4>
                                <p>Continue processing sales even without internet connection</p>
                            </div>
                        </div>

                        <div class="feature-item">
                            <div class="feature-icon">
                                <i class="ri-scan-line"></i>
                            </div>
                            <div class="feature-content">
                                <h4>Barcode Scanning</h4>
                                <p>Quickly scan products with built-in barcode and QR code support</p>
                            </div>
                        </div>

                        <div class="feature-item">
                            <div class="feature-icon">
                                <i class="ri-smartphone-line"></i>
                            </div>
                            <div class="feature-content">
                                <h4>Mobile POS</h4>
                                <p>Turn any tablet or phone into a POS terminal</p>
                            </div>
                        </div>

                        <div class="feature-item">
                            <div class="feature-icon">
                                <i class="ri-exchange-dollar-line"></i>
                            </div>
                            <div class="feature-content">
                                <h4>Multiple Payments</h4>
                                <p>Accept cash, cards, mobile payments, and more</p>
                            </div>
                        </div>
                    </div>

                    <div class="pos-stats">
                        <div class="stat">
                            <div class="stat-value">2.5s</div>
                            <div class="stat-label">Average transaction time</div>
                        </div>
                        <div class="stat">
                            <div class="stat-value">99.9%</div>
                            <div class="stat-label">System uptime</div>
                        </div>
                        <div class="stat">
                            <div class="stat-value">24/7</div>
                            <div class="stat-label">Customer support</div>
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
    name: 'PosPreview',
    data() {
        return {
            cartItems: [
                { id: 1, name: 'iPhone 14 Pro', sku: 'IPH14P-256', quantity: 1, price: '999.00' },
                { id: 2, name: 'AirPods Pro', sku: 'APR-2', quantity: 1, price: '249.00' },
                { id: 3, name: 'Apple Watch Series 8', sku: 'AWS8-41', quantity: 1, price: '399.00' }
            ],
            products: [
                { id: 1, name: 'MacBook Pro', price: '1299', image: 'https://via.placeholder.com/60x60' },
                { id: 2, name: 'iPad Air', price: '599', image: 'https://via.placeholder.com/60x60' },
                { id: 3, name: 'iPhone Case', price: '49', image: 'https://via.placeholder.com/60x60' },
                { id: 4, name: 'USB-C Cable', price: '29', image: 'https://via.placeholder.com/60x60' }
            ]
        }
    }
})
</script>

<style lang="scss" scoped>
.pos-preview-section {
    padding: 100px 0;
    background: linear-gradient(to bottom, #ffffff, #f8fafc);

    .pos-content {
        display: grid;
        grid-template-columns: 1fr;
        gap: 4rem;

        @media (min-width: 992px) {
            grid-template-columns: 1fr 1fr;
            align-items: center;
        }
    }

    .pos-preview {
        .preview-container {
            position: relative;
            background: #1a1a2e;
            border-radius: 24px;
            padding: 2rem;
            box-shadow: 0 40px 80px rgba(0, 0, 0, 0.2);
            overflow: hidden;
        }

        .pos-screen {
            background: #0f172a;
            border-radius: 16px;
            overflow: hidden;
            height: 600px;
            display: flex;
            flex-direction: column;
        }

        .pos-header {
            background: #1e293b;
            padding: 1rem 1.5rem;
            display: flex;
            justify-content: space-between;
            align-items: center;
            border-bottom: 1px solid #334155;

            .store-info {
                display: flex;
                align-items: center;
                gap: 0.75rem;

                .store-name {
                    font-weight: 600;
                    color: white;
                    font-size: 1.125rem;
                }

                .store-status {
                    display: flex;
                    align-items: center;
                    gap: 0.5rem;
                    font-size: 0.75rem;
                    color: #10b981;

                    .status-dot {
                        width: 8px;
                        height: 8px;
                        background: #10b981;
                        border-radius: 50%;
                        animation: pulse 2s infinite;
                    }
                }
            }

            .pos-time {
                color: #94a3b8;
                font-size: 0.875rem;
            }
        }

        .pos-cart {
            flex: 1;
            padding: 1.5rem;
            display: flex;
            flex-direction: column;
            border-bottom: 1px solid #334155;

            .cart-header {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 1.5rem;

                h4 {
                    color: white;
                    font-size: 1.125rem;
                    font-weight: 600;
                    margin: 0;
                }

                .cart-actions {
                    display: flex;
                    gap: 0.5rem;

                    .cart-action-btn {
                        width: 32px;
                        height: 32px;
                        border-radius: 6px;
                        background: #334155;
                        border: none;
                        color: #94a3b8;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        cursor: pointer;
                        transition: all 0.3s ease;

                        &:hover {
                            background: #475569;
                            color: white;
                        }

                        i {
                            font-size: 1rem;
                        }
                    }
                }
            }

            .cart-items {
                flex: 1;
                overflow-y: auto;
                margin-bottom: 1.5rem;

                .cart-item {
                    display: flex;
                    align-items: center;
                    padding: 0.75rem;
                    background: #1e293b;
                    border-radius: 8px;
                    margin-bottom: 0.5rem;

                    .item-info {
                        flex: 1;

                        .item-name {
                            color: white;
                            font-weight: 500;
                            margin-bottom: 0.25rem;
                        }

                        .item-sku {
                            color: #94a3b8;
                            font-size: 0.75rem;
                        }
                    }

                    .item-quantity {
                        display: flex;
                        align-items: center;
                        gap: 0.5rem;
                        margin: 0 1rem;

                        .qty-btn {
                            width: 24px;
                            height: 24px;
                            border-radius: 4px;
                            background: #334155;
                            border: none;
                            color: white;
                            display: flex;
                            align-items: center;
                            justify-content: center;
                            cursor: pointer;

                            &:hover {
                                background: #475569;
                            }
                        }

                        span {
                            color: white;
                            min-width: 24px;
                            text-align: center;
                        }
                    }

                    .item-price {
                        color: white;
                        font-weight: 600;
                        min-width: 80px;
                        text-align: right;
                    }
                }
            }

            .cart-summary {
                background: #1e293b;
                padding: 1rem;
                border-radius: 8px;

                .summary-row {
                    display: flex;
                    justify-content: space-between;
                    margin-bottom: 0.5rem;
                    color: #94a3b8;
                    font-size: 0.875rem;

                    &.total {
                        color: white;
                        font-weight: 600;
                        font-size: 1.125rem;
                        margin-top: 0.5rem;
                        padding-top: 0.5rem;
                        border-top: 1px solid #334155;
                    }
                }
            }
        }

        .product-grid {
            display: grid;
            grid-template-columns: repeat(4, 1fr);
            gap: 0.5rem;
            padding: 1rem;
            border-bottom: 1px solid #334155;

            .product-item {
                background: #1e293b;
                border-radius: 8px;
                padding: 0.75rem;
                text-align: center;
                cursor: pointer;
                transition: all 0.3s ease;

                &:hover {
                    background: #334155;
                    transform: translateY(-2px);
                }

                .product-image {
                    width: 60px;
                    height: 60px;
                    background: #334155;
                    border-radius: 6px;
                    margin: 0 auto 0.5rem;
                    overflow: hidden;

                    img {
                        width: 100%;
                        height: 100%;
                        object-fit: cover;
                    }
                }

                .product-info {
                    .product-name {
                        color: white;
                        font-size: 0.75rem;
                        margin-bottom: 0.25rem;
                        white-space: nowrap;
                        overflow: hidden;
                        text-overflow: ellipsis;
                    }

                    .product-price {
                        color: #10b981;
                        font-weight: 600;
                        font-size: 0.875rem;
                    }
                }
            }
        }

        .payment-methods {
            display: grid;
            grid-template-columns: repeat(4, 1fr);
            gap: 0.5rem;
            padding: 1rem;

            .payment-btn {
                padding: 0.75rem;
                background: #1e293b;
                border: none;
                border-radius: 8px;
                color: #94a3b8;
                display: flex;
                flex-direction: column;
                align-items: center;
                gap: 0.5rem;
                cursor: pointer;
                transition: all 0.3s ease;

                i {
                    font-size: 1.25rem;
                }

                span {
                    font-size: 0.75rem;
                }

                &:hover {
                    background: #334155;
                    color: white;
                }

                &.active {
                    background: var(--primaryColor);
                    color: white;
                }
            }
        }

        .action-buttons {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 0.5rem;
            padding: 1rem;

            .action-btn {
                padding: 1rem;
                border: none;
                border-radius: 8px;
                color: white;
                display: flex;
                align-items: center;
                justify-content: center;
                gap: 0.5rem;
                cursor: pointer;
                transition: all 0.3s ease;
                font-weight: 600;

                i {
                    font-size: 1.25rem;
                }

                &.void {
                    background: #ef4444;

                    &:hover {
                        background: #dc2626;
                    }
                }

                &.hold {
                    background: #f59e0b;

                    &:hover {
                        background: #d97706;
                    }
                }

                &.pay {
                    background: #10b981;

                    &:hover {
                        background: #059669;
                    }
                }
            }
        }

        .floating-barcode {
            position: absolute;
            top: -20px;
            right: -20px;
            background: white;
            border-radius: 12px;
            padding: 1rem;
            display: flex;
            align-items: center;
            gap: 0.75rem;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
            animation: float 3s ease-in-out infinite;

            .barcode-icon {
                width: 40px;
                height: 40px;
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                border-radius: 8px;
                display: flex;
                align-items: center;
                justify-content: center;

                i {
                    color: white;
                    font-size: 1.5rem;
                }
            }

            .barcode-text {
                font-weight: 600;
                color: var(--titleColor);
                white-space: nowrap;
            }
        }

        .floating-receipt {
            position: absolute;
            bottom: -20px;
            left: -20px;
            background: white;
            border-radius: 12px;
            padding: 1rem;
            display: flex;
            align-items: center;
            gap: 0.75rem;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
            animation: float 3s ease-in-out infinite 0.5s;

            .receipt-icon {
                width: 40px;
                height: 40px;
                background: linear-gradient(135deg, #10b981 0%, #34d399 100%);
                border-radius: 8px;
                display: flex;
                align-items: center;
                justify-content: center;

                i {
                    color: white;
                    font-size: 1.5rem;
                }
            }

            .receipt-text {
                font-weight: 600;
                color: var(--titleColor);
                white-space: nowrap;
            }
        }
    }

    .pos-text {
        .section-badge {
            display: inline-block;
            background: rgba(59, 130, 246, 0.1);
            color: #3b82f6;
            padding: 8px 20px;
            border-radius: 50px;
            font-size: 0.875rem;
            font-weight: 500;
            margin-bottom: 1rem;
        }

        .section-title {
            font-size: 2.5rem;
            font-weight: 700;
            margin-bottom: 1.5rem;
            color: var(--titleColor);
        }

        .section-description {
            font-size: 1.125rem;
            line-height: 1.6;
            color: var(--textColor);
            margin-bottom: 2rem;
        }

        .pos-features {
            margin-bottom: 3rem;

            .feature-item {
                display: flex;
                align-items: flex-start;
                gap: 1rem;
                margin-bottom: 1.5rem;

                .feature-icon {
                    width: 48px;
                    height: 48px;
                    background: rgba(59, 130, 246, 0.1);
                    border-radius: 12px;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    flex-shrink: 0;

                    i {
                        font-size: 1.25rem;
                        color: #3b82f6;
                    }
                }

                .feature-content {
                    h4 {
                        font-size: 1.125rem;
                        font-weight: 600;
                        margin-bottom: 0.25rem;
                        color: var(--titleColor);
                    }

                    p {
                        color: var(--textColor);
                        line-height: 1.5;
                        font-size: 0.875rem;
                    }
                }
            }
        }

        .pos-stats {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 1.5rem;

            .stat {
                text-align: center;

                .stat-value {
                    font-size: 2rem;
                    font-weight: 700;
                    color: var(--primaryColor);
                    margin-bottom: 0.5rem;
                }

                .stat-label {
                    font-size: 0.875rem;
                    color: var(--textColor);
                }
            }
        }
    }
}

@keyframes pulse {

    0%,
    100% {
        opacity: 1;
    }

    50% {
        opacity: 0.5;
    }
}
</style>