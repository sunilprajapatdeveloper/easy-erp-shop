<template>
    <div class="mobile-scanner">
        <div class="scanner-header">
            <h3>Mobile Barcode Scanner</h3>
            <div class="scanner-status" :class="statusClass">
                {{ statusText }}
            </div>
        </div>

        <!-- Scanner Registration -->
        <div v-if="!isRegistered" class="scanner-registration">
            <h5>Register Scanner</h5>
            <div class="form-group">
                <label>Scanner Name:</label>
                <input v-model="scannerName" type="text" class="form-control" placeholder="Enter scanner name">
            </div>
            <button @click="registerMobileScanner" class="btn btn-primary"
                :disabled="!scannerName.trim() || !isConnected || isRegistering">
                {{ isRegistering ? 'Registering...' : (isConnected ? 'Register Mobile Scanner' : 'Connecting...') }}
            </button>

            <!-- Debug info -->
            <div v-if="debugInfo" class="debug-info mt-2 p-2 bg-light rounded">
                <small class="text-muted">
                    <strong>Debug:</strong> {{ debugInfo }}
                </small>
            </div>
        </div>

        <!-- Scanner Controls -->
        <div v-else class="scanner-controls">
            <div class="alert alert-success">
                <strong>Scanner Registered Successfully!</strong>
                <p>Scanner ID: {{ scannerId }}</p>
            </div>

            <div class="connection-info">
                <p><strong>Scanner ID:</strong> {{ scannerId }}</p>
                <p><strong>Status:</strong> {{ isConnected ? 'Connected' : 'Disconnected' }}</p>
            </div>

            <div v-if="!isCameraActive" class="camera-controls">
                <button @click="initializeScanner" class="btn btn-primary" :disabled="!isConnected">
                    {{ isConnected ? 'Start Camera Scanner' : 'Connecting...' }}
                </button>

                <!-- Manual barcode input for testing -->
                <div class="manual-test mt-3">
                    <h5>Manual Test</h5>
                    <div class="input-group">
                        <input v-model="manualBarcode" type="text" placeholder="Enter barcode manually"
                            class="form-control">
                        <button @click="testManualScan" class="btn btn-outline-secondary" :disabled="!isConnected">
                            Test Scan
                        </button>
                    </div>
                </div>
            </div>

            <div v-else class="scanner-view">
                <!-- Camera View -->
                <div class="camera-container">
                    <video ref="videoElement" class="scanner-video" playsinline autoplay></video>
                    <div class="scanner-overlay">
                        <div class="scan-frame">
                            <div class="scan-line"></div>
                        </div>
                        <p class="scan-instruction">Point camera at barcode</p>
                        <p class="scan-hint">Click anywhere to simulate scan</p>
                    </div>
                </div>

                <div class="camera-controls">
                    <button @click="stopCamera" class="btn btn-danger">
                        Stop Camera
                    </button>
                </div>
            </div>
        </div>

        <!-- Scan Results -->
        <div v-if="lastScan" class="scan-result mt-3">
            <h5>Last Scan Result:</h5>
            <div class="product-info" :class="{ 'scan-error': !lastScan.success }">
                <template v-if="lastScan.success">
                    <p><strong>Product:</strong> {{ lastScan.productName }}</p>
                    <p><strong>SKU:</strong> {{ lastScan.productSku }}</p>
                    <p><strong>Price:</strong> ${{ lastScan.price }}</p>
                    <p><strong>Stock:</strong> {{ lastScan.stockQuantity }}</p>
                </template>
                <template v-else>
                    <p class="error-message">{{ lastScan.errorMessage }}</p>
                </template>
            </div>
        </div>

        <!-- Error Display -->
        <div v-if="scannerStore.error" class="alert alert-danger mt-3">
            {{ scannerStore.error }}
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted, defineProps, nextTick } from 'vue';
import { useBarcodeScanner } from '@/composables/useBarcodeScanner';
import { useBarcodeScannerStore } from '@/stores/barcodeScannerStore';
import { useUserStore } from '@/stores/userStore';
import { useToast } from 'vue-toastification';
import type { ScannerRegistrationResponse } from '@/types/barcodeScanner';

interface Props {
    companyId: number;
    warehouseId: number;
    userId: number;
}

const props = defineProps<Props>();
const toast = useToast();
const scannerStore = useBarcodeScannerStore();
const userStore = useUserStore();

const {
    isConnected,
    isScanning,
    lastScan,
    sendBarcodeScan,
    connectWebSocket,
    registerScannerViaWebSocket
} = useBarcodeScanner();

// Scanner registration
const isRegistered = ref(false);
const isRegistering = ref(false);
const scannerId = ref<string>('');
const scannerName = ref('Mobile Scanner');
const debugInfo = ref<string>('');

