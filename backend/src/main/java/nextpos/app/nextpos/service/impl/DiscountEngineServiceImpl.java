package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.entity.*;
import nextpos.app.nextpos.model.enums.DiscountSource;
import nextpos.app.nextpos.model.enums.DiscountType;
import nextpos.app.nextpos.repository.DiscountRepository;
import nextpos.app.nextpos.repository.DiscountProductRepository;
import nextpos.app.nextpos.service.interf.DiscountEngineService;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscountEngineServiceImpl implements DiscountEngineService {

    private final DiscountRepository discountRepository;
    private final DiscountProductRepository discountProductRepository; // NEW

    @Override
    public void applyProductDiscounts(Sale sale) {
        // Load all active product-level discounts that match the sale context
        List<Discount> productDiscounts = discountRepository.findActiveProductDiscounts(
                sale.getCompanyId(), sale.getWarehouse().getId(), sale.getDate());

        for (SaleProduct sp : sale.getProducts()) {
            BigDecimal lineDiscount = BigDecimal.ZERO;
            for (Discount discount : productDiscounts) {
                if (discountAppliesToProduct(discount, sp.getProduct().getId())) {
                    BigDecimal amount = calculateDiscountAmount(discount, sp.getProductUnitPrice(), sp.getQuantity());
                    lineDiscount = lineDiscount.add(amount);
                }
            }
            sp.setLineDiscountAmount(lineDiscount);
        }
    }

    @Override
    public void applyOrderDiscount(Sale sale) {
        // Manual discount
        if (sale.getOrderDiscountType() != null && sale.getOrderDiscountValue() != null) {
            BigDecimal subtotal = sale.getSubtotalAmountTxnCurrency();
            BigDecimal manualAmount = computeDiscountAmount(sale.getOrderDiscountType(),
                    sale.getOrderDiscountValue(), subtotal);
            sale.setOrderDiscount(manualAmount);
            sale.setDiscountSource(DiscountSource.MANUAL);
            // Reason is already stored in discountDescription by SaleService
        }
        // System discount (appliedDiscount is already loaded)
        else if (sale.getAppliedDiscount() != null) {
            Discount discount = sale.getAppliedDiscount();
            validateSystemDiscount(discount, sale);
            BigDecimal subtotal = sale.getSubtotalAmountTxnCurrency();
            BigDecimal sysAmount = computeDiscountAmount(discount.getDiscountType(),
                    discount.getDiscountValue(), subtotal);
            sale.setOrderDiscount(sysAmount);
            sale.setDiscountSource(DiscountSource.SYSTEM);
            sale.setDiscountName(discount.getName());
            sale.setDiscountCode(discount.getCode());
            sale.setDiscountDescription(discount.getDescription());
        }
    }

    private BigDecimal computeDiscountAmount(DiscountType type, BigDecimal value, BigDecimal base) {
        if (type == DiscountType.PERCENTAGE) {
            return base.multiply(value).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        } else { // FLAT
            return value;
        }
    }

    private boolean discountAppliesToProduct(Discount discount, Long productId) {
        // Use the join table to check if the product is associated with this discount
        return discountProductRepository.existsByDiscountIdAndProductId(discount.getId(), productId);
    }

    private BigDecimal calculateDiscountAmount(Discount discount, BigDecimal unitPrice, int quantity) {
        BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        if (discount.getDiscountType() == DiscountType.PERCENTAGE) {
            return lineTotal.multiply(discount.getDiscountValue())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        } else {
            return discount.getDiscountValue().multiply(BigDecimal.valueOf(quantity));
        }
    }

    private void validateSystemDiscount(Discount discount, Sale sale) {
        if (!discount.getIsActive())
            throw new RuntimeException("Discount is inactive");

        if (discount.getCompanyId() != null && !discount.getCompanyId().equals(sale.getCompanyId()))
            throw new RuntimeException("Discount company mismatch");

        // warehouseId is Long, not an entity
        if (discount.getWarehouseId() != null &&
                !discount.getWarehouseId().equals(sale.getWarehouse().getId()))
            throw new RuntimeException("Discount warehouse mismatch");

        // Convert sale LocalDate to LocalDateTime for fair comparison
        LocalDate saleDate = sale.getDate();
        if (discount.getStartDate() != null && discount.getStartDate().toLocalDate().isAfter(saleDate))
            throw new RuntimeException("Discount not yet started");
        if (discount.getEndDate() != null && discount.getEndDate().toLocalDate().isBefore(saleDate))
            throw new RuntimeException("Discount expired");

        if (discount.getMinOrderAmount() != null
                && sale.getSubtotalAmountTxnCurrency().compareTo(discount.getMinOrderAmount()) < 0)
            throw new RuntimeException("Order amount below minimum for discount");
        if (discount.getMaxOrderAmount() != null
                && sale.getSubtotalAmountTxnCurrency().compareTo(discount.getMaxOrderAmount()) > 0)
            throw new RuntimeException("Order amount above maximum for discount");
    }
}