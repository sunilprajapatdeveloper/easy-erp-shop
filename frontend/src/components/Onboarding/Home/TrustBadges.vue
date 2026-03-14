<template>
    <section class="trust-badges-section">
        <div class="container">
            <div class="trust-content">
                <p class="trust-label">Trusted by businesses worldwide</p>

                <div class="badges-grid">
                    <div class="badge-item">
                        <div class="badge-icon">
                            <i class="ri-award-line"></i>
                        </div>
                        <div class="badge-text">
                            <div class="badge-number">10,000+</div>
                            <div class="badge-label">Active Businesses</div>
                        </div>
                    </div>

                    <div class="badge-item">
                        <div class="badge-icon">
                            <i class="ri-global-line"></i>
                        </div>
                        <div class="badge-text">
                            <div class="badge-number">50+</div>
                            <div class="badge-label">Countries</div>
                        </div>
                    </div>

                    <div class="badge-item">
                        <div class="badge-icon">
                            <i class="ri-line-chart-line"></i>
                        </div>
                        <div class="badge-text">
                            <div class="badge-number">$2B+</div>
                            <div class="badge-label">Annual Sales Processed</div>
                        </div>
                    </div>

                    <div class="badge-item">
                        <div class="badge-icon">
                            <i class="ri-thumb-up-line"></i>
                        </div>
                        <div class="badge-text">
                            <div class="badge-number">99.9%</div>
                            <div class="badge-label">Uptime</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</template>

<style lang="scss" scoped>
.trust-badges-section {
    padding: 60px 0;
    background: white;
    border-top: 1px solid rgba(0, 0, 0, 0.05);
    border-bottom: 1px solid rgba(0, 0, 0, 0.05);

    .trust-content {
        text-align: center;
    }

    .trust-label {
        font-size: 0.875rem;
        color: var(--textColor);
        text-transform: uppercase;
        letter-spacing: 1px;
        margin-bottom: 2rem;
        opacity: 0.7;
    }

    .badges-grid {
        display: grid;
        grid-template-columns: repeat(2, 1fr);
        gap: 2rem;
        max-width: 800px;
        margin: 0 auto;

        @media (min-width: 768px) {
            grid-template-columns: repeat(4, 1fr);
        }
    }

    .badge-item {
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: 1rem;

        .badge-icon {
            width: 64px;
            height: 64px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;

            i {
                font-size: 1.5rem;
                color: white;
            }
        }

        .badge-text {
            text-align: center;
        }

        .badge-number {
            font-size: 1.5rem;
            font-weight: 700;
            color: var(--titleColor);
            margin-bottom: 0.25rem;
        }

        .badge-label {
            font-size: 0.875rem;
            color: var(--textColor);
            opacity: 0.8;
        }
    }
}
</style>