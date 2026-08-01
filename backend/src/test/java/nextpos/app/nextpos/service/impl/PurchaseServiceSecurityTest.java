package nextpos.app.nextpos.service.impl;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;

import nextpos.app.nextpos.model.entity.Purchase;
import nextpos.app.nextpos.model.entity.Warehouse;
import nextpos.app.nextpos.repository.CurrencyRepository;
import nextpos.app.nextpos.repository.ProductPriceRepository;
import nextpos.app.nextpos.repository.ProductRepository;
import nextpos.app.nextpos.repository.PurchaseRepository;
import nextpos.app.nextpos.repository.SupplierRepository;
import nextpos.app.nextpos.security.access.WarehouseAccessService;
import nextpos.app.nextpos.security.principal.AuthenticatedUser;
import nextpos.app.nextpos.service.interf.ProductStockService;
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
class PurchaseServiceSecurityTest {

    @Mock PurchaseRepository purchaseRepository;
    @Mock SupplierRepository supplierRepository;
    @Mock ProductRepository productRepository;
    @Mock CurrencyRepository currencyRepository;
    @Mock ProductPriceRepository productPriceRepository;
    @Mock ProductStockService productStockService;
    @Mock WarehouseAccessService warehouseAccessService;
    @InjectMocks PurchaseServiceImpl service;

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
    void purchaseReadUsesTenantScopeAndWarehouseBoundary() {
        Purchase purchase = Purchase.builder()
                .id(1L)
                .companyId(7L)
                .warehouse(Warehouse.builder().id(10L).companyId(7L).build())
                .build();
        when(purchaseRepository.findByIdAndCompanyId(1L, 7L)).thenReturn(Optional.of(purchase));

        service.getPurchaseById(1L);

        verify(purchaseRepository).findByIdAndCompanyId(1L, 7L);
        verify(warehouseAccessService).requireAccessible(10L);
    }
}
