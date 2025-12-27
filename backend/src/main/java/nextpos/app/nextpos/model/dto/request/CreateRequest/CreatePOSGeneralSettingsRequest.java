package nextpos.app.nextpos.model.dto.request.CreateRequest;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePOSGeneralSettingsRequest {

    private Long defaultCustomerId;

    @NotNull(message = "Default currency ID is required")
    private Long defaultCurrencyId;

    private String defaultPaymentMethod;

    @Builder.Default
    private boolean defaultTaxInclusive = true;
}
