package nextpos.app.nextpos.config;

import lombok.RequiredArgsConstructor;
import nextpos.app.nextpos.security.websocket.ScannerWebSocketSecurityInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

        private final ScannerWebSocketSecurityInterceptor securityInterceptor;

        @Override
        public void configureClientInboundChannel(ChannelRegistration registration) {
                registration.interceptors(securityInterceptor);
        }

        @Override
        public void configureMessageBroker(MessageBrokerRegistry config) {
                config.enableSimpleBroker("/topic", "/queue", "/user");
                config.setApplicationDestinationPrefixes("/app");
                config.setUserDestinationPrefix("/user");
        }

        @Override
        public void registerStompEndpoints(StompEndpointRegistry registry) {
                registry.addEndpoint("/ws-scanner")
                                .setAllowedOriginPatterns(
                                                "http://localhost:*",
                                                "https://noncondensible-catchingly-beatriz.ngrok-free.dev",
                                                "https://liberal-tick-quiet.ngrok-free.app")
                                .withSockJS();

                registry.addEndpoint("/ws-scanner")
                                .setAllowedOriginPatterns(
                                                "http://localhost:*",
                                                "https://noncondensible-catchingly-beatriz.ngrok-free.dev",
                                                "https://liberal-tick-quiet.ngrok-free.app");
        }
}
