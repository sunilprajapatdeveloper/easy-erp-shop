package nextpos.app.nextpos.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nextpos.app.nextpos.model.entity.Purchase;
import nextpos.app.nextpos.model.entity.PurchaseProduct;
import nextpos.app.nextpos.model.enums.PurchaseStatus;
import nextpos.app.nextpos.model.enums.ShipmentStatus;
import nextpos.app.nextpos.service.interf.ProductStockService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
public class PurchaseResponse {

    private final Long id;
    private final String referenceNumber;
    private final LocalDate date;

    private final Long supplierId;
    private final String supplierName;

    private final Long warehouseId;
    private final String warehouseName;

    private final List<ProductDetail> products;

    private final BigDecimal orderTax;
    private final BigDecimal discount;
    private final BigDecimal shippingCost;

    private final BigDecimal totalAmountTxnCurrency;
    private final BigDecimal totalAmountBaseCurrency;
    private final BigDecimal exchangeRate;

    private final ShipmentStatus shippingStatus;
    private final PurchaseStatus purchaseStatus;

    private final String note;
    private final LocalDate expectedDeliveryDate;

    private final Long createdBy;
    private final LocalDateTime createdAt;
    private final Long updatedBy;
    private final LocalDateTime updatedAt;

    private final Long companyId;

    private final List<PaymentResponse> payments;

    public PurchaseResponse(Purchase purchase, List<PaymentResponse> paymentResponses,
            ProductStockService productStockService) {
        this.id = purchase.getId();
        this.referenceNumber = purchase.getReferenceNumber();
        this.date = purchase.getDate();

        this.supplierId = purchase.getSupplier() != null ? purchase.getSupplier().getId() : null;
        this.supplierName = purchase.getSupplier() != null ? purchase.getSupplier().getName() : null;

        this.warehouseId = purchase.getWarehouse() != null ? purchase.getWarehouse().getId() : null;
        this.warehouseName = purchase.getWarehouse() != null ? purchase.getWarehouse().getName() : null;

        this.products = purchase.getProducts().stream()
                .map(p -> new ProductDetail(p, productStockService))
                .collect(Collectors.toList());

        this.orderTax = purchase.getOrderTax();
        this.discount = purchase.getDiscount();
        this.shippingCost = purchase.getShippingCost();

        this.totalAmountTxnCurrency = purchase.getTotalAmountTxnCurrency();
        this.totalAmountBaseCurrency = purchase.getTotalAmountBaseCurrency();
        this.exchangeRate = purchase.getExchangeRate();

        this.shippingStatus = purchase.getShippingStatus();
        this.purchaseStatus = purchase.getPurchaseStatus();

        this.note = purchase.getNote();
        this.expectedDeliveryDate = purchase.getExpectedDeliveryDate();

        this.createdBy = purchase.getCreatedBy();
        this.createdAt = purchase.getCreatedAt();
        this.updatedBy = purchase.getUpdatedBy();
        this.updatedAt = purchase.getUpdatedAt();

        this.companyId = purchase.getCompanyId();
        this.payments = paymentResponses;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class ProductDetail {
        private final Long productId;
        private final String productName;
        private final String productCode;
        private final BigDecimal productUnitCost;
        private final Integer productStock;
        private final Integer purchaseQty;
        private final BigDecimal productDiscount;
        private final BigDecimal productTax;
        private final BigDecimal lineTotalTxnCurrency;

        public ProductDetail(PurchaseProduct purchaseProduct, ProductStockService productStockService) {
            this.productId = purchaseProduct.getProduct().getId();
            this.productName = purchaseProduct.getProduct().getName();
            this.productCode = purchaseProduct.getProduct().getCode();
            this.productUnitCost = purchaseProduct.getProductUnitCost();

            // Fetch current stock from ProductStockService
            this.productStock = productStockService.getStock(
                    purchaseProduct.getProduct().getId(),
                    purchaseProduct.getPurchase().getWarehouse().getId());

            this.purchaseQty = purchaseProduct.getPurchaseQty();
            this.productDiscount = purchaseProduct.getProductDiscount();
            this.productTax = purchaseProduct.getProductTax();

            // Compute line total (unitCost × qty – discount + tax)
            BigDecimal base = this.productUnitCost.multiply(BigDecimal.valueOf(this.purchaseQty));
            BigDecimal afterDiscount = base.subtract(Optional.ofNullable(this.productDiscount).orElse(BigDecimal.ZERO));
            this.lineTotalTxnCurrency = afterDiscount.add(Optional.ofNullable(this.productTax).orElse(BigDecimal.ZERO));
        }
    }
}
