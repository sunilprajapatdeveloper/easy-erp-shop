package nextpos.app.nextpos.service.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;

import nextpos.app.nextpos.model.dto.request.CreateRoleRequest;
import nextpos.app.nextpos.model.entity.Role;
import nextpos.app.nextpos.repository.PermissionRepository;
import nextpos.app.nextpos.repository.RoleRepository;
import nextpos.app.nextpos.security.principal.AuthenticatedUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {

    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PermissionRepository permissionRepository;

    private GroupServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new GroupServiceImpl(roleRepository, permissionRepository);
        AuthenticatedUser principal = new AuthenticatedUser(
                5L, 7L, null, "owner@example.test", "encoded", true, Set.of(), Set.of());
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities()));
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void roleLookupIsAlwaysScopedToAuthenticatedCompany() {
        when(roleRepository.findByIdAndCompanyId(99L, 7L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getRoleById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Group not found");

        verify(roleRepository).findByIdAndCompanyId(99L, 7L);
    }

    @Test
    void duplicateRoleNameIsCheckedWithinAuthenticatedCompany() {
        CreateRoleRequest request = CreateRoleRequest.builder()
                .name("Cashier")
                .permissionIds(Set.of(1L))
                .build();
        when(roleRepository.findByNameIgnoreCaseAndCompanyId("Cashier", 7L))
                .thenReturn(Optional.of(Role.builder().id(2L).name("Cashier").companyId(7L).build()));

        assertThatThrownBy(() -> service.createRole(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("already in use");
    }
}
