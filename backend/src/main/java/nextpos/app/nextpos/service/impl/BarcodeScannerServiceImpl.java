package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.entity.*;
import nextpos.app.nextpos.model.enums.ScannerStatus;
import nextpos.app.nextpos.model.dto.request.ScannerRegistrationRequest;
import nextpos.app.nextpos.model.dto.request.BarcodeScanRequest;
import nextpos.app.nextpos.model.dto.response.BarcodeScanResponse;
import nextpos.app.nextpos.model.dto.response.ScannerRegistrationResponse;
import nextpos.app.nextpos.repository.*;
import nextpos.app.nextpos.security.context.UserContext;
import nextpos.app.nextpos.security.access.WarehouseAccessService;
import nextpos.app.nextpos.service.interf.BarcodeScannerService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class BarcodeScannerServiceImpl implements BarcodeScannerService {

    private final BarcodeScannerRepository scannerRepository;
    private final ProductRepository productRepository;
    private final ProductPriceRepository productPriceRepository;
    private final ProductStockRepository productStockRepository;
    private final UserRepository userRepository;
    private final WarehouseAccessService warehouseAccessService;
    private final SimpMessagingTemplate messagingTemplate;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String SCAN_CACHE_PREFIX = "barcode_scan:";
    private static final String SCANNER_CACHE_PREFIX = "scanner:";
    private static final long CACHE_TTL = 300;

    @Override
    @Transactional
    public ScannerRegistrationResponse registerScanner(ScannerRegistrationRequest request) {
        Long companyId = UserContext.getCurrentCompanyId();

        // Validate warehouse belongs to company
        Warehouse warehouse = warehouseAccessService.requireAccessible(request.getWarehouseId());

        // Validate user exists
        User assignedUser = userRepository.findByIdAndCompanyId(request.getAssignedUserId(), companyId)
                .orElseThrow(() -> new RuntimeException("User not found or unauthorized"));

        String scannerId = generateScannerId(companyId);

        BarcodeScanner scanner = BarcodeScanner.builder()
                .scannerId(scannerId)
                .name(request.getScannerName())
                .type(request.getScannerType())
                .status(ScannerStatus.ACTIVE)
                .warehouse(warehouse)
                .assignedUser(assignedUser)
                .companyId(companyId)
                .createdAt(LocalDateTime.now())
                .lastConnectedAt(LocalDateTime.now())
                .ipAddress(request.getIpAddress())
                .macAddress(request.getMacAddress())
                .build();

        scannerRepository.save(scanner);
        cacheScannerInfo(scanner);

        log.info("Scanner registered: {} for company: {}, warehouse: {}, user: {}",
                scannerId, companyId, warehouse.getId(), assignedUser.getId());

        return ScannerRegistrationResponse.builder()
                .scannerId(scannerId)
                .status("REGISTERED")
                .message("Scanner successfully registered")
                .build();
    }

    @Override
    @Transactional
    public BarcodeScanResponse processBarcodeScan(BarcodeScanRequest request) {
        Long companyId = UserContext.getCurrentCompanyId();

        BarcodeScanner scanner = validateScanner(request.getScannerId(), companyId);
        Long warehouseId = scanner.getWarehouse().getId();

        String cacheKey = SCAN_CACHE_PREFIX + request.getBarcode() + ":" + companyId;
        BarcodeScanResponse cachedResponse = (BarcodeScanResponse) redisTemplate.opsForValue().get(cacheKey);

        if (cachedResponse != null) {
            broadcastScanToPOS(scanner, cachedResponse);
            return cachedResponse;
        }

        // Find product by barcode
        Product product = productRepository.findByBarcodeAndCompanyIdAndIsDeletedFalse(
                request.getBarcode(), companyId)
                .orElseThrow(() -> new RuntimeException("Product not found for barcode: " + request.getBarcode()));

        // Get product price for the warehouse
        BigDecimal price = getProductPrice(product.getId(), warehouseId, companyId);

        // Get product stock for the warehouse
        Integer stockQuantity = getProductStockQuantity(product.getId(), warehouseId, companyId);

        BarcodeScanResponse response = BarcodeScanResponse.builder()
                .scannerId(request.getScannerId())
                .barcode(request.getBarcode())
                .productId(product.getId())
                .productName(product.getName())
                .productSku(product.getSku())
                .price(price)
                .stockQuantity(stockQuantity)
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();

        redisTemplate.opsForValue().set(cacheKey, response, CACHE_TTL, TimeUnit.SECONDS);

        scanner.setLastConnectedAt(LocalDateTime.now());
        scannerRepository.save(scanner);

        broadcastScanToPOS(scanner, response);

        return response;
    }

    private BigDecimal getProductPrice(Long productId, Long warehouseId, Long companyId) {
        // Try to get warehouse-specific price first
        Optional<ProductPrice> warehousePrice = productPriceRepository.findByProductIdAndWarehouseIdAndCompanyId(
                productId, warehouseId, companyId);

        if (warehousePrice.isPresent()) {
            return warehousePrice.get().getPrice();
        }

        // Fall back to global price (no warehouse) for channel "POS"
        Optional<ProductPrice> globalPrice = productPriceRepository
                .findByProductIdAndWarehouseIsNullAndChannelAndCompanyId(productId, "POS", companyId);

        return globalPrice.map(ProductPrice::getPrice)
                .orElse(BigDecimal.ZERO);
    }

    private Integer getProductStockQuantity(Long productId, Long warehouseId, Long companyId) {
        Optional<ProductStock> productStock = productStockRepository.findByProductIdAndWarehouseIdAndCompanyId(
                productId, warehouseId, companyId);

        return productStock.map(ProductStock::getQuantity)
                .orElse(0);
    }

    @Override
    @Async("scannerTaskExecutor")
    @Transactional
    public BarcodeScanResponse validateAndProcessScan(String scannerId, String barcode) {
        try {
            Long companyId = UserContext.getCurrentCompanyId();

            String scannerCacheKey = SCANNER_CACHE_PREFIX + scannerId + ":" + companyId;
            Boolean scannerActive = (Boolean) redisTemplate.opsForValue().get(scannerCacheKey);

            if (scannerActive == null || !scannerActive) {
                BarcodeScanner scanner = validateScanner(scannerId, companyId);
                redisTemplate.opsForValue().set(scannerCacheKey, true, CACHE_TTL, TimeUnit.SECONDS);
            }

            BarcodeScanRequest scanRequest = BarcodeScanRequest.builder()
                    .scannerId(scannerId)
                    .barcode(barcode)
                    .build();

            return processBarcodeScan(scanRequest);

        } catch (Exception e) {
            log.error("Error in validateAndProcessScan: {}", e.getMessage(), e);
            throw e;
        }
    }

    private BarcodeScanner validateScanner(String scannerId, Long companyId) {
        BarcodeScanner scanner = scannerRepository.findByScannerIdAndCompanyId(scannerId, companyId)
                .orElseThrow(() -> new RuntimeException("Scanner not found or unauthorized"));
        warehouseAccessService.requireAssignment(scanner.getWarehouse().getId());
        return scanner;
    }

    private void broadcastScanToPOS(BarcodeScanner scanner, BarcodeScanResponse response) {
        try {
            String destination = String.format("/topic/scanner/%d/%d",
                    scanner.getWarehouse().getId(),
                    scanner.getAssignedUser().getId());

            messagingTemplate.convertAndSend(destination, response);
            log.debug("Broadcast scan to: {}", destination);
        } catch (Exception e) {
            log.error("Failed to broadcast scan: {}", e.getMessage(), e);
        }
    }

    private void cacheScannerInfo(BarcodeScanner scanner) {
        String cacheKey = SCANNER_CACHE_PREFIX + scanner.getScannerId() + ":" + scanner.getCompanyId();
        redisTemplate.opsForValue().set(cacheKey, true, CACHE_TTL, TimeUnit.SECONDS);
    }

    private String generateScannerId(Long companyId) {
        return "SCAN-" + companyId + "-" + UUID.randomUUID().toString().substring(0, 8);
    }

    @Override
    public void updateScannerStatus(String scannerId, String status) {
        Long companyId = UserContext.getCurrentCompanyId();

        scannerRepository.findByScannerIdAndCompanyId(scannerId, companyId)
                .ifPresent(scanner -> {
                    warehouseAccessService.requireAssignment(scanner.getWarehouse().getId());
                    scanner.setStatus(ScannerStatus.valueOf(status.toUpperCase()));
                    scanner.setLastConnectedAt(LocalDateTime.now());
                    scannerRepository.save(scanner);
                    cacheScannerInfo(scanner);
                });
    }

    @Override
    public List<BarcodeScanner> getScannersByWarehouse(Long warehouseId) {
        Long companyId = UserContext.getCurrentCompanyId();
        warehouseAccessService.requireAccessible(warehouseId);

        return scannerRepository.findByWarehouseIdAndCompanyId(warehouseId, companyId);
    }

    @Override
    public void disconnectScanner(String scannerId) {
        updateScannerStatus(scannerId, "INACTIVE");
        Long companyId = UserContext.getCurrentCompanyId();

        String cacheKey = SCANNER_CACHE_PREFIX + scannerId + ":" + companyId;
        redisTemplate.delete(cacheKey);
    }
}
