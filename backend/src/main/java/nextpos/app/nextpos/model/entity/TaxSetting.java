package nextpos.app.nextpos.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nextpos.app.nextpos.model.enums.TaxInclusionType;
import nextpos.app.nextpos.model.enums.TaxCategory;
import nextpos.app.nextpos.model.enums.TaxApplicationOrder;

import java.math.BigDecimal;

@Entity
@Table(name = "tax_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Reference to the company owning this tax configuration.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    /**
     * Optional: Specific warehouse/branch this tax applies to.
     * If null → tax is global for the company.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    /**
     * Tax type (VAT, GST, TDS, CUSTOM, etc.).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tax_category", nullable = false, length = 50)
    private TaxCategory taxCategory;

    /**
     * Human-readable name for the tax.
     * Example: "Indian GST", "European VAT", "NY Sales Tax"
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * Percentage value of the tax.
     * Example: 18.00 (for 18% GST)
     */
    @Column(name = "rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal rate;

    /**
     * Whether the tax is applied before discount, after discount, etc.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "application_order", nullable = false, length = 50)
    private TaxApplicationOrder applicationOrder;

    /**
     * Whether the tax is included in the price (inclusive) or added on top
     * (exclusive).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "inclusion_type", nullable = false, length = 50)
    private TaxInclusionType inclusionType;

    /**
     * Is this tax currently active?
     */
    @Column(name = "active", nullable = false)
    private boolean active;

    /**
     * Optional: Country/region code (ISO standard).
     * Example: "IN" for India, "US" for United States, "EU" for Europe.
     */
    @Column(name = "region_code", length = 10)
    private String regionCode;

    /**
     * Optional: Custom description or rules.
     */
    @Column(name = "description", length = 255)
    private String description;
}
