package com.team.webkit.backend.api.chat.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // 클라이언트가 WebSocket을 연결할 엔드포인트 설정
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")         // ex: http://localhost:8080/ws
                .setAllowedOriginPatterns("*");            // SockJS 사용 (브라우저 호환성 ↑)
    }

    // 메시지 브로커 구성
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");  // 클라이언트 → 서버 보낼 prefix
        registry.enableSimpleBroker("/topic", "/queue", "/user"); // 서버 → 클라이언트로 보낼 prefix
        registry.setUserDestinationPrefix("/user");  // 1:1 메시지 전송 시 사용자 큐 prefix
    }
}
