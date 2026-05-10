package nextpos.app.nextpos.model.dto.request;

import lombok.Data;
import nextpos.app.nextpos.model.dto.CartItemDto;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CouponValidationRequest {
    private String couponCode;
    private Long customerId;
    private Long warehouseId;
    private Long companyId;
    private String currencyCode;
    private List<CartItemDto> items;
    private BigDecimal shippingCost;
}