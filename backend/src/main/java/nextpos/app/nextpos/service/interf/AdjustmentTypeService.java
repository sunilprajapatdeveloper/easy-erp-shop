package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateAdjustmentTypeRequest;
import nextpos.app.nextpos.model.dto.response.AdjustmentTypeResponse;

import java.util.List;

public interface AdjustmentTypeService {
    AdjustmentTypeResponse createAdjustmentType(CreateAdjustmentTypeRequest request);
    AdjustmentTypeResponse getAdjustmentTypeById(Long id);
    List<AdjustmentTypeResponse> findAllByCreatedBy(Long userId);
    List<AdjustmentTypeResponse> getAllAdjustmentTypes();
    AdjustmentTypeResponse updateAdjustmentType(Long id, CreateAdjustmentTypeRequest request);
    void deleteAdjustmentType(Long id);
}