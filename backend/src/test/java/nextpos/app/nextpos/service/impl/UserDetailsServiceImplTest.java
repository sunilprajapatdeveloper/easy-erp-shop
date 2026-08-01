package nextpos.app.nextpos.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import nextpos.app.nextpos.model.entity.Permission;
import nextpos.app.nextpos.model.entity.Role;
import nextpos.app.nextpos.model.entity.User;
import nextpos.app.nextpos.model.entity.UserWarehouse;
import nextpos.app.nextpos.model.entity.Warehouse;
import nextpos.app.nextpos.model.enums.PermissionType;
import nextpos.app.nextpos.repository.UserRepository;
import nextpos.app.nextpos.security.principal.AuthenticatedUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Test
    void loadsTenantPermissionsAndOnlyActiveSameTenantWarehouses() {
        Permission permission = Permission.builder().name(PermissionType.SALE_POS).build();
        Role role = Role.builder().name("CASHIER").permissions(Set.of(permission)).companyId(7L).build();

        Warehouse allowed = Warehouse.builder().id(10L).companyId(7L).build();
        Warehouse otherTenant = Warehouse.builder().id(20L).companyId(8L).build();
        User user = User.builder()
                .id(3L)
                .email("cashier@example.test")
                .phone("1234567890")
                .password("encoded")
                .status(true)
                .companyId(7L)
                .role(role)
                .defaultWarehouse(allowed)
                .userWarehouses(new HashSet<>())
                .build();
        user.addUserWarehouse(UserWarehouse.builder().warehouse(allowed).active(true).build());
        user.addUserWarehouse(UserWarehouse.builder().warehouse(otherTenant).active(true).build());

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        AuthenticatedUser principal = (AuthenticatedUser) new UserDetailsServiceImpl(userRepository)
                .loadUserByUsername(user.getEmail());

        assertThat(principal.userId()).isEqualTo(3L);
        assertThat(principal.companyId()).isEqualTo(7L);
        assertThat(principal.defaultWarehouseId()).isEqualTo(10L);
        assertThat(principal.warehouseIds()).containsExactly(10L);
        assertThat(principal.getAuthorities()).extracting("authority")
                .containsExactlyInAnyOrder("ROLE_CASHIER", "SALE_POS");
    }

    @Test
    void mapsInactiveDatabaseUserToDisabledPrincipal() {
        Role role = Role.builder().name("CASHIER").permissions(Set.of()).companyId(7L).build();
        User user = User.builder()
                .id(3L)
                .email("disabled@example.test")
                .phone("1234567890")
                .password("encoded")
                .status(false)
                .companyId(7L)
                .role(role)
                .build();
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        AuthenticatedUser principal = (AuthenticatedUser) new UserDetailsServiceImpl(userRepository)
                .loadUserByUsername(user.getEmail());

        assertThat(principal.isEnabled()).isFalse();
    }
}
