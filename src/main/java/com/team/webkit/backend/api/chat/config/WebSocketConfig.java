package com.team.webkit.backend.api.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // 1. 메시지 받을 endpoint 설정 (프론트에서 연결할 때 이걸 사용)
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // WebSocket 연결 주소
                .setAllowedOriginPatterns("*") // 모든 도메인 허용 (CORS)
                .withSockJS(); // SockJS fallback
    }

    // 2. 메시지 브로커 구성
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // /topic으로 시작하는 주소는 구독자에게 메시지를 브로드캐스트
        registry.enableSimpleBroker("/topic");

        // /app으로 시작하는 메시지는 컨트롤러로 라우팅됨 (@MessageMapping)
        registry.setApplicationDestinationPrefixes("/app");
    }
}
