package nextpos.app.nextpos.controller.scanner;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.model.dto.request.ScannerRegistrationRequest;
import nextpos.app.nextpos.model.dto.request.BarcodeScanRequest;
import nextpos.app.nextpos.model.dto.response.BarcodeScanResponse;
import nextpos.app.nextpos.model.dto.response.ScannerRegistrationResponse;
import nextpos.app.nextpos.model.entity.BarcodeScanner;
import nextpos.app.nextpos.service.interf.BarcodeScannerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/scanner")
@RequiredArgsConstructor
public class BarcodeScannerController {

    private final BarcodeScannerService scannerService;

    @PostMapping("/register")
    public ResponseEntity<ScannerRegistrationResponse> registerScanner(
            @Valid @RequestBody ScannerRegistrationRequest request,
            @RequestHeader("X-Company-Id") Long companyId) {

        request.setCompanyId(companyId);
        ScannerRegistrationResponse response = scannerService.registerScanner(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/scan")
    public ResponseEntity<BarcodeScanResponse> processBarcodeScan(
            @Valid @RequestBody BarcodeScanRequest request,
            @RequestHeader("X-Company-Id") Long companyId) {

        request.setCompanyId(companyId);
        BarcodeScanResponse response = scannerService.processBarcodeScan(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<List<BarcodeScanner>> getWarehouseScanners(
            @PathVariable Long warehouseId,
            @RequestHeader("X-Company-Id") Long companyId) {

        List<BarcodeScanner> scanners = scannerService.getScannersByWarehouse(warehouseId, companyId);
        return ResponseEntity.ok(scanners);
    }

    @PostMapping("/{scannerId}/disconnect")
    public ResponseEntity<Void> disconnectScanner(
            @PathVariable String scannerId,
            @RequestHeader("X-Company-Id") Long companyId) {

        scannerService.disconnectScanner(scannerId, companyId);
        return ResponseEntity.ok().build();
    }
}