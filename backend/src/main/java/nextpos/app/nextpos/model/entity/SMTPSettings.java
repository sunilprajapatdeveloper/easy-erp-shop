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
import nextpos.app.nextpos.model.enums.SMTPProvider;

@Entity
@Table(name = "smtp_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SMTPSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Provider type for SMTP (e.g., GMAIL, OUTLOOK, AMAZON_SES, SENDGRID, CUSTOM).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private SMTPProvider provider;

    /**
     * SMTP host (e.g., smtp.gmail.com).
     */
    @Column(nullable = false, length = 150)
    private String host;

    /**
     * SMTP port (e.g., 465, 587).
     */
    @Column(nullable = false)
    private Integer port;

    /**
     * Username/email for authentication.
     */
    @Column(nullable = false, length = 150)
    private String username;

    /**
     * Password or App-specific token.
     */
    @Column(nullable = false, length = 255)
    private String password;

    /**
     * Whether to use SSL (true/false).
     */
    @Column(nullable = false)
    private boolean sslEnabled;

    /**
     * Whether to use TLS (true/false).
     */
    @Column(nullable = false)
    private boolean tlsEnabled;

    /**
     * Sender email (From address).
     */
    @Column(nullable = false, length = 150)
    private String fromEmail;

    /**
     * Human-readable name for sender.
     */
    @Column(length = 100)
    private String fromName;

    /**
     * Many-to-one relationship with company.
     * A company can have multiple SMTP settings.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
}
