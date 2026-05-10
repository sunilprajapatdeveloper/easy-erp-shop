package nextpos.app.nextpos.security.context;

import nextpos.app.nextpos.model.entity.User;
import nextpos.app.nextpos.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**
 * Thread-safe holder for current authenticated user details.
 * Static methods accessible from anywhere after Spring initialisation.
 */
@Component
@Slf4j
public class UserContext {

    private static UserRepository userRepository;
    private final UserRepository instanceUserRepository;

    public UserContext(UserRepository userRepository) {
        this.instanceUserRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        userRepository = this.instanceUserRepository;
        log.info("UserContext initialised with UserRepository");
    }

    /**
     * Retrieves the currently authenticated User entity.
     */
    public static User getAuthenticatedUser() {
        if (userRepository == null) {
            throw new IllegalStateException("UserContext not initialised - UserRepository missing");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new SecurityException("Unauthorized: No authenticated user found");
        }
        String identifier = authentication.getName();
        return userRepository.findByEmail(identifier)
                .orElseGet(() -> userRepository.findByPhone(identifier)
                        .orElseThrow(() -> new SecurityException("Authenticated user not found in database")));
    }

    /**
     * Returns the company ID of the currently authenticated user.
     */
    public static Long getCurrentCompanyId() {
        User user = getAuthenticatedUser();
        if (user.getCompanyId() == null) {
            throw new IllegalStateException("Authenticated user does not belong to any company");
        }
        return user.getCompanyId();
    }

    /**
     * Returns the ID of the currently authenticated user.
     */
    public static Long getCurrentUserId() {
        return getAuthenticatedUser().getId();
    }

    /**
     * Returns the default warehouse ID of the current user (may be null).
     */
    public static Long getCurrentDefaultWarehouseId() {
        User user = getAuthenticatedUser();
        return user.getDefaultWarehouse() != null ? user.getDefaultWarehouse().getId() : null;
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