package nextpos.app.nextpos.security.context;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import nextpos.app.nextpos.security.principal.AuthenticatedUser;

import java.util.Set;

/**
 * Thread-safe holder for current authenticated user details.
 * Static methods accessible from anywhere after Spring initialisation.
 */
public final class UserContext {

    private UserContext() {
    }

    /**
     * Retrieves the currently authenticated User entity.
     */
    public static AuthenticatedUser getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof AuthenticatedUser principal)) {
            throw new SecurityException("Unauthorized: No authenticated user found");
        }
        return principal;
    }

    /**
     * Returns the company ID of the currently authenticated user.
     */
    public static Long getCurrentCompanyId() {
        Long companyId = getAuthenticatedUser().companyId();
        if (companyId == null) {
            throw new IllegalStateException("Authenticated user does not belong to any company");
        }
        return companyId;
    }

    /**
     * Returns the ID of the currently authenticated user.
     */
    public static Long getCurrentUserId() {
        return getAuthenticatedUser().userId();
    }

    /**
     * Returns the default warehouse ID of the current user (may be null).
     */
    public static Long getCurrentDefaultWarehouseId() {
        return getAuthenticatedUser().defaultWarehouseId();
    }

    public static Set<Long> getCurrentWarehouseIds() {
        return getAuthenticatedUser().warehouseIds();
    }

    public static boolean canAccessWarehouse(Long warehouseId) {
        return warehouseId != null && getCurrentWarehouseIds().contains(warehouseId);
    }

    /**
     * Safe version – returns Optional of company ID (empty if not authenticated or
     * no company).
     */
    public static java.util.Optional<Long> getCurrentCompanyIdSafe() {
        try {
            return java.util.Optional.of(getCurrentCompanyId());
        } catch (Exception e) {
            return java.util.Optional.empty();
        }
    }
}
