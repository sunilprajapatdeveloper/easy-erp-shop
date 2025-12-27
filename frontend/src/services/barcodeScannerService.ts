import api from "./api";
import { useUserStore } from "@/stores/userStore";
import type {
  ScannerRegistrationRequest,
  ScannerRegistrationResponse,
  BarcodeScanRequest,
  BarcodeScanResponse,
  BarcodeScanner,
} from "@/types/barcodeScanner";

// Helper: build headers
const getHeaders = () => {
  try {
    const userStore = useUserStore();
    const companyId = userStore.currentUser?.companyId;
    const userId = userStore.currentUser?.id;

    if (!companyId) {
      throw new Error("Company information is missing.");
    }

    const headers: Record<string, string> = {
      "X-Company-Id": companyId.toString(),
    };

    if (userId) {
      headers["X-User-Id"] = userId.toString();
    }

    return headers;
  } catch (error) {
    console.error("Error getting user store:", error);
    throw new Error(
      "Unable to access user information. Please ensure you're logged in."
    );
  }
};

/**
 * Register a new barcode scanner
 */
export const registerScanner = (data: ScannerRegistrationRequest) => {
  const headers = getHeaders();
  return api.post<ScannerRegistrationResponse>("/scanner/register", data, {
    headers,
  });
};

/**
 * Process barcode scan via REST API
 */
export const processBarcodeScan = (data: BarcodeScanRequest) => {
  const headers = getHeaders();
  return api.post<BarcodeScanResponse>("/scanner/scan", data, {
    headers,
  });
};

/**
 * Get all scanners for a warehouse
 */
export const getWarehouseScanners = (warehouseId: number) => {
  const headers = getHeaders();
  return api.get<BarcodeScanner[]>(`/scanner/warehouse/${warehouseId}`, {
    headers,
  });
};

/**
 * Disconnect a scanner
 */
export const disconnectScanner = (scannerId: string) => {
  const headers = getHeaders();
  return api.post<void>(
    `/scanner/${scannerId}/disconnect`,
    {},
    {
      headers,
    }
  );
};

/**
 * Get scanner by ID
 */
export const getScannerById = (scannerId: string) => {
  const headers = getHeaders();
  return api.get<BarcodeScanner>(`/scanner/${scannerId}`, {
    headers,
  });
};
