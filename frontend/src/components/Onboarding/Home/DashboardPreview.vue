<template>
    <section class="dashboard-preview-section">
        <div class="container">
            <div class="dashboard-content">
                <!-- Left: Text Content -->
                <div class="dashboard-text animate-on-scroll">
                    <div class="section-badge">Dashboard</div>
                    <h2 class="section-title">Real-time insights at your fingertips</h2>
                    <p class="section-description">
                        Monitor your business performance with customizable dashboards that show key metrics in
                        real-time.
                        From sales trends to inventory levels, everything you need to make informed decisions.
                    </p>

                    <div class="dashboard-features">
                        <div class="feature-item">
                            <div class="feature-icon">
                                <i class="ri-dashboard-line"></i>
                            </div>
                            <div class="feature-content">
                                <h4>Customizable Dashboards</h4>
                                <p>Arrange widgets to see what matters most to your business</p>
                            </div>
                        </div>

                        <div class="feature-item">
                            <div class="feature-icon">
                                <i class="ri-bar-chart-box-line"></i>
                            </div>
                            <div class="feature-content">
                                <h4>Advanced Analytics</h4>
                                <p>Deep dive into sales, inventory, and customer data</p>
                            </div>
                        </div>

                        <div class="feature-item">
                            <div class="feature-icon">
                                <i class="ri-alarm-warning-line"></i>
                            </div>
                            <div class="feature-content">
                                <h4>Smart Alerts</h4>
                                <p>Get notified about important events and anomalies</p>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Right: Dashboard Preview -->
                <div class="dashboard-preview animate-on-scroll delay-1">
                    <div class="preview-container">
                        <!-- Dashboard Mockup -->
                        <div class="dashboard-mockup">
                            <!-- Header -->
                            <div class="mock-header">
                                <div class="mock-search">
                                    <i class="ri-search-line"></i>
                                    <span>Search reports, products, customers...</span>
                                </div>
                                <div class="mock-user">
                                    <div class="user-avatar">JD</div>
                                </div>
                            </div>

                            <!-- Sidebar -->
                            <div class="mock-sidebar">
                                <div class="sidebar-item active">
                                    <i class="ri-dashboard-line"></i>
                                    <span>Dashboard</span>
                                </div>
                                <div class="sidebar-item">
                                    <i class="ri-shopping-cart-line"></i>
                                    <span>Sales</span>
                                </div>
                                <div class="sidebar-item">
                                    <i class="ri-archive-line"></i>
                                    <span>Inventory</span>
                                </div>
                                <div class="sidebar-item">
                                    <i class="ri-user-line"></i>
                                    <span>Customers</span>
                                </div>
                            </div>

                            <!-- Main Content -->
                            <div class="mock-main">
                                <!-- Stats Row -->
                                <div class="stats-row">
                                    <div class="stat-card">
                                        <div class="stat-label">Today's Sales</div>
                                        <div class="stat-value">$12,458</div>
                                        <div class="stat-change positive">+12.5%</div>
                                    </div>
                                    <div class="stat-card">
                                        <div class="stat-label">New Orders</div>
                                        <div class="stat-value">84</div>
                                        <div class="stat-change positive">+8.2%</div>
                                    </div>
                                    <div class="stat-card">
                                        <div class="stat-label">Inventory Value</div>
                                        <div class="stat-value">$245,785</div>
                                        <div class="stat-change negative">-2.3%</div>
                                    </div>
                                </div>

                                <!-- Chart -->
                                <div class="chart-container">
                                    <div class="chart-header">
                                        <h4>Sales Overview</h4>
                                        <div class="chart-filter">
                                            <span>Last 7 days</span>
                                            <i class="ri-arrow-down-s-line"></i>
                                        </div>
                                    </div>
                                    <div class="chart-content">
                                        <div class="chart-bars">
                                            <div class="chart-bar" style="height: 40%"></div>
                                            <div class="chart-bar" style="height: 65%"></div>
                                            <div class="chart-bar" style="height: 80%"></div>
                                            <div class="chart-bar" style="height: 60%"></div>
                                            <div class="chart-bar" style="height: 90%"></div>
                                            <div class="chart-bar" style="height: 75%"></div>
                                            <div class="chart-bar" style="height: 50%"></div>
                                        </div>
                                    </div>
                                </div>

                                <!-- Recent Activity -->
                                <div class="activity-container">
                                    <h4>Recent Activity</h4>
                                    <div class="activity-list">
                                        <div class="activity-item">
                                            <div class="activity-icon success">
                                                <i class="ri-check-line"></i>
                                            </div>
                                            <div class="activity-content">
                                                <div class="activity-title">Order #ORD-8452 completed</div>
                                                <div class="activity-time">2 minutes ago</div>
                                            </div>
                                        </div>
                                        <div class="activity-item">
                                            <div class="activity-icon warning">
                                                <i class="ri-alert-line"></i>
                                            </div>
                                            <div class="activity-content">
                                                <div class="activity-title">Low stock alert for iPhone 14</div>
                                                <div class="activity-time">15 minutes ago</div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Floating Elements -->
                        <div class="floating-notification">
                            <div class="notification-icon">
                                <i class="ri-shopping-cart-2-line"></i>
                            </div>
                            <div class="notification-content">
                                <div class="notification-title">New Order Received</div>
                                <div class="notification-desc">Order #ORD-8453 - $1,245.00</div>
                            </div>
                        </div>

                        <div class="floating-metric">
                            <div class="metric-value">84%</div>
                            <div class="metric-label">Stock Accuracy</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</template>

