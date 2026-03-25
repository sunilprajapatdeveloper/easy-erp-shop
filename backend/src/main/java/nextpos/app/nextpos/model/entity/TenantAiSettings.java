package nextpos.app.nextpos.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
// import nextpos.app.nextpos.ai.config.EncryptedStringConverter;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.*;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "tenant_ai_settings")
public class TenantAiSettings extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false, unique = true)
    private String tenantId;

    @Column(name = "ai_provider", nullable = false)
    private String aiProvider; // e.g., "openai", "deepseek", "claude", "ollama"

    // @Convert(converter = EncryptedStringConverter.class)
    @Column(name = "api_key", nullable = false)
    private String apiKey;

    @Column(name = "base_url")
    private String baseUrl;

    @Column(name = "model_name")
    private String modelName; // e.g., "gpt-4"

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "enabled_features", columnDefinition = "jsonb")
    private List<String> enabledFeatures; // e.g., ["product_generation", "chat_assistant"]

    @Column(name = "max_tokens_per_month")
    private Long maxTokensPerMonth;

    @Column(name = "tokens_used_current_month")
    private Long tokensUsedCurrentMonth = 0L;

    @Column(name = "request_timeout_ms")
    private Integer requestTimeoutMs = 30000;
}