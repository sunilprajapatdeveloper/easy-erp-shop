package nextpos.app.nextpos.model.dto.response;

import lombok.Getter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nextpos.app.nextpos.model.entity.Sale;
import nextpos.app.nextpos.model.entity.SaleProduct;
import nextpos.app.nextpos.model.enums.SaleSource;
import nextpos.app.nextpos.model.enums.SaleStatus;
import nextpos.app.nextpos.model.enums.ShipmentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleResponse {

    private Long id;
    private String referenceNumber;
    private String invoiceNumber;
    private String receiptNumber;

    private LocalDate date;
    private Long customerId;
    private Long warehouseId;

    private List<ProductDetail> products;

    private BigDecimal orderTax;
    private BigDecimal discount;
    private BigDecimal shippingCost;

    private BigDecimal totalAmountTxnCurrency;
    private BigDecimal dueAmountTxnCurrency;

    private BigDecimal exchangeRate;
    private BigDecimal totalAmountBaseCurrency;
    private BigDecimal dueAmountBaseCurrency;

    private Long currencyId;
    private String currencyCode;

    private ShipmentStatus shipmentStatus;
    private SaleStatus saleStatus;
    private String note;
    private SaleSource source;
    private boolean isRefund;

    private String posTerminalId;
    private Long cashierId;

    private List<PaymentResponse> payments;

    private Long createdBy;
    private LocalDateTime createdAt;
    private Long updatedBy;
    private LocalDateTime updatedAt;
    private Long companyId;

    public SaleResponse(Sale sale, List<PaymentResponse> payments) {
        this.id = sale.getId();
        this.referenceNumber = sale.getReferenceNumber();
        this.invoiceNumber = sale.getInvoiceNumber();
        this.receiptNumber = sale.getReceiptNumber();
        this.date = sale.getDate();

        this.customerId = sale.getCustomer() != null ? sale.getCustomer().getId() : null;
        this.warehouseId = sale.getWarehouse() != null ? sale.getWarehouse().getId() : null;
        this.companyId = sale.getCompanyId();

        this.orderTax = sale.getOrderTax();
        this.discount = sale.getDiscount();
        this.shippingCost = sale.getShippingCost();

        this.totalAmountTxnCurrency = sale.getTotalAmountTxnCurrency();
        this.dueAmountTxnCurrency = sale.getDueAmountTxnCurrency();

        this.exchangeRate = sale.getExchangeRate();
        this.totalAmountBaseCurrency = sale.getTotalAmountBaseCurrency();
        this.dueAmountBaseCurrency = sale.getDueAmountBaseCurrency();

        this.currencyId = sale.getCurrency() != null ? sale.getCurrency().getId() : null;
        this.currencyCode = sale.getCurrency() != null ? sale.getCurrency().getCode() : null;

        this.shipmentStatus = sale.getShipmentStatus();
        this.saleStatus = sale.getSaleStatus();
        this.note = sale.getNote();
        this.source = sale.getSource();
        this.isRefund = sale.isRefund();
        this.posTerminalId = sale.getPosTerminalId();
        this.cashierId = sale.getCashierId();

        this.products = sale.getProducts() == null ? List.of()
                : sale.getProducts().stream().map(ProductDetail::new).collect(Collectors.toList());

        this.payments = payments;
        this.createdBy = sale.getCreatedBy();
        this.createdAt = sale.getCreatedAt();
        this.updatedBy = sale.getUpdatedBy();
        this.updatedAt = sale.getUpdatedAt();
    }

    @Getter
    public static class ProductDetail {
        private final Long productId;
        private final String productName;
        private final String productCode;
        private final BigDecimal productUnitPrice;
        private final Integer saleQty;
        private final BigDecimal productDiscount;
        private final BigDecimal productTax;

        // Computed convenience field
        private final BigDecimal lineTotalTxnCurrency;

        public ProductDetail(SaleProduct saleProduct) {
            this.productId = saleProduct.getProduct() != null ? saleProduct.getProduct().getId() : null;
            this.productName = saleProduct.getProduct() != null ? saleProduct.getProduct().getName() : null;
            this.productCode = saleProduct.getProduct() != null ? saleProduct.getProduct().getCode() : null;
            this.productUnitPrice = saleProduct.getProductUnitPrice();
            this.saleQty = saleProduct.getSaleQty();
            this.productDiscount = saleProduct.getProductDiscount();
            this.productTax = saleProduct.getProductTax();

            // Simple computation for client convenience
            BigDecimal base = this.productUnitPrice.multiply(BigDecimal.valueOf(this.saleQty));
            BigDecimal afterDiscount = base
                    .subtract(this.productDiscount != null ? this.productDiscount : BigDecimal.ZERO);
            this.lineTotalTxnCurrency = afterDiscount.add(this.productTax != null ? this.productTax : BigDecimal.ZERO);
        }
    }
}
