package nextpos.app.nextpos.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecuritySettingsResponse {

    private Boolean enforceTwoFactorAuth;
    private Boolean enforcePasswordPolicy;
    private Boolean restrictIpAccess;
    private String allowedIpRanges;
    private Integer maxLoginAttempts;
    private Integer accountLockDurationMinutes;
    private Integer sessionTimeoutMinutes;
    private Integer passwordExpiryDays;
    private Boolean requireStrongPasswords;
    private Boolean allowDeviceTrust;
}