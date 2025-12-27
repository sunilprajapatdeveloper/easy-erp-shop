package nextpos.app.nextpos.model.dto.request.UpdateRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateBrandingSettingsRequest {

    private String logoLight;
    private String logoDark;
    private String favicon;

    private String primaryColor;
    private String secondaryColor;
    private String accentColor;
    private String backgroundColor;
    private String textColor;

    private String fontFamily;
    private String fontSize;

    private Map<String, Object> customTheme;

    private Boolean isActive;
}