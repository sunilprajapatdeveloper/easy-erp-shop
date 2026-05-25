package nextpos.app.nextpos.controller.discount;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateDiscountRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateDiscountRequest;
import nextpos.app.nextpos.model.dto.response.DiscountResponse;
import nextpos.app.nextpos.pagination.dto.ApiResponse;
import nextpos.app.nextpos.pagination.dto.PaginationResponse;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.service.interf.DiscountAdminService;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/discounts")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class DiscountController {

    private final DiscountAdminService discountAdminService;

    @PostMapping
    @Operation(summary = "Create discount")
    public ResponseEntity<ApiResponse<DiscountResponse>> create(
            @Valid @RequestBody CreateDiscountRequest request) {

        Long companyId = UserContext.getCurrentCompanyId();
        Long userId = UserContext.getCurrentUserId();

        return ResponseEntity.ok(
                ApiResponse.success(
                        discountAdminService.createDiscount(
                                request,
                                companyId,
                                userId)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update discount")
    public ResponseEntity<ApiResponse<DiscountResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDiscountRequest request) {

        Long companyId = UserContext.getCurrentCompanyId();
        Long userId = UserContext.getCurrentUserId();

        return ResponseEntity.ok(
                ApiResponse.success(
                        discountAdminService.updateDiscount(
                                id,
                                request,
                                companyId,
                                userId)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete discount")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id) {

        Long companyId = UserContext.getCurrentCompanyId();

        discountAdminService.deleteDiscount(id, companyId);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PatchMapping("/{id}/toggle")
    @Operation(summary = "Toggle discount active status")
    public ResponseEntity<ApiResponse<Void>> toggle(
            @PathVariable Long id) {

        Long companyId = UserContext.getCurrentCompanyId();

        discountAdminService.toggleActive(id, companyId);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get discount")
    public ResponseEntity<ApiResponse<DiscountResponse>> get(
            @PathVariable Long id) {

        Long companyId = UserContext.getCurrentCompanyId();

        return ResponseEntity.ok(
                ApiResponse.success(
                        discountAdminService.getDiscount(id, companyId)));
    }

    @GetMapping
    @Operation(summary = "Get discounts")
    public ResponseEntity<ApiResponse<PaginationResponse<DiscountResponse>>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Long companyId = UserContext.getCurrentCompanyId();

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(
                ApiResponse.success(
                        discountAdminService.getDiscounts(companyId, pageable)));
    }
}