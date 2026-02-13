package nextpos.app.nextpos.model.dto.request.UpdateRequest;

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
public class UpdateSMTPSettingsRequest {

    private SMTPProvider provider;

    @Pattern(regexp = "^([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,}$", message = "Invalid SMTP host format")
    private String host;

    @Min(1)
    @Max(65535)
    private Integer port;

    @Email(message = "Username must be a valid email address")
    private String username;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{8,}$", message = "Password must be at least 8 characters with letters and numbers")
    private String password;

    private Boolean sslEnabled;
    private Boolean tlsEnabled;

    @Email
    private String fromEmail;
    private String fromName;

    @Min(1000)
    @Max(30000)
    private Integer connectionTimeout;

    @Min(1000)
    @Max(30000)
    private Integer timeout;

    @Min(1000)
    @Max(30000)
    private Integer writeTimeout;

    private Boolean isActive;
}