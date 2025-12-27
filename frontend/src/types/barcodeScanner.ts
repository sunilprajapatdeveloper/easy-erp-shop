export interface ScannerRegistrationRequest {
  scannerName: string;
  scannerType: "MOBILE" | "USB" | "BLUETOOTH" | "WIFI";
  warehouseId: number; // Changed to match backend
  assignedUserId: number; // Changed to match backend
  companyId: number;
  ipAddress?: string;
  macAddress?: string;
}

export interface ScannerRegistrationResponse {
  scannerId: string;
  status: string;
  message: string;
}

export interface BarcodeScanRequest {
  scannerId: string;
  barcode: string;
  companyId: number;
  // Remove warehouseId and userId since backend gets them from scanner
}

export interface BarcodeScanResponse {
  scannerId: string;
  barcode: string;
  productId: number;
  productName: string;
  productSku: string;
  price: number;
  stockQuantity: number;
  success: boolean;
  errorMessage?: string;
  timestamp: string;
}

export interface BarcodeScanner {
  id: number;
  scannerId: string;
  name: string;
  type: "MOBILE" | "USB" | "BLUETOOTH" | "WIFI";
  status: "ACTIVE" | "INACTIVE" | "OFFLINE";
  warehouseId: number;
  assignedUserId: number;
  companyId: number;
  createdAt: string;
  lastConnectedAt: string;
  ipAddress?: string;
  macAddress?: string;
}

export interface ScannerStatusUpdateRequest {
  scannerId: string;
  status: string;
}

export interface ScannerStatusResponse {
  scannerId: string;
  status: string;
  message: string;
}

export interface ScannerDisconnectRequest {
  scannerId: string;
}

export interface ScannerDisconnectResponse {
  scannerId: string;
  status: string;
  message: string;
}
