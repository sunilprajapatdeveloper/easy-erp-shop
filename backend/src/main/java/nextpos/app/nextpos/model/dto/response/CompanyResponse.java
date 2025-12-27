package nextpos.app.nextpos.model.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response DTO for Company entity.
 * Includes company core details, linked settings, and subscription details.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyResponse {

    private Long id;

    // Basic Info
    private String companyName;
    private String phone;
    private String email;
    private String registrationNumber;

    // Location
    private String country;
    private String state;
    private String city;
    private String address;
    private String postalCode;
    private String timezone;

    // Audit fields
    private Long createdBy;
    private LocalDateTime createdAt;
    private Long updatedBy;
    private LocalDateTime updatedAt;

    // Status
    private Boolean isActive;
    private Boolean isDeleted;

    // Nested Settings
    private OnlineOrderingSettingsResponse onlineOrderingSettings;
    private SecuritySettingsResponse securitySettings;
    private ShippingProviderSettingsResponse shippingProviderSettings;
    private SocialMediaSettingsResponse socialMediaSettings;
    private LoyaltySettingsResponse loyaltySettings;
    private BrandingSettingsResponse brandingSettings;
    private TaxSettingResponse taxSetting;
    private CompanyCurrencyResponse currencySetting;

    /**
     * Subscription information:
     * Instead of exposing SubscriptionPlan directly,
     * we expose CompanySubscription which links company ↔ plan.
     */
    private CompanySubscriptionResponse subscription;
}
