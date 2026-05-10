package nextpos.app.nextpos.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "promotion_products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromotionProduct {

    @EmbeddedId
    private PromotionProductId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("promotionId")
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PromotionProductId implements java.io.Serializable {
        private Long promotionId;
        private Long productId;
    }
}