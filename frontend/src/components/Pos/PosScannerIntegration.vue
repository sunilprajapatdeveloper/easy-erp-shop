<template>
    <div class="pos-scanner-integration">
        <div class="scanner-header mb-3">
            <h4>Barcode Scanner</h4>
            <div class="scanner-status" :class="connectionStatusClass">
                {{ connectionStatusText }}
            </div>
        </div>

        <div class="scanner-tabs mb-3">
            <button v-for="tab in tabs" :key="tab.id" @click="activeTab = tab.id"
                :class="['tab-btn', { active: activeTab === tab.id }]">
                {{ tab.label }}
            </button>
        </div>

        <div class="scanner-content">
            <MobileScanner v-if="activeTab === 'mobile'" :company-id="companyId" :warehouse-id="warehouseId"
                :user-id="userId" :scanner-id="mobileScannerId" />

            <PhysicalScanner v-if="activeTab === 'physical'" :company-id="companyId" :warehouse-id="warehouseId"
                :user-id="userId" />
        </div>

        <!-- Quick Actions -->
        <div class="quick-actions mt-3">
            <button @click="closeScanner" class="btn btn-secondary w-100">
                Close Scanner
            </button>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted, defineProps, defineEmits } from 'vue';
import { useBarcodeScanner } from '../../composables/useBarcodeScanner';
import MobileScanner from '../BarcodeScanner/MobileScanner.vue';
import PhysicalScanner from '../BarcodeScanner/PhysicalScanner.vue';

interface Props {
    companyId: number;
    warehouseId: number;
    userId: number;
}

const props = defineProps<Props>();
const emit = defineEmits<{
    close: [];
}>();

const { isConnected } = useBarcodeScanner();

type TabType = 'mobile' | 'physical';

const activeTab = ref<TabType>('mobile');
const mobileScannerId = ref(`MOBILE-${Date.now()}`);

const tabs: { id: TabType; label: string }[] = [
    { id: 'mobile', label: '📱 Mobile Scanner' },
    { id: 'physical', label: '🔌 Physical Scanner' }
];

const connectionStatusClass = computed(() => ({
    'status-connected': isConnected.value,
    'status-disconnected': !isConnected.value
}));

const connectionStatusText = computed(() =>
    isConnected.value ? 'Connected' : 'Disconnected'
);

const closeScanner = () => {
    emit('close');
};

// Listen for barcode scanned events to automatically add products to POS
const handleBarcodeScanned = (event: CustomEvent) => {
    console.log('Barcode scanned:', event.detail);
    // The parent component (PosRightContent) will handle adding the product
};

onMounted(() => {
    window.addEventListener("barcode-scanned", (event) => handleBarcodeScanned(event as CustomEvent));
});

onUnmounted(() => {
    window.removeEventListener("barcode-scanned", (event) => handleBarcodeScanned(event as CustomEvent));
});
</script>

<style scoped>
.pos-scanner-integration {
    max-width: 500px;
    margin: 0 auto;
}

.scanner-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.scanner-status {
    padding: 4px 12px;
    border-radius: 16px;
    font-size: 12px;
    font-weight: bold;
}

.status-connected {
    background: #d4edda;
    color: #155724;
}

.status-disconnected {
    background: #f8d7da;
    color: #721c24;
}

.scanner-tabs {
    display: flex;
    border-bottom: 1px solid #dee2e6;
}

.tab-btn {
    padding: 8px 16px;
    background: none;
    border: none;
    border-bottom: 2px solid transparent;
    cursor: pointer;
    flex: 1;
    text-align: center;
}

.tab-btn.active {
    border-bottom-color: #007bff;
    color: #007bff;
    font-weight: bold;
}

.quick-actions {
    border-top: 1px solid #e0e0e0;
    padding-top: 16px;
}

.btn {
    padding: 8px 16px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 14px;
}

.btn-secondary {
    background: #6c757d;
    color: white;
}
</style>