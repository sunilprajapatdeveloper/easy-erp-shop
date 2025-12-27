// src/main/java/nextpos/app/nextpos/security/context/UserContext.java
package nextpos.app.nextpos.security.context;

import nextpos.app.nextpos.model.entity.User;
import nextpos.app.nextpos.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class UserContext {

    public static User getAuthenticatedUser(UserRepository userRepository) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new RuntimeException("Unauthorized: Cannot perform operation without authentication");
        }

        String identifier = authentication.getName();

        Optional<User> optionalUser = userRepository.findByUsername(identifier);

        if (optionalUser.isEmpty())
            optionalUser = userRepository.findByEmail(identifier);

        if (optionalUser.isEmpty())
            optionalUser = userRepository.findByPhone(identifier);

        return optionalUser.orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }
}
