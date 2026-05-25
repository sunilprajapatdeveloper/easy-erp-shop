package nextpos.app.nextpos.service.impl;

import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.CartItemDto;
import nextpos.app.nextpos.model.entity.Discount;
import nextpos.app.nextpos.model.enums.DiscountType;
import nextpos.app.nextpos.service.interf.DiscountValidationService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class DiscountValidationServiceImpl implements DiscountValidationService {

    @Override
    public boolean isDiscountApplicable(
            Discount discount,
            Long customerId,
            Long warehouseId,
            List<CartItemDto> items,
            BigDecimal subtotal) {

        if (!discount.getIsActive())
            return false;

        LocalDateTime now = LocalDateTime.now();

        if (discount.getStartDate() != null &&
                now.isBefore(discount.getStartDate()))
            return false;

        if (discount.getEndDate() != null &&
                now.isAfter(discount.getEndDate()))
            return false;

        if (discount.getWarehouseId() != null &&
                !discount.getWarehouseId().equals(warehouseId))
            return false;

        if (discount.getMinOrderAmount() != null &&
                subtotal.compareTo(discount.getMinOrderAmount()) < 0)
            return false;

        if (discount.getMaxOrderAmount() != null &&
                subtotal.compareTo(discount.getMaxOrderAmount()) > 0)
            return false;

        return true;
    }

    @Override
    public BigDecimal calculateDiscountAmount(
            Discount discount,
            BigDecimal subtotal) {

        BigDecimal amount = BigDecimal.ZERO;

        if (discount.getDiscountType() == DiscountType.PERCENTAGE) {

            amount = subtotal.multiply(
                    discount.getDiscountValue()
                            .divide(BigDecimal.valueOf(100)));

        } else {

            amount = discount.getDiscountValue();
        }

        if (discount.getMaxDiscountAmount() != null &&
                amount.compareTo(discount.getMaxDiscountAmount()) > 0) {

            amount = discount.getMaxDiscountAmount();
        }

        return amount;
    }
}