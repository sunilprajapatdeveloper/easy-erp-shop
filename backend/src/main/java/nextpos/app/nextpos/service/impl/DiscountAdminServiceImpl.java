package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.exception.ResourceNotFoundException;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateDiscountRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateDiscountRequest;
import nextpos.app.nextpos.model.dto.response.DiscountResponse;
import nextpos.app.nextpos.model.entity.Discount;
import nextpos.app.nextpos.pagination.dto.PaginationResponse;
import nextpos.app.nextpos.repository.DiscountRepository;
import nextpos.app.nextpos.service.interf.DiscountAdminService;
import nextpos.app.nextpos.security.access.WarehouseAccessService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiscountAdminServiceImpl implements DiscountAdminService {

    private final DiscountRepository discountRepository;
    private final WarehouseAccessService warehouseAccessService;

    @Override
    @Transactional
    public DiscountResponse createDiscount(CreateDiscountRequest request,
            Long companyId,
            Long userId) {
        validateWarehouse(request.getWarehouseId());

        Discount discount = Discount.builder()
                .name(request.getName())
                .code(request.getCode())
                .description(request.getDescription())
                .discountType(request.getDiscountType())
                .scope(request.getScope())
                .source(request.getSource())
                .discountValue(request.getDiscountValue())
                .maxDiscountAmount(request.getMaxDiscountAmount())
                .minOrderAmount(request.getMinOrderAmount())
                .maxOrderAmount(request.getMaxOrderAmount())
                .stackable(request.getStackable())
                .autoApply(request.getAutoApply())
                .requiresManagerApproval(request.getRequiresManagerApproval())
                .approvalRequiredAbove(request.getApprovalRequiredAbove())
                .priority(request.getPriority())
                .usageLimit(request.getUsageLimit())
                .usageLimitPerCustomer(request.getUsageLimitPerCustomer())
                .isActive(request.getIsActive())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .warehouseId(request.getWarehouseId())
                .companyId(companyId)
                .createdBy(userId)
                .build();

        discount = discountRepository.save(discount);

        return toResponse(discount);
    }

    @Override
    @Transactional
    public DiscountResponse updateDiscount(Long id,
            UpdateDiscountRequest request,
            Long companyId,
            Long userId) {
        validateWarehouse(request.getWarehouseId());

        Discount discount = discountRepository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Discount not found"));

        discount.setName(request.getName());
        discount.setCode(request.getCode());
        discount.setDescription(request.getDescription());
        discount.setDiscountType(request.getDiscountType());
        discount.setScope(request.getScope());
        discount.setSource(request.getSource());
        discount.setDiscountValue(request.getDiscountValue());
        discount.setMaxDiscountAmount(request.getMaxDiscountAmount());
        discount.setMinOrderAmount(request.getMinOrderAmount());
        discount.setMaxOrderAmount(request.getMaxOrderAmount());
        discount.setStackable(request.getStackable());
        discount.setAutoApply(request.getAutoApply());
        discount.setRequiresManagerApproval(request.getRequiresManagerApproval());
        discount.setApprovalRequiredAbove(request.getApprovalRequiredAbove());
        discount.setPriority(request.getPriority());
        discount.setUsageLimit(request.getUsageLimit());
        discount.setUsageLimitPerCustomer(request.getUsageLimitPerCustomer());
        discount.setIsActive(request.getIsActive());
        discount.setStartDate(request.getStartDate());
        discount.setEndDate(request.getEndDate());
        discount.setWarehouseId(request.getWarehouseId());
        discount.setUpdatedBy(userId);

        discount = discountRepository.save(discount);

        return toResponse(discount);
    }

    @Override
    @Transactional
    public void deleteDiscount(Long id, Long companyId) {

        Discount discount = discountRepository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Discount not found"));

        discountRepository.delete(discount);
    }

    @Override
    @Transactional
    public void toggleActive(Long id, Long companyId) {

        Discount discount = discountRepository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Discount not found"));

        discount.setIsActive(!discount.getIsActive());

        discountRepository.save(discount);
    }

    @Override
    public DiscountResponse getDiscount(Long id, Long companyId) {

        Discount discount = discountRepository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Discount not found"));

        return toResponse(discount);
    }

    @Override
    public PaginationResponse<DiscountResponse> getDiscounts(Long companyId,
            Pageable pageable) {

        Page<Discount> page = discountRepository.findByCompanyId(companyId, pageable);

        return PaginationResponse.of(
                page.map(this::toResponse));
    }

    private DiscountResponse toResponse(Discount d) {

        return DiscountResponse.builder()
                .id(d.getId())
                .name(d.getName())
                .code(d.getCode())
                .description(d.getDescription())
                .discountType(d.getDiscountType())
                .scope(d.getScope())
                .source(d.getSource())
                .discountValue(d.getDiscountValue())
                .maxDiscountAmount(d.getMaxDiscountAmount())
                .minOrderAmount(d.getMinOrderAmount())
                .maxOrderAmount(d.getMaxOrderAmount())
                .stackable(d.getStackable())
                .autoApply(d.getAutoApply())
                .requiresManagerApproval(d.getRequiresManagerApproval())
                .approvalRequiredAbove(d.getApprovalRequiredAbove())
                .priority(d.getPriority())
                .usageLimit(d.getUsageLimit())
                .usageLimitPerCustomer(d.getUsageLimitPerCustomer())
                .isActive(d.getIsActive())
                .startDate(d.getStartDate())
                .endDate(d.getEndDate())
                .warehouseId(d.getWarehouseId())
                .companyId(d.getCompanyId())
                .createdBy(d.getCreatedBy())
                .createdAt(d.getCreatedAt())
                .updatedBy(d.getUpdatedBy())
                .updatedAt(d.getUpdatedAt())
                .build();
    }

    private void validateWarehouse(Long warehouseId) {
        if (warehouseId != null) {
            warehouseAccessService.requireAccessible(warehouseId);
        }
    }
}
