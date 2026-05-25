package nextpos.app.nextpos.model.dto.request.CreateRequest;

import lombok.Data;
import nextpos.app.nextpos.model.enums.DiscountScope;
import nextpos.app.nextpos.model.enums.DiscountSource;
import nextpos.app.nextpos.model.enums.DiscountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateDiscountRequest {

    private String name;
    private String code;
    private String description;

    private DiscountType discountType;
    private DiscountScope scope;
    private DiscountSource source;

    private BigDecimal discountValue;
    private BigDecimal maxDiscountAmount;

    private BigDecimal minOrderAmount;
    private BigDecimal maxOrderAmount;

    private Boolean stackable;
    private Boolean autoApply;

    private Boolean requiresManagerApproval;
    private BigDecimal approvalRequiredAbove;

    private Integer priority;

    private Integer usageLimit;
    private Integer usageLimitPerCustomer;

    private Boolean isActive;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private Long warehouseId;

    private List<Long> productIds;
    private List<Long> categoryIds;
}