package nextpos.app.nextpos.model.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nextpos.app.nextpos.model.enums.ExchangeRateMode;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyResponse {

    private Long id;
    private String companyName;
    private String phone;
    private String email;
    private String registrationNumber;
    private String country;
    private String state;
    private String city;
    private String address;
    private String postalCode;
    private String timezone;
    private Long createdBy;
    private LocalDateTime createdAt;
    private Long updatedBy;
    private LocalDateTime updatedAt;
    private Boolean isActive;
    private Boolean isDeleted;

    // Exchange rate mode
    private ExchangeRateMode exchangeRateMode;

    // Nested Settings
    private OnlineOrderingSettingsResponse onlineOrderingSettings;
    private SecuritySettingsResponse securitySettings;
    private ShippingProviderSettingsResponse shippingProviderSettings;
    private SocialMediaSettingsResponse socialMediaSettings;
    private LoyaltySettingsResponse loyaltySettings;
    private BrandingSettingsResponse brandingSettings;
    private TaxSettingResponse taxSetting;
    private CompanyCurrencyResponse currencySetting;
    private CompanySubscriptionResponse subscription;
}
