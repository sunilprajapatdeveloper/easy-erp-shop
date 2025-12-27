package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateTransferRequest;
import nextpos.app.nextpos.model.dto.response.TransferResponse;

import java.util.List;

public interface TransferService {

    TransferResponse createTransfer(CreateTransferRequest request);

    TransferResponse getTransferById(Long id);

    List<TransferResponse> getMyTransfer();

    List<TransferResponse> getAllTransfer();

    TransferResponse updateTransfer(Long id, CreateTransferRequest request);

    void deleteTransfer(Long id);
}