package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.CartItemDto;
import nextpos.app.nextpos.model.entity.Discount;

import java.math.BigDecimal;
import java.util.List;

public interface DiscountValidationService {

    boolean isDiscountApplicable(
            Discount discount,
            Long customerId,
            Long warehouseId,
            List<CartItemDto> items,
            BigDecimal subtotal);

    BigDecimal calculateDiscountAmount(
            Discount discount,
            BigDecimal subtotal);
}