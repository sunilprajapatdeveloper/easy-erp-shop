package nextpos.app.nextpos.security.access;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import nextpos.app.nextpos.model.entity.Warehouse;
import nextpos.app.nextpos.repository.WarehouseRepository;
import nextpos.app.nextpos.security.principal.AuthenticatedUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class WarehouseAccessServiceTest {

    @Mock
    private WarehouseRepository repository;

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void rejectsSameTenantWarehouseWithoutAssignment() {
        authenticate(Set.of(10L), Set.of());
        when(repository.findByIdAndCompanyIdAndIsDeletedFalse(11L, 7L))
                .thenReturn(Optional.of(Warehouse.builder().id(11L).companyId(7L).build()));

        assertThatThrownBy(() -> new WarehouseAccessService(repository).requireAccessible(11L))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void companyOwnerCanAccessEveryWarehouseInOwnTenant() {
        authenticate(Set.of(), Set.of(new SimpleGrantedAuthority("ROLE_COMPANY_OWNER")));
        Warehouse warehouse = Warehouse.builder().id(11L).companyId(7L).build();
        when(repository.findByIdAndCompanyIdAndIsDeletedFalse(11L, 7L)).thenReturn(Optional.of(warehouse));

        assertThat(new WarehouseAccessService(repository).requireAccessible(11L)).isSameAs(warehouse);
    }

    @Test
    void filtersWarehouseListsForAssignedUser() {
        authenticate(Set.of(10L), Set.of());
        when(repository.findAllByCompanyIdAndIsDeletedFalse(7L)).thenReturn(List.of(
                Warehouse.builder().id(10L).companyId(7L).build(),
                Warehouse.builder().id(11L).companyId(7L).build()));

        assertThat(new WarehouseAccessService(repository).accessibleWarehouses())
                .extracting(Warehouse::getId).containsExactly(10L);
    }

    private void authenticate(Set<Long> warehouseIds,
            Set<SimpleGrantedAuthority> authorities) {
        AuthenticatedUser principal = new AuthenticatedUser(
                5L, 7L, null, "user@example.test", "encoded", true, warehouseIds, Set.copyOf(authorities));
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities()));
    }
}
