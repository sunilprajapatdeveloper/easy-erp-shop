export interface BrandingSettings {
  logoLight?: string;
  logoDark?: string;
  favicon?: string;

  primaryColor: string;
  secondaryColor?: string;
  accentColor?: string;
  backgroundColor?: string;
  textColor?: string;

  fontFamily?: string;
  fontSize?: string;

  customTheme?: Record<string, any>;

  isActive?: boolean;

  createdAt?: string; // ISO string
  updatedAt?: string; // ISO string
}

export interface CreateBrandingSettingsRequest {
  logoLight?: string;
  logoDark?: string;
  favicon?: string;

  primaryColor: string;
  secondaryColor?: string;
  accentColor?: string;
  backgroundColor?: string;
  textColor?: string;

  fontFamily?: string;
  fontSize?: string;

  customTheme?: Record<string, any>;

  isActive?: boolean;
}

export interface UpdateBrandingSettingsRequest {
  logoLight?: string;
  logoDark?: string;
  favicon?: string;

  primaryColor?: string;
  secondaryColor?: string;
  accentColor?: string;
  backgroundColor?: string;
  textColor?: string;

  fontFamily?: string;
  fontSize?: string;

  customTheme?: Record<string, any>;

  isActive?: boolean;
}
