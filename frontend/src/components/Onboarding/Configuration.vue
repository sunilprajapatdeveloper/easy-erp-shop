<template>
    <div class="configuration-step">
        <div class="configuration-grid">
            <!-- POS Configuration -->
            <div class="config-section">
                <div class="section-header">
                    <div class="section-icon">
                        <i class="ri-shopping-cart-line"></i>
                    </div>
                    <div>
                        <h2>POS Configuration</h2>
                        <p>Point of Sale system settings</p>
                    </div>
                </div>

                <div class="config-options">
                    <div class="config-group">
                        <label>Receipt Settings</label>
                        <div class="config-input">
                            <input type="text" v-model="configData.posSettings.receiptHeader"
                                placeholder="Your Business Name" />
                            <span class="input-label">Receipt Header</span>
                        </div>
                        <div class="config-input">
                            <input type="text" v-model="configData.posSettings.receiptFooter"
                                placeholder="Thank you for your business!" />
                            <span class="input-label">Receipt Footer</span>
                        </div>
                    </div>

                    <div class="config-group">
                        <label>Print & Display Settings</label>
                        <div class="toggle-group">
                            <label class="toggle-label">
                                <input type="checkbox" v-model="configData.posSettings.printReceipt" />
                                <span class="toggle-slider"></span>
                                Auto-print receipts
                            </label>
                            <label class="toggle-label">
                                <input type="checkbox" v-model="configData.posSettings.emailReceipt" />
                                <span class="toggle-slider"></span>
                                Email receipts to customers
                            </label>
                            <label class="toggle-label">
                                <input type="checkbox" v-model="configData.posSettings.enableCustomerDisplay" />
                                <span class="toggle-slider"></span>
                                Enable customer display
                            </label>
                            <label class="toggle-label">
                                <input type="checkbox" v-model="configData.posSettings.enableCashDrawer" />
                                <span class="toggle-slider"></span>
                                Enable cash drawer integration
                            </label>
                        </div>
                    </div>

                    <div class="config-group">
                        <label>Tax & Rounding</label>
                        <div class="input-with-label">
                            <input type="number" v-model="configData.posSettings.defaultTaxRate" min="0" max="100"
                                step="0.01" />
                            <span>Default Tax Rate (%)</span>
                        </div>
                        <label class="toggle-label">
                            <input type="checkbox" v-model="configData.posSettings.roundOffAmount" />
                            <span class="toggle-slider"></span>
                            Round off amounts to nearest whole number
                        </label>
                    </div>
                </div>
            </div>

            <!-- Inventory Configuration -->
            <div class="config-section">
                <div class="section-header">
                    <div class="section-icon">
                        <i class="ri-inbox-line"></i>
                    </div>
                    <div>
                        <h2>Inventory Configuration</h2>
                        <p>Stock management settings</p>
                    </div>
                </div>

                <div class="config-options">
                    <div class="config-group">
                        <label>Tracking Settings</label>
                        <div class="toggle-group">
                            <label class="toggle-label">
                                <input type="checkbox" v-model="configData.inventorySettings.enableBatchTracking" />
                                <span class="toggle-slider"></span>
                                Enable batch/lot number tracking
                            </label>
                            <label class="toggle-label">
                                <input type="checkbox" v-model="configData.inventorySettings.enableExpiryTracking" />
                                <span class="toggle-slider"></span>
                                Enable expiry date tracking
                            </label>
                            <label class="toggle-label">
                                <input type="checkbox" v-model="configData.inventorySettings.autoUpdateStock" />
                                <span class="toggle-slider"></span>
                                Auto-update stock on sales/purchases
                            </label>
                        </div>
                    </div>

                    <div class="config-group">
                        <label>Stock Alerts</label>
                        <div class="input-with-label">
                            <input type="number" v-model="configData.inventorySettings.defaultReorderPoint" min="0"
                                step="1" />
                            <span>Default Re-order Point (units)</span>
                        </div>
                        <label class="toggle-label">
                            <input type="checkbox" v-model="configData.notificationSettings.lowStockAlerts" />
                            <span class="toggle-slider"></span>
                            Send low stock alerts
                        </label>
                    </div>
                </div>
            </div>

            <!-- Sales Configuration -->
            <div class="config-section">
                <div class="section-header">
                    <div class="section-icon">
                        <i class="ri-money-dollar-circle-line"></i>
                    </div>
                    <div>
                        <h2>Sales Configuration</h2>
                        <p>Sales transaction settings</p>
                    </div>
                </div>

                <div class="config-options">
                    <div class="config-group">
                        <label>Default Settings</label>
                        <div class="select-group">
                            <div class="select-wrapper">
                                <label>Default Payment Method</label>
                                <select v-model="configData.salesSettings.defaultPaymentMethod">
                                    <option value="cash">Cash</option>
                                    <option value="card">Card</option>
                                    <option value="transfer">Bank Transfer</option>
                                    <option value="check">Check</option>
                                    <option value="digital">Digital Wallet</option>
                                </select>
                            </div>
                            <div class="select-wrapper">
                                <label>Default Customer</label>
                                <select v-model="configData.salesSettings.defaultCustomer">
                                    <option value="walkin">Walk-in Customer</option>
                                    <option value="regular">Regular Customer</option>
                                </select>
                            </div>
                        </div>
                    </div>

                    <div class="config-group">
                        <label>Additional Options</label>
                        <div class="toggle-group">
                            <label class="toggle-label">
                                <input type="checkbox" v-model="configData.salesSettings.enableDiscount" />
                                <span class="toggle-slider"></span>
                                Enable discounts on sales
                            </label>
                            <label class="toggle-label">
                                <input type="checkbox" v-model="configData.salesSettings.enableServiceCharges" />
                                <span class="toggle-slider"></span>
                                Enable service charges
                            </label>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Purchase Configuration -->
            <div class="config-section">
                <div class="section-header">
                    <div class="section-icon">
                        <i class="ri-truck-line"></i>
                    </div>
                    <div>
                        <h2>Purchase Configuration</h2>
                        <p>Purchase order settings</p>
                    </div>
                </div>

                <div class="config-options">
                    <div class="config-group">
                        <label>Order Settings</label>
                        <div class="select-group">
                            <div class="select-wrapper">
                                <label>Default Supplier</label>
                                <select v-model="configData.purchaseSettings.defaultSupplier">
                                    <option value="primary">Primary Supplier</option>
                                    <option value="secondary">Secondary Supplier</option>
                                </select>
                            </div>
                        </div>
                    </div>

                    <div class="config-group">
                        <label>Automation</label>
                        <div class="toggle-group">
                            <label class="toggle-label">
                                <input type="checkbox" v-model="configData.purchaseSettings.enablePurchaseOrder" />
                                <span class="toggle-slider"></span>
                                Enable purchase order system
                            </label>
                            <label class="toggle-label">
                                <input type="checkbox" v-model="configData.purchaseSettings.autoCreateGRN" />
                                <span class="toggle-slider"></span>
                                Auto-create GRN on purchase
                            </label>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Notification Configuration -->
            <div class="config-section">
                <div class="section-header">
                    <div class="section-icon">
                        <i class="ri-notification-line"></i>
                    </div>
                    <div>
                        <h2>Notifications</h2>
                        <p>System alerts and reports</p>
                    </div>
                </div>

                <div class="config-options">
                    <div class="config-group">
                        <label>Notification Channels</label>
                        <div class="toggle-group">
                            <label class="toggle-label">
                                <input type="checkbox" v-model="configData.notificationSettings.emailNotifications" />
                                <span class="toggle-slider"></span>
                                Email notifications
                            </label>
                            <label class="toggle-label">
                                <input type="checkbox" v-model="configData.notificationSettings.pushNotifications" />
                                <span class="toggle-slider"></span>
                                Push notifications
                            </label>
                        </div>
                    </div>

                    <div class="config-group">
                        <label>Reports</label>
                        <div class="toggle-group">
                            <label class="toggle-label">
                                <input type="checkbox" v-model="configData.notificationSettings.dailyReports" />
                                <span class="toggle-slider"></span>
                                Daily summary reports
                            </label>
                            <label class="toggle-label">
                                <input type="checkbox" v-model="configData.notificationSettings.weeklyReports" />
                                <span class="toggle-slider"></span>
                                Weekly performance reports
                            </label>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Data Import -->
            <div class="config-section">
                <div class="section-header">
                    <div class="section-icon">
                        <i class="ri-upload-cloud-line"></i>
                    </div>
                    <div>
                        <h2>Data Import</h2>
                        <p>Import existing data (Optional)</p>
                    </div>
                </div>

                <div class="config-options">
                    <div class="import-options">
                        <div class="import-card" @click="showImportModal = true">
                            <i class="ri-file-excel-line"></i>
                            <h4>Import Products</h4>
                            <p>Import from Excel/CSV</p>
                        </div>
                        <div class="import-card" @click="showImportModal = true">
                            <i class="ri-contacts-line"></i>
                            <h4>Import Customers</h4>
                            <p>Import customer list</p>
                        </div>
                        <div class="import-card" @click="showImportModal = true">
                            <i class="ri-building-line"></i>
                            <h4>Import Suppliers</h4>
                            <p>Import supplier list</p>
                        </div>
                    </div>

                    <p class="import-note">
                        <i class="ri-information-line"></i>
                        You can also import data later from the settings page
                    </p>
                </div>
            </div>
        </div>

        <!-- Preview Card -->
        <div class="preview-card">
            <h3>Configuration Summary</h3>
            <div class="preview-grid">
                <div class="preview-item">
                    <i class="ri-shopping-cart-line"></i>
                    <div>
                        <strong>POS System</strong>
                        <p v-if="configData.posSettings.printReceipt">Auto-print enabled</p>
                        <p v-else>Manual printing</p>
                    </div>
                </div>
                <div class="preview-item">
                    <i class="ri-inbox-line"></i>
                    <div>
                        <strong>Inventory</strong>
                        <p v-if="configData.inventorySettings.autoUpdateStock">Auto-update enabled</p>
                        <p v-else>Manual updates</p>
                    </div>
                </div>
                <div class="preview-item">
                    <i class="ri-money-dollar-circle-line"></i>
                    <div>
                        <strong>Sales</strong>
                        <p>Default: {{ configData.salesSettings.defaultPaymentMethod }}</p>
                    </div>
                </div>
                <div class="preview-item">
                    <i class="ri-notification-line"></i>
                    <div>
                        <strong>Notifications</strong>
                        <p v-if="configData.notificationSettings.emailNotifications">Email enabled</p>
                        <p v-else>No email notifications</p>
                    </div>
                </div>
            </div>
        </div>

        <!-- Import Modal -->
        <div v-if="showImportModal" class="modal-overlay" @click.self="showImportModal = false">
            <div class="modal-content">
                <div class="modal-header">
                    <h3>Import Data</h3>
                    <button class="modal-close" @click="showImportModal = false">
                        <i class="ri-close-line"></i>
                    </button>
                </div>
                <div class="modal-body">
                    <p>You can import data later from the settings page. For now, focus on getting your system set up.
                    </p>
                    <p class="modal-note">Recommended: Start with a clean setup and add data as you go.</p>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-outline" @click="showImportModal = false">
                        Skip for now
                    </button>
                    <button class="btn btn-primary" @click="showImportModal = false">
                        Got it
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, ref, watch } from 'vue'
import { useOnboardingStore } from '@/stores/onboardingStore'
import type { ConfigData } from '@/types/onboarding'

