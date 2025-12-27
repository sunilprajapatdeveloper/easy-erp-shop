package nextpos.app.nextpos.model.dto.response;

import lombok.Getter;
import nextpos.app.nextpos.model.entity.PurchaseReturn;
import nextpos.app.nextpos.model.entity.PurchaseReturnProduct;
import nextpos.app.nextpos.model.enums.PurchaseStatus;
import nextpos.app.nextpos.model.enums.ShipmentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PurchaseReturnResponse {

    private final Long id;
    private final String referenceNumber;
    private final LocalDate date;

    private final Long originalPurchaseId;
    private final Long supplierId;
    private final Long warehouseId;
    private final Long currencyId;

    private final List<ProductDetail> products;

    private final BigDecimal returnTax;
    private final BigDecimal returnDiscount;
    private final BigDecimal shippingCost;

    private final BigDecimal refundAmountTxnCurrency;
    private final BigDecimal refundAmountBaseCurrency;
    private final BigDecimal exchangeRate;

    private final ShipmentStatus shipmentStatus;
    private final PurchaseStatus returnStatus;

    private final String note;

    private final Long createdBy;
    private final LocalDateTime createdAt;
    private final Long updatedBy;
    private final LocalDateTime updatedAt;

    private final Long companyId;

    public PurchaseReturnResponse(PurchaseReturn purchaseReturn) {
        this.id = purchaseReturn.getId();
        this.referenceNumber = purchaseReturn.getReferenceNumber();
        this.date = purchaseReturn.getDate();

        this.originalPurchaseId = purchaseReturn.getOriginalPurchase().getId();
        this.supplierId = purchaseReturn.getSupplier().getId();
        this.warehouseId = purchaseReturn.getWarehouse().getId();
        this.currencyId = purchaseReturn.getCurrency().getId();

        this.products = purchaseReturn.getProducts().stream()
                .map(ProductDetail::new)
                .collect(Collectors.toList());

        this.returnTax = purchaseReturn.getReturnTax();
        this.returnDiscount = purchaseReturn.getReturnDiscount();
        this.shippingCost = purchaseReturn.getShippingCost();

        this.refundAmountTxnCurrency = purchaseReturn.getRefundAmountTxnCurrency();
        this.refundAmountBaseCurrency = purchaseReturn.getRefundAmountBaseCurrency();
        this.exchangeRate = purchaseReturn.getExchangeRate();

        this.shipmentStatus = purchaseReturn.getShipmentStatus();
        this.returnStatus = purchaseReturn.getReturnStatus();

        this.note = purchaseReturn.getNote();

        this.createdBy = purchaseReturn.getCreatedBy();
        this.createdAt = purchaseReturn.getCreatedAt();
        this.updatedBy = purchaseReturn.getUpdatedBy();
        this.updatedAt = purchaseReturn.getUpdatedAt();
        this.companyId = purchaseReturn.getCompanyId();
    }

    @Getter
    public static class ProductDetail {
        private final Long productId;
        private final String productName;
        private final String productCode;
        private final BigDecimal productUnitCost;
        private final Integer returnQty;
        private final BigDecimal productDiscount;
        private final BigDecimal productTax;

        public ProductDetail(PurchaseReturnProduct product) {
            this.productId = product.getProduct().getId();
            this.productName = product.getProduct().getName();
            this.productCode = product.getProduct().getCode();
            this.productUnitCost = product.getProductUnitCost();
            this.returnQty = product.getReturnQty();
            this.productDiscount = product.getProductDiscount();
            this.productTax = product.getProductTax();
        }
    }
}
