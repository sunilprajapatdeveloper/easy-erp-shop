package nextpos.app.nextpos.model.dto.request.UpdateRequest;

import lombok.Data;
import nextpos.app.nextpos.model.entity.PromotionStackingStrategy;
import nextpos.app.nextpos.model.enums.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class UpdatePromotionRequest {
    private String name;
    private String code;
    private String description;
    private PromotionType type;
    private DiscountType discountType;
    private BigDecimal discountValue;
    private BigDecimal maxDiscountAmount;
    private BigDecimal minOrderAmount;
    private BigDecimal maxOrderAmount;
    private Integer usageLimit;
    private Integer usageLimitPerCustomer;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean isActive;
    private Integer stackingPriority;
    private PromotionStackingStrategy stackingStrategy;
    private Integer buyQuantity;
    private Integer getQuantity;
    private BigDecimal getDiscountPercent;
    private Long buyProductId;
    private Long getProductId;
    private Long warehouseId;
    private List<Long> productIds;
    private List<Long> categoryIds;
    private List<CustomerGroup> customerGroups;
}