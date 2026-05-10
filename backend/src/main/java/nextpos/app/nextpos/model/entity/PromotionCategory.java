package nextpos.app.nextpos.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "promotion_categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromotionCategory {

    @EmbeddedId
    private PromotionCategoryId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("promotionId")
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("categoryId")
    @JoinColumn(name = "category_id")
    private Category category;

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PromotionCategoryId implements java.io.Serializable {
        private Long promotionId;
        private Long categoryId;
    }
}