// Camera related refs
const videoElement = ref<HTMLVideoElement | null>(null);
const isCameraActive = ref(false);
const mediaStream = ref<MediaStream | null>(null);
const manualBarcode = ref('');

// Prevent double registration
let registrationInProgress = false;

// Initialize WebSocket connection when component mounts
onMounted(() => {
    console.log('MobileScanner mounted, connecting WebSocket...');
    debugInfo.value = 'Connecting WebSocket...';

    // Connect WebSocket immediately when component mounts
    connectWebSocket({
        companyId: props.companyId,
        warehouseId: props.warehouseId,
        userId: props.userId
    }).then(success => {
        if (success) {
            debugInfo.value = 'WebSocket connected successfully';
            console.log('WebSocket connected successfully');
        } else {
            debugInfo.value = 'WebSocket connection failed';
            console.error('WebSocket connection failed');
        }
    });

    // Listen for scanner registration response
    window.addEventListener('scanner-registered', handleScannerRegistered);
    console.log('Added scanner-registered event listener');
});

const statusClass = computed(() => ({
    'status-connected': isConnected.value,
    'status-disconnected': !isConnected.value
}));

const statusText = computed(() =>
    isConnected.value ? 'Connected' : 'Disconnected'
);

const registerMobileScanner = async () => {
    if (registrationInProgress) {
        toast.warning('Registration already in progress');
        return;
    }

    if (!scannerName.value.trim()) {
        toast.warning('Please enter a scanner name');
        return;
    }

    if (!isConnected.value) {
        toast.error('WebSocket not connected. Please wait...');
        return;
    }

    registrationInProgress = true;
    isRegistering.value = true;
    debugInfo.value = 'Sending registration request...';

    try {
        const registrationData = {
            scannerName: scannerName.value,
            scannerType: 'MOBILE' as const,
            warehouseId: props.warehouseId,
            assignedUserId: props.userId,
            companyId: props.companyId
        };

        console.log('Sending scanner registration...', registrationData);

        const success = registerScannerViaWebSocket(registrationData);

        if (success) {
            debugInfo.value = 'Registration request sent, waiting for response...';
            toast.info('Registration request sent...');

            // Set timeout to handle no response
            setTimeout(() => {
                if (!isRegistered.value && registrationInProgress) {
                    debugInfo.value = 'No response received from server';
                    toast.error('Registration timeout. Please try again.');
                    registrationInProgress = false;
                    isRegistering.value = false;
                }
            }, 10000); // 10 second timeout

        } else {
            debugInfo.value = 'Failed to send registration request';
            toast.error('Failed to send registration request');
            registrationInProgress = false;
            isRegistering.value = false;
        }

    } catch (error) {
        console.error('Failed to register scanner:', error);
        debugInfo.value = `Registration error: ${error}`;
        toast.error('Failed to register scanner');
        registrationInProgress = false;
        isRegistering.value = false;
    }
};

// Listen for registration response
const handleScannerRegistered = (event: Event) => {
    console.log('Received scanner-registered event:', event);

    const customEvent = event as CustomEvent<ScannerRegistrationResponse>;
    const response = customEvent.detail;

    console.log('Registration response received:', response);

    if (response && response.status === "REGISTERED") {
        scannerId.value = response.scannerId;
        isRegistered.value = true;
        isRegistering.value = false;
        registrationInProgress = false;
        debugInfo.value = `Scanner registered successfully: ${response.scannerId}`;

        toast.success('Scanner registered successfully!');
        console.log('Scanner registration completed:', response.scannerId);

    } else if (response) {
        debugInfo.value = `Registration failed: ${response.message}`;
        toast.error(`Scanner registration failed: ${response.message}`);
        isRegistering.value = false;
        registrationInProgress = false;
    } else {
        debugInfo.value = 'Invalid registration response received';
        toast.error('Invalid registration response');
        isRegistering.value = false;
        registrationInProgress = false;
    }
};

const initializeScanner = async () => {
    try {
        await startCamera();
        setupBarcodeDetection();
    } catch (error) {
        console.error('Failed to initialize scanner:', error);
        toast.error('Failed to start camera scanner');
    }
};

const startCamera = async () => {
    try {
        if (mediaStream.value) {
            stopCamera();
        }

        const constraints = {
            video: {
                facingMode: 'environment',
                width: { ideal: 1280 },
                height: { ideal: 720 }
            }
        };

        const stream = await navigator.mediaDevices.getUserMedia(constraints);

        mediaStream.value = stream;
        if (videoElement.value) {
            videoElement.value.srcObject = stream;
            await videoElement.value.play();
        }

        isCameraActive.value = true;
        toast.success('Camera started successfully');

    } catch (error: any) {
        console.error('Error accessing camera:', error);

        if (error.name === 'NotAllowedError') {
            toast.error('Camera permission denied. Please allow camera access.');
        } else if (error.name === 'NotFoundError') {
            toast.error('No camera found on this device.');
        } else {
            toast.error('Cannot access camera. Please check permissions.');
        }
        throw error;
    }
};

