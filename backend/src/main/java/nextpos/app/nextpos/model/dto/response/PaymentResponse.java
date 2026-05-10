package nextpos.app.nextpos.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextpos.app.nextpos.model.enums.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private Long id;
    private String referenceNumber;
    private PaymentSourceType referenceType;
    private Long referenceId;
    private PaymentType paymentType;
    private BigDecimal amountTxnCurrency;
    private PaymentMethod paymentMethod;
    private PaymentGatewayProvider gatewayProvider;
    private String transactionReference;
    private PaymentStatus status;
    private LocalDate paymentDate;
    private String note;
    private String message;
    private String currencyCode;
    private BigDecimal exchangeRate;
    private BigDecimal amountBaseCurrency;
    private String idempotencyKey;
    private Map<String, Object> paymentMetadata;
    private String referenceCurrencyCode;
    private BigDecimal referenceAmount;
    private Long warehouseId;
    private String posTerminalId;
    private ExchangeRateSource exchangeRateSource;
    private Long createdBy;
    private LocalDateTime createdAt;
    private Long updatedBy;
    private LocalDateTime updatedAt;
    private Long companyId;
}
