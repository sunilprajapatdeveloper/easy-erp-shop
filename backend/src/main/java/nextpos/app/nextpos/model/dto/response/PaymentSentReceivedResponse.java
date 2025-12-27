package nextpos.app.nextpos.model.dto.response;

import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentSentReceivedResponse {
    private List<BigDecimal> paymentsSent;
    private List<BigDecimal> paymentsReceived;
    private List<String> dates;
}