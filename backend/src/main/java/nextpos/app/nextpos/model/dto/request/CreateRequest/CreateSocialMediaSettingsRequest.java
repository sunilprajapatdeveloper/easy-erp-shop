package nextpos.app.nextpos.model.dto.request.CreateRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextpos.app.nextpos.model.enums.SocialMediaPlatform;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSocialMediaSettingsRequest {

    @NotNull(message = "Company ID is required")
    private Long companyId;

    @NotNull(message = "Platform is required")
    private SocialMediaPlatform platform;

    @Size(max = 255, message = "Profile URL must be less than 255 characters")
    private String profileUrl;

    @Size(max = 100, message = "Username must be less than 100 characters")
    private String username;

    @NotBlank(message = "API Key is required")
    private String apiKey;

    @NotBlank(message = "API Secret is required")
    private String apiSecret;

    private String accessToken;

    private Boolean enabled;

    private Map<String, Object> providerConfig;
}