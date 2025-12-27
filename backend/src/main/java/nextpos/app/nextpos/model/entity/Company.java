package nextpos.app.nextpos.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "companies", indexes = {
        @Index(name = "idx_company_name", columnList = "company_name"),
        @Index(name = "idx_company_email", columnList = "email"),
        @Index(name = "idx_company_phone", columnList = "phone")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(unique = true, nullable = false)
    private String phone;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "registration_number")
    private String registrationNumber;

    @Column
    private String country;
    @Column
    private String state;
    @Column
    private String city;
    @Column
    private String address;
    @Column(name = "postal_code")
    private String postalCode;
    @Column
    private String timezone;

    @Column(name = "created_by", updatable = false)
    private Long createdBy;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_by")
    private Long updatedBy;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "is_deleted")
    @Builder.Default
    private Boolean isDeleted = false;

    @OneToOne(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private OnlineOrderingSettings onlineOrderingSettings;

    @OneToOne(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private SecuritySettings securitySettings;

    @OneToOne(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private ShippingProviderSettings shippingProviderSettings;

    @OneToOne(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private SocialMediaSettings socialMediaSettings;

    @OneToOne(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private LoyaltySettings loyaltySettings;

    @OneToOne(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private BrandingSettings brandingSettings;

    @OneToOne(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private TaxSetting taxSetting;

    /**
     * Updated mapping: a company can have multiple currencies
     */
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<CompanyCurrency> currencies = new ArrayList<>();

    /**
     * This allows multiple companies to share the same plan
     */
    @OneToOne(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private CompanySubscription companySubscription;

    /**
     * Helper method to get default currency easily
     */
    public CompanyCurrency getDefaultCurrency() {
        return currencies.stream()
                .filter(CompanyCurrency::isDefaultCurrency)
                .findFirst()
                .orElse(null);
    }
}