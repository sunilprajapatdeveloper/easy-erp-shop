package nextpos.app.nextpos.model.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
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
public class CreatePaymentRequest {

    @NotNull
    private PaymentSourceType referenceType;

    @NotNull
    private Long referenceId;

    private String referenceNumber;

    @NotNull
    private PaymentType paymentType;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal amountTxnCurrency;

    @NotNull
    private PaymentMethod paymentMethod;

    private PaymentGatewayProvider gatewayProvider;

    private String paymentMetadata;

    @NotNull
    private PaymentStatus status;

    @NotNull
    private LocalDate paymentDate;

    private String transactionReference;

    @NotNull
    @Size(max = 100)
    private String idempotencyKey;

    @NotNull
    private String currencyCode;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal exchangeRate;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal amountBaseCurrency;

    private String referenceCurrencyCode;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal referenceAmount;

    private Long warehouseId;

    private String posTerminalId;

    private ExchangeRateSource exchangeRateSource;
}