package nextpos.app.nextpos.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nextpos.app.nextpos.model.entity.Brand;

import java.time.LocalDateTime;
@Getter
@AllArgsConstructor
@Builder
public class BrandResponse {
    private Long id;
    private String name;
    private String description;
    private String image;
    private Long createdBy;
    private LocalDateTime createdAt;
    private Long updatedBy;
    private LocalDateTime updatedAt;
    private Long companyId;

    public BrandResponse(Brand brand) {
        this.id = brand.getId();
        this.name = brand.getName();
        this.description = brand.getDescription();
        this.image = brand.getImage();
        this.createdBy = brand.getCreatedBy();
        this.createdAt = brand.getCreatedAt();
        this.updatedBy = brand.getUpdatedBy();
        this.updatedAt = brand.getUpdatedAt();
        this.companyId = brand.getCompanyId();
    }
}
