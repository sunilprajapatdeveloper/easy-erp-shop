package nextpos.app.nextpos.controller.verification;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.VerificationRequest;
import nextpos.app.nextpos.model.dto.request.VerificationValidationRequest;
import nextpos.app.nextpos.model.dto.response.VerificationCreationResponse;
import nextpos.app.nextpos.model.dto.response.VerificationResult;
import nextpos.app.nextpos.model.enums.VerificationStatus;
import nextpos.app.nextpos.model.enums.VerificationType;
import nextpos.app.nextpos.service.interf.EmailVerificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/verifications")
@RequiredArgsConstructor
@Tag(name = "Verification", description = "Operations related to identity and email verification")
public class VerificationController {

    private final EmailVerificationService verificationService;

    @PostMapping
    @Operation(summary = "Initiate verification", description = "Creates a new verification request and sends a token.")
    @ApiResponse(responseCode = "201", description = "Verification created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request")
    @ApiResponse(responseCode = "429", description = "Too many requests")
    public ResponseEntity<VerificationCreationResponse> requestVerification(
            @Valid @RequestBody VerificationRequest request) {
        var response = verificationService.createVerification(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/validate")
    @Operation(summary = "Validate verification token")
    @ApiResponse(responseCode = "200", description = "Token validated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid token")
    @ApiResponse(responseCode = "404", description = "Verification not found")
    @ApiResponse(responseCode = "410", description = "Token expired")
    @ApiResponse(responseCode = "429", description = "Too many attempts")
    public ResponseEntity<VerificationResult> validateVerification(
            @Valid @RequestBody VerificationValidationRequest request) {
        return ResponseEntity.ok(verificationService.validateVerification(request));
    }

    @PostMapping("/resend")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Resend verification email")
    @ApiResponse(responseCode = "202", description = "Resend request accepted")
    @ApiResponse(responseCode = "429", description = "Too many resend requests")
    public void resendVerification(
            @RequestParam @Valid String email,
            @RequestParam VerificationType verificationType) {
        verificationService.resendVerification(email, verificationType);
    }

    @GetMapping("/{verificationId}/status")
    @Operation(summary = "Check verification status")
    @ApiResponse(responseCode = "200", description = "Status retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Verification not found")
    public ResponseEntity<VerificationStatus> checkStatus(@PathVariable String verificationId) {
        return ResponseEntity.ok(verificationService.checkStatus(verificationId));
    }

    @DeleteMapping("/{verificationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Revoke/Cancel verification")
    @ApiResponse(responseCode = "204", description = "Verification revoked successfully")
    @ApiResponse(responseCode = "404", description = "Verification not found")
    public void revokeVerification(@PathVariable String verificationId) {
        verificationService.revokeVerification(verificationId);
    }
}