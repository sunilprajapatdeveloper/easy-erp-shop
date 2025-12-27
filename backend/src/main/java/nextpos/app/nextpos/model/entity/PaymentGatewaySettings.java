package nextpos.app.nextpos.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nextpos.app.nextpos.model.enums.PaymentGatewayProvider;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "payment_gateway_settings")
public class PaymentGatewaySettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Company to which this gateway config belongs
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    /**
     * Supported payment gateways (add more as needed).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "gateway_type", nullable = false, length = 50)
    private PaymentGatewayProvider gatewayType;

    /**
     * Public key or client ID (depends on gateway).
     */
    @Column(name = "public_key", length = 255)
    private String publicKey;

    /**
     * Secret key or client secret (sensitive).
     */
    @Column(name = "secret_key", length = 255)
    private String secretKey;

    /**
     * Optional merchant ID or account identifier.
     */
    @Column(name = "merchant_id", length = 100)
    private String merchantId;

    /**
     * Currency supported for this gateway config (e.g., USD, INR).
     */
    @Column(name = "currency", length = 10)
    private String currency;

    /**
     * Whether this gateway is currently enabled for the company.
     */
    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    /**
     * Additional config (like webhook secret, environment mode).
     */
    @Column(name = "webhook_secret", length = 255)
    private String webhookSecret;

    @Column(name = "sandbox_mode", nullable = false)
    private boolean sandboxMode;
}