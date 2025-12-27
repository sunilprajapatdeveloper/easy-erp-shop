package nextpos.app.nextpos.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import jakarta.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@Builder
public class CreateCategoryRequest {
    @NotBlank private final String name;
    @NotBlank private final String code;
}
