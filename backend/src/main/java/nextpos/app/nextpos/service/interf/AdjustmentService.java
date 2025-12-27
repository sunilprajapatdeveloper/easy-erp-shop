package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateAdjustmentRequest;
import nextpos.app.nextpos.model.dto.response.AdjustmentResponse;

import java.util.List;

public interface AdjustmentService {
    AdjustmentResponse createAdjustment(CreateAdjustmentRequest request);
    AdjustmentResponse getAdjustmentById(Long id);
    List<AdjustmentResponse> getMyAdjustments();
    List<AdjustmentResponse> getAllAdjustments();
    AdjustmentResponse updateAdjustment(Long id, CreateAdjustmentRequest request);
    void deleteAdjustment(Long id);
}