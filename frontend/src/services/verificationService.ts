import api from "./api";
import type {
  VerificationRequest,
  VerificationValidationRequest,
  VerificationCreationResponse,
  VerificationResult,
  VerificationStatus,
  VerificationType,
} from "@/types/Verification";

/**
 * Initiate a verification request (sends token)
 */
export const requestVerification = (data: VerificationRequest) =>
  api.post<VerificationCreationResponse>("/verifications", data);

/**
 * Validate a verification token
 */
export const validateVerification = (data: VerificationValidationRequest) =>
  api.post<VerificationResult>("/verifications/validate", data);

/**
 * Resend verification email
 */
export const resendVerification = (
  email: string,
  verificationType: VerificationType,
) =>
  api.post<void>("/verifications/resend", null, {
    params: { email, verificationType },
  });

/**
 * Check the current status of a verification
 */
export const checkVerificationStatus = (verificationId: string) =>
  api.get<VerificationStatus>(`/verifications/${verificationId}/status`);

/**
 * Revoke/cancel a verification
 */
export const revokeVerification = (verificationId: string) =>
  api.delete<void>(`/verifications/${verificationId}`);
