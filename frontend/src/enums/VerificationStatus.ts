export enum VerificationStatus {
  PENDING = "PENDING",
  VERIFIED = "VERIFIED",
  EXPIRED = "EXPIRED",
  FAILED = "FAILED",
  LOCKED = "LOCKED",
  REVOKED = "REVOKED",
}

export const VerificationStatusLabels: Record<VerificationStatus, string> = {
  [VerificationStatus.PENDING]: "Pending",
  [VerificationStatus.VERIFIED]: "Verified",
  [VerificationStatus.EXPIRED]: "Expired",
  [VerificationStatus.FAILED]: "Failed",
  [VerificationStatus.LOCKED]: "Locked",
  [VerificationStatus.REVOKED]: "Revoked",
};
