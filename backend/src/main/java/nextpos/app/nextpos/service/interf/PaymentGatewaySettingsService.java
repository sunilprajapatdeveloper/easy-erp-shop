package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateRequest.CreatePaymentGatewaySettingRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdatePaymentGatewaySettingRequest;
import nextpos.app.nextpos.model.dto.response.PaymentGatewaySettingsResponse;
import nextpos.app.nextpos.model.entity.PaymentGatewaySettings;
import nextpos.app.nextpos.model.enums.PaymentGatewayProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PaymentGatewaySettingsService {

    // Company-level operations (current user's company)
    PaymentGatewaySettingsResponse createForCurrentCompany(CreatePaymentGatewaySettingRequest request);

    PaymentGatewaySettingsResponse updateForCurrentCompany(UpdatePaymentGatewaySettingRequest request);

    void deleteForCurrentCompany(Long id);

    PaymentGatewaySettingsResponse getForCurrentCompany(Long id);

    List<PaymentGatewaySettingsResponse> getAllForCurrentCompany();

    Page<PaymentGatewaySettingsResponse> getForCurrentCompanyPaginated(Pageable pageable);

    // System-level operations (only for super admin)
    PaymentGatewaySettingsResponse createSystemSettings(CreatePaymentGatewaySettingRequest request);

    PaymentGatewaySettingsResponse updateSystemSettings(UpdatePaymentGatewaySettingRequest request);

    void deleteSystemSettings(Long id);

    PaymentGatewaySettingsResponse getSystemSettings(Long id);

    List<PaymentGatewaySettingsResponse> getAllSystemSettings();

    PaymentGatewaySettings getDecryptedEntityForCompany(Long companyId, PaymentGatewayProvider gatewayType);

    PaymentGatewaySettings getDecryptedSystemEntity(PaymentGatewayProvider gatewayType);
}