package nextpos.app.nextpos.security.context;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Set;

import nextpos.app.nextpos.security.principal.AuthenticatedUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

class UserContextTest {

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void derivesTenantAndWarehouseAccessFromPrincipal() {
        AuthenticatedUser principal = new AuthenticatedUser(
                5L, 7L, 10L, "user@example.test", "encoded", true,
                Set.of(10L, 11L), Set.of(new SimpleGrantedAuthority("SALE_POS")));
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities()));

        assertThat(UserContext.getCurrentUserId()).isEqualTo(5L);
        assertThat(UserContext.getCurrentCompanyId()).isEqualTo(7L);
        assertThat(UserContext.getCurrentDefaultWarehouseId()).isEqualTo(10L);
        assertThat(UserContext.canAccessWarehouse(11L)).isTrue();
        assertThat(UserContext.canAccessWarehouse(99L)).isFalse();
    }

    @Test
    void rejectsNonAuthoritativePrincipalTypes() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("user@example.test", null, Set.of()));

        assertThatThrownBy(UserContext::getCurrentCompanyId)
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("Unauthorized");
    }
}
