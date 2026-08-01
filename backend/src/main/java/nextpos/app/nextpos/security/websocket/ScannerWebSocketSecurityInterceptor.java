package nextpos.app.nextpos.security.websocket;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.security.jwt.JwtUtils;
import nextpos.app.nextpos.security.principal.AuthenticatedUser;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/** Authenticates STOMP independently of the HTTP handshake and restricts scanner destinations. */
@Component
@RequiredArgsConstructor
public class ScannerWebSocketSecurityInterceptor implements ChannelInterceptor {

    private static final String BEARER = "Bearer ";
    private static final String SCANNER_APP_PREFIX = "/app/scanner/";
    private static final String SCANNER_TOPIC_PREFIX = "/topic/scanner/";
    private static final String POS_PERMISSION = "SALE_POS";

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) throw new AccessDeniedException("Invalid STOMP message");
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            accessor.setUser(authenticate(accessor.getFirstNativeHeader("Authorization")));
            return message;
        }

        if (accessor.getCommand() == null || StompCommand.DISCONNECT.equals(accessor.getCommand())) return message;
        AuthenticatedUser user = requireUser(accessor.getUser());
        requirePosPermission(user);

        if (StompCommand.SEND.equals(accessor.getCommand())) {
            if (!StringUtils.hasText(accessor.getDestination())
                    || !accessor.getDestination().startsWith(SCANNER_APP_PREFIX)) {
                throw new AccessDeniedException("WebSocket destination is not allowed");
            }
        } else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            validateSubscription(accessor.getDestination(), user);
        }
        return message;
    }

    private Authentication authenticate(String authorization) {
        if (!StringUtils.hasText(authorization) || !authorization.startsWith(BEARER)) {
            throw new AccessDeniedException("WebSocket authentication is required");
        }
        String token = authorization.substring(BEARER.length());
        if (!jwtUtils.validateToken(token)) throw new AccessDeniedException("Invalid WebSocket token");
        AuthenticatedUser user = (AuthenticatedUser) userDetailsService
                .loadUserByUsername(jwtUtils.getUsernameFromToken(token));
        if (!user.isEnabled()) throw new AccessDeniedException("User is disabled");
        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }

    private AuthenticatedUser requireUser(java.security.Principal principal) {
        if (!(principal instanceof Authentication authentication)
                || !(authentication.getPrincipal() instanceof AuthenticatedUser user)) {
            throw new AccessDeniedException("WebSocket authentication is required");
        }
        return user;
    }

    private void requirePosPermission(AuthenticatedUser user) {
        if (user.getAuthorities().stream().noneMatch(authority -> POS_PERMISSION.equals(authority.getAuthority()))) {
            throw new AccessDeniedException("POS permission is required");
        }
    }

    private void validateSubscription(String destination, AuthenticatedUser user) {
        if (destination != null && destination.startsWith("/user/queue/scanner/")) return;
        if (destination == null || !destination.startsWith(SCANNER_TOPIC_PREFIX)) {
            throw new AccessDeniedException("WebSocket subscription is not allowed");
        }
        String[] parts = destination.substring(SCANNER_TOPIC_PREFIX.length()).split("/");
        if (parts.length != 2) throw new AccessDeniedException("Invalid scanner subscription");
        try {
            Long warehouseId = Long.valueOf(parts[0]);
            Long userId = Long.valueOf(parts[1]);
            boolean owner = user.getAuthorities().stream()
                    .anyMatch(authority -> "ROLE_COMPANY_OWNER".equals(authority.getAuthority()));
            if (!user.userId().equals(userId) || (!owner && !user.warehouseIds().contains(warehouseId))) {
                throw new AccessDeniedException("Scanner subscription is outside the authenticated scope");
            }
        } catch (NumberFormatException exception) {
            throw new AccessDeniedException("Invalid scanner subscription", exception);
        }
    }
}
