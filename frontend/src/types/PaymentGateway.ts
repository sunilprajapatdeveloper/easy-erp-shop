// Enum matching backend PaymentGatewayProvider
export enum PaymentGatewayProvider {
  STRIPE = "STRIPE",
  PAYPAL = "PAYPAL",
  RAZORPAY = "RAZORPAY",
  PHONEPE = "PHONEPE",
  GOOGLEPAY = "GOOGLEPAY",
  UNKNOWN = "UNKNOWN",
}

// Response DTO (masked values)
export interface PaymentGatewaySettings {
  id: number;
  companyId: number | null; // null for system‑wide settings
  gatewayType: PaymentGatewayProvider;
  publicKeyMasked: string;
  secretKeyMasked: string;
  merchantIdMasked: string | null;
  currency: string | null;
  enabled: boolean;
  webhookSecretMasked: string | null;
  sandboxMode: boolean;
}

// Request for creating settings
export interface CreatePaymentGatewaySettingsRequest {
  gatewayType: PaymentGatewayProvider;
  publicKey?: string;
  secretKey?: string;
  merchantId?: string;
  currency?: string;
  enabled: boolean;
  webhookSecret?: string;
  sandboxMode?: boolean;
}

// Request for updating settings
export interface UpdatePaymentGatewaySettingsRequest {
  id: number;
  gatewayType?: PaymentGatewayProvider;
  publicKey?: string;
  secretKey?: string;
  merchantId?: string;
  currency?: string;
  enabled?: boolean;
  webhookSecret?: string;
  sandboxMode?: boolean;
}

// Paginated response (for company paginated endpoint)
export interface PaginatedPaymentGatewaySettings {
  content: PaymentGatewaySettings[];
  pageable: any;
  totalPages: number;
  totalElements: number;
}
