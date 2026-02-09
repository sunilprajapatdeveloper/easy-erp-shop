package nextpos.app.nextpos.controller.verification;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.entity.EmailVerification;
import nextpos.app.nextpos.model.enums.VerificationStatus;
import nextpos.app.nextpos.repository.EmailVerificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/verifications")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Verification Admin", description = "Admin verification management APIs")
public class VerificationAdminController {

    private final EmailVerificationRepository verificationRepository;

    @GetMapping
    @Operation(summary = "List all verifications")
    public ResponseEntity<Page<EmailVerification>> listVerifications(Pageable pageable) {
        Page<EmailVerification> verifications = verificationRepository.findAll(pageable);
        return ResponseEntity.ok(verifications);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get verification details")
    public ResponseEntity<EmailVerification> getVerification(@PathVariable UUID id) {
        return verificationRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Force revoke verification")
    public ResponseEntity<Void> forceRevoke(@PathVariable UUID id) {
        verificationRepository.findById(id).ifPresent(verification -> {
            verification.setStatus(VerificationStatus.REVOKED);
            verificationRepository.save(verification);
        });
        return ResponseEntity.noContent().build();
    }
}