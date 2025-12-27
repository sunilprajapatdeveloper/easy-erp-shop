package nextpos.app.nextpos.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

import nextpos.app.nextpos.model.entity.Category;
@Getter
@AllArgsConstructor
@Builder
public class CategoryResponse {
    private Long id;
    private String name;
    private String code;
    private Long createdBy;
    private LocalDateTime createdAt;
    private Long updatedBy;
    private LocalDateTime updatedAt;
    private Long companyId;

    public CategoryResponse(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.code = category.getCode();
        this.createdBy = category.getCreatedBy();
        this.createdAt = category.getCreatedAt();
        this.updatedBy = category.getUpdatedBy();
        this.updatedAt = category.getUpdatedAt();
        this.companyId = category.getCompanyId();
    }
}
