package nextpos.app.nextpos.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "pos_receipt_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class POSReceiptSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * Warehouse/branch this receipt setting belongs to
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "warehouse_id", nullable = false, updatable = false)
    private Warehouse warehouse;

    /**
     * Whether to show company logo on receipts
     */
    @Column(name = "show_logo", nullable = false)
    @Builder.Default
    private boolean showLogo = true;

    /**
     * Path or identifier for the logo (could be file path, S3 URL, etc.)
     */
    @Column(name = "logo_path", length = 500)
    private String logoPath;

    /**
     * Header text (appears at the top of the receipt)
     */
    @Column(name = "header_text", length = 500)
    private String headerText;

    /**
     * Footer text (appears at the bottom of the receipt)
     */
    @Column(name = "footer_text", length = 500)
    private String footerText;

    /**
     * Show tax breakdown (GST, TDS, etc.) on receipts
     */
    @Column(name = "show_tax_breakdown", nullable = false)
    @Builder.Default
    private boolean showTaxBreakdown = true;

    /**
     * Whether to show itemized discount information
     */
    @Column(name = "show_discounts", nullable = false)
    @Builder.Default
    private boolean showDiscounts = true;

    /**
     * Receipt width (for thermal printers: 58mm / 80mm)
     */
    @Column(name = "receipt_width", length = 20)
    @Builder.Default
    private String receiptWidth = "80mm";

    /**
     * Show cashier/employee name on receipt
     */
    @Column(name = "show_cashier_name", nullable = false)
    @Builder.Default
    private boolean showCashierName = true;

    /**
     * Audit fields
     */
    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
