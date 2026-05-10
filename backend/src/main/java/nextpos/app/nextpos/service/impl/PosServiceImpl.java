package nextpos.app.nextpos.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextpos.app.nextpos.model.dto.request.CreatePaymentRequest;
import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateSaleRequest;
import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateSaleRequest;
import nextpos.app.nextpos.model.dto.response.PaymentResponse;
import nextpos.app.nextpos.model.dto.response.SaleResponse;
import nextpos.app.nextpos.model.enums.SaleSource;
import nextpos.app.nextpos.service.interf.PaymentService;
import nextpos.app.nextpos.service.interf.PosService;
import nextpos.app.nextpos.service.interf.SaleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PosServiceImpl implements PosService {

        private final SaleService saleService;
        private final PaymentService paymentService; // added for payment delegation

        @Override
        @Transactional
        public SaleResponse createSale(CreateSaleRequest request) {
                CreateSaleRequest posRequest = CreateSaleRequest.builder()
                                .date(request.getDate())
                                .customerId(request.getCustomerId())
                                .warehouseId(request.getWarehouseId())
                                .products(request.getProducts())
                                .orderTax(request.getOrderTax())
                                .orderDiscount(request.getOrderDiscount())
                                .shippingCost(request.getShippingCost())
                                .shipmentStatus(request.getShipmentStatus())
                                .saleStatus(request.getSaleStatus())
                                .source(SaleSource.POS)
                                .note(request.getNote())
                                .currencyId(request.getCurrencyId())
                                .exchangeRate(request.getExchangeRate())
                                .build();
                return saleService.createSale(posRequest);
        }

        @Override
        public SaleResponse getSaleDetails(Long saleId) {
                return saleService.getSaleById(saleId);
        }

        @Override
        @Transactional
        public SaleResponse updateSale(Long id, UpdateSaleRequest request) {
                if (request.getSource() == null) {
                        request.setSource(SaleSource.POS);
                }
                return saleService.updateSale(id, request);
        }

        @Override
        @Transactional
        public PaymentResponse addPaymentToSale(Long saleId, CreatePaymentRequest paymentRequest) {
                return paymentService.processPayment(saleId, paymentRequest);
        }

        @Override
        public byte[] generateReceipt(Long saleId) {
                // Placeholder – real PDF generation to be implemented
                return ("Receipt for sale ID: " + saleId).getBytes();
        }
}
