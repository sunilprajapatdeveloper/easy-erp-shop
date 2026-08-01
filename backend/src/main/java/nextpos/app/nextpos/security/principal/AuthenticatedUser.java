package nextpos.app.nextpos.security.principal;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Immutable, authoritative identity used for the lifetime of an authenticated request.
 * Tenant and warehouse identifiers originate from the database, never request headers.
 */
public record AuthenticatedUser(
        Long userId,
        Long companyId,
        Long defaultWarehouseId,
        String username,
        String password,
        boolean enabled,
        Set<Long> warehouseIds,
        Set<GrantedAuthority> authorities) implements UserDetails {

    public AuthenticatedUser {
        warehouseIds = warehouseIds == null ? Set.of() : Set.copyOf(warehouseIds);
        authorities = authorities == null ? Set.of() : Set.copyOf(authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.unmodifiableSet(authorities);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
