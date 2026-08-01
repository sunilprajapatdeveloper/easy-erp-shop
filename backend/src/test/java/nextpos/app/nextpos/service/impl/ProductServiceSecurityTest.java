package nextpos.app.nextpos.service.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import nextpos.app.nextpos.model.dto.request.CreateRequest.CreateProductRequest;
import nextpos.app.nextpos.repository.BrandRepository;
import nextpos.app.nextpos.repository.CategoryRepository;
import nextpos.app.nextpos.repository.ProductPriceRepository;
import nextpos.app.nextpos.repository.ProductRepository;
import nextpos.app.nextpos.repository.ProductStockRepository;
import nextpos.app.nextpos.repository.ProductTaxRepository;
import nextpos.app.nextpos.repository.ProductUnitRepository;
import nextpos.app.nextpos.security.access.WarehouseAccessService;
import nextpos.app.nextpos.security.principal.AuthenticatedUser;
import nextpos.app.nextpos.service.helper.BarcodeHelper;
import nextpos.app.nextpos.service.interf.MediaService;
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
class ProductServiceSecurityTest {

    @Mock ProductRepository productRepository;
    @Mock CategoryRepository categoryRepository;
    @Mock BrandRepository brandRepository;
    @Mock ProductUnitRepository productUnitRepository;
    @Mock ProductPriceRepository productPriceRepository;
    @Mock ProductStockRepository productStockRepository;
    @Mock ProductTaxRepository productTaxRepository;
    @Mock BarcodeHelper barcodeHelper;
    @Mock MediaService mediaService;
    @Mock WarehouseAccessService warehouseAccessService;
    @InjectMocks ProductServiceImpl service;

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
    void resolvesProductCategoryInsideAuthenticatedTenant() {
        CreateProductRequest request = new CreateProductRequest();
        request.setCode("P-1");
        request.setBarcode("B-1");
        request.setCategoryId(99L);
        when(categoryRepository.findByIdAndCompanyId(99L, 7L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.createProduct(request))
                .isInstanceOf(IllegalArgumentException.class);

        verify(categoryRepository).findByIdAndCompanyId(99L, 7L);
    }

    @Test
    void bulkDeleteFailsAtomicallyWhenAnyProductIsOutsideTenant() {
        when(productRepository.findAllByIdInAndCompanyIdAndIsDeletedFalse(List.of(1L, 2L), 7L))
                .thenReturn(List.of());

        assertThatThrownBy(() -> service.bulkDelete(List.of(1L, 2L)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("authenticated company");
    }

    @Test
    void validatesWarehouseBeforeWarehouseQualifiedProductRead() {
        assertThatThrownBy(() -> service.getProductById(1L, 10L, true, true, true))
                .isInstanceOf(IllegalArgumentException.class);

        verify(warehouseAccessService).requireAccessible(10L);
    }
}
