package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.VerificationRequest;
import nextpos.app.nextpos.model.dto.request.VerificationValidationRequest;
import nextpos.app.nextpos.model.dto.response.VerificationCreationResponse;
import nextpos.app.nextpos.model.dto.response.VerificationResult;
import nextpos.app.nextpos.model.enums.VerificationStatus;
import nextpos.app.nextpos.model.enums.VerificationType;

public interface EmailVerificationService {
    VerificationCreationResponse createVerification(VerificationRequest request);

    VerificationResult validateVerification(VerificationValidationRequest validationRequest);

    void resendVerification(String email, VerificationType verificationType);

    void revokeVerification(String verificationId);

    VerificationStatus checkStatus(String verificationId);
}