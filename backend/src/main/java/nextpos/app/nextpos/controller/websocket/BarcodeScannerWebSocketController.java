package nextpos.app.nextpos.controller.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.request.BarcodeScanRequest;
import nextpos.app.nextpos.model.dto.request.ScannerDisconnectRequest;
import nextpos.app.nextpos.model.dto.request.ScannerRegistrationRequest;
import nextpos.app.nextpos.model.dto.request.ScannerStatusUpdateRequest;
import nextpos.app.nextpos.model.dto.response.BarcodeScanResponse;
import nextpos.app.nextpos.model.dto.response.ScannerDisconnectResponse;
import nextpos.app.nextpos.model.dto.response.ScannerRegistrationResponse;
import nextpos.app.nextpos.model.dto.response.ScannerStatusResponse;
import nextpos.app.nextpos.service.interf.BarcodeScannerService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BarcodeScannerWebSocketController {

        private final BarcodeScannerService scannerService;

        @MessageMapping("/scanner/register")
        @SendToUser("/queue/scanner/registration")
        public ScannerRegistrationResponse registerScanner(
                        @Payload ScannerRegistrationRequest request,
                        @Header("companyId") Long companyId) {

                try {
                        log.debug("Registering scanner via WebSocket: {} for company: {}",
                                        request.getScannerName(), companyId);

                        request.setCompanyId(companyId);
                        ScannerRegistrationResponse response = scannerService.registerScanner(request);

                        log.info("Scanner registered successfully via WebSocket: {}", response.getScannerId());
                        return response;

                } catch (Exception e) {
                        log.error("Error registering scanner via WebSocket: {}", e.getMessage(), e);
                        return ScannerRegistrationResponse.builder()
                                        .scannerId("")
                                        .status("FAILED")
                                        .message("Scanner registration failed: " + e.getMessage())
                                        .build();
                }
        }

        @MessageMapping("/scanner/scan")
        @SendToUser("/queue/scanner/response")
        public BarcodeScanResponse processBarcodeScan(
                        @Payload BarcodeScanRequest request,
                        @Header("companyId") Long companyId) {

                try {
                        log.debug("Processing barcode scan from scanner: {} for company: {}",
                                        request.getScannerId(), companyId);

                        BarcodeScanResponse response = scannerService.validateAndProcessScan(
                                        request.getScannerId(),
                                        request.getBarcode(),
                                        companyId);

                        log.info("Barcode processed successfully: {} -> {}", request.getBarcode(),
                                        response.getProductName());
                        return response;

                } catch (Exception e) {
                        log.error("Error processing barcode scan: {}", e.getMessage(), e);
                        return BarcodeScanResponse.builder()
                                        .scannerId(request.getScannerId())
                                        .barcode(request.getBarcode())
                                        .success(false)
                                        .errorMessage(e.getMessage())
                                        .timestamp(java.time.LocalDateTime.now())
                                        .build();
                }
        }

        @MessageMapping("/scanner/status")
        @SendToUser("/queue/scanner/status")
        public ScannerStatusResponse updateScannerStatus(
                        @Payload ScannerStatusUpdateRequest request,
                        @Header("companyId") Long companyId) {

                try {
                        log.debug("Updating scanner status: {} for scanner: {}, company: {}",
                                        request.getStatus(), request.getScannerId(), companyId);

                        scannerService.updateScannerStatus(request.getScannerId(), companyId, request.getStatus());

                        return ScannerStatusResponse.builder()
                                        .scannerId(request.getScannerId())
                                        .status(request.getStatus())
                                        .message("Scanner status updated successfully")
                                        .build();

                } catch (Exception e) {
                        log.error("Error updating scanner status: {}", e.getMessage(), e);
                        return ScannerStatusResponse.builder()
                                        .scannerId(request.getScannerId())
                                        .status("ERROR")
                                        .message("Failed to update scanner status: " + e.getMessage())
                                        .build();
                }
        }

        @MessageMapping("/scanner/disconnect")
        @SendToUser("/queue/scanner/disconnect")
        public ScannerDisconnectResponse disconnectScanner(
                        @Payload ScannerDisconnectRequest request,
                        @Header("companyId") Long companyId) {

                try {
                        log.info("Disconnecting scanner: {} for company: {}",
                                        request.getScannerId(), companyId);

                        scannerService.disconnectScanner(request.getScannerId(), companyId);

                        return ScannerDisconnectResponse.builder()
                                        .scannerId(request.getScannerId())
                                        .status("DISCONNECTED")
                                        .message("Scanner disconnected successfully")
                                        .build();

                } catch (Exception e) {
                        log.error("Error disconnecting scanner: {}", e.getMessage(), e);
                        return ScannerDisconnectResponse.builder()
                                        .scannerId(request.getScannerId())
                                        .status("ERROR")
                                        .message("Failed to disconnect scanner: " + e.getMessage())
                                        .build();
                }
        }
}