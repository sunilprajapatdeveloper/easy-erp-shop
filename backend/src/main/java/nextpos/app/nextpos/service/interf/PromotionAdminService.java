package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateRequest.CreatePromotionRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdatePromotionRequest;
import nextpos.app.nextpos.model.dto.response.PromotionResponse;
import nextpos.app.nextpos.pagination.dto.PaginationResponse;
import org.springframework.data.domain.Pageable;

public interface PromotionAdminService {
    PromotionResponse createPromotion(CreatePromotionRequest request, Long companyId, Long userId);

    PromotionResponse updatePromotion(Long id, UpdatePromotionRequest request, Long companyId, Long userId);

    void deletePromotion(Long id, Long companyId);

    void toggleActive(Long id, Long companyId);

    PromotionResponse getPromotion(Long id, Long companyId);

    PaginationResponse<PromotionResponse> getPromotions(Long companyId, Pageable pageable);
}