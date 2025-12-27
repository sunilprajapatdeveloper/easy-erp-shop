package nextpos.app.nextpos.model.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nextpos.app.nextpos.model.enums.CacheProvider;

import java.util.HashMap;
import java.util.Map;


@Entity
@Table(name = "cache_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CacheSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Owning company for this cache configuration.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    /**
     * Type of cache provider (e.g., REDIS, MEMCACHED, HAZELCAST).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private CacheProvider provider;

    /**
     * Flag to enable/disable this cache configuration.
     */
    @Column(nullable = false)
    @Builder.Default
    private boolean enabled = true;

    /**
     * General default TTL (time-to-live) in seconds.
     */
    @Column(name = "default_ttl_seconds")
    private Integer defaultTtlSeconds;

    /**
     * Provider-specific configurations stored as key-value pairs.
     * Examples:
     * For Redis → host, port, password, database
     * For Memcached → servers, timeout
     * For Hazelcast → clusterName, members
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "cache_provider_properties", joinColumns = @JoinColumn(name = "cache_settings_id"))
    @MapKeyColumn(name = "property_key", length = 100)
    @Column(name = "property_value", length = 500)
    @Builder.Default
    private Map<String, String> providerProperties = new HashMap<>();
}