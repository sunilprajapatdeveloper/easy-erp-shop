package nextpos.app.nextpos.model.dto.request.CreateRequest;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nextpos.app.nextpos.model.enums.SMTPProvider;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSMTPSettingsRequest {

    @NotNull(message = "Company ID is required")
    private Long companyId;

    @NotNull(message = "SMTP provider is required")
    private SMTPProvider provider;

    @NotBlank(message = "SMTP host is required")
    @Pattern(regexp = "^([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,}$", message = "Invalid SMTP host format")
    private String host;

    @NotNull(message = "SMTP port is required")
    @Min(1)
    @Max(65535)
    private Integer port;

    @NotBlank(message = "Username is required")
    @Email(message = "Username must be a valid email address")
    private String username;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{8,}$", message = "Password must be at least 8 characters with letters and numbers")
    private String password;

    @Builder.Default
    private boolean sslEnabled = false;

    @Builder.Default
    private boolean tlsEnabled = true;

    @NotBlank(message = "From email is required")
    @Email
    private String fromEmail;

    private String fromName;

    @Builder.Default
    @Min(1000)
    @Max(30000)
    private Integer connectionTimeout = 5000;

    @Builder.Default
    @Min(1000)
    @Max(30000)
    private Integer timeout = 5000;

    @Builder.Default
    @Min(1000)
    @Max(30000)
    private Integer writeTimeout = 5000;
}