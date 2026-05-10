package nextpos.app.nextpos.model.entity;

import jakarta.persistence.*;
import lombok.*;
import nextpos.app.nextpos.model.enums.CustomerGroup;

@Entity
@Table(name = "promotion_customer_groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromotionCustomerGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id", nullable = false)
    private Promotion promotion;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_group", nullable = false, length = 30)
    private CustomerGroup customerGroup;
}