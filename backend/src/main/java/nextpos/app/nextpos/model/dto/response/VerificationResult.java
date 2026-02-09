package nextpos.app.nextpos.model.dto.response;

import lombok.Data;
import nextpos.app.nextpos.model.enums.VerificationStatus;

import java.time.LocalDateTime;

@Data
public class VerificationResult {
    private boolean success;
    private String email;
    private VerificationStatus status;
    private LocalDateTime verifiedAt;
    private String referenceId;
    private String referenceType;
    private String message;
}