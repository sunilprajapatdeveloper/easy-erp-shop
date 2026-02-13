package nextpos.app.nextpos.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequest implements Serializable {

    private Long companyId;

    @NotEmpty(message = "At least one recipient is required")
    private List<@Email String> to;

    private List<@Email String> cc;
    private List<@Email String> bcc;

    private String subject;
    private String content;

    @Builder.Default
    private boolean isHtml = true;

    private String replyTo;

    private String from;

    public EmailRequest(String to, String subject, String content, boolean isHtml) {
        this.to = List.of(to);
        this.subject = subject;
        this.content = content;
        this.isHtml = isHtml;
        this.companyId = null;
        this.cc = null;
        this.bcc = null;
        this.replyTo = null;
        this.from = null;
    }
}