package nextpos.app.nextpos.model.dto.request.UpdateRequest;

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
public class UpdateCompanyRequest {

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

    // Exchange rate mode
    private ExchangeRateMode exchangeRateMode;

    // Status flags
    private Boolean isActive;
    private Boolean isDeleted;
}
