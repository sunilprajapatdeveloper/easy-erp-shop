package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.exception.ResourceNotFoundException;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreatePromotionRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdatePromotionRequest;
import nextpos.app.nextpos.model.dto.response.PromotionResponse;
import nextpos.app.nextpos.model.entity.*;
import nextpos.app.nextpos.model.enums.CustomerGroup;
import nextpos.app.nextpos.pagination.dto.PaginationResponse;
import nextpos.app.nextpos.repository.*;
import nextpos.app.nextpos.service.interf.PromotionAdminService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PromotionAdminServiceImpl implements PromotionAdminService {

    private final PromotionRepository promotionRepository;
    private final PromotionProductRepository productRepository;
    private final PromotionCategoryRepository categoryRepository;
    private final PromotionCustomerGroupRepository customerGroupRepository;
    private final ProductRepository productJpaRepository;
    private final CategoryRepository categoryJpaRepository;

    @Override
    @Transactional
    @CacheEvict(value = "activePromotions", allEntries = true)
    public PromotionResponse createPromotion(CreatePromotionRequest request, Long companyId, Long userId) {
        Promotion promotion = Promotion.builder()
                .name(request.getName())
                .code(request.getCode())
                .description(request.getDescription())
                .type(request.getType())
                .discountType(request.getDiscountType())
                .discountValue(request.getDiscountValue())
                .maxDiscountAmount(request.getMaxDiscountAmount())
                .minOrderAmount(request.getMinOrderAmount())
                .maxOrderAmount(request.getMaxOrderAmount())
                .usageLimit(request.getUsageLimit())
                .usageLimitPerCustomer(request.getUsageLimitPerCustomer())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .stackingPriority(request.getStackingPriority())
                .stackingStrategy(request.getStackingStrategy())
                .buyQuantity(request.getBuyQuantity())
                .getQuantity(request.getGetQuantity())
                .getDiscountPercent(request.getGetDiscountPercent())
                .buyProduct(request.getBuyProductId() != null
                        ? productJpaRepository.findById(request.getBuyProductId()).orElse(null)
                        : null)
                .getProduct(request.getGetProductId() != null
                        ? productJpaRepository.findById(request.getGetProductId()).orElse(null)
                        : null)
                .warehouseId(request.getWarehouseId())
                .companyId(companyId)
                .createdBy(userId)
                .build();
        promotion = promotionRepository.save(promotion);

        // Save mappings
        if (request.getProductIds() != null) {
            for (Long productId : request.getProductIds()) {
                Product product = productJpaRepository.getReferenceById(productId);
                PromotionProduct pp = PromotionProduct.builder()
                        .id(new PromotionProduct.PromotionProductId(promotion.getId(), productId))
                        .promotion(promotion)
                        .product(product)
                        .build();
                productRepository.save(pp);
            }
        }
        if (request.getCategoryIds() != null) {
            for (Long categoryId : request.getCategoryIds()) {
                Category category = categoryJpaRepository.getReferenceById(categoryId);
                PromotionCategory pc = PromotionCategory.builder()
                        .id(new PromotionCategory.PromotionCategoryId(promotion.getId(), categoryId))
                        .promotion(promotion)
                        .category(category)
                        .build();
                categoryRepository.save(pc);
            }
        }
        if (request.getCustomerGroups() != null) {
            for (CustomerGroup group : request.getCustomerGroups()) {
                PromotionCustomerGroup pcg = PromotionCustomerGroup.builder()
                        .promotion(promotion)
                        .customerGroup(group)
                        .build();
                customerGroupRepository.save(pcg);
            }
        }

        return toResponse(promotion);
    }

    @Override
    @Transactional
    @CacheEvict(value = "activePromotions", allEntries = true)
    public PromotionResponse updatePromotion(Long id, UpdatePromotionRequest request, Long companyId, Long userId) {
        Promotion promotion = promotionRepository.findById(id)
                .filter(p -> p.getCompanyId().equals(companyId))
                .orElseThrow(() -> new ResourceNotFoundException("Promotion not found"));

        promotion.setName(request.getName());
        promotion.setCode(request.getCode());
        promotion.setDescription(request.getDescription());
        promotion.setType(request.getType());
        promotion.setDiscountType(request.getDiscountType());
        promotion.setDiscountValue(request.getDiscountValue());
        promotion.setMaxDiscountAmount(request.getMaxDiscountAmount());
        promotion.setMinOrderAmount(request.getMinOrderAmount());
        promotion.setMaxOrderAmount(request.getMaxOrderAmount());
        promotion.setUsageLimit(request.getUsageLimit());
        promotion.setUsageLimitPerCustomer(request.getUsageLimitPerCustomer());
        promotion.setStartDate(request.getStartDate());
        promotion.setEndDate(request.getEndDate());
        promotion.setIsActive(request.getIsActive());
        promotion.setStackingPriority(request.getStackingPriority());
        promotion.setStackingStrategy(request.getStackingStrategy());
        promotion.setBuyQuantity(request.getBuyQuantity());
        promotion.setGetQuantity(request.getGetQuantity());
        promotion.setGetDiscountPercent(request.getGetDiscountPercent());
        if (request.getBuyProductId() != null)
            promotion.setBuyProduct(productJpaRepository.getReferenceById(request.getBuyProductId()));
        if (request.getGetProductId() != null)
            promotion.setGetProduct(productJpaRepository.getReferenceById(request.getGetProductId()));
        promotion.setWarehouseId(request.getWarehouseId());
        promotion.setUpdatedBy(userId);

        promotion = promotionRepository.save(promotion);

        // Update mappings
        productRepository.deleteByIdPromotionId(promotion.getId());
        if (request.getProductIds() != null) {
            for (Long productId : request.getProductIds()) {
                Product product = productJpaRepository.getReferenceById(productId);
                PromotionProduct pp = PromotionProduct.builder()
                        .id(new PromotionProduct.PromotionProductId(promotion.getId(), productId))
                        .promotion(promotion)
                        .product(product)
                        .build();
                productRepository.save(pp);
            }
        }
        categoryRepository.deleteByIdPromotionId(promotion.getId());
        if (request.getCategoryIds() != null) {
            for (Long categoryId : request.getCategoryIds()) {
                Category category = categoryJpaRepository.getReferenceById(categoryId);
                PromotionCategory pc = PromotionCategory.builder()
                        .id(new PromotionCategory.PromotionCategoryId(promotion.getId(), categoryId))
                        .promotion(promotion)
                        .category(category)
                        .build();
                categoryRepository.save(pc);
            }
        }
        customerGroupRepository.deleteByPromotionId(promotion.getId());
        if (request.getCustomerGroups() != null) {
            for (CustomerGroup group : request.getCustomerGroups()) {
                PromotionCustomerGroup pcg = PromotionCustomerGroup.builder()
                        .promotion(promotion)
                        .customerGroup(group)
                        .build();
                customerGroupRepository.save(pcg);
            }
        }

        return toResponse(promotion);
    }

    @Override
    @Transactional
    @CacheEvict(value = "activePromotions", allEntries = true)
    public void deletePromotion(Long id, Long companyId) {
        Promotion promotion = promotionRepository.findById(id)
                .filter(p -> p.getCompanyId().equals(companyId))
                .orElseThrow(() -> new ResourceNotFoundException("Promotion not found"));
        promotionRepository.delete(promotion);
    }

    @Override
    @Transactional
    @CacheEvict(value = "activePromotions", allEntries = true)
    public void toggleActive(Long id, Long companyId) {
        Promotion promotion = promotionRepository.findById(id)
                .filter(p -> p.getCompanyId().equals(companyId))
                .orElseThrow(() -> new ResourceNotFoundException("Promotion not found"));
        promotion.setIsActive(!promotion.getIsActive());
        promotionRepository.save(promotion);
    }

    @Override
    public PromotionResponse getPromotion(Long id, Long companyId) {
        Promotion promotion = promotionRepository.findById(id)
                .filter(p -> p.getCompanyId().equals(companyId))
                .orElseThrow(() -> new ResourceNotFoundException("Promotion not found"));
        return toResponse(promotion);
    }

    @Override
    public PaginationResponse<PromotionResponse> getPromotions(Long companyId, Pageable pageable) {
        Page<Promotion> page = promotionRepository.findByCompanyId(companyId, pageable);
        return PaginationResponse.of(page.map(this::toResponse));
    }

    private PromotionResponse toResponse(Promotion p) {
        return PromotionResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .code(p.getCode())
                .description(p.getDescription())
                .type(p.getType())
                .discountType(p.getDiscountType())
                .discountValue(p.getDiscountValue())
                .maxDiscountAmount(p.getMaxDiscountAmount())
                .minOrderAmount(p.getMinOrderAmount())
                .maxOrderAmount(p.getMaxOrderAmount())
                .usageLimit(p.getUsageLimit())
                .usageLimitPerCustomer(p.getUsageLimitPerCustomer())
                .startDate(p.getStartDate())
                .endDate(p.getEndDate())
                .isActive(p.getIsActive())
                .stackingPriority(p.getStackingPriority())
                .stackingStrategy(p.getStackingStrategy())
                .buyQuantity(p.getBuyQuantity())
                .getQuantity(p.getGetQuantity())
                .getDiscountPercent(p.getGetDiscountPercent())
                .buyProductId(p.getBuyProduct() != null ? p.getBuyProduct().getId() : null)
                .getProductId(p.getGetProduct() != null ? p.getGetProduct().getId() : null)
                .warehouseId(p.getWarehouseId())
                .companyId(p.getCompanyId())
                .createdBy(p.getCreatedBy())
                .createdAt(p.getCreatedAt())
                .updatedBy(p.getUpdatedBy())
                .updatedAt(p.getUpdatedAt())
                .productIds(productRepository.findProductIdsByPromotionId(p.getId()))
                .categoryIds(categoryRepository.findCategoryIdsByPromotionId(p.getId()))
                .customerGroups(customerGroupRepository.findGroupsByPromotionId(p.getId()))
                .build();
    }
}