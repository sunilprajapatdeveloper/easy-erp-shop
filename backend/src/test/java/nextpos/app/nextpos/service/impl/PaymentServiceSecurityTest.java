package nextpos.app.nextpos.service.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import java.util.Set;

import nextpos.app.nextpos.factory.PaymentStrategyFactory;
import nextpos.app.nextpos.model.dto.request.CreatePaymentRequest;
import nextpos.app.nextpos.model.dto.request.UpdatePaymentRequest;
import nextpos.app.nextpos.model.entity.Payment;
import nextpos.app.nextpos.model.entity.Sale;
import nextpos.app.nextpos.model.entity.Warehouse;
import nextpos.app.nextpos.model.enums.PaymentSourceType;
import nextpos.app.nextpos.repository.PaymentRepository;
import nextpos.app.nextpos.repository.SaleRepository;
import nextpos.app.nextpos.security.access.WarehouseAccessService;
import nextpos.app.nextpos.security.principal.AuthenticatedUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class PaymentServiceSecurityTest {

    @Mock PaymentRepository paymentRepository;
    @Mock SaleRepository saleRepository;
    @Mock PaymentStrategyFactory strategyFactory;
    @Mock WarehouseAccessService warehouseAccessService;
    @InjectMocks PaymentServiceImpl service;

    @BeforeEach
    void authenticate() {
        AuthenticatedUser principal = new AuthenticatedUser(
                5L, 7L, null, "user@example.test", "encoded", true, Set.of(10L), Set.of());
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities()));
    }

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void updateChecksExistingPaymentsWarehouseBeforeMutation() {
        Payment payment = Payment.builder().id(2L).companyId(7L).warehouseId(10L).build();
        when(paymentRepository.findByIdAndCompanyId(2L, 7L)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.updatePayment(2L, new UpdatePaymentRequest());

        verify(warehouseAccessService).requireAssignment(10L);
    }

    @Test
    void rejectsWarehouseThatDoesNotMatchReferencedSale() {
        Sale sale = Sale.builder().id(4L).companyId(7L)
                .warehouse(Warehouse.builder().id(10L).companyId(7L).build()).build();
        when(saleRepository.findByIdAndCompanyId(4L, 7L)).thenReturn(Optional.of(sale));
        CreatePaymentRequest request = CreatePaymentRequest.builder()
                .referenceType(PaymentSourceType.SALE).referenceId(4L).warehouseId(11L).build();

        assertThatThrownBy(() -> service.createPayment(request))
                .hasMessageContaining("does not match");
    }
}
