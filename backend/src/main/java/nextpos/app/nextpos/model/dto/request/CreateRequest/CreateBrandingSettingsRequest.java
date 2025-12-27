package nextpos.app.nextpos.model.dto.request.CreateRequest;

import jakarta.validation.constraints.NotBlank;
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
public class CreateBrandingSettingsRequest {

    // Logos
    private String logoLight;
    private String logoDark;
    private String favicon;

    // Colors
    @NotBlank(message = "Primary color is required")
    private String primaryColor;

    private String secondaryColor;
    private String accentColor;
    private String backgroundColor;
    private String textColor;

    // Typography
    private String fontFamily;
    private String fontSize;

    // Additional UI customization
    private Map<String, Object> customTheme;

    private Boolean isActive;
}