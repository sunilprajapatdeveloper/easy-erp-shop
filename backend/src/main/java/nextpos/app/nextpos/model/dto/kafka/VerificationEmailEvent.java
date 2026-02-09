package nextpos.app.nextpos.model.dto.kafka;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.DurationDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.DurationSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nextpos.app.nextpos.model.enums.VerificationType;

import java.time.Duration;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationEmailEvent {
    private UUID verificationId;
    private String email;
    private String token;
    private VerificationType verificationType;

    @JsonSerialize(using = DurationSerializer.class)
    @JsonDeserialize(using = DurationDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Duration expiryDuration;
}