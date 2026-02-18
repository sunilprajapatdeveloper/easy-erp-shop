export enum VerificationType {
  USER_REGISTRATION = "USER_REGISTRATION",
  PASSWORD_RESET = "PASSWORD_RESET",
  COMPANY_VERIFICATION = "COMPANY_VERIFICATION",
  WAREHOUSE_VERIFICATION = "WAREHOUSE_VERIFICATION",
  EMAIL_CHANGE_CONFIRMATION = "EMAIL_CHANGE_CONFIRMATION",
  EMAIL_OTP_LOGIN = "EMAIL_OTP_LOGIN",
  TWO_FACTOR_AUTH = "TWO_FACTOR_AUTH",
  TRANSACTION_CONFIRMATION = "TRANSACTION_CONFIRMATION",
  DOCUMENT_APPROVAL = "DOCUMENT_APPROVAL"
}

export const VerificationTypeLabels: Record<VerificationType, string> = {
  [VerificationType.USER_REGISTRATION]: "User Registration",
  [VerificationType.PASSWORD_RESET]: "Password Reset",
  [VerificationType.COMPANY_VERIFICATION]: "Company Verification",
  [VerificationType.WAREHOUSE_VERIFICATION]: "Warehouse Verification",
  [VerificationType.EMAIL_CHANGE_CONFIRMATION]: "Email Change Confirmation",
  [VerificationType.EMAIL_OTP_LOGIN]: "Email OTP Login",
  [VerificationType.TWO_FACTOR_AUTH]: "Two-Factor Authentication",
  [VerificationType.TRANSACTION_CONFIRMATION]: "Transaction Confirmation",
  [VerificationType.DOCUMENT_APPROVAL]: "Document Approval"
};