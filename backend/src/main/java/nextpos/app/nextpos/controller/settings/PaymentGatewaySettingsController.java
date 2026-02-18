package nextpos.app.nextpos.controller.settings;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreatePaymentGatewaySettingRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdatePaymentGatewaySettingRequest;
import nextpos.app.nextpos.model.dto.response.PaymentGatewaySettingsResponse;
import nextpos.app.nextpos.service.interf.PaymentGatewaySettingsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payment-gateway")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class PaymentGatewaySettingsController {

    private final PaymentGatewaySettingsService service;

    // ========== Company-level endpoints ==========

    @PostMapping("/company")
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    @Operation(summary = "Create payment gateway settings for current company")
    public ResponseEntity<PaymentGatewaySettingsResponse> createForCompany(
            @Valid @RequestBody CreatePaymentGatewaySettingRequest request) {
        return new ResponseEntity<>(service.createForCurrentCompany(request), HttpStatus.CREATED);
    }

    @PutMapping("/company")
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    @Operation(summary = "Update payment gateway settings for current company")
    public ResponseEntity<PaymentGatewaySettingsResponse> updateForCompany(
            @Valid @RequestBody UpdatePaymentGatewaySettingRequest request) {
        return ResponseEntity.ok(service.updateForCurrentCompany(request));
    }

    @DeleteMapping("/company/{id}")
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    @Operation(summary = "Delete payment gateway settings for current company")
    public ResponseEntity<Void> deleteForCompany(@PathVariable Long id) {
        service.deleteForCurrentCompany(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/company/{id}")
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    @Operation(summary = "Get a specific gateway setting for current company")
    public ResponseEntity<PaymentGatewaySettingsResponse> getForCompany(@PathVariable Long id) {
        return ResponseEntity.ok(service.getForCurrentCompany(id));
    }

    @GetMapping("/company")
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    @Operation(summary = "List all gateway settings for current company")
    public ResponseEntity<List<PaymentGatewaySettingsResponse>> getAllForCompany() {
        return ResponseEntity.ok(service.getAllForCurrentCompany());
    }

    @GetMapping("/company/paginated")
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    @Operation(summary = "Paginated list of gateway settings for current company")
    public ResponseEntity<Page<PaymentGatewaySettingsResponse>> getPaginatedForCompany(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(service.getForCurrentCompanyPaginated(pageable));
    }

    // ========== System-level endpoints (super admin only) ==========

    @PostMapping("/system")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "Create system-wide payment gateway settings")
    public ResponseEntity<PaymentGatewaySettingsResponse> createSystem(
            @Valid @RequestBody CreatePaymentGatewaySettingRequest request) {
        return new ResponseEntity<>(service.createSystemSettings(request), HttpStatus.CREATED);
    }

    @PutMapping("/system")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "Update system-wide payment gateway settings")
    public ResponseEntity<PaymentGatewaySettingsResponse> updateSystem(
            @Valid @RequestBody UpdatePaymentGatewaySettingRequest request) {
        return ResponseEntity.ok(service.updateSystemSettings(request));
    }

    @DeleteMapping("/system/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "Delete system-wide payment gateway settings")
    public ResponseEntity<Void> deleteSystem(@PathVariable Long id) {
        service.deleteSystemSettings(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/system/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "Get a specific system gateway setting")
    public ResponseEntity<PaymentGatewaySettingsResponse> getSystem(@PathVariable Long id) {
        return ResponseEntity.ok(service.getSystemSettings(id));
    }

    @GetMapping("/system")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "List all system-wide gateway settings")
    public ResponseEntity<List<PaymentGatewaySettingsResponse>> getAllSystem() {
        return ResponseEntity.ok(service.getAllSystemSettings());
    }
}