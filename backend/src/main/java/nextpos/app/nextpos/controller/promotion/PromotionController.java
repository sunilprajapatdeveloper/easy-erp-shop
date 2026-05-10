package nextpos.app.nextpos.controller.promotion;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CouponValidationRequest;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreatePromotionRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdatePromotionRequest;
import nextpos.app.nextpos.model.dto.response.CouponValidationResponse;
import nextpos.app.nextpos.model.dto.response.PromotionResponse;
import nextpos.app.nextpos.pagination.dto.ApiResponse;
import nextpos.app.nextpos.pagination.dto.PaginationResponse;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.service.interf.PromotionAdminService;
import nextpos.app.nextpos.service.interf.PromotionEngineService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/promotions")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class PromotionController {

    private final PromotionAdminService adminService;
    private final PromotionEngineService engineService;

    @PostMapping("/validate")
    @Operation(summary = "Validate a coupon code and get discount details")
    public ResponseEntity<ApiResponse<CouponValidationResponse>> validateCoupon(@Valid @RequestBody CouponValidationRequest request) {
        CouponValidationResponse response = engineService.validateCoupon(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping
    @Operation(summary = "Create a new promotion (admin only)")
    public ResponseEntity<ApiResponse<PromotionResponse>> createPromotion(@Valid @RequestBody CreatePromotionRequest request) {
        Long companyId = UserContext.getCurrentCompanyId();
        Long userId = UserContext.getCurrentUserId();
        PromotionResponse response = adminService.createPromotion(request, companyId, userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing promotion")
    public ResponseEntity<ApiResponse<PromotionResponse>> updatePromotion(@PathVariable Long id,
                                                                          @Valid @RequestBody UpdatePromotionRequest request) {
        Long companyId = UserContext.getCurrentCompanyId();
        Long userId = UserContext.getCurrentUserId();
        PromotionResponse response = adminService.updatePromotion(id, request, companyId, userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a promotion (soft delete via isActive false, but here we physically delete)")
    public ResponseEntity<ApiResponse<Void>> deletePromotion(@PathVariable Long id) {
        Long companyId = UserContext.getCurrentCompanyId();
        adminService.deletePromotion(id, companyId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PatchMapping("/{id}/toggle")
    @Operation(summary = "Activate or deactivate a promotion")
    public ResponseEntity<ApiResponse<Void>> togglePromotion(@PathVariable Long id) {
        Long companyId = UserContext.getCurrentCompanyId();
        adminService.toggleActive(id, companyId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get promotion by ID")
    public ResponseEntity<ApiResponse<PromotionResponse>> getPromotion(@PathVariable Long id) {
        Long companyId = UserContext.getCurrentCompanyId();
        PromotionResponse response = adminService.getPromotion(id, companyId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @Operation(summary = "List all promotions for the company")
    public ResponseEntity<ApiResponse<PaginationResponse<PromotionResponse>>> listPromotions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Long companyId = UserContext.getCurrentCompanyId();
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        PaginationResponse<PromotionResponse> response = adminService.getPromotions(companyId, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}