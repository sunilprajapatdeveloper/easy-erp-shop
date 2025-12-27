import { defineStore } from "pinia";
import { ref, computed } from "vue";
import type {
  BarcodeScanner,
  ScannerRegistrationRequest,
  BarcodeScanRequest,
  BarcodeScanResponse,
} from "@/types/barcodeScanner";
import {
  registerScanner,
  getWarehouseScanners,
  disconnectScanner,
  processBarcodeScan,
} from "@/services/barcodeScannerService";

export const useBarcodeScannerStore = defineStore("barcodeScanner", () => {
  // State
  const scanners = ref<BarcodeScanner[]>([]);
  const currentScanner = ref<BarcodeScanner | null>(null);
  const lastScan = ref<BarcodeScanResponse | null>(null);
  const loading = ref(false);
  const error = ref<string | null>(null);
  const isConnected = ref(false);

  // Getters
  const activeScanners = computed(() =>
    scanners.value.filter((scanner) => scanner.status === "ACTIVE")
  );

  const warehouseScanners = computed(
    () => (warehouseId: number) =>
      scanners.value.filter((scanner) => scanner.warehouseId === warehouseId)
  );

  // Actions
  const registerNewScanner = async (request: ScannerRegistrationRequest) => {
    loading.value = true;
    error.value = null;
    try {
      const response = await registerScanner(request);
      return response.data;
    } catch (err: any) {
      console.error("Scanner registration failed:", err);
      error.value =
        err.response?.data?.message ||
        err.message ||
        "Failed to register scanner";
      throw err;
    } finally {
      loading.value = false;
    }
  };

  const fetchWarehouseScanners = async (warehouseId: number) => {
    loading.value = true;
    error.value = null;
    try {
      const response = await getWarehouseScanners(warehouseId);
      scanners.value = response.data;
      return response.data;
    } catch (err: any) {
      console.error("Failed to fetch warehouse scanners:", err);
      error.value =
        err.response?.data?.message ||
        err.message ||
        "Failed to fetch scanners";
      throw err;
    } finally {
      loading.value = false;
    }
  };

  const disconnectCurrentScanner = async (scannerId: string) => {
    loading.value = true;
    error.value = null;
    try {
      await disconnectScanner(scannerId);

      // Update local state
      const scannerIndex = scanners.value.findIndex(
        (s) => s.scannerId === scannerId
      );
      if (scannerIndex !== -1) {
        scanners.value[scannerIndex].status = "INACTIVE";
      }

      if (currentScanner.value?.scannerId === scannerId) {
        currentScanner.value = null;
      }
    } catch (err: any) {
      console.error("Failed to disconnect scanner:", err);
      error.value =
        err.response?.data?.message ||
        err.message ||
        "Failed to disconnect scanner";
      throw err;
    } finally {
      loading.value = false;
    }
  };

  const processScan = async (scanRequest: BarcodeScanRequest) => {
    loading.value = true;
    error.value = null;
    try {
      const response = await processBarcodeScan(scanRequest);
      lastScan.value = response.data;
      return response.data;
    } catch (err: any) {
      console.error("Barcode scan failed:", err);
      error.value =
        err.response?.data?.message ||
        err.message ||
        "Failed to process barcode scan";
      throw err;
    } finally {
      loading.value = false;
    }
  };

  const setLastScan = (scanResponse: BarcodeScanResponse) => {
    lastScan.value = scanResponse;
  };

  const setConnectionStatus = (connected: boolean) => {
    isConnected.value = connected;
  };

  const setCurrentScanner = (scanner: BarcodeScanner | null) => {
    currentScanner.value = scanner;
  };

  const clearError = () => {
    error.value = null;
  };

  const clearLastScan = () => {
    lastScan.value = null;
  };

  return {
    // State
    scanners,
    currentScanner,
    lastScan,
    loading,
    error,
    isConnected,

    // Getters
    activeScanners,
    warehouseScanners,

    // Actions
    registerNewScanner,
    fetchWarehouseScanners,
    disconnectCurrentScanner,
    processScan,
    setLastScan,
    setConnectionStatus,
    setCurrentScanner,
    clearError,
    clearLastScan,
  };
});
