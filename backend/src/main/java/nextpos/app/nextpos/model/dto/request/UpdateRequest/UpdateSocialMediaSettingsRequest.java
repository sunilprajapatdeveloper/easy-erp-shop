package nextpos.app.nextpos.model.dto.request.UpdateRequest;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateSocialMediaSettingsRequest {

    private String profileUrl;

    private String username;

    private String apiKey;

    private String apiSecret;

    private String accessToken;

    private Boolean enabled;

    private Map<String, Object> providerConfig;
}