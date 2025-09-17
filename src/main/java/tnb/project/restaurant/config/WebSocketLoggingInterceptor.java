package tnb.project.restaurant.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

public class WebSocketLoggingInterceptor implements ChannelInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketLoggingInterceptor.class);

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor != null) {
            StompCommand command = accessor.getCommand();
            String destination = accessor.getDestination();
            if (command != null) {
                switch (command) {
                    case CONNECT:
                        logger.info("[WebSocket] CONNECT from session: {} destination: {}", accessor.getSessionId(), destination);
                        break;
                    case CONNECTED:
                        logger.info("[WebSocket] CONNECTED: {} destination: {}", accessor.getSessionId(), destination);
                        break;
                    case DISCONNECT:
                        logger.info("[WebSocket] DISCONNECT: {} destination: {}", accessor.getSessionId(), destination);
                        break;
                    case SUBSCRIBE:
                        logger.info("[WebSocket] SUBSCRIBE: session={}, destination={}", accessor.getSessionId(), destination);
                        break;
                    case SEND:
                        logger.info("[WebSocket] SEND: session={}, destination={}", accessor.getSessionId(), destination);
                        break;
                    default:
                        logger.info("[WebSocket] {}: session={}, destination={}", command, accessor.getSessionId(), destination);
                }
            }
        }
        return message;
    }
}
