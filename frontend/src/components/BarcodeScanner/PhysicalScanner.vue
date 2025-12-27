<template>
    <div class="physical-scanner">
        <div class="scanner-config">
            <h4>Physical Scanner Configuration</h4>

            <div class="form-group">
                <label>Scanner Type:</label>
                <select v-model="selectedScannerType" class="form-control">
                    <option value="USB">USB Scanner</option>
                    <option value="BLUETOOTH">Bluetooth Scanner</option>
                    <option value="WIFI">WiFi Scanner</option>
                </select>
            </div>

            <div v-if="selectedScannerType === 'WIFI'" class="form-group">
                <label>IP Address:</label>
                <input v-model="scannerConfig.ipAddress" type="text" class="form-control">
            </div>

            <div v-if="selectedScannerType === 'BLUETOOTH'" class="form-group">
                <label>MAC Address:</label>
                <input v-model="scannerConfig.macAddress" type="text" class="form-control">
            </div>

            <button @click="registerScanner" class="btn btn-primary">
                Register Scanner
            </button>
        </div>

        <div v-if="isRegistered" class="scanner-input">
            <h5>Scanner Ready</h5>
            <p>Start scanning with your physical scanner</p>

            <!-- Hidden input for physical scanner input -->
            <input ref="scannerInput" type="text" class="scanner-input-field" @input="handlePhysicalScan" autofocus />
        </div>

        <div v-if="lastScan" class="scan-result">
            <h5>Last Scan:</h5>
            <div class="product-info">
                <p><strong>{{ lastScan.productName }}</strong> - ${{ lastScan.price }}</p>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, defineProps } from 'vue';
import { useBarcodeScanner } from '@/composables/useBarcodeScanner';

interface Props {
    companyId: number;
    warehouseId: number;
    userId: number;
}

const props = defineProps<Props>();

const {
    lastScan,
    sendBarcodeScan,
    connectWebSocket
} = useBarcodeScanner();

const selectedScannerType = ref<'USB' | 'BLUETOOTH' | 'WIFI'>('USB');
const scannerInput = ref<HTMLInputElement | null>(null);
const isRegistered = ref(false);
const scannerId = ref<string>('');
const scannerConfig = ref({
    ipAddress: '',
    macAddress: ''
});

let scanTimeout: number | null = null;

// Initialize WebSocket
connectWebSocket({
    companyId: props.companyId,
    warehouseId: props.warehouseId,
    userId: props.userId
});

const registerScanner = async () => {
    try {
        // Generate scanner ID
        scannerId.value = `PHYSICAL-${selectedScannerType.value}-${Date.now()}`;

        console.log('Scanner registered:', scannerId.value);
        isRegistered.value = true;

        // Focus the hidden input field for physical scanner input
        setTimeout(() => {
            scannerInput.value?.focus();
        }, 100);

    } catch (error) {
        console.error('Failed to register scanner:', error);
    }
};

const handlePhysicalScan = (event: Event) => {
    const target = event.target as HTMLInputElement;
    const barcode = target.value.trim();

    if (barcode && barcode.length > 0) {
        // Clear previous timeout
        if (scanTimeout) clearTimeout(scanTimeout);

        // Set timeout to handle complete barcode scan
        scanTimeout = window.setTimeout(() => {
            processBarcodeScan(barcode);
            target.value = ''; // Clear input
        }, 100);
    }
};

const processBarcodeScan = (barcode: string) => {
    if (scannerId.value) {
        sendBarcodeScan(barcode, scannerId.value, {
            companyId: props.companyId,
            warehouseId: props.warehouseId,
            userId: props.userId
        });
    }
};

onMounted(() => {
    // Add global keyboard listener for physical scanners
    document.addEventListener('keydown', handleGlobalKeyPress);
});

onUnmounted(() => {
    document.removeEventListener('keydown', handleGlobalKeyPress);
    if (scanTimeout) clearTimeout(scanTimeout);
});

const handleGlobalKeyPress = (event: KeyboardEvent) => {
    // Physical scanners often act as keyboards
    // Focus the hidden input when any key is pressed (except modifier keys)
    if (!event.ctrlKey && !event.altKey && !event.metaKey && scannerInput.value) {
        scannerInput.value.focus();
    }
};
</script>

<style scoped>
.physical-scanner {
    border: 1px solid #e0e0e0;
    border-radius: 8px;
    padding: 16px;
    background: white;
}

.form-group {
    margin-bottom: 12px;
}

.form-group label {
    display: block;
    margin-bottom: 4px;
    font-weight: bold;
}

.scanner-input-field {
    opacity: 0;
    position: absolute;
    left: -1000px;
}

.scan-result {
    margin-top: 16px;
    padding: 12px;
    background: #f8f9fa;
    border-radius: 4px;
}

.btn {
    padding: 8px 16px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 14px;
}

.btn-primary {
    background: #007bff;
    color: white;
}

.form-control {
    width: 100%;
    padding: 8px 12px;
    border: 1px solid #ced4da;
    border-radius: 4px;
}
</style>