package nextpos.app.nextpos.security.authorization;

import java.util.Set;
import java.util.function.Supplier;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

@Component
public class BusinessApiAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Override
    @SuppressWarnings("deprecation")
    public AuthorizationDecision check(Supplier<Authentication> authenticationSupplier,
            RequestAuthorizationContext context) {
        Authentication authentication = authenticationSupplier.get();
        if (authentication == null || !authentication.isAuthenticated()) {
            return new AuthorizationDecision(false);
        }
        Set<String> required = BusinessPermissionRules.requiredAuthorities(
                context.getRequest().getRequestURI(), context.getRequest().getMethod());
        boolean granted = !required.isEmpty() && authentication.getAuthorities().stream()
                .anyMatch(authority -> required.contains(authority.getAuthority()));
        return new AuthorizationDecision(granted);
    }
}
