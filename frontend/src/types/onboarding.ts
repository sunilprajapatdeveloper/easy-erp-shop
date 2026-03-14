export interface UserData {
  name: string;
  email: string;
  phone: string;
  password: string;
}

export interface CompanyData {
  // Basic Information
  legalName: string;
  tradingName: string;
  businessType: string;
  industry: string;
  email: string;
  phone: string;
  taxId?: string;
  registrationNumber?: string;

  // Address
  address: {
    street: string;
    city: string;
    state: string;
    postalCode: string;
    country: string;
  };

  // Settings
  timezone: string;
  primaryCurrency: string;
  additionalCurrencies: string[];
  subdomain: string;

  // Branding
  logo?: string | null;
  brandColor: string;

  // Features
  enableMultiWarehouse: boolean;
  enableMultiCurrency: boolean;
  enableInventoryTracking: boolean;
  enablePos: boolean;
  enableEcommerce: boolean;
}

export interface PlanData {
  subscriptionPlanId: number;
  billingCycle: 'monthly' | 'annual';
  price: number;
  isTrial: boolean;
  trialEndDate?: string;
}

export interface PaymentInfo {
  cardNumber: string;
  cardholderName: string;
  expiry: string;
}

// Define a type for warehouse type
export type WarehouseType =
  | "retail"
  | "wholesale"
  | "manufacturing"
  | "storage"
  | "distribution"
  | "ecommerce";

export interface WarehouseData {
  name: string;
  code: string;
  type: WarehouseType;
  address: {
    street: string;
    city: string;
    state: string;
    postalCode: string;
    country: string;
  };
  managerName: string;
  managerEmail: string;
  phone: string;
  currency: string;
  isDefault: boolean;
  settings: {
    enableInventoryTracking: boolean;
    enableBarcode: boolean;
    lowStockAlert: number;
    requireApproval: boolean;
    enablePos?: boolean;
    defaultTaxRate?: number;
    enableReceiving?: boolean;
    enableQualityCheck?: boolean;
    defaultSupplier?: string;
  };
}

export interface ConfigData {
  // POS Configuration
  posSettings: {
    receiptHeader: string;
    receiptFooter: string;
    defaultTaxRate: number;
    roundOffAmount: boolean;
    enableCustomerDisplay: boolean;
    enableCashDrawer: boolean;
    printReceipt: boolean;
    emailReceipt: boolean;
  };

  // Inventory Settings
  inventorySettings: {
    enableBatchTracking: boolean;
    enableExpiryTracking: boolean;
    defaultReorderPoint: number;
    autoUpdateStock: boolean;
  };

  // Sales Settings
  salesSettings: {
    defaultPaymentMethod: string;
    defaultCustomer: string;
    enableDiscount: boolean;
    enableServiceCharges: boolean;
  };

  // Purchase Settings
  purchaseSettings: {
    defaultSupplier: string;
    enablePurchaseOrder: boolean;
    autoCreateGRN: boolean;
  };

  // Notification Settings
  notificationSettings: {
    emailNotifications: boolean;
    pushNotifications: boolean;
    lowStockAlerts: boolean;
    dailyReports: boolean;
    weeklyReports?: boolean;
  };
}
