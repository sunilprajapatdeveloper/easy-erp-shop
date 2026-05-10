package nextpos.app.nextpos.model.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextpos.app.nextpos.model.enums.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePaymentRequest {

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal amountTxnCurrency;

    private PaymentMethod paymentMethod;

    private PaymentGatewayProvider gatewayProvider;

    private PaymentStatus status;

    private LocalDate paymentDate;

    private String note;

    private String transactionReference;

    private String currencyCode;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal exchangeRate;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal amountBaseCurrency;

    @Size(max = 100)
    private String idempotencyKey;

    private String referenceCurrencyCode;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal referenceAmount;

    private Long warehouseId;

    private String posTerminalId;

    private ExchangeRateSource exchangeRateSource;
}