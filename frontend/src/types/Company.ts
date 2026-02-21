import { BrandingSettings } from "./BrandingSettings";
import { CompanySubscription } from "./CompanySubscription";
import { CompanyCurrency } from "./CompanyCurrency";
import { LoyaltySettings } from "./LoyaltySettings";
import { OnlineOrderingSettings } from "./OnlineOrderingSettings";
import { SecuritySettings } from "./SecuritySettings";
import { ShippingProviderSettings } from "./ShippingProviderSettings";
import { SocialMediaSettings } from "./SocialMediaSettings";
import { TaxSetting } from "./TaxSetting";

export interface CompanyListItem {
  id: number;
  companyName: string;
  phone: string;
  email: string;
  country?: string;
  state?: string;
  city?: string;
  address?: string;
  isActive: boolean;
}

export interface CompanyDetail {
  id: number;
  companyName: string;
  phone: string;
  email: string;
  registrationNumber?: string;

  country?: string;
  state?: string;
  city?: string;
  address?: string;
  postalCode?: string;
  timezone?: string;

  createdBy: number | null;
  createdAt: string;
  updatedBy: number | null;
  updatedAt: string | null;

  isActive: boolean;
  isDeleted: boolean;

  // Nested Settings
  onlineOrderingSettings?: OnlineOrderingSettings;
  securitySettings?: SecuritySettings;
  shippingProviderSettings?: ShippingProviderSettings;
  socialMediaSettings?: SocialMediaSettings;
  loyaltySettings?: LoyaltySettings;
  brandingSettings?: BrandingSettings;
  taxSetting?: TaxSetting;
  currencySetting?: CompanyCurrency;
  subscription?: CompanySubscription;
}

export interface CreateCompanyRequest {
  companyName: string;
  phone: string;
  email: string;
  registrationNumber?: string;
  country?: string;
  state?: string;
  city?: string;
  address?: string;
  postalCode?: string;
  timezone?: string;
  enableOnlineOrdering?: boolean;
  enableLoyaltyProgram?: boolean;
}

export interface UpdateCompanyRequest {
  companyName?: string;
  phone?: string;
  email?: string;
  registrationNumber?: string;
  country?: string;
  state?: string;
  city?: string;
  address?: string;
  postalCode?: string;
  timezone?: string;
  enableOnlineOrdering?: boolean;
  enableLoyaltyProgram?: boolean;
  isActive?: boolean;
  isDeleted?: boolean;
}

export type Company = CompanyDetail;
