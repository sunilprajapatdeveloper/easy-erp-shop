import { VerificationType } from "@/enums/VerificationType";
import { VerificationStatus } from "@/enums/VerificationStatus";

export interface VerificationRequest {
  email: string;
  verificationType: VerificationType;
  referenceId?: string;
  referenceType?: string;
  metadata?: string;
}

export interface VerificationValidationRequest {
  email: string;
  token: string;
  verificationType: VerificationType;
}

export interface VerificationCreationResponse {
  verificationId: string;
  expiresAt: string;
  message: string;
}

export interface VerificationResult {
  success: boolean;
  email: string;
  status: VerificationStatus;
  verifiedAt?: string;
  referenceId?: string;
  referenceType?: string;
  message?: string;
}

export type { VerificationType, VerificationStatus };
