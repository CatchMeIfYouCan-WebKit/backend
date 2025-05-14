package com.team.webkit.backend.api.chat.controller;

import com.team.webkit.backend.api.chat.dto.ChatMessageRequest;
import com.team.webkit.backend.api.chat.dto.ChatMessageResponse;
import com.team.webkit.backend.api.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final ChatService chatService;

    // ✅ 실시간 메시지 처리
    @MessageMapping("/chat.sendMessage") // 프론트 → 서버
    @SendTo("/topic/chat/{roomId}")      // 서버 → 같은 방 모든 사용자
    public ChatMessageResponse sendMessage(ChatMessageRequest request) {
        return chatService.saveMessage(request);
    }
}
