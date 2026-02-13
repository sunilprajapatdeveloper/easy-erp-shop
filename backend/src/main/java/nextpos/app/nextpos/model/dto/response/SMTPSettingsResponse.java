package nextpos.app.nextpos.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nextpos.app.nextpos.model.enums.SMTPProvider;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SMTPSettingsResponse {
    private Long id;
    private Long companyId;
    private String companyName;
    private SMTPProvider provider;
    private String host;
    private Integer port;
    private String username;
    private String fromEmail;
    private String fromName;
    private boolean sslEnabled;
    private boolean tlsEnabled;
    private Integer connectionTimeout;
    private Integer timeout;
    private Integer writeTimeout;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}