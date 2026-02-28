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
            @Valid @RequestBody ScannerRegistrationRequest request) {

        ScannerRegistrationResponse response = scannerService.registerScanner(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/scan")
    public ResponseEntity<BarcodeScanResponse> processBarcodeScan(
            @Valid @RequestBody BarcodeScanRequest request) {

        BarcodeScanResponse response = scannerService.processBarcodeScan(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<List<BarcodeScanner>> getWarehouseScanners(
            @PathVariable Long warehouseId) {

        List<BarcodeScanner> scanners = scannerService.getScannersByWarehouse(warehouseId);
        return ResponseEntity.ok(scanners);
    }

    @PostMapping("/{scannerId}/disconnect")
    public ResponseEntity<Void> disconnectScanner(
            @PathVariable String scannerId) {

        scannerService.disconnectScanner(scannerId);
        return ResponseEntity.ok().build();
    }
}