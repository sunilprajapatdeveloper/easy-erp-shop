package nextpos.app.nextpos.model.dto.request;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest implements Serializable {
    private String to;
    private String subject;
    private String content;
    private boolean isHtml;
}