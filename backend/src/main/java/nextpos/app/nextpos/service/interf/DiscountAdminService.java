package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateDiscountRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateDiscountRequest;
import nextpos.app.nextpos.model.dto.response.DiscountResponse;
import nextpos.app.nextpos.pagination.dto.PaginationResponse;
import org.springframework.data.domain.Pageable;

public interface DiscountAdminService {

    DiscountResponse createDiscount(CreateDiscountRequest request, Long companyId, Long userId);

    DiscountResponse updateDiscount(Long id, UpdateDiscountRequest request, Long companyId, Long userId);

    void deleteDiscount(Long id, Long companyId);

    void toggleActive(Long id, Long companyId);

    DiscountResponse getDiscount(Long id, Long companyId);

    PaginationResponse<DiscountResponse> getDiscounts(Long companyId, Pageable pageable);
}