export default defineComponent({
    name: 'ConfigurationStep',
    emits: ['validated'],
    setup(_, { emit }) {
        const onboardingStore = useOnboardingStore()
        const showImportModal = ref(false)

        const configData = ref<ConfigData>({
            posSettings: {
                receiptHeader: '',
                receiptFooter: 'Thank you for your business!',
                defaultTaxRate: 0,
                roundOffAmount: false,
                enableCustomerDisplay: true,
                enableCashDrawer: true, // Added missing property
                printReceipt: true,
                emailReceipt: false
            },
            inventorySettings: {
                enableBatchTracking: false,
                enableExpiryTracking: false,
                defaultReorderPoint: 10,
                autoUpdateStock: true
            },
            salesSettings: {
                defaultPaymentMethod: 'cash',
                defaultCustomer: 'walkin',
                enableDiscount: true,
                enableServiceCharges: false
            },
            purchaseSettings: {
                defaultSupplier: 'primary',
                enablePurchaseOrder: true,
                autoCreateGRN: false
            },
            notificationSettings: {
                emailNotifications: true,
                pushNotifications: true,
                lowStockAlerts: true,
                dailyReports: true,
                weeklyReports: false // Optional property
            }
        })

        const validateForm = () => {
            // Configuration is always valid (optional step)
            emit('validated', true)
            return true
        }

        // Watch for changes and validate
        watch(configData, validateForm, { deep: true })

        // Save to store when changed
        watch(() => configData.value, (newValue) => {
            onboardingStore.setConfigData(newValue)
        }, { deep: true })

        return {
            configData,
            showImportModal,
            validateForm
        }
    }
})
</script>