<style lang="scss" scoped>
.dashboard-preview-section {
    padding: 100px 0;
    background: linear-gradient(to bottom, #f8fafc, #ffffff);

    .dashboard-content {
        display: grid;
        grid-template-columns: 1fr;
        gap: 4rem;

        @media (min-width: 992px) {
            grid-template-columns: 1fr 1fr;
            align-items: center;
        }
    }

    .dashboard-text {
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
            margin-bottom: 1.5rem;
            color: var(--titleColor);
        }

        .section-description {
            font-size: 1.125rem;
            line-height: 1.6;
            color: var(--textColor);
            margin-bottom: 2rem;
        }

        .dashboard-features {
            .feature-item {
                display: flex;
                align-items: flex-start;
                gap: 1rem;
                margin-bottom: 1.5rem;

                .feature-icon {
                    width: 48px;
                    height: 48px;
                    background: rgba(102, 126, 234, 0.1);
                    border-radius: 12px;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    flex-shrink: 0;

                    i {
                        font-size: 1.25rem;
                        color: var(--primaryColor);
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
                    }
                }
            }
        }
    }

    .dashboard-preview {
        .preview-container {
            position: relative;
            background: white;
            border-radius: 24px;
            padding: 2rem;
            box-shadow: 0 40px 80px rgba(0, 0, 0, 0.1);
            overflow: hidden;
            border: 1px solid rgba(0, 0, 0, 0.05);
        }

        .dashboard-mockup {
            background: #f8fafc;
            border-radius: 16px;
            overflow: hidden;
            height: 500px;
            display: flex;
            flex-direction: column;

            .mock-header {
                background: white;
                padding: 1rem 1.5rem;
                display: flex;
                justify-content: space-between;
                align-items: center;
                border-bottom: 1px solid #e5e7eb;

                .mock-search {
                    display: flex;
                    align-items: center;
                    gap: 0.5rem;
                    padding: 0.5rem 1rem;
                    background: #f3f4f6;
                    border-radius: 8px;
                    color: #6b7280;
                    font-size: 0.875rem;

                    i {
                        font-size: 1rem;
                    }
                }

                .mock-user {
                    .user-avatar {
                        width: 40px;
                        height: 40px;
                        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                        border-radius: 50%;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        color: white;
                        font-weight: 600;
                    }
                }
            }

            .mock-sidebar {
                background: white;
                width: 200px;
                padding: 1.5rem;
                border-right: 1px solid #e5e7eb;

                .sidebar-item {
                    display: flex;
                    align-items: center;
                    gap: 0.75rem;
                    padding: 0.75rem 1rem;
                    border-radius: 8px;
                    color: #6b7280;
                    margin-bottom: 0.5rem;
                    cursor: pointer;
                    transition: background 0.3s ease;

                    &:hover {
                        background: #f3f4f6;
                    }

                    &.active {
                        background: rgba(102, 126, 234, 0.1);
                        color: var(--primaryColor);
                        font-weight: 500;
                    }

                    i {
                        font-size: 1.25rem;
                    }
                }
            }

            .mock-main {
                flex: 1;
                padding: 1.5rem;
                display: flex;
                flex-direction: column;
                gap: 1.5rem;

                .stats-row {
                    display: grid;
                    grid-template-columns: repeat(3, 1fr);
                    gap: 1rem;

                    .stat-card {
                        background: white;
                        padding: 1rem;
                        border-radius: 12px;
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
                            margin-bottom: 0.25rem;
                        }

                        .stat-change {
                            font-size: 0.75rem;
                            font-weight: 500;

                            &.positive {
                                color: #10b981;
                            }

                            &.negative {
                                color: #ef4444;
                            }
                        }
                    }
                }

                .chart-container {
                    background: white;
                    padding: 1rem;
                    border-radius: 12px;
                    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);

                    .chart-header {
                        display: flex;
                        justify-content: space-between;
                        align-items: center;
                        margin-bottom: 1rem;

                        h4 {
                            font-size: 1rem;
                            font-weight: 600;
                            color: var(--titleColor);
                            margin: 0;
                        }

                        .chart-filter {
                            display: flex;
                            align-items: center;
                            gap: 0.5rem;
                            padding: 0.5rem 0.75rem;
                            background: #f3f4f6;
                            border-radius: 6px;
                            font-size: 0.875rem;
                            color: #6b7280;
                            cursor: pointer;
                        }
                    }

                    .chart-content {
                        .chart-bars {
                            display: flex;
                            align-items: flex-end;
                            gap: 0.5rem;
                            height: 100px;
                            padding: 1rem 0;

                            .chart-bar {
                                flex: 1;
                                background: linear-gradient(to top, #667eea, #764ba2);
                                border-radius: 4px 4px 0 0;
                                min-height: 10px;
                            }
                        }
                    }
                }

                .activity-container {
                    background: white;
                    padding: 1rem;
                    border-radius: 12px;
                    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);

                    h4 {
                        font-size: 1rem;
                        font-weight: 600;
                        margin-bottom: 1rem;
                        color: var(--titleColor);
                    }

                    .activity-list {
                        .activity-item {
                            display: flex;
                            align-items: center;
                            gap: 1rem;
                            padding: 0.75rem 0;
                            border-bottom: 1px solid #f3f4f6;

                            &:last-child {
                                border-bottom: none;
                            }

                            .activity-icon {
                                width: 32px;
                                height: 32px;
                                border-radius: 50%;
                                display: flex;
                                align-items: center;
                                justify-content: center;
                                flex-shrink: 0;

                                &.success {
                                    background: rgba(16, 185, 129, 0.1);
                                    color: #10b981;
                                }

                                &.warning {
                                    background: rgba(245, 158, 11, 0.1);
                                    color: #f59e0b;
                                }

                                i {
                                    font-size: 1rem;
                                }
                            }

                            .activity-content {
                                flex: 1;

                                .activity-title {
                                    font-size: 0.875rem;
                                    font-weight: 500;
                                    color: var(--titleColor);
                                    margin-bottom: 0.25rem;
                                }

                                .activity-time {
                                    font-size: 0.75rem;
                                    color: #6b7280;
                                }
                            }
                        }
                    }
                }
            }
        }

        .floating-notification {
            position: absolute;
            top: 30px;
            right: 30px;
            background: white;
            border-radius: 12px;
            padding: 1rem;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
            display: flex;
            align-items: center;
            gap: 1rem;
            animation: slideIn 0.5s ease-out;
            z-index: 2;

            .notification-icon {
                width: 40px;
                height: 40px;
                background: linear-gradient(135deg, #10b981 0%, #34d399 100%);
                border-radius: 50%;
                display: flex;
                align-items: center;
                justify-content: center;

                i {
                    color: white;
                    font-size: 1.25rem;
                }
            }

            .notification-content {
                .notification-title {
                    font-size: 0.875rem;
                    font-weight: 600;
                    color: var(--titleColor);
                    margin-bottom: 0.25rem;
                }

                .notification-desc {
                    font-size: 0.75rem;
                    color: #6b7280;
                }
            }
        }

        .floating-metric {
            position: absolute;
            bottom: 30px;
            left: 30px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border-radius: 12px;
            padding: 1rem;
            color: white;
            text-align: center;
            box-shadow: 0 10px 30px rgba(102, 126, 234, 0.3);
            animation: pulse 2s infinite;

            .metric-value {
                font-size: 1.5rem;
                font-weight: 700;
                margin-bottom: 0.25rem;
            }

            .metric-label {
                font-size: 0.75rem;
                opacity: 0.9;
            }
        }
    }
}

@keyframes slideIn {
    from {
        transform: translateX(100px);
        opacity: 0;
    }

    to {
        transform: translateX(0);
        opacity: 1;
    }
}

@keyframes pulse {

    0%,
    100% {
        transform: scale(1);
    }

    50% {
        transform: scale(1.05);
    }
}
</style>