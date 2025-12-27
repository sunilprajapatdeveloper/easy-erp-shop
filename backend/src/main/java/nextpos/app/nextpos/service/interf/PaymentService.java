package nextpos.app.nextpos.service.interf;

import nextpos.app.nextpos.model.dto.request.CreatePaymentRequest;
import nextpos.app.nextpos.model.dto.request.UpdatePaymentRequest;
import nextpos.app.nextpos.model.dto.response.PaymentResponse;
import nextpos.app.nextpos.model.enums.PaymentSourceType;

import java.util.List;

public interface PaymentService {

    PaymentResponse createPayment(CreatePaymentRequest request);

    PaymentResponse updatePayment(Long id, UpdatePaymentRequest request);

    void deletePayment(Long id);

    PaymentResponse getPayment(Long id);

    List<PaymentResponse> getPaymentsByReference(PaymentSourceType referenceType, Long referenceId);
    
    PaymentResponse processPayment(Long saleId, CreatePaymentRequest request);
}