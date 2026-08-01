package nextpos.app.nextpos.security.websocket;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Set;

import nextpos.app.nextpos.security.jwt.JwtUtils;
import nextpos.app.nextpos.security.principal.AuthenticatedUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;

class ScannerWebSocketSecurityInterceptorTest {

    private final JwtUtils jwtUtils = mock(JwtUtils.class);
    private final UserDetailsService userDetailsService = mock(UserDetailsService.class);
    private final MessageChannel channel = mock(MessageChannel.class);
    private final ScannerWebSocketSecurityInterceptor interceptor =
            new ScannerWebSocketSecurityInterceptor(jwtUtils, userDetailsService);
    private AuthenticatedUser user;

    @BeforeEach
    void setUp() {
        user = new AuthenticatedUser(7L, 3L, 11L, "owner@example.test", "hash", true,
                Set.of(11L), Set.of(new SimpleGrantedAuthority("SALE_POS")));
    }

    @Test
    void connectRequiresValidBearerToken() {
        assertThatThrownBy(() -> interceptor.preSend(message(StompCommand.CONNECT, null, null), channel))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void rejectsAnotherUsersOrWarehousesTopic() {
        var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        assertThatThrownBy(() -> interceptor.preSend(
                message(StompCommand.SUBSCRIBE, "/topic/scanner/99/8", authentication), channel))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void acceptsAuthenticatedAssignedScannerTopic() {
        var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        interceptor.preSend(message(StompCommand.SUBSCRIBE, "/topic/scanner/11/7", authentication), channel);
    }

    @Test
    void authenticatesConnectFromDatabaseBackedPrincipal() {
        when(jwtUtils.validateToken("token")).thenReturn(true);
        when(jwtUtils.getUsernameFromToken("token")).thenReturn(user.username());
        when(userDetailsService.loadUserByUsername(user.username())).thenReturn(user);
        interceptor.preSend(connectMessage("Bearer token"), channel);
    }

    private Message<byte[]> connectMessage(String authorization) {
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.CONNECT);
        accessor.setNativeHeader("Authorization", authorization);
        accessor.setLeaveMutable(true);
        return MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());
    }

    private Message<byte[]> message(StompCommand command, String destination,
            UsernamePasswordAuthenticationToken authentication) {
        StompHeaderAccessor accessor = StompHeaderAccessor.create(command);
        accessor.setDestination(destination);
        accessor.setUser(authentication);
        accessor.setLeaveMutable(true);
        return MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());
    }
}
