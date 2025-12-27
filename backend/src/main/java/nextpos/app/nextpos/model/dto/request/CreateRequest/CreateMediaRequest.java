package nextpos.app.nextpos.model.dto.request.CreateRequest;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
public class CreateMediaRequest {

    @NotNull
    private Long companyId;

    @NotNull
    private String entityType;

    @NotNull
    private Long entityId;

    @NotNull
    private MultipartFile file;

    private Boolean isPrimary;
}
