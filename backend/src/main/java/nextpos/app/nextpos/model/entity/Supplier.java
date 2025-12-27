package nextpos.app.nextpos.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "supplier")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 150)
    private String email;

    @Column(length = 50)
    private String phone;

    @Column(length = 100)
    private String country;

    @Column(length = 100)
    private String city;

    @Column(length = 255)
    private String address;

    @Column(name = "tax_number", length = 100)
    private String taxNumber;

    @Column(name = "created_by", nullable = false, updatable = false)
    private Long createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    // Optional: Add soft delete flag
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    // Optional: Add external reference code for ERP integration
    @Column(name = "external_code", length = 100)
    private String externalCode;
}
