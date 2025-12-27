package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.ScannerRegistrationRequest;
import nextpos.app.nextpos.model.dto.request.BarcodeScanRequest;
import nextpos.app.nextpos.model.dto.response.BarcodeScanResponse;
import nextpos.app.nextpos.model.dto.response.ScannerRegistrationResponse;
import nextpos.app.nextpos.model.entity.BarcodeScanner;

import java.util.List;

public interface BarcodeScannerService {

    ScannerRegistrationResponse registerScanner(ScannerRegistrationRequest request);

    BarcodeScanResponse processBarcodeScan(BarcodeScanRequest request);

    void updateScannerStatus(String scannerId, Long companyId, String status);

    List<BarcodeScanner> getScannersByWarehouse(Long warehouseId, Long companyId);

    void disconnectScanner(String scannerId, Long companyId);

    BarcodeScanResponse validateAndProcessScan(String scannerId, String barcode, Long companyId);
}