const stopCamera = () => {
    if (mediaStream.value) {
        mediaStream.value.getTracks().forEach(track => {
            track.stop();
        });
        mediaStream.value = null;
    }

    if (videoElement.value) {
        videoElement.value.srcObject = null;
    }

    isCameraActive.value = false;
    toast.info('Camera stopped');
};

const setupBarcodeDetection = () => {
    if (videoElement.value) {
        videoElement.value.addEventListener('click', simulateBarcodeScan);
    }
};

const simulateBarcodeScan = () => {
    const testBarcodes = [
        '1234567890123',
        '1234567890124',
        '1234567890125',
        '1234567890126'
    ];

    const randomBarcode = testBarcodes[Math.floor(Math.random() * testBarcodes.length)];
    handleBarcodeDetected(randomBarcode);
};

const handleBarcodeDetected = (barcode: string) => {
    if (!scannerId.value) {
        toast.error('Scanner not registered');
        return;
    }

    console.log('Sending barcode scan:', barcode, 'with scanner:', scannerId.value);
    sendBarcodeScan(barcode, scannerId.value, {
        companyId: props.companyId,
        warehouseId: props.warehouseId,
        userId: props.userId
    });
};

const testManualScan = () => {
    if (manualBarcode.value.trim()) {
        handleBarcodeDetected(manualBarcode.value.trim());
        manualBarcode.value = '';
    } else {
        toast.warning('Please enter a barcode');
    }
};

onUnmounted(() => {
    stopCamera();
    if (videoElement.value) {
        videoElement.value.removeEventListener('click', simulateBarcodeScan);
    }
    window.removeEventListener('scanner-registered', handleScannerRegistered);
    console.log('MobileScanner unmounted');
});
</script>

<style scoped>
.mobile-scanner {
    border: 1px solid #e0e0e0;
    border-radius: 8px;
    padding: 16px;
    background: white;
}

.scanner-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;
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

.scanner-registration {
    padding: 16px;
    background: #f8f9fa;
    border-radius: 4px;
    margin-bottom: 16px;
}

.scanner-registration h5 {
    margin-top: 0;
    margin-bottom: 12px;
}

.form-group {
    margin-bottom: 12px;
}

.form-group label {
    display: block;
    margin-bottom: 4px;
    font-weight: bold;
}

.debug-info {
    font-size: 12px;
    border-left: 3px solid #007bff;
}

.connection-info {
    background: #f8f9fa;
    padding: 12px;
    border-radius: 4px;
    margin-bottom: 16px;
}

.connection-info p {
    margin: 4px 0;
    font-size: 14px;
}

.camera-container {
    position: relative;
    width: 100%;
    max-width: 400px;
    margin: 0 auto 16px;
}

.scanner-video {
    width: 100%;
    height: 300px;
    border-radius: 8px;
    background: #000;
    object-fit: cover;
}

.scanner-overlay {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    pointer-events: none;
}

.scan-frame {
    width: 250px;
    height: 150px;
    border: 3px solid #007bff;
    border-radius: 12px;
    position: relative;
    background: rgba(0, 123, 255, 0.1);
}

.scan-line {
    width: 100%;
    height: 3px;
    background: #007bff;
    position: absolute;
    top: 50%;
    animation: scan 2s linear infinite;
}

@keyframes scan {
    0% {
        top: 10%;
    }

    50% {
        top: 90%;
    }

    100% {
        top: 10%;
    }
}

.scan-instruction {
    color: white;
    text-align: center;
    margin-top: 16px;
    font-weight: bold;
    text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.8);
}

.scan-hint {
    color: #ccc;
    text-align: center;
    margin-top: 8px;
    font-size: 12px;
}

.camera-controls {
    display: flex;
    gap: 8px;
    justify-content: center;
    margin-bottom: 16px;
}

.manual-test {
    margin-top: 16px;
    padding: 16px;
    background: #f8f9fa;
    border-radius: 4px;
}

.scan-result {
    padding: 12px;
    background: #f8f9fa;
    border-radius: 4px;
}

.scan-error {
    background: #f8d7da;
    border: 1px solid #f5c6cb;
}

.error-message {
    color: #721c24;
    font-weight: bold;
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

.btn-secondary {
    background: #6c757d;
    color: white;
}

.btn-danger {
    background: #dc3545;
    color: white;
}

.btn-outline-secondary {
    background: transparent;
    border: 1px solid #6c757d;
    color: #6c757d;
}

.btn:disabled {
    opacity: 0.6;
    cursor: not-allowed;
}

.form-control {
    width: 100%;
    padding: 8px 12px;
    border: 1px solid #ced4da;
    border-radius: 4px;
}

.input-group {
    display: flex;
    gap: 8px;
}

.input-group .form-control {
    flex: 1;
}
</style>