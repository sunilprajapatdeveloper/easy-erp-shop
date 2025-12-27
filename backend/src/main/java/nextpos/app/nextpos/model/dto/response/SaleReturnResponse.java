package nextpos.app.nextpos.model.dto.response;

import lombok.Getter;
import nextpos.app.nextpos.model.entity.SaleReturn;
import nextpos.app.nextpos.model.entity.SaleReturnProduct;
import nextpos.app.nextpos.model.enums.SaleStatus;
import nextpos.app.nextpos.model.enums.ShipmentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class SaleReturnResponse {

    private final Long id;
    private final String referenceNumber;
    private final LocalDate date;

    private final Long originalSaleId;
    private final Long customerId;
    private final Long warehouseId;

    private final List<ProductDetail> products;

    private final BigDecimal returnTax;
    private final BigDecimal returnDiscount;
    private final BigDecimal shippingCost;

    private final BigDecimal refundAmountTxnCurrency;
    private final BigDecimal refundAmountBaseCurrency;
    private final BigDecimal exchangeRate;

    private final Long currencyId;
    private final String currencyCode;

    private final ShipmentStatus shipmentStatus;
    private final SaleStatus returnStatus;

    private final String note;

    private final List<PaymentResponse> payments;

    private final Long createdBy;
    private final LocalDateTime createdAt;
    private final Long updatedBy;
    private final LocalDateTime updatedAt;

    private final Long companyId;

    public SaleReturnResponse(SaleReturn saleReturn, List<PaymentResponse> payments) {
        this.id = saleReturn.getId();
        this.referenceNumber = saleReturn.getReferenceNumber();
        this.date = saleReturn.getDate();

        this.originalSaleId = saleReturn.getOriginalSale() != null ? saleReturn.getOriginalSale().getId() : null;
        this.customerId = saleReturn.getCustomer() != null ? saleReturn.getCustomer().getId() : null;
        this.warehouseId = saleReturn.getWarehouse() != null ? saleReturn.getWarehouse().getId() : null;

        this.products = saleReturn.getProducts() == null ? List.of()
                : saleReturn.getProducts().stream().map(ProductDetail::new).collect(Collectors.toList());

        this.returnTax = saleReturn.getReturnTax();
        this.returnDiscount = saleReturn.getReturnDiscount();
        this.shippingCost = saleReturn.getShippingCost();

        this.refundAmountTxnCurrency = saleReturn.getRefundAmountTxnCurrency();
        this.refundAmountBaseCurrency = saleReturn.getRefundAmountBaseCurrency();
        this.exchangeRate = saleReturn.getExchangeRate();

        this.currencyId = saleReturn.getCurrency() != null ? saleReturn.getCurrency().getId() : null;
        this.currencyCode = saleReturn.getCurrency() != null ? saleReturn.getCurrency().getCode() : null;

        this.shipmentStatus = saleReturn.getShipmentStatus();
        this.returnStatus = saleReturn.getReturnStatus();

        this.note = saleReturn.getNote();
        this.payments = payments;

        this.createdBy = saleReturn.getCreatedBy();
        this.createdAt = saleReturn.getCreatedAt();
        this.updatedBy = saleReturn.getUpdatedBy();
        this.updatedAt = saleReturn.getUpdatedAt();
        this.companyId = saleReturn.getCompanyId();
    }

    @Getter
    public static class ProductDetail {
        private final Long productId;
        private final String productName;
        private final String productCode;
        private final BigDecimal productUnitPrice;
        private final Integer returnQty;
        private final BigDecimal returnDiscount;
        private final BigDecimal returnTax;
        private final BigDecimal lineTotalTxnCurrency;

        public ProductDetail(SaleReturnProduct product) {
            this.productId = product.getProduct() != null ? product.getProduct().getId() : null;
            this.productName = product.getProduct() != null ? product.getProduct().getName() : null;
            this.productCode = product.getProduct() != null ? product.getProduct().getCode() : null;
            this.productUnitPrice = product.getProductUnitPrice();
            this.returnQty = product.getReturnQty();
            this.returnDiscount = product.getReturnDiscount();
            this.returnTax = product.getReturnTax();

            // Simple line total calculation for client convenience
            BigDecimal base = this.productUnitPrice != null && this.returnQty != null
                    ? this.productUnitPrice.multiply(BigDecimal.valueOf(this.returnQty))
                    : BigDecimal.ZERO;
            BigDecimal afterDiscount = base
                    .subtract(this.returnDiscount != null ? this.returnDiscount : BigDecimal.ZERO);
            this.lineTotalTxnCurrency = afterDiscount.add(this.returnTax != null ? this.returnTax : BigDecimal.ZERO);
        }
    }
}