<style lang="scss" scoped>
.configuration-step {
    .configuration-header {
        text-align: center;
        margin-bottom: 3rem;

        h1 {
            font-size: 2.5rem;
            margin-bottom: 0.5rem;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }

        .subtitle {
            font-size: 1.125rem;
            color: var(--textColor);
        }
    }

    .configuration-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
        gap: 2rem;
        margin-bottom: 3rem;

        .config-section {
            background: white;
            border-radius: 16px;
            padding: 2rem;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);

            .section-header {
                display: flex;
                align-items: center;
                gap: 1rem;
                margin-bottom: 2rem;
                padding-bottom: 1rem;
                border-bottom: 1px solid rgba(0, 0, 0, 0.08);

                .section-icon {
                    width: 50px;
                    height: 50px;
                    border-radius: 12px;
                    background: linear-gradient(135deg, #667eea15 0%, #764ba215 100%);
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    font-size: 1.5rem;
                    color: var(--primaryColor);
                }

                h2 {
                    font-size: 1.5rem;
                    margin: 0 0 0.25rem 0;
                    color: var(--titleColor);
                }

                p {
                    margin: 0;
                    color: var(--textColor);
                    opacity: 0.7;
                }
            }

            .config-options {
                .config-group {
                    margin-bottom: 1.5rem;

                    &:last-child {
                        margin-bottom: 0;
                    }

                    label {
                        display: block;
                        font-weight: 600;
                        color: var(--titleColor);
                        margin-bottom: 1rem;
                        font-size: 1rem;
                    }

                    .config-input {
                        position: relative;
                        margin-bottom: 1rem;

                        input {
                            width: 100%;
                            padding: 0.875rem 1rem;
                            border: 2px solid rgba(0, 0, 0, 0.1);
                            border-radius: 8px;
                            font-size: 1rem;
                            transition: border-color 0.3s ease;

                            &:focus {
                                outline: none;
                                border-color: var(--primaryColor);
                                box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
                            }
                        }

                        .input-label {
                            position: absolute;
                            left: 0.75rem;
                            top: -0.5rem;
                            background: white;
                            padding: 0 0.5rem;
                            font-size: 0.75rem;
                            color: var(--textColor);
                            opacity: 0.7;
                        }
                    }

                    .toggle-group {
                        display: flex;
                        flex-direction: column;
                        gap: 0.75rem;
                    }

                    .toggle-label {
                        display: flex;
                        align-items: center;
                        gap: 0.75rem;
                        cursor: pointer;
                        font-size: 0.9375rem;
                        color: var(--textColor);

                        input {
                            display: none;
                        }

                        .toggle-slider {
                            position: relative;
                            width: 44px;
                            height: 24px;
                            background: rgba(0, 0, 0, 0.1);
                            border-radius: 12px;
                            transition: background-color 0.3s ease;
                            flex-shrink: 0;

                            &::before {
                                content: '';
                                position: absolute;
                                width: 20px;
                                height: 20px;
                                background: white;
                                border-radius: 50%;
                                top: 2px;
                                left: 2px;
                                transition: transform 0.3s ease;
                                box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
                            }
                        }

                        input:checked+.toggle-slider {
                            background: var(--primaryColor);

                            &::before {
                                transform: translateX(20px);
                            }
                        }
                    }

                    .input-with-label {
                        display: flex;
                        align-items: center;
                        gap: 1rem;
                        margin-bottom: 1rem;

                        input {
                            width: 100px;
                            padding: 0.75rem 1rem;
                            border: 2px solid rgba(0, 0, 0, 0.1);
                            border-radius: 8px;
                            font-size: 1rem;
                            text-align: center;

                            &:focus {
                                outline: none;
                                border-color: var(--primaryColor);
                            }
                        }

                        span {
                            font-size: 0.9375rem;
                            color: var(--textColor);
                        }
                    }

                    .select-group {
                        display: grid;
                        grid-template-columns: 1fr;
                        gap: 1rem;

                        .select-wrapper {
                            label {
                                display: block;
                                margin-bottom: 0.5rem;
                                font-weight: 500;
                                color: var(--titleColor);
                                font-size: 0.9375rem;
                            }

                            select {
                                width: 100%;
                                padding: 0.75rem 1rem;
                                border: 2px solid rgba(0, 0, 0, 0.1);
                                border-radius: 8px;
                                font-size: 1rem;
                                background: white;
                                cursor: pointer;

                                &:focus {
                                    outline: none;
                                    border-color: var(--primaryColor);
                                }
                            }
                        }
                    }

                    .import-options {
                        display: grid;
                        grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
                        gap: 1rem;
                        margin-bottom: 1rem;

                        .import-card {
                            background: rgba(0, 0, 0, 0.02);
                            border: 2px dashed rgba(0, 0, 0, 0.1);
                            border-radius: 8px;
                            padding: 1.5rem;
                            text-align: center;
                            cursor: pointer;
                            transition: all 0.3s ease;

                            &:hover {
                                border-color: var(--primaryColor);
                                background: rgba(102, 126, 234, 0.05);
                            }

                            i {
                                font-size: 2rem;
                                color: var(--primaryColor);
                                margin-bottom: 0.75rem;
                            }

                            h4 {
                                font-size: 0.9375rem;
                                margin: 0 0 0.25rem 0;
                                color: var(--titleColor);
                            }

                            p {
                                font-size: 0.75rem;
                                color: var(--textColor);
                                opacity: 0.7;
                                margin: 0;
                            }
                        }
                    }

                    .import-note {
                        display: flex;
                        align-items: center;
                        gap: 0.5rem;
                        font-size: 0.875rem;
                        color: var(--textColor);
                        opacity: 0.7;
                        margin: 0;

                        i {
                            font-size: 1rem;
                        }
                    }
                }
            }
        }
    }

    .preview-card {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        border-radius: 16px;
        padding: 2rem;
        color: white;

        h3 {
            margin: 0 0 1.5rem 0;
            font-size: 1.5rem;
        }

        .preview-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 1.5rem;

            .preview-item {
                display: flex;
                align-items: center;
                gap: 1rem;

                i {
                    font-size: 2rem;
                    opacity: 0.9;
                }

                div {
                    flex: 1;

                    strong {
                        display: block;
                        font-size: 1rem;
                        margin-bottom: 0.25rem;
                        opacity: 0.9;
                    }

                    p {
                        margin: 0;
                        font-size: 0.875rem;
                        opacity: 0.7;
                    }
                }
            }
        }
    }

    .modal-overlay {
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background: rgba(0, 0, 0, 0.5);
        display: flex;
        align-items: center;
        justify-content: center;
        z-index: 1000;
        animation: fadeIn 0.3s ease;

        .modal-content {
            background: white;
            border-radius: 16px;
            width: 90%;
            max-width: 500px;
            animation: slideUp 0.3s ease;

            .modal-header {
                display: flex;
                justify-content: space-between;
                align-items: center;
                padding: 1.5rem 2rem;
                border-bottom: 1px solid rgba(0, 0, 0, 0.1);

                h3 {
                    margin: 0;
                    font-size: 1.5rem;
                    color: var(--titleColor);
                }

                .modal-close {
                    background: none;
                    border: none;
                    font-size: 1.5rem;
                    color: var(--textColor);
                    cursor: pointer;
                    padding: 0.5rem;

                    &:hover {
                        color: var(--primaryColor);
                    }
                }
            }

            .modal-body {
                padding: 2rem;

                p {
                    margin: 0 0 1rem 0;
                    color: var(--textColor);
                    line-height: 1.6;

                    &:last-child {
                        margin-bottom: 0;
                    }
                }

                .modal-note {
                    font-style: italic;
                    opacity: 0.8;
                    font-size: 0.9375rem;
                }
            }

            .modal-footer {
                display: flex;
                justify-content: flex-end;
                gap: 1rem;
                padding: 1.5rem 2rem;
                border-top: 1px solid rgba(0, 0, 0, 0.1);
            }
        }
    }
}

@keyframes fadeIn {
    from {
        opacity: 0;
    }

    to {
        opacity: 1;
    }
}

@keyframes slideUp {
    from {
        opacity: 0;
        transform: translateY(20px);
    }

    to {
        opacity: 1;
        transform: translateY(0);
    }
}

@media (max-width: 992px) {
    .configuration-step {
        .configuration-grid {
            grid-template-columns: 1fr;
        }
    }
}

@media (max-width: 768px) {
    .configuration-step {
        .configuration-header h1 {
            font-size: 2rem;
        }

        .configuration-grid {
            .config-section {
                padding: 1.5rem;
            }
        }

        .preview-card .preview-grid {
            grid-template-columns: 1fr;
        }
    }
}
</style>