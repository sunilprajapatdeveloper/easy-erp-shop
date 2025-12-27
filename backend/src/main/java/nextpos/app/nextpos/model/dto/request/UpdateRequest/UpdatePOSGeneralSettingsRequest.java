package nextpos.app.nextpos.model.dto.request.UpdateRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePOSGeneralSettingsRequest {

    private Long defaultCustomerId;

    private Long defaultCurrencyId;

    private String defaultPaymentMethod;

    private Boolean defaultTaxInclusive;
}
