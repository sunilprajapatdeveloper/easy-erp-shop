package nextpos.app.nextpos.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nextpos.app.nextpos.model.entity.Transfer;
import nextpos.app.nextpos.model.entity.TransferProduct;
import nextpos.app.nextpos.model.enums.ShipmentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
public class TransferResponse {
    private Long id;
    private Long fromWarehouse;
    private Long toWarehouse;
    private LocalDate date;
    private List<ProductDetail> products;
    private BigDecimal orderTax;
    private BigDecimal discount;
    private BigDecimal shippingCost;
    private BigDecimal grandTotal;
    private ShipmentStatus status;
    private String note;
    private Long createdBy;
    private LocalDateTime createdAt;
    private Long updatedBy;
    private LocalDateTime updatedAt;
    private Long companyId;

    public TransferResponse(Transfer transfer) {
        this.id = transfer.getId();
        this.fromWarehouse = transfer.getFromWarehouse() != null ? transfer.getFromWarehouse().getId() : null;
        this.toWarehouse = transfer.getToWarehouse() != null ? transfer.getToWarehouse().getId() : null;
        this.date = transfer.getDate();
        this.products = transfer.getProducts().stream().map(ProductDetail::new).collect(Collectors.toList());
        this.orderTax = transfer.getOrderTax();
        this.discount = transfer.getDiscount();
        this.shippingCost = transfer.getShippingCost();
        this.grandTotal = transfer.getGrandTotal();
        this.status = transfer.getStatus();
        this.note = transfer.getNote();
        this.createdBy = transfer.getCreatedBy();
        this.createdAt = transfer.getCreatedAt();
        this.updatedBy = transfer.getUpdatedBy();
        this.updatedAt = transfer.getUpdatedAt();
        this.companyId = transfer.getCompanyId();
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class ProductDetail {
        private Long productId;
        private String productCode;
        private BigDecimal productUnitCost;
        private Integer productStock;
        private Integer transferredQty;
        private BigDecimal productDiscount;
        private BigDecimal productTax;
        private BigDecimal subTotal;

        public ProductDetail(TransferProduct tp) {
            this.productId = tp.getProductId();
            this.productCode = tp.getProductCode();
            this.productUnitCost = tp.getProductUnitCost();
            this.productStock = tp.getProductStock();
            this.transferredQty = tp.getTransferredQty();
            this.productDiscount = tp.getProductDiscount();
            this.productTax = tp.getProductTax();
            this.subTotal = tp.getSubTotal();
        }
    }
}
