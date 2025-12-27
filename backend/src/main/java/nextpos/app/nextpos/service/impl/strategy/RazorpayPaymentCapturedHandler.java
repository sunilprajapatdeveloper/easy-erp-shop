// package nextpos.app.nextpos.service.impl.strategy;

// import com.fasterxml.jackson.databind.JsonNode;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import nextpos.app.nextpos.model.dto.request.CreateRazorpayWebhookRequest;
// import nextpos.app.nextpos.model.entity.Sale;
// import nextpos.app.nextpos.model.enums.PaymentStatus;
// import nextpos.app.nextpos.repository.SaleRepository;
// import nextpos.app.nextpos.service.interf.RazorpayWebhookEventHandler;
// import org.springframework.stereotype.Component;

// import java.time.LocalDateTime;
// import java.util.Optional;

// @Slf4j
// @Component("payment.captured")
// @RequiredArgsConstructor
// public class RazorpayPaymentCapturedHandler implements RazorpayWebhookEventHandler {

//     private final SaleRepository saleRepository;
//     private final ObjectMapper objectMapper;

//     @Override
//     public void handle(CreateRazorpayWebhookRequest request) {
//         try {
//             JsonNode root = objectMapper.readTree(request.getPayload());

//             JsonNode paymentEntity = root.path("payload").path("payment").path("entity");
//             String razorpayOrderId = paymentEntity.path("order_id").asText();
//             String razorpayPaymentId = paymentEntity.path("id").asText();
//             String status = paymentEntity.path("status").asText();
//             String method = paymentEntity.path("method").asText();
//             int amount = paymentEntity.path("amount").asInt(); // amount in paise
//             String email = paymentEntity.path("email").asText(null);

//             log.info("Received payment.captured event for Razorpay orderId: {}, paymentId: {}", razorpayOrderId,
//                     razorpayPaymentId);

//             if (!"captured".equalsIgnoreCase(status)) {
//                 log.warn("Skipping event: Payment is not captured. Status: {}", status);
//                 return;
//             }

//             Optional<Sale> optionalSale = saleRepository.findByRazorpayOrderId(razorpayOrderId);
//             if (optionalSale.isEmpty()) {
//                 log.error("No matching sale found for Razorpay orderId: {}", razorpayOrderId);
//                 return;
//             }

//             Sale sale = optionalSale.get();

//             if (PaymentStatus.PAID.name().equals(sale.getPaymentStatus())) {
//                 log.info("Sale already marked as PAID. Skipping update. Sale ID: {}", sale.getId());
//                 return;
//             }

//             sale.setPaymentStatus(PaymentStatus.PAID.name());
//             sale.setRazorpayPaymentId(razorpayPaymentId);
//             sale.setPaymentMethod(method.toUpperCase());
//             sale.setPaidAt(LocalDateTime.now());

//             saleRepository.save(sale);

//             log.info("Sale updated successfully. Sale ID: {}, Payment ID: {}", sale.getId(), razorpayPaymentId);

//         } catch (Exception e) {
//             log.error("Error handling Razorpay payment.captured webhook", e);
//         }
//     }
// }
