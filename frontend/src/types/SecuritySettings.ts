export interface SecuritySettings {
  enforceTwoFactorAuth: boolean;
  enforcePasswordPolicy: boolean;
  restrictIpAccess: boolean;
  allowedIpRanges?: string;
  maxLoginAttempts: number;
  accountLockDurationMinutes: number;
  sessionTimeoutMinutes: number;
  passwordExpiryDays: number;
  requireStrongPasswords: boolean;
  allowDeviceTrust: boolean;
}

export interface CreateSecuritySettingsRequest {
  enforceTwoFactorAuth: boolean;
  enforcePasswordPolicy: boolean;
  restrictIpAccess: boolean;
  allowedIpRanges?: string;
  maxLoginAttempts: number;
  accountLockDurationMinutes: number;
  sessionTimeoutMinutes: number;
  passwordExpiryDays: number;
  requireStrongPasswords: boolean;
  allowDeviceTrust: boolean;
}

export interface UpdateSecuritySettingsRequest {
  enforceTwoFactorAuth: boolean;
  enforcePasswordPolicy: boolean;
  restrictIpAccess: boolean;
  allowedIpRanges?: string;
  maxLoginAttempts: number;
  accountLockDurationMinutes: number;
  sessionTimeoutMinutes: number;
  passwordExpiryDays: number;
  requireStrongPasswords: boolean;
  allowDeviceTrust: boolean;
}
