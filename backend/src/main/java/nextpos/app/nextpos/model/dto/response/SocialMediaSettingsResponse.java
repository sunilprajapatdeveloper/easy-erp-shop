package nextpos.app.nextpos.model.dto.response;

import java.time.LocalDateTime;
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
public class SocialMediaSettingsResponse {

    private SocialMediaPlatform platform;

    private String profileUrl;

    private String username;

    private Boolean enabled;

    private Map<String, Object> providerConfig;

    private Long createdBy;

    private LocalDateTime createdAt;

    private Long updatedBy;

    private LocalDateTime updatedAt;
}