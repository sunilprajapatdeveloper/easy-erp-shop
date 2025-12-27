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
import nextpos.app.nextpos.model.enums.SMSProvider;

@Entity
@Table(name = "sms_provider_config")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SMSProviderConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Parent SMS settings reference.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sms_settings_id", nullable = false)
    private SMSSettings smsSettings;

    /**
     * The SMS provider type (e.g., TWILIO, NEXMO, MSG91, AWS_SNS).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private SMSProvider providerType;

    /**
     * API key / Auth token for the provider.
     */
    @Column(nullable = false, length = 255)
    private String apiKey;

    /**
     * Secret key / Password for the provider (if required).
     */
    @Column(length = 255)
    private String apiSecret;

    /**
     * Sender ID or phone number (depends on provider).
     */
    @Column(length = 100)
    private String senderId;

    /**
     * Mark one provider as primary.
     */
    @Column(nullable = false)
    @Builder.Default
    private boolean primaryProvider = false;

    /**
     * Additional config (like region, endpoint, etc.).
     */
    @Column(length = 500)
    private String additionalConfig;
}
