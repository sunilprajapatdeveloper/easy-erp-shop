package nextpos.app.nextpos.model.dto.request.UpdateRequest;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateMediaRequest {

    @NotNull
    private Long id;

    private Boolean isPrimary;
}
