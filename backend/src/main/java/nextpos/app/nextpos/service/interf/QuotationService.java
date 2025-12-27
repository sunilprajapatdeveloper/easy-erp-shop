package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreateQuotationRequest;
import nextpos.app.nextpos.model.dto.response.QuotationResponse;

public interface QuotationService {

    QuotationResponse createQuotation(CreateQuotationRequest request);

    QuotationResponse getQuotationById(Long id);

    QuotationResponse updateQuotation(Long id, CreateQuotationRequest request);

    void deleteQuotation(Long id, Long deletedByUserId);
}
