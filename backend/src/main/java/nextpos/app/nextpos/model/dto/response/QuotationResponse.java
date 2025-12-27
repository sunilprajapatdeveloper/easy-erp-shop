package nextpos.app.nextpos.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import nextpos.app.nextpos.model.entity.Quotation;
import nextpos.app.nextpos.model.entity.QuotationProduct;
import nextpos.app.nextpos.model.enums.ShipmentStatus;

@Getter
@AllArgsConstructor
@Builder
public class QuotationResponse {
    private Long id;
    private Long customerId;
    private Long warehouseId;
    private List<ProductDetail> products;
    private BigDecimal orderTax;
    private BigDecimal discount;
    private BigDecimal shippingCost;
    private ShipmentStatus status;
    private String note;
    private Long createdBy;
    private LocalDateTime createdAt;
    private Long updatedBy;
    private LocalDateTime updatedAt;
    private Long companyId;

    public QuotationResponse(Quotation quotation, List<ProductDetail> productDetails) {
        this.id = quotation.getId();
        this.customerId = quotation.getCustomerId();
        this.warehouseId = quotation.getWarehouseId();
        this.products = productDetails;
        this.orderTax = quotation.getOrderTax();
        this.discount = quotation.getDiscount();
        this.shippingCost = quotation.getShippingCost();
        this.status = quotation.getStatus();
        this.note = quotation.getNote();
        this.createdBy = quotation.getCreatedBy();
        this.createdAt = quotation.getCreatedAt();
        this.updatedBy = quotation.getUpdatedBy();
        this.updatedAt = quotation.getUpdatedAt();
        this.companyId = quotation.getCompanyId();
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class ProductDetail {
        private Long productId;
        private String productCode;
        private BigDecimal productUnitCost;
        private Integer productStock;
        private Integer quantity;
        private BigDecimal productDiscount;
        private BigDecimal productTax;
        private BigDecimal subTotal;

        public ProductDetail(QuotationProduct product) {
            this.productId = product.getProduct().getId();
            this.productCode = product.getProductCode();
            this.productUnitCost = product.getProductUnitCost();
            this.productStock = product.getProductStock();
            this.quantity = product.getQuantity();
            this.productDiscount = product.getProductDiscount();
            this.productTax = product.getProductTax();
            this.subTotal = product.getSubTotal();
        }
    }
}
