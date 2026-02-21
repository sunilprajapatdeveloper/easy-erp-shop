package nextpos.app.nextpos.security.context;

import nextpos.app.nextpos.model.entity.User;
import nextpos.app.nextpos.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public final class UserContext {

    private UserContext() {
    }

    /**
     * Retrieves the currently authenticated User entity.
     * 
     * @param userRepository the UserRepository to look up the user
     * @return the User object
     * @throws RuntimeException if no authenticated user is found
     */
    public static User getAuthenticatedUser(UserRepository userRepository) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new RuntimeException("Unauthorized: Cannot perform operation without authentication");
        }

        String identifier = authentication.getName();

        // Try email first, then phone
        Optional<User> optionalUser = userRepository.findByEmail(identifier);

        if (optionalUser.isEmpty())
            optionalUser = userRepository.findByPhone(identifier);

        return optionalUser.orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }

    /**
     * Returns the company ID of the currently authenticated user, if any.
     * 
     * @param userRepository the UserRepository to look up the user
     * @return Optional containing the company ID, or empty if user has no company
     *         or is not authenticated
     */
    public static Optional<Long> getCurrentUserCompanyId(UserRepository userRepository) {
        try {
            User user = getAuthenticatedUser(userRepository);
            if (user.getCompanyId() != null) {
                return Optional.of(user.getCompanyId());
            }
        } catch (RuntimeException e) {
            // User not authenticated or not found – return empty
        }
        return Optional.empty();
    }
}