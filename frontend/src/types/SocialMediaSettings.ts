import { SocialMediaPlatform } from "@/enums/SocialMediaPlatform";

export interface SocialMediaSettings {
  id: number;
  companyId: number;
  platform: SocialMediaPlatform;
  profileUrl?: string;
  username?: string;
  apiKey: string;
  apiSecret: string;
  accessToken?: string;
  enabled: boolean;
  providerConfig?: Record<string, any>;
  createdBy?: number;
  createdAt?: string;
  updatedBy?: number;
  updatedAt?: string;
}

export interface CreateSocialMediaSettingsRequest {
  companyId: number;
  platform: SocialMediaPlatform;
  profileUrl?: string;
  username?: string;
  apiKey: string;
  apiSecret: string;
  accessToken?: string;
  enabled?: boolean;
  providerConfig?: Record<string, any>;
}

export interface UpdateSocialMediaSettingsRequest {
  profileUrl?: string;
  username?: string;
  apiKey?: string;
  apiSecret?: string;
  accessToken?: string;
  enabled?: boolean;
  providerConfig?: Record<string, any>;
}
