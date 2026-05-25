package nextpos.app.nextpos.model.dto.response;

import lombok.*;
import nextpos.app.nextpos.model.entity.SaleProduct;
import nextpos.app.nextpos.model.enums.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleProductResponse {
    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal productUnitPrice;
    private Integer quantity;
    private BigDecimal lineDiscountAmount;
    private BigDecimal lineNetAmount;
    private BigDecimal lineTaxAmount;
    private BigDecimal lineGrossAmount;
    private String taxName;
    private TaxCategory taxCategory;
    private BigDecimal taxRate;
    private TaxInclusionType taxInclusionType;
    private TaxApplicationOrder taxApplicationOrder;

    public static SaleProductResponse fromEntity(SaleProduct sp) {
        return SaleProductResponse.builder()
                .id(sp.getId())
                .productId(sp.getProduct().getId())
                .productName(sp.getProduct().getName())
                .productUnitPrice(sp.getProductUnitPrice())
                .quantity(sp.getQuantity())
                .lineDiscountAmount(sp.getLineDiscountAmount())
                .lineNetAmount(sp.getLineNetAmount())
                .lineTaxAmount(sp.getLineTaxAmount())
                .lineGrossAmount(sp.getLineGrossAmount())
                .taxName(sp.getTaxName())
                .taxCategory(sp.getTaxCategory())
                .taxRate(sp.getTaxRate())
                .taxInclusionType(sp.getTaxInclusionType())
                .taxApplicationOrder(sp.getTaxApplicationOrder())
                .build();
    }
}