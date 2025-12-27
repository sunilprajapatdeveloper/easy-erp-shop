<template>
    <div class="mobile-scanner-page">
        <h2 class="title">Mobile Barcode Scanner</h2>

        <PosScannerIntegration :company-id="userStore.currentUser?.companyId || 1"
            :warehouse-id="userStore.currentUser?.defaultWarehouseId || 1" :user-id="userStore.currentUser?.id || 1" />

        <div class="status-box" v-if="lastScan">
            <strong>Last Scan:</strong>
            <p>{{ lastScan.productName }} — ₹{{ lastScan.price }}</p>
        </div>
    </div>
</template>

<script setup lang="ts">
import PosScannerIntegration from "@/components/Pos/PosScannerIntegration.vue";
import { useUserStore } from "@/stores/userStore";
import { useBarcodeScannerStore } from "@/stores/barcodeScannerStore";
import { computed } from "vue";

const userStore = useUserStore();
const scannerStore = useBarcodeScannerStore();

const lastScan = computed(() => scannerStore.lastScan);
</script>

<style scoped>
.mobile-scanner-page {
    padding: 20px;
}

.title {
    text-align: center;
    font-weight: bold;
    margin-bottom: 20px;
}

.status-box {
    background: #d1fae5;
    padding: 12px;
    border-radius: 8px;
    margin-top: 20px;
}
</style>
