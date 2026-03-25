package nextpos.app.nextpos.model.entity;

import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Entity
@Table(name = "ai_prompts")
public class AiPrompt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "prompt_key", nullable = false)
    private String promptKey;

    @Column(name = "version", nullable = false)
    private Integer version;

    @Column(name = "prompt_template", nullable = false, columnDefinition = "TEXT")
    private String promptTemplate;

    @Column(name = "model")
    private String model; // optional, which model this prompt is optimised for

    @Column(name = "temperature", precision = 3, scale = 2)
    private BigDecimal temperature = BigDecimal.valueOf(0.7);

    @Column(name = "max_tokens")
    private Integer maxTokens = 1000;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "description")
    private String description;

    @Column(name = "created_by")
    private String createdBy;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "tags", columnDefinition = "jsonb")
    private List<String> tags;

    @Column(name = "created_at")
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }
}