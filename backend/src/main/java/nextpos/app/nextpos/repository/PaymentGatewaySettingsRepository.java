package nextpos.app.nextpos.repository;

import nextpos.app.nextpos.model.entity.PaymentGatewaySettings;
import nextpos.app.nextpos.model.enums.PaymentGatewayProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentGatewaySettingsRepository extends JpaRepository<PaymentGatewaySettings, Long> {

    // Find all settings for a specific company (including system? maybe separate)
    List<PaymentGatewaySettings> findByCompanyId(Long companyId);

    Page<PaymentGatewaySettings> findByCompanyId(Long companyId, Pageable pageable);

    Optional<PaymentGatewaySettings> findByIdAndCompanyId(Long id, Long companyId);

    Optional<PaymentGatewaySettings> findByIdAndCompanyIsNull(Long id);

    Optional<PaymentGatewaySettings> findByCompanyIdAndGatewayType(Long companyId, PaymentGatewayProvider gatewayType);

    // Find system settings (company is null)
    Optional<PaymentGatewaySettings> findByCompanyIsNullAndGatewayType(PaymentGatewayProvider gatewayType);

    List<PaymentGatewaySettings> findByCompanyIsNull();

    // Check existence
    boolean existsByCompanyIdAndGatewayType(Long companyId, PaymentGatewayProvider gatewayType);

    boolean existsByCompanyIsNullAndGatewayType(PaymentGatewayProvider gatewayType);
}
