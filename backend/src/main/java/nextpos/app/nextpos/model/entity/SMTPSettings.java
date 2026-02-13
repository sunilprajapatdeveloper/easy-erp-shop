package nextpos.app.nextpos.model.entity;

import jakarta.persistence.*;
import lombok.*;
import nextpos.app.nextpos.model.enums.SMTPProvider;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private SMTPProvider provider;

    @Column(nullable = false, length = 150)
    private String host;

    @Column(nullable = false)
    private Integer port;

    @Column(nullable = false, length = 150)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false)
    private boolean sslEnabled;

    @Column(nullable = false)
    private boolean tlsEnabled;

    @Column(nullable = false, length = 150)
    private String fromEmail;

    @Column(length = 100)
    private String fromName;

    @Column(name = "connection_timeout", nullable = false)
    @Builder.Default
    private Integer connectionTimeout = 5000;

    @Column(name = "timeout", nullable = false)
    @Builder.Default
    private Integer timeout = 5000;

    @Column(name = "write_timeout", nullable = false)
    @Builder.Default
    private Integer writeTimeout = 5000;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}