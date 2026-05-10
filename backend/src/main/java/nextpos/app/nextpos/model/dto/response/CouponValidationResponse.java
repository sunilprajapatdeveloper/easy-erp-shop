package nextpos.app.nextpos.model.dto.response;

import lombok.Builder;
import lombok.Data;
import nextpos.app.nextpos.model.enums.DiscountType;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CouponValidationResponse {
    private boolean valid;
    private String message;
    private BigDecimal discountAmount;
    private DiscountType discountType;
    private BigDecimal appliedDiscountValue;
    private Long appliedPromotionId;
    private String appliedPromotionName;
    private List<AppliedProductDiscount> productDiscounts;
    private boolean freeShipping;
}