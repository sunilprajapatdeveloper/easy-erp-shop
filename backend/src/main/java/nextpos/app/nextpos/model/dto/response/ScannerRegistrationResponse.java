package nextpos.app.nextpos.model.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScannerRegistrationResponse {
    private String scannerId;
    private String status;
    private String message;
}