package nextpos.app.nextpos.model.entity;

import lombok.*;
import nextpos.app.nextpos.model.enums.ScannerStatus;
import nextpos.app.nextpos.model.enums.ScannerType;
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

import java.time.LocalDateTime;

@Entity
@Table(name = "barcode_scanners")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BarcodeScanner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String scannerId; // Unique identifier for the scanner

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScannerType type; // MOBILE, USB, BLUETOOTH, WIFI

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScannerStatus status; // ACTIVE, INACTIVE, OFFLINE

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User assignedUser;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime lastConnectedAt;

    private String ipAddress; // For WiFi scanners
    private String macAddress; // For Bluetooth scanners

    @Column(name = "company_id", nullable = false)
    private Long companyId;
}
