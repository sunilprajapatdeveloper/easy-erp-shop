package nextpos.app.nextpos.model.dto.request.UpdateRequest;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateSecuritySettingsRequest {

    @NotNull
    private Boolean enforceTwoFactorAuth;

    @NotNull
    private Boolean enforcePasswordPolicy;

    @NotNull
    private Boolean restrictIpAccess;

    private String allowedIpRanges;

    @Min(1)
    @Max(10)
    private Integer maxLoginAttempts;

    @Min(1)
    private Integer accountLockDurationMinutes;

    @Min(1)
    private Integer sessionTimeoutMinutes;

    @Min(1)
    private Integer passwordExpiryDays;

    @NotNull
    private Boolean requireStrongPasswords;

    @NotNull
    private Boolean allowDeviceTrust;
}