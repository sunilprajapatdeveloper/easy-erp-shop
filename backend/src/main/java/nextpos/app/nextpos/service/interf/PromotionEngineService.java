package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.CartItemDto;
import nextpos.app.nextpos.model.dto.request.CouponValidationRequest;
import nextpos.app.nextpos.model.dto.response.CouponValidationResponse;

import java.math.BigDecimal;
import java.util.List;

public interface PromotionEngineService {
    CouponValidationResponse validateCoupon(CouponValidationRequest request);

    List<CouponValidationResponse> getAutoApplicablePromotions(Long companyId, Long warehouseId, Long customerId,
            List<CartItemDto> items, BigDecimal shippingCost,
            String currencyCode);

    void recordPromotionUsage(Long promotionId, Long saleId, Long customerId, Long companyId);
}