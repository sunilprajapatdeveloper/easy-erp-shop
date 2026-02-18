import { defineStore } from "pinia";
import type {
  VerificationRequest,
  VerificationValidationRequest,
  VerificationCreationResponse,
  VerificationResult,
  VerificationStatus,
  VerificationType,
} from "@/types/Verification";
import {
  requestVerification,
  validateVerification,
  resendVerification,
  checkVerificationStatus,
  revokeVerification,
} from "@/services/verificationService";

interface VerificationState {
  loading: boolean;
  error: string | null;
  lastCreation: VerificationCreationResponse | null;
  lastValidation: VerificationResult | null;
  currentStatus: VerificationStatus | null;
}

export const useVerificationStore = defineStore("verification", {
  state: (): VerificationState => ({
    loading: false,
    error: null,
    lastCreation: null,
    lastValidation: null,
    currentStatus: null,
  }),

  actions: {
    /**
     * Request a new verification (sends email)
     */
    async request(payload: VerificationRequest) {
      this.loading = true;
      this.error = null;
      try {
        const response = await requestVerification(payload);
        this.lastCreation = response.data;
        return response.data;
      } catch (err: any) {
        this.error =
          err.response?.data?.message ||
          err.message ||
          "Failed to request verification";
        throw err;
      } finally {
        this.loading = false;
      }
    },

    /**
     * Validate a verification token
     */
    async validate(payload: VerificationValidationRequest) {
      this.loading = true;
      this.error = null;
      try {
        const response = await validateVerification(payload);
        this.lastValidation = response.data;
        return response.data;
      } catch (err: any) {
        this.error =
          err.response?.data?.message || err.message || "Validation failed";
        throw err;
      } finally {
        this.loading = false;
      }
    },

    /**
     * Resend verification email
     */
    async resend(email: string, verificationType: VerificationType) {
      this.loading = true;
      this.error = null;
      try {
        await resendVerification(email, verificationType);
      } catch (err: any) {
        this.error =
          err.response?.data?.message || err.message || "Failed to resend";
        throw err;
      } finally {
        this.loading = false;
      }
    },

    /**
     * Check the status of a verification
     */
    async checkStatus(verificationId: string) {
      this.loading = true;
      this.error = null;
      try {
        const response = await checkVerificationStatus(verificationId);
        this.currentStatus = response.data;
        return response.data;
      } catch (err: any) {
        this.error =
          err.response?.data?.message ||
          err.message ||
          "Failed to check status";
        throw err;
      } finally {
        this.loading = false;
      }
    },

    /**
     * Revoke a verification
     */
    async revoke(verificationId: string) {
      this.loading = true;
      this.error = null;
      try {
        await revokeVerification(verificationId);
        // Optionally clear related state
        if (this.lastCreation?.verificationId === verificationId) {
          this.lastCreation = null;
        }
        if (this.currentStatus) {
          this.currentStatus = null;
        }
      } catch (err: any) {
        this.error =
          err.response?.data?.message || err.message || "Failed to revoke";
        throw err;
      } finally {
        this.loading = false;
      }
    },

    /**
     * Reset store state
     */
    reset() {
      this.$reset();
    },
  },
});
