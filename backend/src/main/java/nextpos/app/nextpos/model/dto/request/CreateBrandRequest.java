package nextpos.app.nextpos.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import jakarta.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@Builder
public class CreateBrandRequest {
    @NotBlank private final String name;
    private final String description;
    private final String image;
}
